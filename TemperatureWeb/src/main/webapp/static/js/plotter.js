var chart;
var min;
var max;

function plot(response){	
	
	generatedDataPoints = [];
	len = response.results.length;
	
	for(var i = 0; i < len; i++){
		generatedDataPoints.push({
			y : response.results[i].tempC,
			x : new Date(response.results[i].timestamp)
		})
		
	}
	
	min = response.min;
	max = response.max;
	span = max - min;
	if(span == 0) span = 1;
	min = min-0.1*span;
	max = max+0.1*span;

		 
	chart = new CanvasJS.Chart("chartContainer", {
		data: [              
		{
			type: "line",
			showInLegend: true,
			legendText: response.owner,
			dataPoints: generatedDataPoints	
		}
		],
		axisX: {
			valueFormatString: "DDD HH:mm"
		},
		axisY: {
			minimum: min,
			maximum: max
		},
		backgroundColor: "#d0f4bc"
	});
	chart.render();
	
}

function updateChart(response){
	
	generatedDataPoints = [];
	len = response.results.length;
	
	for(var i = 0; i < len; i++){
		generatedDataPoints.push({
			y : response.results[i].tempC,
			x : new Date(response.results[i].timestamp)
		})
		
	}
	
    var newSeries = {
		type: "line",
		showInLegend: true,
		legendText: response.owner,
		dataPoints: generatedDataPoints
    };
    
	chart.options.data.push(newSeries);
	updateMinMax(response);
	chart.render();
}

function updateMinMax(response){
	minTemp = response.min;
	maxTemp = response.max;
	
	if(min > minTemp) min = minTemp;
	if(max < maxTemp) max = maxTemp;
		
	span = max - min;
	if(span == 0) span = 1;
	
	chart.options.axisY.minimum = min - span * 0.1;
	chart.options.axisY.maximum = max + span * 0.1;
}

function clearChart(){
	for(var i=chart.data.length-1; i >= 0; i--){
		chart.data[i].remove();
	}
	min = Number.POSITIVE_INFINITY;
	max = Number.NEGATIVE_INFINITY;
	chart.render();
}