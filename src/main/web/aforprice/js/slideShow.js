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
				
				circularThumbnails: false,
				
				defaultSlide: 1,
				commonSlide: null,
				
				userSlideDimensions: function(elem, container) {return;},
				slideChangeCallback: function(elemId) {return;},
				
				autoplayPeriod: 15000,
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
			setSlide($(selectedSlide).attr('data-slide'));
			
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
//				console.log($(this).outerWidth(true));
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
			
			if(options.selectionArrow) {
//				console.log("containerWidth");
//				console.log(containerWidth);
				selectionArrow.css("left", ((containerWidth/2) - 5)+"px");
			}
			
			var finalPos = -1*(thumbPosition.left + (thumbnailWidth/2) - (containerWidth/2));
			thumbRelContainer
				.stop()
				.animate({left: finalPos+"px"}, 500);
		},
		
		setCircular = function() {
			//Prepending and Appending thumbnails to the thumbnail Container
			thumbnails.clone().appendTo(thumbRelContainer);
			
			var prep = [];
			thumbnails.each(function(){
				prep.push($(this));
			});
			prep.reverse();
			
			for(var i=0; i<prep.length; i++) {
				$(prep[i]).clone().prependTo(thumbRelContainer);
			}
//			initializeVariables();
		},
		
		positionCircularThumbnails = function(start, end) {
			var difference = end - start;
//			console.log(difference);
			if(difference < 0) {
				for(var i=difference; i<0; i++) {
					thumbnails.last().prependTo(thumbRelContainer);
					thumbnails = thumbRelContainer.children('li');
				}
			} else if(difference > 0) {
				for(var i=0; i<difference; i++) {
					thumbnails.first().appendTo(thumbRelContainer);
					thumbnails = thumbRelContainer.children('li');
				}
			}
			
			var	thumb1Position = selectedSlide.position(),
				thumb2Position = thumbnails.eq(thumbnails.index(selectedSlide) + difference).position(),
				thumbnailWidth = selectedSlide.outerWidth(true),
				containerWidth = thumbContainer.width();
			
			if(options.selectionArrow)
				selectionArrow.css("left", ((containerWidth/2) - 5)+"px");
//			console.log(thumbRelContainer.position());
//			console.log(thumb1Position);
//			console.log(thumb2Position);
			
			var InitialPosition = thumbRelContainer.position().left - (thumb1Position.left - thumb2Position.left);
			thumbRelContainer.stop().css({left: InitialPosition+"px"});
			finalPos = -1*(selectedSlide.position().left + (thumbnailWidth/2) - (containerWidth/2));
			thumbRelContainer
				.stop()
				.animate({left: finalPos+"px"}, 500);
		},
		
		setSlideDimensions = function() {
			var widthRatio, heightRatio;
			//console.log("setSlideDimensions");
			slides.each(function(){
				widthRatio = Math.floor(($(this).attr('data-widthratio')/$(this).attr('data-widthparts')) * 100)/100;
				widthRatio = isNaN(widthRatio) ? 1 : widthRatio;
				heightRatio = Math.floor(($(this).attr('data-heightratio')/$(this).attr('data-heightparts')) * 100)/100;
				heightRatio = isNaN(heightRatio) ? 1 : heightRatio;
				
				$(this).width(slideContainer.innerWidth() * widthRatio);
				$(this).height(slideContainer.innerHeight() * heightRatio);
				//console.log(slideContainer.innerWidth() + "," + slideContainer.innerHeight());
				//console.log($(this));
				options.userSlideDimensions($(this), slideContainer);
			});
			
			if(options.commonSlide != null) {
				widthRatio = Math.floor(($(options.commonSlide).attr('data-widthratio')/$(options.commonSlide).attr('data-widthparts')) * 98)/100;
				widthRatio = isNaN(widthRatio) ? 1 : widthRatio;
				heightRatio = Math.floor(($(options.commonSlide).attr('data-heightratio')/$(options.commonSlide).attr('data-heightparts')) * 98)/100;
				heightRatio = isNaN(heightRatio) ? 1 : heightRatio;
				
				$(options.commonSlide).width(slideContainer.innerWidth() * widthRatio);
				$(options.commonSlide).height(slideContainer.innerHeight() * heightRatio);
			}
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
			options.slideChangeCallback(eleId);
		},
		
		setThumbnail = function(newSlide) {
			selectedSlide.removeClass(options.thumbSelectionClass);
			
			var oldSlide = selectedSlide;
			selectedSlide = newSlide;
			selectedSlide.addClass(options.thumbSelectionClass);
			
			if(options.circularThumbnails) {
				//Remove some thumbnails from the end/start and add them to the start/end
				var startIndex = thumbnails.index(oldSlide);
				var finalIndex = thumbnails.index(newSlide);
				positionCircularThumbnails(startIndex, finalIndex);
				thumbnails = thumbRelContainer.children('li');
			} else {
				positionThumbnails();
			}
			
			setSlide(selectedSlide.attr('data-slide'));
		},
		
		changeSlide = function(e) {
			e.preventDefault();
			clearTimeout(autoplayTimer);
			//var target = $(e.target);
			//console.log($(e.target)[0].tagName);
			newSlide = $(e.target)[0].tagName == "LI"? $(e.target) : $(e.target).parent();
//			console.log(newSlide);
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
		
		logVariables = function() {
			console.log("SlideContainer:");
			console.log(slideContainer);
			console.log("slides:");
			console.log(slides);
			
			console.log("thumbcontainer:");
			console.log(thumbContainer);
			console.log("thumbRelContainer:");
			console.log(thumbRelContainer);
			console.log("thumbnails:");
			console.log(thumbnails);
			
			console.log("nextButton:");
			console.log(nextButton);
			console.log("prevButton:");
			console.log(prevButton);
			
			console.log("selectedSlide:");
			console.log(selectedSlide);
			console.log("autoplayTimer:" + autoplayTimer);
			
			console.log("selectionArrow:");
			console.log(selectionArrow);
		},
		
		init = function() {
			initializeVariables();
//			logVariables();
			if(options.circularThumbnails) {
//				console.log("here");
				setCircular();
			}
			setStyles();
			
			if(options.circularThumbnails) {
//				initializeVariables();
				thumbnails = thumbRelContainer.children('li');
			}
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