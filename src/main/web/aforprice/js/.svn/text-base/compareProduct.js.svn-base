/*
 * @author: Chirag Maheshwari
 * 
 * This file contains the jQuery extension 
 * for adding the compare bar functionality
 * to the pages
 * 
 * dependent on the Google graphs API given in googleCharts.js
 * for making the graphs.
 */
(function($) {
	
	var compareProduct = function(elem, userOptions) {
		var defaults = {
				maxProducts: 4,
				
				checkboxClass: 'add_prod_compare',
				prodListClass: 'compare_list',
					
				productImage: 'product_image',
				products: 'terminal_product',

				compareButtonClass: 'compare_button'
		},
		options = $.extend(defaults, userOptions);
		
		var products = $('.'+options.products),
			productImages = products.find('.'+options.productImage),
			compareList = $('.'+options.prodListClass).children('ul'),
			checkBoxes = products.find('.'+options.checkboxClass),
			compareButtons = $('.'+options.compareButtonClass),
			comparedProducts = new Array(),
			productIdList = new Array(),
			totalCompare = 0;
		
		
		var refreshCompareList = function() {
			var compareList1 = compareList.first().children('li'),
				compareList2 = compareList.last().children('li');
			
			for(i=0; i<options.maxProducts; i++) {
				$(compareList1[i]).css('background-image', '');
				$(compareList1[i]).children('img').remove();
				$(compareList2[i]).css('background-image', '');
				$(compareList2[i]).children('img').remove();
			}
			
			var productIndex;
			for(i in comparedProducts) {
				productIndex = comparedProducts[i];
				
				$(compareList1[i]).css('background-image', "url('"+ $(productImages[productIndex]).attr('src') +"')");
				var closeButton = jQuery('<img/>', {
					src : "img/close.png",
					onclick: "removeCompare("+productIndex+")"
				});
				closeButton.appendTo($(compareList1[i]));
				$(compareList2[i]).css('background-image', "url('"+ $(productImages[productIndex]).attr('src') +"')");
				closeButton.clone().appendTo($(compareList2[i]));
			}
		}
		
		removeCompare = function(index) {
			var position = comparedProducts.indexOf(index),
				positionOfProductId = productIdList.indexOf($(checkBoxes[index]).attr('id'));
			
			$(checkBoxes[index]).attr('checked', false);
			if(position == -1)
				return;
			else {
				totalCompare--;
				comparedProducts.splice(position, 1);
				refreshCompareList();

				/* Removing it from the product ID list */
				productIdList.splice(positionOfProductId, 1);
			}
		},
		
		addCompare = function(index) {
			if(comparedProducts.indexOf(index) == -1) {
				totalCompare++;
				comparedProducts.push(index);
				refreshCompareList();

				/* adding it to the list of product ID's */
				productIdList[index] = $(checkBoxes[index]).attr('id');
			} else
				return;
		},
		
		changeCompare = function(index) {
			if($(checkBoxes[index]).attr('checked') === undefined)
				removeCompare(index);
			else if($(checkBoxes[index]).attr('checked') == 'checked') {
				if(totalCompare == options.maxProducts) {
					$(checkBoxes[index]).attr('checked', false);
					alert('No more products can be added to compare.');
				} else {
					addCompare(index);
				}
			}
		},

		compareProducts = function() {
			var parentEle,
				priceHistory,
				name,
				historyArray = new Array(),
				nameArray = new Array();

			/* First getting their price history arrays. */
			for(i in productIdList) {
				/* Find the parent element upto terminal product */
				parentEle = $('#'+productIdList[i]).parents('.'+options.products);
				priceHistory = parentEle.find('.price_history').attr('data-history');
				name = 'pr('+productIdList[i]+')';

				historyArray.push(priceHistory);
				nameArray.push(name);
			}

			compareProductGraphs(historyArray, nameArray);
		},
		
		bindEvents = function() {
			/* Adding event handlers to the checkboxes */
			$(checkBoxes).each(function(index, value) {
				$(this).change(function() {
					changeCompare(index);
				});
			});

			/* Adding event handlers to the compare button */
			$(compareButtons).on('click', function() {
				compareProducts();
			});
		},
		
		init = function() {
			bindEvents();
		};
		
		init();
	};
	
	$.fn.compareProduct = function(userOptions) {
		return this.each(function(){
			var	elem = $(this),
				compare = new compareProduct(elem, userOptions);
			
			elem.data('compare', compare);
		});
	};
})(jQuery);