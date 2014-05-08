var productFinance = {
		id: null,	// id of the main graph container
		
		graphs: {price: null, summary: null},	/* contains the graph objects returned by the 
		flotr library */
		
		containers: {price: null, summary: null, flag: null}, /* will contain the element objects 
		that hold the graphs */
		
		handles: {left: null, right: null, scroll: null},	/* "handles" contain the element objects
		that help in the zoom in and zoom out function of the graph */
	    
		bounds: {xmin: null, xmax: null, ymin: null, ymax: null}, /* */
	    
		labels: [],		/* array containing the labels of the different products */
	    
		numOfGraph: null,	/* contains the number of active graphs */
		
		priceData: [],		/* array containing the price's of the different products */
		summaryData: [],	/* array containing the summary of the different products */
		flagData: [],	/**/
		
		xTickFormatter: Flotr.defaultTickFormatter,
	    yTickFormatter: Flotr.defaultTickFormatter,
	    trackFormatter: Flotr.defaultTrackFormatter,	/* these are the flotr's default 
	    tick formatters */
	    
	    init: function(id, label, priceData, summaryDaya, flagData) {
	    	/* initialising the number of graphs to 1 */
	    	this.numOfGraph = 1;
	    	
	    	/* setting the id of the main graph container */
	    	this.id = id;
	    	
	    	/* setting the data for the graphs */
	    	this.labels.push(label);
	    	this.priceData.push(priceData);
	    	this.summaryData.push(summaryData);
	    	this.flagData.push(flagData);
	    	
	    	/* sets the DOM structure for the graph */
	    	this.builDom();
	    	
	    	/* draws the graphs */
	    	this.graphs.price = this.priceGraph(this.priceData);
	    	this.graphs.summary = this.summaryGraph(this.summaryData);
	    },
	    
	    /* clears the main graph container during initialisation so that
	     * it does not contain any of the div's with the desired id's */
	    clearContainer: function() {
	    	var elems = ['priceGraph', 'summaryGraph', 'flagContainer', 
	    	             'leftHandle', 'rightHandle', 'scrollHandle'];
	    	elems.each(function(e) {
	    		el = $$("#" + this.id + "#" + e);
	    		if( el[0] && el[0].remove )
	    			el[0].remove();
	    	});
		},
		
		addData: function(label, priceData, summaryData, flagData, id) {
			if(!this.numOfGraph) {
				this.init(id, label, priceData, summaryData, flagData);
				return;
			}
			
			/* increase the number of active graphs */
			this.numOfGraph++;
			
			/* push the new data into the rescpective arrays */
			this.labels.push(label);
			this.priceData.push(priceData);
			this.summaryData.push(summaryData);
			this.flagData.push(flagData);
			
			/* draws the graphs */
	    	this.graphs.price = this.priceGraph(this.priceData);
	    	this.graphs.summary = this.summaryGraph(this.summaryData);
		},
	    
		/* builds the DOM structure required */
	    builDom: function() {
	    	var container = $(this.id);
	    	this.clearContainer();
	    	
	    	this.containers.price = new Element('div', {id: 'priceGraph', style: 'width: 100%; height: 500px'});
	    	this.containers.summary = new Element('div', {id: 'summaryGraph', style: 'width: 100%; height: 125px'});
	    	
	    	container.insert(this.containers.price);
	    	container.insert(this.containers.summary);
	    },
		
		priceGraph: function(data) {
			p = Flotr.draw(
					$$('#'+this.id+' #priceGraph')[0],
					data,
					{
						lines: {lineWidth: 1},
						grid: {outlineWidth: 0, labelMargin: 4},
						mouse: {track: true, position: 'ne', trackDecimals: 4},
						shadowSize: 0
					}
				);
			
			return p;
		},
	    
	    summaryGraph: function(data) {
	    	p = Flotr.draw(
					$$('#'+this.id+' #summaryGraph')[0],
					data,
					{
						grid: {outlineWidth: 0, labelMargin: 4, verticalLines: false, horizontalLines: false},
						shadowSize: 0
					}
				);
	    	return p;
	    }
};