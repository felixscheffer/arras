(function() {
	define(["jquery", "jquery.colorbox"], function($, colorbox) {
		
		var init = function(triggerSelector, options) {
			
			if(triggerSelector) {
				$(triggerSelector).colorbox(options);
			}
			else {
				// show the lightbox immediately
				$.colorbox(options);
			}

			$(window).resize(function () {
				if($('#cboxOverlay').is(':visible')){
					$.colorbox.resize({width: options.width, height: options.height});
				}
			});
		};
		
		return {
			init: init,
			
			attachToZone: function(zoneSelector, triggerSelector, options) {
				$(zoneSelector).bind('t5:zone:did-update', function(){ 
					init(triggerSelector, options);
				});
			}
		};
	});
}.call(this));