ShadowBudget.Desktop = function(){
};

ShadowBudget.extend(ShadowBudget.Desktop, ShadowBudget.Component, {
	run: function(){
		var self = this;
		this.container.fadeIn();
		self.handleResize();

		// center out menu
		var desktopMenu = this.container.find('#desktopMenu');
		desktopMenu.css('margin-left', ($('#desktopHeader').width() - desktopMenu.outerWidth(true))/2);
/*		
		ShadowBudget.defer(function(){
			self.handleResize();
		});
*/		
	},
	
	init: function( readyCallback ){
		var self = this;
		self.initCallback = readyCallback;
		this.container.css({
			'visibility': 'hidden',
			'display': ''
		});
		
		self.pages = {};
		$('#pageContainer .page').each( function( idx, el ){
			var elt = $(el);
			self.pages[elt.attr('component')] = elt;
		});
		$('.menuitem').click( function(evt){
			var tgt = $(evt.currentTarget);
			var pageName = tgt.attr('component');
			self.switchPage(pageName);
			var curActive = $('#desktopMenu .menuitem.active');
			curActive.find('.note').fadeOut(function(){
				curActive.removeClass('active');
			});
			
			tgt.find('.note').fadeIn( function(){
				tgt.addClass('active');
			});
		});
		var activeItem = null;
		activeItem = $('#desktopMenu .menuitem.active'); 
		if(activeItem.length == 0){
			activeItem = $('#desktopMenu .menuitem:first');
			activeItem.addClass('active');
		} 
		var activePageName = activeItem.attr('component');
		self.switchPage(activePageName);
		
		// set page container height
		self.handleResize();
		
		$(window).resize( function(){
			self.handleResize();
		});
		
		self.loadPages();
		
		return false;
	},
	
	loadPages: function(){
		var self = this;
		for( var pName in this.pages ){
			var pageContainer = this.pages[pName];
			ShadowBudget.requestComponent(pName,pageContainer, function(name, component){
				self.componentReady(name, component);
			});
		}
	},
	
	componentReady: function( name, component ){
		if( this.pageComponents == null || this.pageComponents == undefined ) {
			this.pageComponents = {};
		}
		this.pageComponents[name] = component;
		for(var reqPage in this.pages){
			if(this.pageComponents[reqPage] == null || this.pageComponents[reqPage]==undefined){
				return;
			}
		}
		// everithing is loaded
		this.container.css({
			'visibility': 'visible',
			'display': 'none'
		});
		this.initCallback();
		// init complete
	},
	
	handleResize: function(){
		var body = $('body');
		var delta = parseInt(body.css('margin-bottom')) + parseInt(body.css('margin-top'));
		var windowHeight = $(window).height();
		console.log('window height: ' + windowHeight);
		$('#pageContainer').height(windowHeight - $('#desktopHeader').outerHeight(true) - delta - 1);
	},
	
	switchPage: function(pageName){
		var oldActivePage = $('#pageContainer .page.active');
		if(oldActivePage.length > 0){
			oldActivePage.fadeOut( function(){
				oldActivePage.removeClass('active');
			});
		}
		var newActivePage = this.pages[pageName];
		
		if( this.container.is(':visible') ) {
			newActivePage.fadeIn(function(){
				newActivePage.addClass('active');
			});
		} else {
			newActivePage.addClass('active');
		}
	}
	
});

ShadowBudget.registerComponent('desktop', new ShadowBudget.Desktop());