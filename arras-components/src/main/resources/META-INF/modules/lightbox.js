(function() {
	define(["jquery", "jquery.colorbox"], function($, colorbox) {
		
		var supportedOptions = ["rel", "transition", "height", "width", "innerWidth", "innerHeight", "inline", "fixed", "slideshow", "iframe"];
		
		$("[data-container-type=lightbox]").each(function() {
			
			var $this = $(this);
			var options = readOptions($this, supportedOptions);
			$this.colorbox(options);
		});
		
		$("[data-container-type=lightbox-content]").each(function() {
			
			var $this = $(this);
			
			var zone = $this.attr("data-zone");
			var open = $this.attr("data-open");
			
			if(open === true || zone !== undefined) {
				
				var options = readOptions($this, supportedOptions);
				options["inline"] = true;
				options["href"] = "#" + this.id;
				
				// show the lightbox immediately
				if(open === true) {
					$.colorbox(options);
				}
				
				// show the lightbox when the zone was updated
				if(zone !== undefined) {
					$("#"+zone).bind('t5:zone:did-update', function(){ 
						$.colorbox(options);
					});
				}				
			}
		})
		
		function readOptions($this, optionNames) {
			
			var options = {};
			
			for(i=0; i<optionNames.length; i++) {
				
				var optionName = optionNames[i];
				options[optionName] = $this.attr("data-"+optionName);
			}
			
			return options;
		}
		
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
	});
}.call(this));