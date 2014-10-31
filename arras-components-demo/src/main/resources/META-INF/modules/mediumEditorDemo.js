(function() {
	define(["t5/core/dom", "t5/core/pageinit", "arras/events"], function(dom, pi, events) {
		
		var CONTAINER_SELECTOR = "[data-container-type=content-container]"
		
		dom.onDocument("click", "[data-container-type=content-submit]", function() {

			var container = this.closest(CONTAINER_SELECTOR);

			var url = container.attr("data-url");
			var data = {};

			container.trigger(events.submit, data);

			dom.ajaxRequest(url, {
				data : {
					content : JSON.stringify(data)
				},
				success : function(response) {
					if (response.json) {

						pi.handlePartialPageRenderResponse(response);
						if (response.json.content) {
							container.update(response.json.content);
							container.trigger(t5events.zone.didUpdate);
						}
					}
				}
			});
		});
	})

}).call(this);