/*
 * @Auhtor: Alok Malakar
 * This file contains all the methods to plot different
 * graphs of a product
 */

/* Author: Chirag Maheshwari
	This function generates the division where the graph will be shown.
	If there is already a division with the same name, it deletes that
	and makes a new one.

	Returns: Width of the newly created division. (To be used for iBox parameter)
	 */
function createDivisionForGraph() {
	if($("#main_graph").length > 0) {
		$("#main_graph").remove();
	}

	var width = (0.90 * bodyWidth) - 50,
		height = (0.80 * viewportHeight) - 50;

	division = jQuery('<div/>', {
		'id': 'main_graph'
	});
	division.css({'width': width+"px", 'height': height+"px"});

	division.appendTo($('body'));

	return width;
}


function plotPriceHistory(name, priceTicks) {
	var timeArray = new Array()
		priceArray = new Array();

	priceTicks = jQuery.parseJSON(priceTicks);
	
	for(i=0;i<priceTicks.length;++i){
		timeArray[i] = priceTicks[i]['time'];
		priceArray[i] = priceTicks[i]['value'];
	}
	for(i=0;i<window.eventHistory.length;++i){
		window.eventHistory[i] = new Date(window.eventHistory[i]);
	}
	
	if (timeArray.length <1) {
		alert("NOT ENOUGH DATA !!");
	} else {
		var width = createDivisionForGraph();

		drawChartWithStepFunction(timeArray , priceArray, 'main_graph', name);
		//drawChartWithEvents(timeArray,priceArray,'main_graph', name, window.eventHistory, window.eventHistoryDesc);
		//drawVisualization();   // to test default setting copied google code as it is
		var htmlNode = document.getElementById('main_graph');
		//console.log(name);
		var title = name;
		iBox.show(htmlNode, title, {'width': width});
	}
}


function plotReviewHistory(name, reviewHistory) {
	var reviewTimeArray = new Array();
	var reviewRatingArray = new Array();

	reviewHistory = jQuery.parseJSON(reviewHistory);

	for(i=0;i<reviewHistory.length;++i){
		reviewTimeArray[i] = reviewHistory[i]['time'];
		reviewRatingArray[i] = reviewHistory[i]['reviewRating'];
	}
	
	if (reviewTimeArray.length <1) {
		alert("NOT ENOUGH DATA !!");
	}
	else{
		var width = createDivisionForGraph();
		
		//drawChartWithEvents(reviewTimeArray,reviewRatingArray,'main_graph', 'id',window.eventHistory, window.eventHistoryDesc);	
		drawChart(reviewTimeArray,reviewRatingArray,'main_graph', 'id');
		var htmlNode1 = document.getElementById('main_graph');	
		var title = name;
		iBox.show(htmlNode1,title, {'width': width});
	}	
}

function plotSellRankHistory(name, sellRankHistory) {
	var sellRankTimeArray = new Array();
	var sellRankValueArray = new Array();

	sellRankHistory = jQuery.parseJSON(sellRankHistory);

	for(i=0;i<sellRankHistory.length;++i){
		sellRankTimeArray[i] = new Date(sellRankHistory[i]['time']);
		sellRankValueArray[i] = sellRankHistory[i]['value'];
	}
	
	if (sellRankTimeArray.length <1) {
		alert("NOT ENOUGH DATA !!");
	}
	else{
		var width = createDivisionForGraph();	
		
		drawChart(sellRankTimeArray,sellRankValueArray,'main_graph', 'id');	
		var htmlNode2 = document.getElementById('main_graph');	
		var title = name;
		iBox.show(htmlNode2,title, {'width': width});   
	}
}

/*
 * Plot combined graphs of sellRankHistory and reviewHistory
 */
function plotCombinedGraph(name, reviewHistory, sellRankHistory) {
	var timeArray = new Array();
	var reviewRatingArray = new Array();
	var sellRankValueArray = new Array();
	
	for(i=0;i<reviewHistory.length;++i){
		timeArray[i] = new Date(reviewHistory[i]['time']);
		reviewRatingArray[i] = reviewHistory[i]['reviewRating'];
	}
	for(i=0;i<sellRankHistory.length;++i){
		sellRankValueArray[i] = sellRankHistory[i]['value'];
	}

	if (timeArray.length<1)
		alert("NOT ENOUGH DATA !!");
	else{
		var width = createDivisionForGraph();	

		drawLineChart(timeArray,reviewRatingArray,sellRankValueArray,'main_graph');
		var htmlNode2 = document.getElementById('main_graph');	
		var title = name;
		iBox.show(htmlNode2,title, {'width': width});  
	}
}

/* @author: Chirag Maheshwari
 * When a product is compared with others,
 * gets 2D array of price and time and draws a graph
 * in an iBox
 * 
 * TODO: Extend this functionality properly.
 * Does not handle the case if any product has no data.
 */
function compareProductGraphs(priceTicks, names) {
 	var priceArrays = new Array(),
 		timeArrays = new Array(),
 		maxTime = (new Date()).getTime(),
 		minTime = maxTime
 		allTimes = new Array();

 	/* if no products to compare, just return */
 	if(priceTicks.length < 1)
 		return;

 	for(i in priceTicks) {
 		priceTicks[i] = jQuery.parseJSON(priceTicks[i]);

 		priceArrays[i] = new Array();
 		timeArrays[i] = new Array();
 		for(j in priceTicks[i]) {
 			priceArrays[i].push(priceTicks[i][j]['value']);
 			timeArrays[i].push(priceTicks[i][j]['time']);

 			if(minTime > priceTicks[i][j]['time']) {
 				minTime = priceTicks[i][j]['time'];
 			}
 		}
 	}

 	/* creating the all Times */
 	var oneDay = 1 * 24 * 60 * 60 * 1000;
 	while(minTime < maxTime) {
 		allTimes.push(minTime);
 		minTime = minTime + oneDay;
 	}
 	allTimes.push(maxTime);

 	var width = createDivisionForGraph();
 	/* Calling the google charts interface to draw the graph in the created division */
 	drawChartForComparison(timeArrays, priceArrays, 'main_graph', names, allTimes);

 	var htmlNode = document.getElementById('main_graph');
 	var title = "COMPARING: ";
 	for(i in names) {
 		title = title + names[i] + ",";
 	}
	iBox.show(htmlNode, title, {'width': width});
}