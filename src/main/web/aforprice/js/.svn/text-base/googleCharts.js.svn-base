/* @author: chirag Maheshwari
 * 
 * Common script to generate a google chart on a given element
 * with the given array of data.
 * This is not entirely generalised.
 * But generates a graph which is basically for product price
 * history seeking.
 * 
 * first include the following before this:
 * <script type='text/javascript' src='http://www.google.com/jsapi'></script>
 */
google.load('visualization', '1', {'packages': ['annotatedtimeline']});
google.setOnLoadCallback(setChartReady);

/*
 * A global variable which tells if a chart is ready to be drawn or not.
 */
var	chart_ready = false,
	chartTimeout = null;

function setChartReady() {
//	as soon as the chart is ready to be drawn the global
//	variables value changes
//	console.log("Charts are ready to be drawn.");
	chart_ready = true;
}

/*
 * function to draw a chart on the division with given elementName ID.
 * 
 * Title of the price will be the given productName.
 * priceTimeArray will contain the corresponding timestamps of the 
 * respective prices in the priceArray.
 */
function drawChart(priceTimeArray, priceArray, elementName, productName) {
	var data = new google.visualization.DataTable();
	
	data.addColumn('date', 'Date');
	data.addColumn('number', productName);
	
	var rows = new Array();
	
	for(i=0;i<priceArray.length;i++){
		rows[i] = [new Date(priceTimeArray[i]), priceArray[i]];
	}
	
	data.addRows(rows);
	var chart = new google.visualization.AnnotatedTimeLine(document.getElementById(elementName));
	
	chart.draw(data, {
		displayAnnotations: false,
		wmode: 'transparent',	// it sets the flash player parameter to tansparent 
			// so that HTML can work over the flash object
		scaleType: 'maximized',	// To make the chart span from minimum to maximum insteat of 0 to maximum
		fill: 50
	});
}


/*
 * drawChartWithStepFunction()
 * @param priceTimeArray
 * @param priceArray
 * @param elementName
 * @param productName
 * 
 * Changes the array given to a step function, by looking for changes ahead
 * and then calls drawChart() to finally draw the chart.
 */
function drawChartWithStepFunction(priceTimeArray, priceArray, elementName, productName) {
	var data = new google.visualization.DataTable();
	data.addColumn('date', 'Date');
	data.addColumn('number', productName);
	
	var rows = new Array(),
		oneDay = 1 * 24 * 60 * 60 * 1000,
		i, j;
	
	if(priceArray.length > 0)
		rows.push([new Date(priceTimeArray[0]), priceArray[0]]);
	for(i=1;i < priceArray.length;i++){

		/* Pushing every day in between the previous timestamp and 
		the new one for a smooth price watching in the graph. */
		j = 1;
		while((priceTimeArray[i-1] + j*oneDay) < priceTimeArray[i]) {
			// console.log("am here!!");
			rows.push([new Date(priceTimeArray[i-1] + j*oneDay), priceArray[i-1]]);
			j++;
		}

		rows.push([new Date(priceTimeArray[i]), priceArray[i-1]]);
		rows.push([new Date(priceTimeArray[i]), priceArray[i]]);
	}
	if(priceArray.length > 0) {
		j = 1;
		var dateObj = new Date();

		while((priceTimeArray[i-1] + j*oneDay) < dateObj.getTime()) {
			rows.push([new Date(priceTimeArray[i-1] + j*oneDay), priceArray[i-1]]);
			j++;
		}

		rows.push([dateObj, priceArray[i-1]]);
	}
	// console.log(rows);
	
	data.addRows(rows);
	var chart = new google.visualization.AnnotatedTimeLine(document.getElementById(elementName));
	
	chart.draw(data, {
		displayAnnotations: false,
		wmode: 'transparent',	// it sets the flash player parameter to tansparent 
			// so that HTML can work over the flash object
		scaleType: 'maximized',	// To make the chart span from minimum to maximum insteat of 0 to maximum
		fill: 50,
		allValuesSuffix: '$'
	});
}

/*
 * When a slide changes on the main page.
 * This function is called, with the slides name.
 * It extracts the priceArray, priceTimeArray and productName
 * variable from the slide number and calls the drawchart function
 * with the available arrays and elementName as "main_graph"
 */
function changeMainGraph(eleId) {
	var productSerialNumber = ''+eleId;
	productSerialNumber = productSerialNumber.substring(productSerialNumber.indexOf("_"));

	if(chartTimeout)
		clearTimeout(chartTimeout);
	
	if(chart_ready) {
		drawChartWithStepFunction( 
				main_graph_prices['priceTimeArray'+productSerialNumber],
				main_graph_prices['priceArray'+productSerialNumber],
				"main_graph",
				main_graph_prices['productName'+productSerialNumber]
				);
	} else {
		chartTimeout = setTimeout(function() { changeMainGraph(eleId); }, 1000);
	}
}

google.load('visualization', '1', {'packages': ['corechart']});
//google.setOnLoadCallback(drawLineChart);

