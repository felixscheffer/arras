(function() {
	define(["t5/core/dom", "arras/lightbox"], function(dom, lightbox) {
		
		var SELECTOR = "[data-component-type=image-selector]";
		
		dom.scanner(SELECTOR, function(element) {
			
			element.on("click", function() {
				
				var $this = this.closest(SELECTOR);
				
				var href = $this.attr("href");
				var value = $this.findFirst("img").attr("src");
				var targets = dom.body.find(href);
				
				for(var i=0; i<targets.length; i++) {
					targets[i].attr("src", value);
				}
				
				lightbox.close();
			})
		});
	})
	
}).call(this);