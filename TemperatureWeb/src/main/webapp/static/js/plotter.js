var chart;
var min;
var max;

function plot(response){	
	
/*	if(chart != null){
		if(choice){
			clearChart();
		}
		updateChart(response);
		return;
	}*/
	
	generatedDataPoints = [];
	len = response.temperature.length;
	
	for(var i = 0; i < len; i++){
		generatedDataPoints.push({
			y : response.temperature[i].tempC,
			x : new Date(response.temperature[i].timestamp)
		})
		
	}
		 
	chart = new CanvasJS.Chart("chartContainer", {
		data: [              
		{
			type: "line",
			showInLegend: true,
			legendText: "temperature",
			dataPoints: generatedDataPoints	
		}
		],
		axisY:{
	    	minimum: -10,
	    	maximum: +10,
		 },
		backgroundColor: "#EEEEEC"
	});
	chart.render();
	
}

function updateChart(response){
	
	generatedDataPoints = [];
	len = response.rateDate.length;
	
	for(var i = 0; i < len; i++){
		generatedDataPoints.push({
			y : response.rateDate[i].exchangeRate,
			x : new Date(response.rateDate[i].date)
		})
		
	}
	
    var newSeries = {
		type: "line",
		showInLegend: true,
		legendText: response.targetCurrency+"/"+response.baseCurrency,
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