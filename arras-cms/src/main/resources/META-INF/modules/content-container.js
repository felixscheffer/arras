(function() {

	define([ "t5/core/dom", "t5/core/pageinit", "t5/core/events", "./content-events" ], function(
			dom, pi, t5events, events) {

		var CONTAINER_SELECTOR = "[data-container-type=content-container]";
		var BLOCK_SELECTOR = "[data-component-type=content-block]";
		var PARENT_SELECTOR = BLOCK_SELECTOR + "," + CONTAINER_SELECTOR;

		dom.onDocument("click", "[data-container-type=content-submit]",
				function() {

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
								if(response.json.content) {								
									container.update(response.json.content);
									container.trigger(t5events.zone.didUpdate);
								}
							}
						}
					});
				});

		dom.scanner(BLOCK_SELECTOR, function(block) {

			var container = block.findParent(PARENT_SELECTOR);

			if (container === null) {
				return;
			}

			var off = dom.on(container.element, events.submit, submit);

			block.on(events.syncHandler, function() {

				if (off) {
					off();
				}

				off = dom.on(container.element, events.submit, submit);

				return false;
			})

			function submit(event, data) {

				var subdata = {}
				var context = block.attr("data-context")

				if (data[context] === undefined) {
					data[context] = []
				}

				console.log("block submit: " + context);

				data[context].push(subdata);

				block.trigger(events.submit, subdata);
			}

			block.on(events.submit, function(event) {

				// dont bubble up the events we trigger or we end up in an
				// infinite loop when the
				// event reaches the container
				// block -> container -> block
				event.nativeEvent.stopPropagation();
			});

			block.on(events.removeHandler, function(event) {

				if (off) {
					off();
					off = null;
				}

				event.nativeEvent.stopPropagation();
			});
		})

		dom.scanner("[data-sortable]", function(sortable) {

			var container = sortable.closest(CONTAINER_SELECTOR);

			if (!container) {
				// not inside of a container, so we dont care
				return;
			}

			sortable.on("sortupdate", function() {

				this.find(BLOCK_SELECTOR).forEach(function(element) {
					// inform all blocks that the order may have changed
					element.trigger(events.syncHandler);
				})
			})
		});
	})
}).call(this);