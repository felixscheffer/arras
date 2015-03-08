(function() {
	define(["t5/core/dom", "jquery", "arras/original/select2.amd"], function(dom, $, select2SourceFile) {
		
		require(["jquery.select2"], function(select2) {
		
			dom.scanner("[data-component-type=select2]", function(elementWrapper) {
				$(elementWrapper.element).select2();
			});
		});
	});
	
}).call(this);