/* @author: Alok Malakar */
function drawLineChart(timeArray,reviewArray,sellRankArray,elementName,productName) {

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Date');
    data.addColumn('number', 'Review History');
    data.addColumn('number', 'Sell Rank History');
	
    var rows = new Array();
	if (reviewArray.length > sellRankArray.length){
		var length = reviewArray.length; }
		else 
			var length = sellRankArray.length; 
	
	for(i=0;i<length;i++){
		rows[i] = [new Date(timeArray[i]), reviewArray[i], sellRankArray[i]];
	}
	data.addRows(rows);
    var chart = new google.visualization.LineChart(document.getElementById(elementName));
    chart.draw(data, {
        width: 600,
        height: 400,
        series: {
            0: {
                targetAxisIndex: 0
            },
            1: {
                targetAxisIndex: 1
            }
        },
        vAxes: {
            0: {
                minValue: 0,
                maxValue: 5,
                label: 'Review History'
            },
            1: {
                minValue: 0,
                maxValue: 100,
                label: 'Sell Rank History'
            }
        }
    });
}


/* auhtor: Alok Malakar */
function drawChartWithEvents(priceTimeArray, priceArray, elementName, productName,holidayList, holidayDesc) {
	var data = new google.visualization.DataTable();

	
	data.addColumn('date', 'Date');
	data.addColumn('number', productName);
	data.addColumn('string', 'title1');
	
	var rows = new Array();
	console.log(priceTimeArray);
	
		i=0;
		j=0;
		k=0;
	while (holidayList.length >j){
	
		if( priceTimeArray[i] < holidayList[j]) {
			//console.log("price-"+priceTimeArray[i]+i);
			rows[k] = [new Date(priceTimeArray[i]), priceArray[i],null];
			i++;
			}
		else if(priceTimeArray[i] > holidayList[j]) {
			console.log(holidayList[j]+holidayDesc[j]);
			rows[k] = [new Date(holidayList[j]), null, holidayDesc[j]];
			j++;
			}
		
		else {
			console.log(holidayList[j]+holidayDesc[j]);
			rows[k] = [new Date(holidayList[j]), priceArray[i], holidayDesc[j]];
			j++;
			i++;
			}
		//console.log(k);	
		k++;
		}
		
	/*
	for(i=0;i<priceArray.length;i++){
		rows[i] = [new Date(priceTimeArray[i]), priceArray[i],null];
	}
	rows[5] = [new Date(priceTimeArray[5]), priceArray[5],'Event'];
	*/
	
	data.addRows(rows);
	var chart = new google.visualization.AnnotatedTimeLine(document.getElementById(elementName));
	
	chart.draw(data, {
		displayAnnotations: true,
		//wmode: 'transparent',	// it sets the flash player parameter to tansparent 
			// so that HTML can work over the flash object
		scaleType: 'maximized',	// To make the chart span from minimum to maximum insteat of 0 to maximum
		//fill: 50
	});
}

/* This is used for comparing products.
 * takes in a 2D price array and time array 
 * and one dimensinal name array.
 * 
 * These are 2-D arrays because we have data for multiple products in one array.
 */
function drawChartForComparison(priceTimeArrays, priceArrays, elementName, nameArray, allTimeArray) {
	var data = new google.visualization.DataTable();

	data.addColumn('date', 'Date');
	for(i in nameArray) {
		data.addColumn('number', nameArray[i]);
	}

	var chart = new google.visualization.AnnotatedTimeLine(
		document.getElementById('main_graph'));

/* TODO: add rows to the graph for multiple products */

	chart.draw(data, {
		displayAnnotations: false,
		wmode: 'transparent',	// it sets the flash player parameter to tansparent 
			// so that HTML can work over the flash object
		scaleType: 'maximized',	// To make the chart span from minimum to maximum insteat of 0 to maximum
		fill: 50
	});
}


// to test default settings google example code is copied here 
/*
google.load('visualization', '1', {packages: ['annotatedtimeline']});
google.setOnLoadCallback(drawVisualization);

function drawVisualization() {
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Date');
    data.addColumn('number', 'Sold Pencils');
    data.addColumn('string', 'title1');
    data.addColumn('string', 'text1');
    data.addColumn('number', 'Sold Pens');
    data.addColumn('string', 'title2');
    data.addColumn('string', 'text2');
    data.addRows(6);
    data.setValue(0, 0, new Date(2008, 1, 1));
    data.setValue(0, 1, 30000);
    data.setValue(0, 4, 40645);
    data.setValue(1, 0, new Date(2008, 1, 2));
    data.setValue(1, 1, 14045);
    data.setValue(1, 4, 20374);
    data.setValue(2, 0, new Date(2008, 1, 3));
    data.setValue(2, 1, 55022);
    data.setValue(2, 4, 50766);
    data.setValue(3, 0, new Date(2008, 1, 4));
    data.setValue(3, 1, 75284);
    data.setValue(3, 4, 14334);
    data.setValue(3, 5, 'Out of Stock');
    data.setValue(3, 6, 'Ran out of stock on pens at 4pm');
    data.setValue(4, 0, new Date(2008, 1, 5));
    data.setValue(4, 1, 41476);
    data.setValue(4, 2, 'Bought Pens');
    data.setValue(4, 3, 'Bought 200k pens');
    data.setValue(4, 4, 66467);
    data.setValue(5, 0, new Date(2008, 1, 6));
    data.setValue(5, 1, 33322);
    data.setValue(5, 4, 39463);

    var annotatedtimeline = new google.visualization.AnnotatedTimeLine(document.getElementById('main_graph'));
    
    google.visualization.events.addListener(annotatedtimeline, 'rangechange', function () {
        console.log('Start: ' + annotatedtimeline.getVisibleChartRange().start.toString());
    });
    
    annotatedtimeline.draw(data, {
        'displayAnnotations': true
    });
}*/