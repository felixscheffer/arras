(function() {
	define(["t5/core/dom"], function(dom) {
		
		var removeSel = ".content-remove";
		var moveSel = ".content-move";
		
		var elementToRemove = null;

		var overlayElements = []
		overlayElements = overlayElements.concat(dom.body.find(".content-overlay-border"));
		overlayElements = overlayElements.concat(dom.body.find(".content-overlay-actions"))
		
		dom.onDocument("click", function() {
			
			overlayElements.forEach(function(element) {
				element.hide();
			})
			
			elementToRemove = null;
		})
		
		dom.onDocument("click", "[data-component-type=content-block]", function() {
			
			var block  = this.closest("[data-component-type=content-block]");
			
			for(var i = 0; i<overlayElements.length; i++) {
				overlayElements[i].remove();
				block.insertBefore(overlayElements[i]);
				overlayElements[i].show();
			}
			
			var allowRemoval = block.attr("data-removal") === "true";
			if(allowRemoval) {
				// include the sizer
				elementToRemove = block.parent();
			}
			
			show(removeSel, allowRemoval);
			show(moveSel, allowRemoval);
			
			return false;
		})
		
		function show(sel, show) {

			var element = dom.body.findFirst(sel);
			
			if(show === true) {
				element.show();
			}
			else {
				element.hide();
			}
		}
		
		dom.onDocument("click", removeSel, function() {
			
			if(elementToRemove !== null && confirm("Are you sure?") ) {
				elementToRemove.remove();
			}
		})
		
	})
}).call(this);