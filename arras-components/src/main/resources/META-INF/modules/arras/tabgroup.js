/* ========================================================================
 * Bootstrap: tab.js v3.1.0
 * http://getbootstrap.com/javascript/#tabs
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


(function () {
  'use strict';
  
  define(["t5/core/dom", "./transition"], function(dom, transition) { 

	  //assign .active class to li.dropdown when a dropdown item is marked as active
	  var actives = dom.body.find(".nav-tabs .dropdown-menu > .active")
	  for(var i =0; i<actives.length; i++) {
	      actives[i].closest(".dropdown").addClass("active");
	  }

	  dom.onDocument("click", "[data-toggle=tab]", function() {
	    
	      var ul = this.closest("ul")
	      while(ul.hasClass("dropdown-menu")) {	      
	    	  ul = ul.findParent("ul") 
	      }
	      
	      var parent = this.findParent("li")

	      if(parent.hasClass("active"))
	        return false

	      var selector = this.attr("href")
	      //strip for ie7
	      selector = selector && selector.replace("/.*(?=#[^\s]*$)/", '')
	      
	      if( !this.trigger("arras:tabs:show") )
	      	return

	      var target = dom.body.findFirst(selector)
	      
	      var updateLink = parent.attr("data-update-tab")
	      if(updateLink) {
	      
	    	  var content = dom.ajaxRequest(updateLink, { 
	    		  success: function(response) { target.update(response.json.content) } 
	    	  });
	      } 

	      initialize(parent, ul)
	      initialize(target, target.parent())
	      
	  });
	      
	  function initialize(element, container) {

	      var active = container.findFirst("> .active")
	      var supportsTransition = transition && active.hasClass("fade")

	      var next = function() {
	    	  
	    	  if(active) {
	    		  active.removeClass("active")
	    		  var activeDropdown = active.findFirst("> .dropdown-menu > .active");
	    		  if(activeDropdown) {
	    			  activeDropdown.removeClass("active")  
	    		  }
	    	  }

	          element.addClass("active")
	        
	          if(supportsTransition)
	        	  element.addClass("in")
	          else
	        	  element.removeClass("fade")
	        
	          if(element.findParent(".dropdown-menu"))
	        	  element.closest("li.dropdown").addClass("active")
	      }
	      
	      if(supportsTransition) {
	    	  
	    	  onOnce(active, transition.end, next);
	    	  transition.emulateTransitionEnd(active, 200)
	      }
	      else {
	    	  next()
	      }
	      
	      active.removeClass("in")
	  }
	  
	  // emulating jQuery.one() 
	  function onOnce(elementWrapper, event, handler) {
		  
		  var off = dom.on(elementWrapper.element, event, function() {
			 
			  // remove the event handler and call the actual handler
			  off();
			  handler.call(arguments);
		  });
	  }
  });
 
}).call(this);
