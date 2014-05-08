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
	    
	    init: function(id, label, priceData, summaryData, flagData) {
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
	    	
	    	/* attaches the events to their respective containers */
	    	this.attachEventObservers();
	    	
	    	
	    	/* draws the graphs */
	    	this.graphs.price = this.priceGraph(this.priceData);
	    	this.graphs.summary = this.summaryGraph(this.summaryData);
	    	
	    	/* setting the initial selection area in the summaryGraph */
	    	
	    	//setting the initial bounds first
	    	this.bounds.xmin = this.priceData[0][0][0];
	    	this.bounds.xmax = this.priceData[0][this.priceData[0].length - 1][0];
	    	this.bounds.ymin = null;
	    	this.bounds.ymax = null;
	    	
	    	var area = {
	    		x1: this.bounds.xmin,
	    		x2: this.bounds.xmax,
	    		y1: this.bounds.ymin,
	    		y2: this.bounds.ymax
	    	};
	    	this.graphs.summary.setSelection(area);
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
	    	
	    	/* setting the initial selection area in the summaryGraph */
	    	//setting the bounds
	    	if(this.bounds.xmin > this.priceData[this.numOfGraph - 1][0][0])
	    		this.bounds.xmin = this.priceData[this.numOfGraph - 1][0][0];
	    	if(this.bounds.xmax < this.priceData[this.numOfGraph - 1][this.priceData[this.numOfGraph - 1].length - 1][0])
	    		this.bounds.xmax = this.priceData[this.numOfGraph - 1][this.priceData[this.numOfGraph - 1].length - 1][0];
	    	
	    	var area = {
		    		x1: this.bounds.xmin,
		    		x2: this.bounds.xmax,
		    		y1: this.bounds.ymin,
		    		y2: this.bounds.ymax
		    	};
		    this.graphs.summary.setSelection(area);
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
	    
		/* builds the DOM structure required */
	    builDom: function() {
	    	var container = $(this.id);
	    	this.clearContainer();
	    	
	    	this.containers.price = new Element('div', {id: 'priceGraph', style: 'width: 100%; height: 500px'});
	    	this.containers.summary = new Element('div', {id: 'summaryGraph', style: 'width: 100%; height: 125px'});
	    	
	    	this.handles.left = new Element('div', {id: 'leftHandle', 'class': 'handle zoomHandle', style: 'display: none;'});
	    	this.handles.right = new Element('div', {id: 'rightHandle', 'class': 'handle zoomHandle', style: 'display: none;'});
	    	this.handles.scroll = new Element('div', {id: 'scrollHandle', 'class': 'handle scrollHandle', style: 'display: none;'});
	    	this.handles.left.onselectstart = function() {return false;};
	    	this.handles.right.onselectstart = function() {return false;};
	    	this.handles.scroll.onselectstart = function() {return false;};
	    	
	    	container.insert(this.containers.price);
	    	container.insert(this.containers.summary);
	    	container.insert(this.handles.left);
	    	container.insert(this.handles.right);
	    	container.insert(this.handles.scroll);
	    },
	    
	    attachEventObservers: function() {
	    	/* flotr:select event to the summary graph  */
	    	Event.observe(this.containers.summary, 'flotr:select', this.selectObserver.bind(this));
	    	Event.observe(this.containers.summary, 'flotr:select', this.positionZoomHandles.bind(this));
	    	Event.observe(this.containers.summary, 'flotr:select', this.positionScrollHandle.bind(this));
	    	
	    	Event.observe(this.containers.summary, 'flotr:click', this.reset.bind(this));
	    	
	    	Event.observe(this.containers.summary, 'mousedown', this.hideSelection.bind(this));
	    	Event.observe(this.handles.scroll, 'mousedown', this.scrollHandleObserver.bind(this));
	    	Event.observe(this.handles.left, 'mousedown', this.zoomHandleObserver.bind(this));
	    	Event.observe(this.handles.right, 'mousedown', this.zoomHandleObserver.bind(this));
	    },
	    
	    reset: function(event) {
	    	this.graphs.price = this.priceGraph(this.priceData);
	    	
	    	this.handles.left.hide();
	    	this.handles.right.hide();
	    	this.handles.scroll.hide();
	    },
	    
	    hideSelection: function(event) {
	    	this.handles.left.hide();
	    	this.handles.right.hide();
	    	this.handles.scroll.hide();
	    	
	    	this.graphs.summary.clearSelection();
	    },
	    
	    selectObserver: function(event) {
	    	//console.log(event);
	    	var xmin = Math.floor(event.memo[0].x1),
	    		xmax = Math.ceil(event.memo[0].x2),
	    		newBounds = {x1: xmin, x2: xmax, y1: null, y2: null};
	    	
	    	/* drawing the graph with the new bounds extracted from the select event */
	    	this.graphs.price = this.priceGraph(this.priceData, newBounds);
	    },
	    
	    positionZoomHandles: function(event) {
	    	//console.log(event);
	    	var x1 = event.memo[0].x1,
	    		x2 = event.memo[0].x2,
	    		xaxis = event.memo[1].axes.x,
	    		plotOffset = event.memo[1].plotOffset,
	    		height     = this.containers.summary.getHeight(),
	            offset     = this.containers.summary.positionedOffset(),
	            dimensions, xPosOne, xPosTwo, xPosLeft, xPosRight, yPos;
	    	
	    	dimensions = this.handles.left.getDimensions();
	    	//console.log(Position.offsetParent(this.containers.summary));
	    	//console.log("offset =" + offset[0] + ", plotOffset.left="+plotOffset.left+", xaxis.d2p(x1)"+xaxis.d2p(x1)+", dimensions.width"+dimensions.width);
	    	//console.log("offset =" + offset[0] + ", plotOffset.left="+plotOffset.left+", xaxis.d2p(x1)"+xaxis.d2p(x2)+", dimensions.width"+dimensions.width);
	    	xPosOne   = Math.floor(offset[0]+plotOffset.left+xaxis.d2p(x1)-dimensions.width/2+1);
	        xPosTwo   = Math.ceil(offset[0]+plotOffset.left+xaxis.d2p(x2)-dimensions.width/2);
	        xPosLeft  = Math.min(xPosOne, xPosTwo);
	        xPosRight = Math.max(xPosOne, xPosTwo);
	        yPos      = Math.floor(offset[1]+height/2 - dimensions.height/2);

	        this.handles.left.setStyle({position: 'absolute', left: xPosLeft+'px', top: yPos+'px'});
	        this.handles.right.setStyle({position: 'absolute', left: xPosRight+'px', top: yPos+'px'});
	        this.handles.left.show();
	        this.handles.right.show();
	    },
	    
	    zoomHandleObserver: function(event) {
	    	var zoomHandle = event.element(),
	    		x = event.clientX,
	    		prevSelection = this.graphs.summary.prevSelection;
	    	
	    	var handleObserver = function(event) {
	    		Event.stopObserving(document, 'mousemove', handleObserver);
	    		
	    		var deltaX = event.clientX - x,
	    			xAxis = this.graphs.summary.axes.x,
	    			x1, x2, area;
	    		
	    		if(Element.identify(zoomHandle) == 'leftHandle') {
	    			x1 = xAxis.p2d(Math.min(prevSelection.first.x, prevSelection.second.x) + deltaX);
	    			x2 = xAxis.p2d(Math.max(prevSelection.first.x, prevSelection.second.x));
	    		} else {
	    			x1 = xAxis.p2d(Math.min(prevSelection.first.x, prevSelection.second.x));
	    			x2 = xAxis.p2d(Math.max(prevSelection.first.x, prevSelection.second.x) + deltaX);
	    		}
	    		
	    		if(x1 < this.bounds.xmin) {
	    			x1 = this.bounds.xmin;
	    		}
	    		if(x1 > this.bounds.xmax) {
	    			x1 = this.bounds.xmax;
	    		}
	    		if(x2 < this.bounds.xmin) {
	    			x2 = this.bounds.xmin;
	    		}
	    		if(x2 > this.bounds.xmax) {
	    			x2 = this.bounds.xmax;
	    		}
	    		
	    		area = {
	    			x1: x1,
	    			x2: x2,
	    			y1: null,
	    			y2: null,
	    		};
	    		
	    		if (area.x1 != prevSelection.first.x || area.x2 != prevSelection.second.x) {
	                this.graphs.summary.setSelection(area);
	            }
	    		
	    		Event.observe(document, 'mousemove', handleObserver);
	    	}.bind(this);
	    	
	    	var endHandleObserver = function(event) {
	    		Event.stopObserving(document, 'mousemove', handleObserver);
	    		Event.stopObserving(document, 'mouseup', EndhandleObserver);
	    	}.bind(this);
	    	
	    	Event.observe(document, 'mousemove', handleObserver);
	    	Event.observe(document, 'mouseup', endHandleObserver);
	    },
	    
	    positionScrollHandle: function(event) {
	    	//console.log(event);
	    	var x1          = event.memo[0].x1,
	            x2          = event.memo[0].x2,
	            xaxis       = event.memo[1].axes.x,
	            plotOffset  = event.memo[1].plotOffset,
	            graphOffset = this.containers.summary.positionedOffset(),
	            graphHeight = this.containers.summary.getHeight(),
	            width, xPosLeft, yPos;

	        width = Math.floor(xaxis.d2p(x2) - xaxis.d2p(x1)) + 8;
	        width = (width < 10) ? 18 : width;
	
	        xPosLeft = Math.floor(graphOffset[0] + plotOffset.left + xaxis.d2p(x1) + (xaxis.d2p(x2) - xaxis.d2p(x1) - width)/2);
	        yPos     = Math.ceil(graphOffset[1] + graphHeight - 2);
	
	        this.handles.scroll.setStyle({position: 'absolute', left: xPosLeft+'px', top: yPos+'px', width: width+'px'});
	        this.handles.scroll.show();
	    },
	    
	    scrollHandleObserver: function(event) {
	    	//console.log(event);
	    	var x = event.clientX;
	    		prevSelection = this.graphs.summary.prevSelection;
	    		
	    	var handleObserver = function(event) {
	    		//console.log("mouse move :");console.log(event);
	    		Event.stopObserving(document, 'mousemove', handleObserver);
	    		
	    		var deltaX = event.clientX - x,
	    			xAxis = this.graphs.summary.axes.x,
	    			x1 = xAxis.p2d(Math.min(prevSelection.first.x, prevSelection.second.x) + deltaX),
	    			x2 = xAxis.p2d(Math.max(prevSelection.first.x, prevSelection.second.x) + deltaX),
	    			area;
	    		
	    		if(x1 < this.bounds.xmin) {
	    			x2 = this.bounds.xmin + (x2 - x1);
	    			x1 = this.bounds.xmin;
	    		}
	    		if(x2 > this.bounds.xmax) {
	    			x1 = this.bounds.xmax - (x2 - x1);
	    			x2 = this.bounds.xmax;
	    		}
	    		
	    		area = {
	    				x1: x1,
	    				x2: x2,
	    				y1: null,
	    				y2: null
	    		};
	    		
	    		if(area.x1 != prevSelection.first.x) {
	    			this.graphs.summary.setSelection(area);
	    		}
	    		
	    		Event.observe(document, 'mousemove', handleObserver);
	    	}.bind(this);
	    	
	    	var EndHandleObserver = function(event) {
	    		//console.log("mouse up :"); console.log(event);
	    		Event.stopObserving(document, 'mousemove', handleObserver);
	    		Event.stopObserving(document, 'mouseup', EndHandleObserver);
	    	}.bind(this);
	    	
	    	Event.observe(document, 'mousemove', handleObserver);
	    	Event.observe(document, 'mouseup', EndHandleObserver);
	    },
		
		priceGraph: function(data, bounds) {
			var xmin = null,
				xmax = null,
				ymin = null,
				ymax = null;
			
			if(bounds) {
				xmin = bounds.x1;
				xmax = bounds.x2;
				ymin = bounds.y1;
				ymax = bounds.y2;
			}
			
			p = Flotr.draw(
					$$('#'+this.id+' #priceGraph')[0],
					data,
					{
						lines: {
							lineWidth: 2
						},
						xaxis: {
							min: xmin,
							max: xmax,
							showLabels: false
						},
						yaxis: {
							min: ymin,
							max: ymax,
							margin: false,
							autoscaleMargin: .5
						},
						grid: {
							outlineWidth: 0, 
							labelMargin: 4
						},
						mouse: {
							track: true, 
							position: 'ne', 
							trackDecimals: 4
						},
						shadowSize: false
					}
				);
			
			return p;
		},
	    
	    summaryGraph: function(data) {
	    	p = Flotr.draw(
					$$('#'+this.id+' #summaryGraph')[0],
					data,
					{
						lines: {
							lineWidth: 2
						},
						xaxis: {
							margin: false
						},
						yaxis: {
							autoscaleMargin: .5,
							showLabels: false
						},
						grid: {
							outlineWidth: 0, 
							labelMargin: 4, 
							verticalLines: false, 
							horizontalLines: false
						},
						selection: {
							mode: 'x'
						},
						shadowSize: false
					}
				);
	    	return p;
	    }
};