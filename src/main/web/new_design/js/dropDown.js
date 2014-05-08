(function($) {
	var dropDown = function(elem, userOptions) {
		var defaults = {
				showInitial: false,
				navButtonId: 'nav_button',
				
				dropDownClass: 'dropdown',
				mainMenuClass: 'main_menu',
				subMenuClass: 'sub_menu',
				
				subMenuDataAtt: 'data-catid',
				
				subMenuDataClass: 'sub_menu_data',
				
				selectionClass: 'select'
		},
		options = $.extend(defaults, userOptions);
		
		var navButton = $("#"+options.navButtonId),
			dropMenu = $(elem.children("."+options.dropDownClass).first()),
			mainMenu = $(dropMenu.children("."+options.mainMenuClass)),
			mainMenus = $(mainMenu.children('ul').children('li')),
			subMenu = $(dropMenu.children("."+options.subMenuClass)),
			subMenus = subMenu.children("."+options.subMenuDataClass);
		
		var setStyles = function() {
			navButton.addClass(options.selectionClass);
			subMenu.css("height", mainMenu.css("height"));
		},
		
		selectedSub = null,
		
		hideMenu = function() {
			//console.log("hiding menu");
			hideSubMenu();
			dropMenu.hide();
			navButton.removeClass(options.selectionClass);
		},
		
		showMenu = function() {
			//console.log("showing menu");
			dropMenu.show();
			setStyles();
		},
		
		hideSubMenu = function() {
			if(selectedSub != null) {
				selectedSub.removeClass(options.selectionClass);
				$("#"+selectedSub.attr(options.subMenuDataAtt)).hide();
				selectedSub = null;
			}
			subMenu.hide();
		},
		
		showSubMenu = function() {
			var obj = $(this);
			if(subMenu.css("display") == "none") {
				subMenu.css("display", "inline-block");
			}
			if(selectedSub != null) {
				selectedSub.removeClass(options.selectionClass);
				$("#"+selectedSub.attr(options.subMenuDataAtt)).hide();
			}
			selectedSub = obj;
			selectedSub.addClass(options.selectionClass);
			
			$("#"+selectedSub.attr(options.subMenuDataAtt)).show();
		},
		
		bindEvents = function() {
			if(!options.showInitial)
				elem.hover(showMenu, hideMenu);
			else
				elem.mouseleave(hideSubMenu);
			
			mainMenus.each(function(){
				$(this).on('mouseover', showSubMenu);
			});
		},
		
		init = function() {
			if(options.showInitial) {
				showMenu();
			}
			bindEvents();
		};
		
		init();
	};
	
	$.fn.dropDown = function(userOptions) {
		return this.each(function(){
			var	elem = $(this),
				dropdown = new dropDown(elem, userOptions);
			
			elem.data('dropDown', dropdown);
		});
	};
})(jQuery);