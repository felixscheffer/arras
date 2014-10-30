(function() {
	
	define(["t5/core/dom", "event-hub"], function(dom, EventHub) {

		var containerSel = "[data-container-type=content-container]";
		var blockSel = "[data-component-type=content-block]";
		var parentSel = blockSel + "," + containerSel;
		
		var SUBMIT_EVENT = "arras:submit";
	
		var Walker = function(element, selectors) {
			var selector = selectors.join(",");
			this.elements = element.find(selector);
			this.current = null;
		}
		
		Walker.prototype.forEach = function(callback) {
			
			var parent = this.current;
			
			var lastElement = null;
			
			while(i<this.elements.length) {
				
				var element = this.elements[i];
				
				if(parent != null && !parent.element.contains(element.element)) {
					// this is no longer a subelement
					break;
				}
				
				i++;
				
				if(lastElement == null || !lastElement.element.contains(element.element)) {
					this.current = element;
					lastElement = element;
					console.log("processing: "+element.attr("data-context"));
					callback(element);
				}
				
				//else:
				//  skip subelements - they should have been handled by the callback
				
			}
		}
		
		
		dom.onDocument("click", "[data-container-type=content-submit]", function() {
			submit(this.closest(containerSel));
		});
		
		function submit(container) {
			
			var url = container.attr("data-url");
			var data = {};
			var selectors = ["[data-component-type=content-block]","[data-container-type=medium-editor]", "[data-component-type=content-image]"];
			
			var walker = new Walker(container, selectors);
			walker.forEach(function(element) {
				element.trigger("arras:submit", { data:data, walker:walker });
			})
			
			dom.ajaxRequest(url, {
				data: { content: JSON.stringify(data) }
			});	
		}
		
		dom.scanner(blockSel, function(block) {
			
			var container = block.findParent(parentSel);
			
			if(container === null) {
				return
			}
			
			
			block.on(SUBMIT_EVENT, function(event, eventContext) {

				var walker = eventContext.walker;
				var data = eventContext.data;
				var subdata = {}
				var context = block.attr("data-context")
				
				if(data[context] === undefined) {
					data[context] = []
				}
				
				console.log("block submit: " + context);
				
				data[context].push(subdata);

				walker.forEach(function(child) {	
					child.trigger(SUBMIT_EVENT, {data:subdata, walker:walker});
				})
				
				return false;
			})
		})
		
		
		// TODO: move to medium-editor.js
		dom.scanner("[data-container-type=medium-editor]", function(editor) {
			
			editor.on(SUBMIT_EVENT, function(event, eventContext) {
				
				console.log("medium-editor: " + editor.attr("data-context"));
				
				var data = eventContext.data;
				var context = editor.attr("data-context");
				var content = editor.element.innerHTML.trim();
				
				data[context] = content;
				
				return false;
			})
		})
		
		dom.scanner("[data-component-type=content-image]", function(image) {
			
			image.on(SUBMIT_EVENT, function(event, eventContext) {
				
				console.log("image: " + image.attr("data-context"));
				
				var data = eventContext.data;
				var context = image.attr("data-context");
				
				// "image" is the div wrapping the actual img tag
				data[context] = image.findFirst("> img").attr("src");
				
				return false;
			});
		})
		
	})
}).call(this);