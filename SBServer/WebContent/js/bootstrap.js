var ShadowBudget = ShadowBudget || {
	
	// variables
	components: {},
	subscriptions: {},
	queue: [],
	handles: {},
	containers: {},
	
	// methods
	extend: function( child, parent, body ){
		var F = function(){};
		F.prototype = parent.prototype;
		child.prototype = new F();
		child.prototype.constructor = child;
		child.superclass = parent.prototype;
		if(body){
			$.extend(child.prototype, body);
		}
	},
	
	defer: function( proc, timeout ){
		var self = this;
		if(timeout == null || timeout == undefined ){
			timeout = 100;
		}
		window.setTimeout(function(){
			proc.call(self);
		}, timeout);
	},
	
	messageBox: function( title, body, type ){
		var dlg = $('#dlgMsg');
		var bodyContainer = dlg.find('.dlg-body');
		var iconContainer = dlg.find('.dlg-icon');
		if( type == null || type == undefined ){
			iconContainer.css('display','none');
		} else {
			iconContainer.css('display','');
			
			// remove all previously set classes
			if(iconContainer.hasClass('icon-error')){
				iconContainer.removeClass('icon-error');
			}
			if(iconContainer.hasClass('icon-warning')){
				iconContainer.removeClass('icon-warning');
			}
			if(iconContainer.hasClass('icon-info')){
				iconContainer.removeClass('icon-info');
			}
			if(iconContainer.hasClass('icon-question')){
				iconContainer.removeClass('icon-question');
			}
			
			if(type == 'error' ){
				iconContainer.addClass('icon-error');
			} else if( type == 'warning' ){
				iconContainer.addClass('icon-warning');
			} else if( type == 'info'){
				iconContainer.addClass('icon-info');
			} else if( type == 'question'){
				iconContainer.addClass('icon-question');
			}
		}
		bodyContainer.empty();
		dlg.attr('title', title);
		bodyContainer.append('<p>' + body + '</p>');
		dlg.dialog({
			modal: true,
			width: '400px',
			buttons: {
				Ok: function(){
					$(this).dialog('close');
				}
			}
		});
	},
	
	registerComponent: function( name, obj ){
		var self = this;
		this.components[name] = obj;
		obj._init( ShadowBudget.containers[name], function(){
			self.publish(this, 'ready', {
				name: name
			});
		});
	},
	
	publish: function(sender, msg, params){
		this.queue.push({
			msg: msg,
			sender: sender,
			params: params
		});
	},
	
	subscribe: function(who, msg, callback ){
		var sLst = this.subscriptions[msg];
		if(sLst == null || sLst == undefined){
			sLst = this.subscriptions[msg] = [];
		}
		sLst[sLst.length] = { 
			target: who,
			callback: callback
		};
	},
	
	run: function() {
		var self = this;
		var cLoaderComponent = new ShadowBudget.ComponentLoader();
		this.registerComponent('cLoader', cLoaderComponent);
		this.subscribe(this,'ready', this.handleComponentReady);
		this.loop();
		// load authentication module
		this.requestComponent( 'auth', null, function(name, componentObj){
			// on loaded
			componentObj.run( function(){
				self.runDesktop();
			});
		} );
	},
	
	handleComponentReady: function(msg){
		var cName = msg.params.name;
		if( this.handles[cName] == null || this.handles[cName] == undefined ){
			return;
		}
		for( var i in this.handles[cName] ) {
			var callback = this.handles[cName][i];
			if(callback){
				callback( cName, this.components[cName]);
			}
		}
	},
	
	bindComponentReady: function(cName, callback){
		if( this.handles[cName] == null || this.handles[cName] == undefined ){
			this.handles[cName] = [];
		} 
		this.handles[cName][this.handles[cName].length] = callback; 
	},
	
	requestComponent: function( name, container, callback ){
		var component = this.components[name];
		if( component == null || component == undefined ){
			this.bindComponentReady(name, callback);
			this.publish(this, 'loadComponent', {
				name: name,
				container: container
			});
			
		} else {
			if(callback){
				callback(name, component);
			}
		}
	},
	
	loop: function(){
		var self = this;
		// proceed queue
		while(this.queue.length > 0){
			var msg = this.queue.shift();
			var sLst = this.subscriptions[msg.msg];
			if(sLst == null || sLst == undefined || sLst.length == 0){
				continue;
			}
			for( var i in sLst ) {
				var tgt = sLst[i];
				if(tgt.callback == null || tgt.callback == undefined ){
					continue;
				}
				tgt.callback.call(tgt.target, {
					sender: msg.sender,
					msg: msg.msg,
					params: msg.params
				});
			}
		}
		
		// set timer
		this.loopTimerId = window.setTimeout(function(){
			self.loop();
		}, 100);
	},
		
	runDesktop: function(){
		this.requestComponent('desktop', null, function(name, componentObj){
			componentObj.run();
		});
	}
};

// component base class

ShadowBudget.Component = function() {
	
};

ShadowBudget.Component.prototype = {
	init: function( callback ){
		return true;
	},
	
	_init: function( container, readyCallback ) {
		this.container = container;
		if( this.init(readyCallback) ){
			if(readyCallback){
				readyCallback();
			}
		}
	},
	
	publish: function( msg, params ){
		ShadowBudget.publish( this, msg, params );
	},
	
	subscribe: function( msg, callback ){
		ShadowBudget.subscribe( this, msg, callback );
	},
	
	messageBox: function( title, body, type ){
		ShadowBudget.messageBox(title,body,type);
	}
};

// component loader
ShadowBudget.ComponentLoader = function(){
};

ShadowBudget.extend(ShadowBudget.ComponentLoader, ShadowBudget.Component, {
	init: function(readyCallback){
		var self = this;
		this.subscribe('loadComponent', function(msg){
			var cName = msg.params.name;
			this.loadComponent(msg.params.name, msg.params.container, function(){
				self.publish('loadedComponent',{name: cName});
				
				if(msg.params.callback != null && msg.params.callback != undefined){
					msg.params.callback.call(msg.sender);
				}
			});
		});
	},
	
	loadComponent: function( moduleName, container, readyCallback ){
		var self = this;
		$.ajax({
			url: 'chost?component='+moduleName,
			success: function( data ){
				if(!data.status) {
					self.messageBox('Component load error', data.message, 'error');
					return;
				}
				if(container == null || container == undefined ){
					container = $('body');
				}
				var component = $('<div id="'+moduleName+'" class="component"></div>');
				component.css('display','none');
				ShadowBudget.containers[moduleName] = component;
				container.append(component);
				component.append(data.response.data);
				if(readyCallback){
					readyCallback();
				}
			},
			dataType: 'json'
		});
	},

});

$( function(){
	ShadowBudget.run();
});
