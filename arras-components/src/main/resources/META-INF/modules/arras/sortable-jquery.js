(function() {
	define(["jquery", "t5/core/dom", "t5/core/events", "jquery-ui/sortable"], function($, dom, events, sortable)  {
		
		dom.scanner("[data-sortable]", function(elementWrapper) {
			
			var $this = $(elementWrapper.element);
			
			var conf = {
				cancel: elementWrapper.attr("data-cancel") || undefined,
				handle: elementWrapper.attr("data-handle") || undefined,
				placeholder: elementWrapper.attr("data-placeholder") || undefined
			};
			
			$this.sortable(conf);
		});
	})
	
}).call(this);