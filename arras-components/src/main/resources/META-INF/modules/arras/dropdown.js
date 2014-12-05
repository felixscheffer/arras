/* ========================================================================
 * Bootstrap: dropdown.js v3.1.0
 * http://getbootstrap.com/javascript/#dropdowns
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


(function() {
	'use strict'
	
	define(["t5/core/dom", "underscore"], function(dom, _) {
	
		var backdrop = '.dropdown-backdrop'
		var toggle   = '[data-toggle=dropdown]'
		
		function doToggle(event) {
			
			var $this = this;
			
			if ($this.hasClass('.disabled') || $this.attr('disabled')) return
			
		    var $parent  = getParent($this)
		    var isActive = $parent.hasClass('open')

		    clearMenus()

		    if (!isActive) {
		    	
		        if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav') !== null) {
		        	
		            // if mobile we use a backdrop because click events don't delegate
		        	$this.after('<div class="dropdown-backdrop"/>')
		        }

		        var relatedTarget = { relatedTarget: this }
		        var successful = $parent.trigger('arras:dropdown:show', relatedTarget)

		        if (!successful)
		        	return

		        $parent.addClass('open')
		               .trigger('arras:dropdown:shown', relatedTarget)

		        $this.focus()
		    }
		    
			return false;
		}
		
		function keydown(e) {
			
		    if (!/(38|40|27)/.test(e.nativeEvent.keyCode)) return

		    var $this = this.closest(toggle + ', [role=menu], [role=listbox]')

		    e.stop()

		    if ($this.hasClass('.disabled') || $this.attr('disabled')) return

		    var $parent  = getParent($this)
		    var isActive = $parent.hasClass('open')

		    if (!isActive || (isActive && e.nativeEvent.keyCode == 27)) {
		    	
		    	if (e.nativeEvent.which == 27)
		    		$parent.findFirst(toggle).focus()
		    	
		    	console.log("$this.element: " + $this.element);
		    	console.log("$this.element.click: " + $this.element.click);
		    	
		    	return $this.element.click()
		    }

		    var desc = " li > a "; //' li:not(.divider):visible a'
		    var $items = $parent.find('[role=menu]' + desc + ', [role=listbox]' + desc)

		    $items = _.filter($items, function(element) { return !element.parent().hasClass("divider") && element.parent().visible() })
		    
		    if (!$items.length)
		    	return

		    
		    //var index = $items.index($items.filter(':focus'))
		    var focused = document.activeElement;
		    var index = -1
		    
		    for(var i=0; i<$items.length; ++i) {
		    	
		    	var ee = $items[i].element
		    	if(ee.isEqualNode(focused)) {
		    		index = i;
		    		break;
		    	}
		    }

		    if (e.nativeEvent.keyCode == 38 && index > 0)                 index--                        // up
		    if (e.nativeEvent.keyCode == 40 && index < $items.length - 1) index++                        // down
		    if (!~index)                                      			  index = 0

		    $items[index].focus()
		    
		}
		
		function clearMenus(e) {
			
			var backdropElement = dom.body.findFirst(backdrop);
			if(backdropElement) {
				backdropElement.remove()
			}
				
			dom.body.find(toggle).forEach(function (element) {
				
			      var $parent = getParent(element)
			      
			      if (!$parent.hasClass('open'))
			    	  return
			    	  
			      var relatedTarget = { relatedTarget: element }
			      var successful = $parent.trigger('arras:dropdown:hide', relatedTarget)
			      
			      if (!successful)
			    	  return
			    	  
			      $parent.removeClass('open').trigger('arras:dropdown:hidden', relatedTarget)
			})
		}

		function getParent($this) {
			
		    var selector = $this.attr('data-target')

		    if (!selector) {
		    	selector = $this.attr('href')
		    	selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
		    }

		    var $parent = selector && dom.find(selector)

		    return $parent && $parent.length ? $parent[0] : $this.parent()
		}
	
			  
		dom.onDocument('click', clearMenus);
		dom.onDocument('click', '.dropdown form', function (e) { return false })
		dom.onDocument('click', toggle, doToggle);
		dom.onDocument('click', '.dropdown-backdrop', clearMenus );
		dom.onDocument('keydown', toggle + ', [role=menu], [role=listbox]', keydown)
		
	})
	
}).call(this);