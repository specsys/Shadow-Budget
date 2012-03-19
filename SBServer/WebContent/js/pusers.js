ShadowBudget.UsersPage = function(){
};

ShadowBudget.extend(ShadowBudget.UsersPage, ShadowBudget.Component, {
	init: function(readyCallback){
		this.container.css({
			'display':''
		});

		ShadowBudget.defer(function(){
			readyCallback();
		});
		
		return false;
	}
});

ShadowBudget.registerComponent('pusers', new ShadowBudget.UsersPage());