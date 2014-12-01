(function() {
	define(["t5/core/dom", "t5/core/events"], function(dom, events) {
		
		dom.body.on(events.zone.refresh, function() {
			
			var icon = this.attr("data-icon");
			
			if(icon === null) {
				return;
			}
			
			this.prepend("<div class=\"loading-overlay\"><i class=\""+ icon + "\" /></div>");
		});
		
		dom.body.on(events.zone.didUpdate, function() {
			
			var overlay = this.findFirst("> .loading-overlay");
			
			if(overlay) {
				overlay.hide();
			}
		});
		
	})
}).call(this);