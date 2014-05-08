/*
 * @author: Chirag Maheshwari
 * 
 * This contains the code to maintain the category tree in the
 * drop down menu and the search box drop down.
 * 
 * It changes the category tree using an ajax query to the
 * server.
 * */
var Categories = {
		retailer: null,
		/*
		 * Request variables required
		 */
		xmlhttp: null,
		result: null,
		retailImage: null,
		ajaxTimeout: null,
		
		setRetailer: function(retail) {
			/*
			 * As this is the first function to be called
			 * when a request to a category is made
			 * */
			date = new Date();
			this.retailer = retail;
		},
		
		setCategories: function() {
			var mainDiv = $("#categories");
				mainDiv.html("");
			var mainCats = jQuery('<div/>',{
				'class': 'main_menu'
			}).appendTo(mainDiv);
			var subMenu = jQuery('<div/>',{
				'class': 'sub_menu',
				'style': 'position: relative;'
			}).appendTo(mainDiv);
			
			mainCats.html("<div style=\"border-top: 1px solid #0066a8; margin: 10px 5px 0px; font-weight: normal; padding: 10px;\"><a href=\"categoryDirectory.php?retailer="+this.retailer+"\">Full Category Directory</a></div>");
			mainCats = jQuery('<ul/>',{}).prependTo(mainCats);
			for(i=1; i<this.result.length; i++) {
				var temp_cat = jQuery('<li/>',{
					'data-catid': 'cat_'+i
				}).appendTo(mainCats);
				temp_cat.html("<a href=\"category.php?retailer="+this.retailer+"&category="+this.result[i].ID+"\">"+this.result[i].name+"</a><div class=\"rightArrow\">");
			}
			
			subMenu.html("<div class=\"menu_divider\" style=\"position: absolute; height: 100%; left: 0px; top: 0px; border-left: 1px solid #DEDEDE; border-right: 2px solid #F7F7F7;\">");
			for(i=1; i<this.result.length; i++) {
				var temp_div = jQuery('<div/>',{
					'id': 'cat_'+i,
					'class': 'sub_menu_data'
				}).appendTo(subMenu);
				temp_div = jQuery('<ul/>',{}).prependTo(temp_div);
				
				if(this.result[i].child != null) {
					var children = this.result[i].child;
					for(j=0; j<children.length; j++) {
						if(children[i] === undefined)
							continue;
						console.warn("children");
						console.log(children[i]);
						var temp_cat = jQuery('<li/>',{	}).appendTo(temp_div);
						if(!children[i].parent)
							temp_cat.html("<a href=\"terminal.php?retailer="+this.retailer+"&category="+children[j].ID+"\">"+children[j].name+"</a>");
						else
							temp_cat.html("<a href=\"category.php?retailer="+this.retailer+"&category="+children[j].ID+"\">"+children[j].name+"</a>");
					}
				}
			}
			
			$("#navigation").dropDown();
		},

		/* If there is no data found, just stop processing. */
		revertChanges : function() {
			$(".sticky_retail").attr("src", Categories.retailImage);
			$(".sticky_retail").attr("width", "50");
			$(".sticky_retail").attr("height", "39");
		},
		
		changeUrl: function() {
			Url.pushStateSearch('retailer', this.retailer);
		},
		
		completeRequest: function() {
			/* first clearing out the tieout internval */
			clearTimeout(Categories.ajaxTimeout);
			Categories.ajaxTimeout = null;

			if(Categories.xmlhttp.readyState == 4 && Categories.xmlhttp.status == 200) {
				//console.log(Categories.xmlhttp.responseText);
				if(Categories.xmlhttp.responseText.indexOf("Error", 0) == -1) {
					Categories.result = jQuery.parseJSON(Categories.xmlhttp.responseText);
				} else {
					console.log("there was some error in accessing the server.\n"+Categories.xmlhttp.responseText);

					Categories.revertChanges();
					return;
				}
				console.log(Categories.result);
				Categories.setCategories();
				Categories.changeUrl();
				
				date = new Date();
				Categories.xmlhttp = null;
				
				/*
				 * Change back the sticky retailer.
				 */
				$(".sticky_retail").attr("src", Categories.retailImage);
				$(".sticky_retail").attr("width", "50");
				$(".sticky_retail").attr("height", "39");
			}
		},
		
		getCategories: function() {
			/*
			 * If there is already a request running, cancel it.
			 */
			if(this.xmlhttp != null) {
				this.xmlhttp.abort();
				this.xmlhttp = null;
			}
			
			if(window.XMLHttpRequest) {
				this.xmlhttp = new XMLHttpRequest();
			} else {
				this.xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			this.xmlhttp.onreadystatechange = this.completeRequest;
			this.xmlhttp.open("GET", "category/getCategoryMenu.php?retailer="+this.retailer+"&async=1");
			this.xmlhttp.send();

			/* If the request doesnt complete in 5 seconds,
				times out the request. */
			this.ajaxTimeout =  setTimeout(function() {
				Categories.xmlhttp.abort();
				
				Categories.revertChanges();
			}, 5000);
		},
		
		loadCategoriesMenu: function(retail) {
			/*
			 * setting the retailer which we get from 
			 * the retail selector.
			 * 
			 * CHECK: why 'this' keyword is not working?
			 * Ans: As the scope of this function is the same as
			 * retail selector and not category. This is because
			 * calling it is a part of retailSelector variable(selectionCallback)
			 * */
			Categories.setRetailer(retail.attr('data-select'));
			
			/*
			 * Change the sticky retailer to a "loading" image
			 */
			Categories.retailImage = $(".sticky_retail").attr("src");
			$(".sticky_retail").attr("src", "img/loader.gif");
			$(".sticky_retail").attr("width", "16");
			$(".sticky_retail").attr("height", "16");
			
			Categories.getCategories();
		}
};