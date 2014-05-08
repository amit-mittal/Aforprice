/* 
 * @author: Chirag Maheshwari
 * 
 * this jQuery plugin will covert every 
 * division for which it is called for(mainly
 * for elements with class "stars" as defined in the css)
 * into a "reviewing star".
 * three arguments are needed, 
 * data-type = "normal", "big"
 * 
 * data-rating = a float value
 * 
 * data-show = used to show a review or take an review,
 * if data-show is not set, it will be assumed as true.
 * (still left to be implemented)
 * 
 * the number of stars = 5
 * 
 * the css for the same is defined in starify.css
 */

(function($){
	var	widths = {
		'big': 47,
		'normal': 21
	},
		heights = {
			'big': 46,
			'normal': 20
	};
	
	var	reviewStars = function(elem) {
		var	options = {
				fillingClass: 'filling',
				type: 'normal',
				
				number: 5,
				show: true
		};
		
		var	filling = null,
			rating = parseFloat(elem.attr('data-rating'));
		
		var	setupDom = function(){
			options.type = elem.attr('data-type');
			
			elem.addClass(options.type);
			
			jQuery('<div/>',{
			}).appendTo(elem);
			filling = elem.children('div').first();
			filling.addClass(options.fillingClass);
//			console.log(filling);
		},
		
		setupDimensions = function() {
			elem.height(heights[options.type]);
			elem.width(widths[options.type] * options.number);
			
			filling.height(heights[options.type]);
			filling.width(widths[options.type] * rating);
		},
		
		init = function() {
			setupDom();
			setupDimensions();
		};
		
		init();
	};
	
	$.fn.starify = function() {
		return this.each(function(){
			var	elem = $(this);
				review = new reviewStars(elem);
				
			elem.data('review', review);
		});
	};
})(jQuery);