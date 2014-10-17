(function() {
	define(["shim/medium-editor"], function(MediumEditor) {
		
		/*
		 * , {
            buttonLabels: 'fontawesome'
        }
		 */
		new MediumEditor('.editable');
	})
}).call(this);