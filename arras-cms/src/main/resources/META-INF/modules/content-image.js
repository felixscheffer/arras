(function() {
	define(["t5/core/dom", "arras/lightbox", "./content-events"], function(dom, lightbox, events) {
		
		var SELECTOR = "[data-component-type=image-selector]";
		
		var CONTAINER_SEL = "[data-container-type=content-container]" + "," + "[data-component-type=content-block]";
		
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
		
		dom.scanner("[data-component-type=content-image]", function(image) {
		
			var container = image.findParent(CONTAINER_SEL);

			container.on(events.submit, function(event, data) {

				console.log("image: " + image.attr("data-context"));

				var context = image.attr("data-context");

				// "image" is the div wrapping the actual img tag
				data[context] = image.findFirst("> img").attr("src");
			});
		})
	})
	
}).call(this);