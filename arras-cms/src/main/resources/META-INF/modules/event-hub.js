(function() {
	
	define([], function() {
		
		var EventHub = function() {
			this.listeners = [];			
		}
		
		EventHub.prototype.add = function(listener) {
			this.listeners.push(listener);
		}
		
		EventHub.prototype.notify = function(data) {
			for(var i=0; i<this.listeners.length; i++) {
				this.listeners[i](data);
			}
		}
		
		return EventHub;
	})
	
	
}).call(this)