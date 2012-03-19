ShadowBudget.AdminPage = function(){
};

ShadowBudget.extend(ShadowBudget.AdminPage, ShadowBudget.Component, {
	init: function(){
		this.container.css({
			'display':''
		});
		return true;
	}
});

ShadowBudget.registerComponent('padmin', new ShadowBudget.AdminPage());