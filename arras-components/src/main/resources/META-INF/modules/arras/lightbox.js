// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

(function() {
	define(["jquery", "shim/jquery.colorbox", "t5/core/dom"], function($, colorbox, dom) {
		
		var supportedOptions = ["rel", "transition", "height", "width", "innerWidth", "innerHeight", "inline", "fixed", "slideshow", "iframe"];
		
		dom.scanner("[data-container-type=lightbox]", function(elementWrapper) {
			
			var $this = $(elementWrapper.element);
			var options = readOptions($this, supportedOptions);
			$this.colorbox(options);
		});
		
		dom.scanner("[data-container-type=lightbox-content]", function(elementWrapper) {
			
			var $this = $(elementWrapper.element);
			
			var zone = $this.attr("data-zone");
			var open = $this.attr("data-open");
			
			if(open === true || zone !== undefined) {
				
				var options = readOptions($this, supportedOptions);
				options["inline"] = true;
				options["href"] = "#" + $this.attr("id");
				
				// show the lightbox immediately
				if(open === true) {
					$.colorbox(options);
				}
				
				// show the lightbox when the zone was updated
				if(zone !== undefined) {
					$("#"+zone).bind('t5:zone:did-update', function(){ 
						$.colorbox(options);
					});
				}				
			}
		});
		
		// resize the lightbox when the size of the window changes
		$(window).resize(function () {
			if($('#cboxOverlay').is(':visible')){
				$.colorbox.resize();
			}
		});
		
		function readOptions($this, optionNames) {
			
			var options = {};
			
			for(i=0; i<optionNames.length; i++) {
				
				var optionName = optionNames[i];
				options[optionName] = $this.attr("data-"+optionName);
			}
			
			return options;
		}
		
		return {
			close: function() {
				$.colorbox.close();
			}
		}
	});
}.call(this));