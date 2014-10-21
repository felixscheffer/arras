(function() {
	define(["t5/core/dom"], function(dom) {
		
		dom.onDocument("click", "[data-container-type=remote-submit]", function() {
			
			var selector = this.attr("data-selector");
			
			var submit = dom.body.findFirst(selector);
			
			if(submit) {
				submit.element.click();
			}
		});
	})
	
}).call(this);