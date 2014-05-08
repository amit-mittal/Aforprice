(function($){
	
	var slideShow = function(elem, userOptions) {
		var defaults = {
				slideContainerClass: 'slide_selected',
				slideClass: 'slide',
				thumbnailContainerClass: 'slide_thumbnail',
				
				navButtons: true,
				nextButtonClass: 'slideNext',
				prevButtonClass: 'slidePrev',

				selectionArrow: false,
				selectionsArrowId: 'slide_select_arrow',
				
				thumbSelectionClass: 'selected',
				
				defaultSlide: 1,
				
				userSlideDimensions: function(elem, container) {return;},
				
				autoplayPeriod: 7500,
				autoplay: false,
				playing: false
		},
		options = $.extend(defaults, userOptions);
		
		var	slideContainer = null, slides = null,
			thumbContainer = null, thumbRelContainer = null, thumbnails = null,
			nextButton = null, prevButton = null,
			selectedSlide = null, autoplayTimer = 0,
			selectionArrow = null;
		var thumbRelWidth = 0;
		
		var initializeVariables = function() {
			slideContainer = elem.children("."+options.slideContainerClass),
			slides = slideContainer.children("."+options.slideClass),
			thumbContainer = elem.children("."+options.thumbnailContainerClass),
			thumbRelContainer = thumbContainer.children('ul').first(),
			thumbnails = thumbRelContainer.children('li');
			selectedSlide = thumbnails.eq(options.defaultSlide - 1);
			
			if(options.navButtons) {
				nextButton = elem.children("."+options.nextButtonClass),
				prevButton = elem.children("."+options.prevButtonClass);
			}
			if(options.selectionArrow) {
				selectionArrow = thumbContainer.children("#"+options.selectionsArrowId);
			}
		},
		
		setStyles = function() {
			if(!selectedSlide) {
				selectedSlide = thumbnails.eq(options.defaultSlide - 1);
			}
			selectedSlide.addClass(options.thumbSelectionClass);
			
			thumbnails.each(function() {
				thumbRelWidth += $(this).outerWidth(true);
			});
//			console.log(thumbRelWidth);
			positionThumbnails();
			
			setSlideDimensions();
		},
		
		positionThumbnails = function() {
//			console.log("positionThumbnails");
			if(thumbRelWidth <= thumbContainer.width()) {
				var finalPos = (thumbContainer.width()/2) - (thumbRelWidth/2);
//				console.log(finalPos);
				thumbRelContainer
					.stop()
					.animate({left: finalPos+"px"}, 500);
				return;
			}
			
			var	thumbPosition = selectedSlide.position(),
				thumbnailWidth = selectedSlide.outerWidth(true),
				containerWidth = thumbContainer.width();
			
			if(options.selectionArrow)
				selectionArrow.css("left", ((containerWidth/2) - 5)+"px");
			
			var finalPos = -1*(thumbPosition.left + (thumbnailWidth/2) - (containerWidth/2));
			thumbRelContainer
				.stop()
				.animate({left: finalPos+"px"}, 500);
		},
		
		setSlideDimensions = function() {
//			console.log("setSlideDimensions");
			slides.each(function(){
				$(this).width(slideContainer.innerWidth());
				$(this).height(slideContainer.innerHeight());
				options.userSlideDimensions($(this), slideContainer);
			});
		},
		
		getNextSlide = function() {
			var nextSlide;
			if(selectedSlide) {
				nextSlide = selectedSlide.next();
				if(!nextSlide.length)
					nextSlide = thumbnails.eq(0);
			} else {
				nextSlide = thumbnails.eq(0);
			}
			return nextSlide;
		},
		
		getPrevSlide = function() {
			var prevSlide;
			if(selectedSlide) {
				prevSlide = selectedSlide.prev();
				if(!prevSlide.length)
					prevSlide = thumbnails.eq(thumbnails.length - 1);
			} else {
				prevSlide = thumbnails.eq(thumbnails.length - 1);
			}
			return prevSlide;
		},
		
		setPrevSlide = function(e) {
			e.preventDefault();
			var newSlide = getPrevSlide();
			setThumbnail(newSlide);
		},
		
		setNextSlide = function(e) {
			e.preventDefault();
			var newSlide = getNextSlide();
			setThumbnail(newSlide);
		},
		
		setNextSlideAuto = function() {
			var newSlide = getNextSlide();
			setThumbnail(newSlide);
			autoplayTimer = setTimeout(setNextSlideAuto, options.autoplayPeriod);
		},
		
		setSlide = function(eleId) {
			slideContainer.children('.slide:visible').hide();
			$("#"+eleId).show();
		},
		
		setThumbnail = function(newSlide) {
			selectedSlide.removeClass(options.thumbSelectionClass);
			
			selectedSlide = newSlide;
			selectedSlide.addClass(options.thumbSelectionClass);
			positionThumbnails();
			
			setSlide(selectedSlide.attr('data-slide'));
		},
		
		changeSlide = function(e) {
			e.preventDefault();
			clearTimeout(autoplayTimer);
			//var target = $(e.target);
			//console.log($(e.target)[0].tagName);
			newSlide = $(e.target)[0].tagName == "LI"? $(e.target) : $(e.target).parent();
			console.log(newSlide);
			setThumbnail(newSlide);
		},
		
		bindEvents = function() {
			thumbnails.each(function(){
				$(this).on('click', changeSlide);
			});
			
			$(window).resize(function() {
				positionThumbnails();
				setSlideDimensions();
			});
			
			if(options.navButtons) {
				nextButton.on('click', setNextSlide);
				prevButton.on('click', setPrevSlide);
			}
		},
		
		play = function() {
			options.playing = true;
			autoplayTimer = setTimeout(setNextSlideAuto, options.autoplayPeriod);
		},
		
//		logVariables = function() {
//			console.log("SlideContainer:");
//			console.log(slideContainer);
//			console.log("slides:");
//			console.log(slides);
//			
//			console.log("thumbcontainer:");
//			console.log(thumbContainer);
//			console.log("thumbRelContainer:");
//			console.log(thumbRelContainer);
//			console.log("thumbnails:");
//			console.log(thumbnails);
//			
//			console.log("nextButton:");
//			console.log(nextButton);
//			console.log("prevButton:");
//			console.log(prevButton);
//			
//			console.log("selectedSlide:");
//			console.log(selectedSlide);
//			console.log("autoplayTimer:" + autoplayTimer);
//			
//			console.log("selectionArrow:");
//			console.log(selectionArrow);
//		},
		
		init = function() {
			initializeVariables();
//			logVariables();
			setStyles();
			bindEvents();
			if(options.autoplay)
				play();
		};
		
		init();
	};
	
	$.fn.slideshow = function(userOptions) {
		return this.each(function(){
			var elem = $(this),
				slider = new slideShow(elem, userOptions);
			
			elem.data('slideshow', slider);
		});
	};
})(jQuery);