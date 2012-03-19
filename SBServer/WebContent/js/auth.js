ShadowBudget.Auth = function(){
};

ShadowBudget.extend(ShadowBudget.Auth, ShadowBudget.Component, {
	init: function(){
		var self = this;
		this.container.css({
			'visibility':'hidden',
			'display':''
		});

		var btn = $('#loginButton').button();
		btn.css('margin-left', - btn.width()/2);
		self.userBox = $('input[name="user"]');
		self.pwdBox = $('input[name="password"]');

		this.container.css({
			'visibility':'visible',
			'display':'none'
		});

		self.userBox.keypress( function(evt){
			if(evt.which==13){
				self.pwdBox.focus();
			}
		});
		
		self.pwdBox.keypress( function(evt){
			if(evt.which==13){
				self.postData();
			}
		});
		
		btn.click(function(){
			self.postData();
		});
		
		return true;
	},
	
	run: function( callback ){
		this.callback = callback;
		this.container.fadeIn();
		this.userBox.focus();
	},
	
	postData: function(callback){
		var self = this;
		var userName = this.userBox.val();
		var pwd = this.pwdBox.val();
		$.ajax({
			type: 'post',
			url: 'auth',
			data: {
				cmd: 'login',
				user: userName,
				pwd: pwd
			},
			dataType: 'json',
			success: function(data){
				if(!data.status){
					if(data.errorType =='WrongAuthenticationException'){
						// say no no no
						self.sayNoNoNo();
					} else {
						// internal error - show msg box
						ShadowBudget.messageBox(data.errorType, data.message, 'error');
					}
					return;
				}
				
				ShadowBudget.user = data.response;
				self.container.fadeOut(300, function(){
					if(self.callback){
						self.callback();
					}
				});
			}
		});
		
	},
	
	sayNoNoNo: function(){
		var self = this;
		var loginForm = this.container.find('#loginForm');
		loginForm.animate({'margin-left':'-=100'},100);
		loginForm.animate({'margin-left':'+=200'},100);
		loginForm.animate({'margin-left':'-=200'},100);
		loginForm.animate({'margin-left':'+=200'},100);
		loginForm.animate({'margin-left':'-=100'},100, function(){
			self.pwdBox.val('');
			self.userBox.focus();
			self.userBox.select();
		});
	}
});

ShadowBudget.registerComponent('auth', new ShadowBudget.Auth());

