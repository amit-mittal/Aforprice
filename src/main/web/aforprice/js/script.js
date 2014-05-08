/*
 * @author: Chirag Maheshwari
 * 
 * It contains the common js functions to be used
 * in most of the pages.
 * @TODO: clean up this code.
 * */
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
}

function initializeSearchBar() {
	//Search bar category selector
	$("#select_cat").bind('click', function(event){
		if($("#search_cat:visible").length == 0) {
			$("#search_cat").show();
			event.stopPropagation();
			$(document).bind('click', function(event) {
				if($(event.target).parents().index($("#search_cat")) == -1) {
					$("#search_cat").hide();
				}
			});
		} else {
			$("#search_cat").hide({
				duration: 0,
				complete: function() {
					$(document).unbind('click');
				}
			});
		}
	});
	$("#search_cat").bind('change', function(){
		if($(this).children('option:selected').attr('data-nickname') != "All")
			$("#selected_cat").html($(this).children('option:selected').html());
		else
			$("#selected_cat").html("All");
	});
}

function mostViewedResize() {
	/*var hello = $("#most_viewed");
	if($("#viewed_items").outerHeight(true) > 350) {
		while($("#viewed_items").outerHeight(true) > 350) {
			hello.children('ul').children(':first').remove();
		}
	} else {
		while($("#viewed_items").outerHeight(true) < 350) {
			hello.children('ul').append(hello.children('ul').children(':first').clone());
		}
		mostViewedResize();
	}*/
}

function mainContentResize(elem, container) {
	/*elem.children('.product_graph').first().width(container.innerWidth());
	elem.children('.product_graph').first().height(container.innerHeight());
	if(elem.children('.product_image').children('.arrow').hasClass('down')) {
		elem.children('.product_image').first().width(container.innerWidth() * 0.20);
		elem.children('.product_image').first().height(container.innerHeight() * 0.20);
	} else {
		elem.children('.product_image').first().width(container.innerWidth());
		elem.children('.product_image').first().height(container.innerHeight());
	}*/
}

function mainContentHover() {
	/*container = $("#slideshow").children('.slide_selected');
	container.children('.slide').each(function(){
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
	});*/
}

function subCategoryListExpandCollapse_resize() {
	/* Remove the previous settings */
	$("#expand_sub_category").off('click');
	$("#sub_cats").children('ul').children('li').each(function() {
		$(this).show();
	});

	/* Set again the settings */
	subCategoryListExpandCollapse();
}

function subCategoryListExpandCollapse() {
	var subCategoryList = $("#sub_cats").children('ul').children('li'),
		firstElement = subCategoryList.first();
		expandCollapseButton = $("#expand_sub_category"),
		needForExpandCollapse = false,
		hidingStartPosition = -1;

	/* First check if there is a need for expand/collapse button */
	if(firstElement != undefined)
		var firstElementOffset = firstElement.offset();

	for(var i=1; i<subCategoryList.length; i++) {
		/* If any element in the subcategory list has the same left position as the first element, then
		the list is starting from the initial position, i.e., a new line is being used which has to be
		hidden. */

		if($(subCategoryList[i]).offset().left == firstElementOffset.left) {
			needForExpandCollapse = true;
			hidingStartPosition = i;
			break;
		}
	}

	if(needForExpandCollapse == true) {
	/* When the expand/collapse feature is needed, hide all the extra list items and 
	click event handler to the expand collapse button. */
		expandCollapseButton.show();

		if(expandCollapseButton.hasClass('down_double')) {
			for(i=hidingStartPosition; i<subCategoryList.length; i++) {
				$(subCategoryList[i]).hide();
			}	
		}

		expandCollapseButton.on('click', 
			{'position': hidingStartPosition}, 
			function(event) {
			if(expandCollapseButton.hasClass('down_double')) {
				$(this).removeClass('down_double');
				$(this).addClass('up_double');

				for(i=0; i<subCategoryList.length; i++) {
					$(subCategoryList[i]).show();
				}
			} else {
				$(this).addClass('down_double');
		 		$(this).removeClass('up_double');

		 		for(i=event.data.position; i<subCategoryList.length; i++) {
					$(subCategoryList[i]).hide();
				}
			}
		});
	}
}

function bindGraphButtons() {

	/* First binding the hover events (color changing) */
	$('.history_icon').hover(function() {
		var iconImage = $(this).attr('src');
		iconImage = iconImage.replace(/.png/gi, "_hover.png");

		$(this).attr('src', iconImage);
	}, function() {
		var iconImage = $(this).attr('src');
		iconImage = iconImage.replace(/_hover.png/gi, ".png");

		$(this).attr('src', iconImage);
	});

	/* binding the price history graph */
	$('.price_history').on('click', function() {
		var productName = jQuery.trim($(this).parents('.terminal_product').find('.product_name').html()),
			priceHistory = $(this).attr('data-history');

		plotPriceHistory(productName, priceHistory);
	});

	$('.review_history').on('click', function() {
		var productName = jQuery.trim($(this).parents('.terminal_product').find('.product_name').html()),
			ReviewHistory = $(this).attr('data-history');

		plotReviewHistory(productName, ReviewHistory);
	});

	/* Chirag: uncommented it as the rank history icon is not being shown. */
	// $('.rank_history').on('click', function() {
	// 	var productName = jQuery.trim($(this).parent().parent().find('.product_name').html()),
	// 		RankHistory = $(this).attr('data-history');

	// 	plotSellRankHistory(productName, RankHistory);
	// });
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
		multipleSelection: multi,
		defaultRetailer: retailer
	});
	$('.stars').starify();
	$('#navigation').dropDown();
//	$('#news_slider').newsRotate();
});