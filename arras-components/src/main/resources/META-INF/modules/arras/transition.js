/* ========================================================================
 * Bootstrap: transition.js v3.1.0
 * http://getbootstrap.com/javascript/#transitions
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */

(function() {
	'use strict';
	
	define(["underscore", "t5/core/dom"], function(_, dom) {


		// CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
		// ============================================================
		
		function transitionEnd() {
		    var el = document.createElement('bootstrap')
		
		    var transEndEventNames = {
		      'WebkitTransition' : 'webkitTransitionEnd',
		      'MozTransition'    : 'transitionend',
		      'OTransition'      : 'oTransitionEnd otransitionend',
		      'transition'       : 'transitionend'
		    }
		
		    for (var name in transEndEventNames) {
			    if (el.style[name] !== undefined) {
			        return { end: transEndEventNames[name] }
			    }
		    }
		
		    return false // explicit for ie8 ( ._.)
		}
	
		var transition = transitionEnd()
		
		return _.extend(transition, {
			
			// http://blog.alexmaccaw.com/css-transitions
			emulateTransitionEnd: function($el, duration) {
		        
				var called = false
		        var off = dom.on($el.element, transition.end, function() { called = true })
	
		        var callback = function() {
					
					// remove the event handler and trigger the event manually if it was not triggered yet
					off()
					
					if (!called)
						$el.trigger(transition.end)
				}
	
		        setTimeout(callback, duration)
	
		        return this
			}
		});
	});
}).call(this);
