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
	
		var exports = transitionEnd()
		
		return _.extend(exports, {
			
			// http://blog.alexmaccaw.com/css-transitions
			emulateTransitionEnd: function($el, duration) {
		        
				var called = false
				
		        $el.on(exports.end, function() { called = true })
	
		        var callback = function() { 
					if (!called)
						$el.trigger(exports.end)
				}
	
		        setTimeout(callback, duration)
	
		        return this
			}
		});
	});
}).call(this);
