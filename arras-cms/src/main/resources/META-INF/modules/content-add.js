(function() {
	
	define(["t5/core/dom", "t5/core/events"], function(dom, events) {
		
		dom.onDocument("click", "[data-component-type=content-add]", function() {
			
			var element = this.closest("[data-component-type=content-add]");
			var url = element.attr("data-url");
			
			dom.ajaxRequest(url, {
				success: function(response) {
					var _ref = response.json;
					addContent(element, _ref != null ? _ref.content : void 0);
				}
			});
		})
		
		function addContent(element, content) {
			
			var container = element.closest(".content-blocks").findFirst("> div");
			container.append(content);
			
			// workaround: append does not return the new element...
			var newElement = container.findFirst("> :last-child");
			newElement.trigger(events.zone.didUpdate);
		}
	})
	
}).call(this);