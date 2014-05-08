(function($){
	
	var retailSelector = function(elem, userOptions){
		var defaults = {
				containerID: elem,
				logosDivId: "retailer_logos",
				logoSelectionClass: "selected",
				
				prevButtonId: "retail_prev_button",
				nextButtonId: "retail_next_button",
				
				selectAllButton: "retail_all",
				selectAllText: "Select all<br /><span class=\"bolder\">Retailers</span>",
				clearAllText: "Clear all<br /><span class=\"bolder\">Retailers</span>",
				buttonSelectionClass: "selected_button",
				
				defaultRetailer: "walmart",
				scrollSpeedPix: 5,
				scrollSpeedTime: 10,
				
				selectionCallback: function(selected) {
										console.log(selected);
									},
				multipleSelection: true,
				
				stickyClass: 'sticky_retail'
		},
		
		options = $.extend(defaults, userOptions);
		
		var logoWrapper = $("#"+options.logosDivId),
			logoContainer = logoWrapper.children('ul').first(),
			logos = logoContainer.children('li'),
			logoImages = logos.children('img'),
			prevButton = $("#"+options.prevButtonId),
			nextButton = $("#"+options.nextButtonId),
			selectAllButton = $("#"+options.selectAllButton),
			defaultRetailer = logos.filter("[data-select=\""+options.defaultRetailer+"\"]").children('img'),
			allRetailSelected = false,
			totalWidth = 0,
			selectedRetailer = null,
			stickySelected = null;
		
		
		var setStyles = function() {
			totalWidth = 0;
			logos.each(function(index, value){
				totalWidth += $(value).outerWidth(true);
			});
			logoContainer.width(totalWidth);
			while(logoContainer.height() > logoImages.first().outerHeight(true)) {
				totalWidth += 10;
				logoContainer.width(totalWidth);
			}
			//console.log(logoContainer.height());
			//console.log(logoImages.first().outerHeight(true));
			defaultRetailer.addClass(options.logoSelectionClass);
			selectedRetailer = defaultRetailer;
			
			if(!options.multipleSelection) {
				selectedRetailer.parent().hide();
				stickySelected = selectedRetailer.clone();
				stickySelected.addClass(options.stickyClass).prependTo(logoWrapper.parent());
			}
		},
		
		toggleAll = function(e) {
			if(!allRetailSelected) {
				selectAllButton
					.html(options.clearAllText)
					.addClass(options.buttonSelectionClass);
				
				logoImages.each(function(){
					$(this).addClass(options.logoSelectionClass);
				});
				allRetailSelected = true;
			} else {
				selectAllButton
					.html(options.selectAllText)
					.removeClass(options.buttonSelectionClass);
			
				logoImages.each(function(){
					$(this).removeClass(options.logoSelectionClass);
				});
				defaultRetailer.addClass(options.logoSelectionClass);
				allRetailSelected = false;
			}
			lexicographicOrder();
			options.selectionCallback(logoImages.filter("."+options.logoSelectionClass).parent());
		},
		
		toggleRetailer = function() {
			if(stickySelected)
				stickySelected.remove();
			if($(this).hasClass(options.logoSelectionClass)) {
				$(this).removeClass(options.logoSelectionClass);
			} else {
				$(this).addClass(options.logoSelectionClass);
			}
			if(selectedRetailer) {
				selectedRetailer.parent().show();
				selectedRetailer.removeClass(options.logoSelectionClass);
				selectedRetailer = null;
			}
			
			selectedRetailer = logoImages.filter("."+options.logoSelectionClass);
			
			if(selectedRetailer.length == 0) {
				defaultRetailer.addClass(options.logoSelectionClass);
				selectedRetailer = defaultRetailer;
			}
			
			//Sticky logo in the starting
			selectedRetailer.parent().hide();
			stickySelected = selectedRetailer.clone();
			stickySelected.addClass(options.stickyClass).prependTo(logoWrapper.parent());
			
//			console.log(logoWrapper.parent());
			lexicographicOrder();
			options.selectionCallback(selectedRetailer.parent());
		},
		
		scroll = function(e) {
			var button = null,
				change = 0;
			if(e.target.id == options.prevButtonId) {
				button = prevButton;
				change = options.scrollSpeedPix;
			}
			else if(e.target.id == options.nextButtonId) {
				button = nextButton;
				change = -1*options.scrollSpeedPix;
			}
			
			var timer = 0;
			
			button.off('mouseover');
			button.on('mouseout', function(e) {
				clearTimeout(timer);
				button.off('mouseout');
				button.on('mouseover', scroll);
			});
			
			var handler = function() {
				var left = parseInt(logoContainer.css("left"));
				console.log("change: "+change+", left"+left);
				
				if(((totalWidth - logoWrapper.width()) > Math.abs(left) && button == nextButton) 
						|| (Math.abs(left) > 0 && button == prevButton)) {
					left = left + change;
					logoContainer.css("left", left+"px");
					timer = setTimeout(function(){handler();}, options.scrollSpeedTime);
				}
			};
			
			handler();
		},
		
		selectRetailer = function() {
			if($(this).hasClass(options.logoSelectionClass)) {
				$(this).removeClass(options.logoSelectionClass);
				
				if(allRetailSelected) {
					selectAllButton
						.removeClass(options.buttonSelectionClass)
						.html(options.selectAllText);
					allRetailSelected = false;
				}
				
			} else {
				$(this).addClass(options.logoSelectionClass);
			}
			
			var count_all = logoImages.length,
				count_select = logoImages.filter("."+options.logoSelectionClass).length;
			
			if(count_all == count_select) {
				selectAllButton
					.html(options.clearAllText)
					.addClass(options.buttonSelectionClass);
				allRetailSelected = true;
			} else if(count_select == 0) {
				defaultRetailer.addClass(options.logoSelectionClass);
			}
			
			lexicographicOrder();
			options.selectionCallback(logoImages.filter("."+options.logoSelectionClass).parent());
		},
		
		bindEvents = function() {
			if(options.multipleSelection)
				selectAllButton.on('click', toggleAll);
			prevButton.on('mouseover', scroll);
			nextButton.on('mouseover', scroll);
			
			if(options.multipleSelection) {
				logoImages.each(function(index, value) {
					$(value).on('click', selectRetailer);
				});
			} else {
				logoImages.each(function(index, value) {
					$(value).on('click', toggleRetailer);
				});
			}
		},
		
		lexicographicOrder = function() {
			var retailers = new Array(),
				i = 0;
			
			logos.each(function(){
				if($(this).children('img').hasClass(options.logoSelectionClass))
					retailers[i] = "1"+$(this).attr("data-select");
				else
					retailers[i] = "2"+$(this).attr("data-select");
				i++;
			});
			retailers.sort();
			//console.log(retailers);
			
			for(i=0; i<retailers.length; i++) {
				logos.filter('[data-select="'+retailers[i].substring(1)+'"]').appendTo(logoContainer);
			}
		},
		
		init = function() {
			setStyles();
			bindEvents();
			lexicographicOrder();
		};
		
		init();
	};
	
	$.fn.retailerSelection = function(userOptions) {
		return this.each(function() {
			var elem = $(this);
			selector = new retailSelector(elem, userOptions);
			
			elem.data('retailSelector', selector);
		});
	};
	
})(jQuery);