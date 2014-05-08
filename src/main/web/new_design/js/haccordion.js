/* script for the horizontal accordion */
(function($){
	var hAccordion = function(elem, userOptions){
		var defaults = {
				containerWidth: null,
				containerHeight: null,
				mainHeaderWidth: null,
				headerWidth: null,
				
				activateOn: 'click',
				firstSlide: 1,
				slideApeed: 800,
				onTriggerSlide: function(){},	//callback on slide activate
				onSlideAnimComplete: function(){},	//callback on slide animation complete
				
				easing : 'swing'
		};
		
		var options = $.extend(defaults, userOptions);
		
		//console.log(options);
	
		var	slides = null,
			header = null,
			slideLen = null,
			slideWidth = null;
		
		var setStyles = function() {
			options.containerWidth = elem.width();
			options.containerHeight = elem.height();
			options.mainHeaderWidth = elem.children('.div_head').outerHeight(true);
			options.headerWidth = elem.children('ol').children('li').children(':first-child').first().outerHeight(true);
			
			slides = elem.children('ol').children('li'),
			header = slides.children(':first-child'),
			slideLen = slides.length,
			slideWidth = options.containerWidth - (slideLen * options.headerWidth) - (options.mainHeaderWidth); //1 is for the border size
			
			setSlidePositions();
			return;
		},
		
		setSlidePositions = function() {
			var selected = header.filter('.selected_accord');
			
			if(!selected.length)
				header.eq(options.firstSlide - 1).addClass('selected_accord');
			
			header.each(function(index, value){
				var	$this = $(this),
					left = (index * options.headerWidth) + options.mainHeaderWidth,
					margin = header.first().next(),
					offset = parseInt(margin.css('marginLeft'), 10) || parseInt(margin.css('marginRight'), 10) || 0;
				//console.log("offset = "+ offset);
				if(selected.length) {
					if(index > header.index(selected))
						left += slideWidth;
				} else {
					if(index >= options.firstSlide)
						left += slideWidth;
				}
				var padLeft = !index ? 0 : options.headerWidth;
				$this
					.css('left', left)
					.next()
						.width(slideWidth - offset - 1) //1 is to account for the border
						.css({left: (left+options.headerWidth), paddingLeft : padLeft });
			});
		},
		
		//currentSlide = options.firstSlide - 1,
		
		slideAnimCompleteFlag = false,
		
		animSlide = function(triggerTab) {
			var _this = this;
			//console.log("in single");
			if (typeof this.pos === 'undefined') this.pos = slideWidth;
			
			header.removeClass('selected_accord').filter(this.elem).addClass('selected_accord');
			
			if (!!this.index) {
                this.elem
                    .add(this.next)
                    .stop(true)
                    .animate({
                        left : this.pos + (this.index * options.headerWidth) + (options.mainHeaderWidth)
                    }, 
                        options.slideSpeed, 
                        options.easing,
                        function() { 
                            // flag ensures that fn is only called one time per triggerSlide
                            if (!slideAnimCompleteFlag) {
                                // trigger onSlideAnimComplete callback in context of sibling div (jQuery wrapped)
                                options.onSlideAnimComplete.call(triggerTab ? triggerTab.next : _this.prev.next());
                                slideAnimCompleteFlag = true;
                            }                                      
                        });                          

                    // remove, then add selected class
                    header.removeClass('selected_accord').filter(this.prev).addClass('selected_accord'); 
            }
		},
		
		animSlideGroup = function(triggerTab) {
			//console.log("in group");
			var group = ['left', 'right'];

            $.each(group, function(index, side) {
                var filterExpr, left;

                if (side === 'left')  {
                    filterExpr = ':lt(' + (triggerTab.index + 1) + ')';
                    left = 0;
                } else {
                    filterExpr = ':gt(' + triggerTab.index + ')';
                    left = slideWidth;
                }

                slides
                    .filter(filterExpr) 
                    .children('h2')                           
                    .each(function() {
                        var $this = $(this),
                            tab = {
                                elem : $this, 
                                index : header.index($this),
                                next : $this.next(),
                                prev : $this.parent().prev().children('h2'),
                                pos : left
                            };                               

                        // trigger item anim, pass original trigger context for callback fn
                        animSlide.call(tab, triggerTab);
                    });

            });

            // remove, then add selected class
            header.removeClass('selected_accord').filter(triggerTab.elem).addClass('selected_accord');
		},
		
		triggerSlide = function(e) {
			var	$this = $(this),
				tab = {
					elem : $this,
					index : header.index($this),
					next : $this.next(),
					prev : $this.parent().prev().children('h2'),
				};
			
			//currentSlide = tab.index;
			
			slideAnimCompleteFlag = false;
			
			options.onTriggerSlide.call(tab.next);
			
			if($this.hasClass('selected_accord') && $this.position().left < slideWidth / 2) {
				animSlide(tab);
			} else {
				animSlideGroup(tab);
			}
		},
		
		bindEvents = function() {
			if(options.activateOn === 'click') {
				header.on('click.horizontalAccordion', triggerSlide);
			} else if(options.activateOn === 'mouseover'){
				header.on('click.horizontalAccordion mouseover.horizontalAccordion', triggerSlide);
			}
			$(window).resize(function(){
				setStyles();
			});
		},
		
		init = function() {
			setStyles();
			bindEvents();
		};
		
		init();
	};
	
	$.fn.horizontalAccordion = function(userOptions) {
		
		return this.each(function(){
			var elem = $(this);
			
			accord = new hAccordion(elem, userOptions);
			elem.data('horizontalAccordion', accord);
		});
	};
})(jQuery);