/* starts the scripts on-load */
var viewportHeight, viewportWidth,
	bodyWidth, bodyHeight;

function calcDimensions() {
	viewportHeight = $(window).height();
	viewportWidth = $(window).width();
	//console.log("viewport Height: "+viewportHeight+"px, viewport Width: "+viewportWidth);
	
	bodyHeight = $('body').height();
	bodyWidth = $('body').width();
	//console.log("body Height: "+bodyHeight+"px, body Width: "+bodyWidth);
}

function setHeaderElementSizes() {
	var retailerLogosWidth, searchBarWidth;
	
	if(viewportWidth > parseInt($('body').css('minWidth'))) {
		retailerLogosWidth = Math.floor(viewportWidth - RLW);
		searchBarWidth = Math.floor(viewportWidth - 550);
	} else {
		retailerLogosWidth = Math.floor(bodyWidth - RLW);
		searchBarWidth = Math.floor(bodyWidth - 450);
	}
	$("#searchBar").width(searchBarWidth);
	$("#retailer_logos").width(retailerLogosWidth);
	
	$("#pricealert_thumb").width($("#pricealert_thumb").parent().innerWidth() - 75);
}

function initializeSearchBar() {
	//Search bar category selector
	$("#select_cat").bind('click', function(){
		if($("#search_cat:visible").length == 0) {
			$("#search_cat").show();
		} else {
			$("#search_cat").hide();
		}
	});
	$("#search_cat").bind('change', function(){
		$("#selected_cat").html($(this).val());
	});
}

function mostViewedResize() {
	var hello = $("#most_viewed");
	if($("#viewed_items").outerHeight(true) > 350) {
		while($("#viewed_items").outerHeight(true) > 350) {
			hello.children('ul').children(':first').remove();
		}
	} else {
		while($("#viewed_items").outerHeight(true) < 350) {
			hello.children('ul').append(hello.children('ul').children(':first').clone());
		}
		mostViewedResize();
	}
}

function mainContentResize(elem, container) {
	elem.children('.product_graph').first().width(container.innerWidth());
	elem.children('.product_graph').first().height(container.innerHeight());
	elem.children('.product_image').first().width(container.innerWidth());
	elem.children('.product_image').first().height(container.innerHeight());
}

function mainContentHover() {
	container = $("#slideshow").children('.slide_selected');
	container.children('.slide').each(function(){
		$(this).children('.product_image').children('.arrow').on('mouseover', function(){
			if(!$(this).hasClass('down')) {
				container.children('.slide').children('.product_image').each(function(){
					$(this)
						.stop()
						.animate({height: container.height()*0.20, width: container.width()*0.20, left: 10, top: 10}, 750);
					$(this).children('.arrow').first().addClass('down');
				});
			}
		});
		
		$(this).children('.product_image').children('.arrow').on('click', function(){
			if($(this).hasClass('down')) {
				container.children('.slide').children('.product_image').each(function(){
					$(this)
						.stop()
						.animate({height: container.height(), width: container.width(), left: 0, top: 0}, 750);
					$(this).children('.arrow').first().removeClass('down');
				});
			} else {
				container.children('.slide').children('.product_image').each(function(){
					$(this)
						.stop()
						.animate({height: container.height()*0.20, width: container.width()*0.20, left: 10, top: 10}, 750);
					$(this).children('.arrow').first().addClass('down');
				});
			}
		});
	});
	/*container.children('.slide').each(function(){
		$(this).children('.product_image').children('.arrow').on('mouseover', function(){
			if($(this).hasClass('down')) {
				$(this).parent()
					.stop()
					.animate({height: container.height(), width: container.width(), left: 0, top: 0}, 750);
				$(this).removeClass('down');
			} else {
				$(this).parent()
					.stop()
					.animate({height: container.height()*0.20, width: container.width()*0.20, left: 10, top: 10}, 750);
				$(this).addClass('down');
			}
		});
	});*/
}

$(document).ready(function(){
	calcDimensions();
	setHeaderElementSizes();
	pageSpecificResize();
	mostViewedResize();
	$(window).resize(function(){
		calcDimensions();
		setHeaderElementSizes();
		mostViewedResize();
		pageSpecificResize();
	});
	
	pageSpecificFunctions();
	initializeSearchBar();
	$("#retail_select").retailerSelection({
		multipleSelection: multi
	});
	$('.stars').starify();
	$('#navigation').dropDown();
	$('#news_slider').newsRotate();
});