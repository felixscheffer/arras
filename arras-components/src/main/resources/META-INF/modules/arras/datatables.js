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
	define(["jquery", "t5/core/pageinit", "./original/jquery.dataTables"], function($, pi, datatable) {
		
		return {
			init: function(specs) {
				
				var defaults = {
					
					sDom: "TC<\"clear\">Rlfrtpi",
					bJQueryUI:  true,
					bAutoWidth: false,
					
					// don't save the state. creates a strange behavior and confuses the tests
					bStateSave: false,

					sPaginationType: "full_numbers",
					

					
		    		/**
	    			 * For ajax mode, we need to call Tapestry.loadScriptsInReply in a callback to take into account
	    			 * propertyOverrides rendered in server-side
	    			 * */
	    			fnDrawCallback: function( oSettings ) {
	    				if(oSettings.jqXHR){
	    					pi.handlePartialPageRenderResponse({ json: oSettings.jqXHR.responseJSON }, function(){});
	    				}
	        	    }
	    		};
				
				if (specs.params.sAjaxSource !== undefined) {
					
					// define some more defaults when we're using ajax
		            defaults.bServerSide = true;
		            defaults.bProcessing = true;
		            // bDeferRender is supposed to speed things up, but i am not sure if it does
		            defaults.bDeferRender = true;
				}
	
				if(specs.bCaseSensitive === true) {
					
					// ensure compatibility with java's sort() which is case-sensitive
					// note that this is a global method and will affect all DataTables on a page
					jQuery.fn.dataTableExt.oSort["string-pre"] = function ( a ) {
						
						if ( typeof a != 'string' ) {
							a = (a !== null && a.toString) ? a.toString() : '';
						}
						
						return a/*.toLowerCase()*/;
					}
				}
				
				specs.params = $.extend({}, defaults, specs.params);
				
				$("#" + specs.id).dataTable(specs.params);
				
			}
		};
	});
}).call(this);