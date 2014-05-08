/* Script for News rotator */
(function($){
	$.fn.newsRotate = function(userOptions) {
		
		var defaults = {
				wait: 1500
		};
		var options = $.extend(defaults, userOptions);
		
		var rotate = function(current) {
			var next = current.next().length ? current.next() : current.parent().children().first();
			current.hide();
			next.show();
			//console.log(next);
			setTimeout(function() {rotate(next);}, options.wait);
		};
		
		return this.each(function(){
			var obj = $(this).children().children().first();
			obj.show();
			setTimeout(function() {rotate(obj);}, options.wait);
		});
	};
})(jQuery);
/* Script for news Rotator ends */