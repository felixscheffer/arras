(function() {
	define(["t5/core/dom", "t5/core/events", "shim/medium-editor"], function(dom, events, MediumEditor) {
		
		var containerSel = "[data-container-type=medium-editor]";
		var editorSel = "> div";
		
		dom.scanner(containerSel, function(container) {
			
			var editor = container.findFirst(editorSel);
		
			var options = {};
			
			//data-disable-toolbar and data-disable-editing should work out of the box
			addOption.call(editor, options, "data-buttons", "buttons");
			
			var fontawesome = editor.attr("data-fontawesome") 
			if(fontawesome === null || fontawesome === "true") {
				options["buttonLabels"] = 'fontawesome';
			}
			
			new MediumEditor(editor.element, options);			
		});
		
		function addOption(options, attributeName, optionName) {
			
			var rawdata = this.attr(attributeName);
			
			if (rawdata) {				
				options[optionName] = eval(rawdata);
			}
		}
		
		dom.onDocument(events.form.prepareForSubmit, function() {
			
			this.find(containerSel).forEach(function(container) {
				
				var hidden = container.findFirst("input[type=hidden]");
				var editor = container.findFirst(editorSel);
				var serialized = editor.element.innerHTML.trim();
				
				hidden.value(serialized);
			})
		});
		
		/*
		 * , {
            buttonLabels: 'fontawesome'
        }
		 */
	})
}).call(this);