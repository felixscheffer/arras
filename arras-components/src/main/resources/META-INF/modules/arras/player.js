(function() {

	define([ "t5/core/dom", "arras/video-js/video.dev" ],
			
			function(dom, videojs) {

				dom.scanner("[data-component-type=player]", function(player) {

					var tag = player.findFirst("video, audio");
					
					videojs(tag.element, {});
				})
			})
}).call(this);