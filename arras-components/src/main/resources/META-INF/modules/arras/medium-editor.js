(function() {
	define(["t5/core/dom", "shim/medium-editor", "arras/events"], function(dom, MediumEditor, events) {
		
		var CONTAINER_SEL = "[data-container-type=content-container]" + "," + "[data-component-type=content-block]";
		
		dom.scanner("[data-container-type=medium-editor]", function(editor) {
			
			var options = {};
			
			//data-disable-toolbar and data-disable-editing should work out of the box
			addOption.call(editor, options, "data-buttons", "buttons");
			
			var fontawesome = editor.attr("data-fontawesome") 
			if(fontawesome === null || fontawesome === "true") {
				options["buttonLabels"] = 'fontawesome';
			}
			
			new MediumEditor(editor.element, options);
			
			handleSubmit(editor);
		});
		
		function addOption(options, attributeName, optionName) {
			
			var rawdata = this.attr(attributeName);
			
			if (rawdata) {				
				options[optionName] = eval(rawdata);
			}
		}
		
		function handleSubmit(editor) {
			
			var container = editor.findParent(CONTAINER_SEL);
			
			if(container == null) {
				return;
			}

			container.on(events.submit, function(event, data) {

				console.log("medium-editor: " + editor.attr("data-context"));

				var context = editor.attr("data-context");
				var content = editor.element.innerHTML.trim();

				data[context] = content;
			})
		}
	})
}).call(this);