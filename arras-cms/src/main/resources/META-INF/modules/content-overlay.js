(function() {
	define(["t5/core/dom", "arras/events"], function(dom, events) {
		
		var removeSel = ".content-remove";
		var moveSel = ".content-move";
		
		var blockToRemove = null;

		var overlayElements = []
		overlayElements = overlayElements.concat(dom.body.find(".content-overlay-border"));
		overlayElements = overlayElements.concat(dom.body.find(".content-overlay-actions"))
		
		dom.onDocument("click", function() {
			
			overlayElements.forEach(function(element) {
				element.hide();
			})
			
			blockToRemove = null;
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
				blockToRemove = block;
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
			
			if(blockToRemove !== null && confirm("Are you sure?") ) {
				
				// include the sizer
				blockToRemove.parent().remove();
				blockToRemove.trigger(events.removeHandler);
			}
		})
		
	})
}).call(this);