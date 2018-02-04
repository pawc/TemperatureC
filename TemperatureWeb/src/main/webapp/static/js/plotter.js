var chart;

var minTemperature = Number.POSITIVE_INFINITY;
var maxTemperature = Number.NEGATIVE_INFINITY;

var minHumidity = Number.POSITIVE_INFINITY;
var maxHumidity = Number.NEGATIVE_INFINITY;

var minPressure = Number.POSITIVE_INFINITY;
var maxPressure = Number.NEGATIVE_INFINITY;

function plot(response){	
	
	generatedDataPoints = [];
	generatedDataPointsNull = [];
	len = response.results.length;
	
	for(var i = 0; i < len; i++){
		generatedDataPoints.push({
			y : response.results[i].tempC,
			x : new Date(response.results[i].timestamp)
		})
		
	}
	
	for(var i = 0; i < len; i++){
		generatedDataPointsNull.push({
			y : null,
			x : new Date(response.results[i].timestamp)
		})
		
	}
	
	var minLocal = response.min;
	var maxLocal = response.max;
	
	if(minTemperature > minLocal) minTemperature = minLocal;
	if(maxTemperature < maxLocal) maxTemperature = maxLocal;
	
	var span = maxTemperature - minTemperature;
	if(span == 0) span = 1;
	
	chart = new CanvasJS.Chart("chartContainer", {
		axisX: {
			valueFormatString: "DDD HH:mm"
		},
		axisY: {
			title : "temperature [C]",
			minimum: minTemperature,
			maximum: maxTemperature,
		},
		axisY2: [{
			title : "humidity [%]",
		},
		{	title : "pressure [hPa]",		
		}],
		backgroundColor: "#d0f4bc",
		data: [{
				type: "line",
				showInLegend: true,
				legendText: response.owner+"-"+response.type,
				dataPoints: generatedDataPoints
			},
			{
				type: "line",
				showInLegend: false,
				axisYType: "secondary",
				legendText: "humidity",
				dataPoints: generatedDataPointsNull
			},
			{
				type: "line",
				showInLegend: false,
				axisYType: "secondary",
				axisYIndex: 1,
				legendText: "pressure",
				dataPoints: generatedDataPointsNull
			}],
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
		axisYType: getYType(response.type),
		axisYIndex: getYIndex(response.type),
		legendText: response.owner+"-"+response.type,
		dataPoints: generatedDataPoints
    };   
    
	chart.options.data.push(newSeries);
	updateMinMax(response);
	chart.render();
}

function getYType(type){
	if(type == "pressure" || type == "humidity") {
		return "secondary";
	}
	else{
		return "primary";
	}
}

function getYIndex(type){
	if(type == "temperature" || type == "humidity") {
		return 0;
	}
	else{
		return 1;
	}
}

function updateMinMax(response){
	
	var minLocal;
	var maxLocal;
	var span;
	
	if(response.type == "temperature"){
		minLocal = response.min;
		maxLocal = response.max;
		
		if(minTemperature > minLocal) minTemperature = minLocal;
		if(maxTemperature < maxLocal) maxTemperature = maxLocal;
		
		span = maxTemperature - minTemperature;
		if(span == 0) span = 1;
		
		chart.options.axisY.minimum = minTemperature - span * 0.1;
		chart.options.axisY.maximum = maxTemperature + span * 0.1;
	}
	
	if(response.type == "humidity"){
		minLocal = response.min;
		maxLocal = response.max;
		
		if(minHumidity > minLocal) minHumidity = minLocal;
		if(maxHumidity < maxLocal) maxHumidity = maxLocal;
		
		span = maxHumidity - minHumidity;
		if(span == 0) span = 1;
		
		chart.options.axisY2[0].minimum = minHumidity - span * 0.1;
		chart.options.axisY2[0].maximum = maxHumidity + span * 0.1;
	}
	
	if(response.type == "pressure"){
		minLocal = response.min;
		maxLocal = response.max;
		
		if(minPressure > minLocal) minPressure = minLocal;
		if(maxPressure < maxLocal) maxPressure = maxLocal;
		
		span = maxPressure - minPressure;
		if(span == 0) span = 1;
		
		chart.options.axisY2[1].minimum = minPressure - span * 0.1;
		chart.options.axisY2[1].maximum = maxPressure + span * 0.1;
	}
	
}

function clearChart(){
	for(var i=chart.data.length-1; i >= 0; i--){
		chart.data[i].remove();
	}
	minTemperature = Number.POSITIVE_INFINITY;
	maxTemperature = Number.NEGATIVE_INFINITY;
	minHumidity = Number.POSITIVE_INFINITY;
	maxHumidity = Number.NEGATIVE_INFINITY;
	minPressure = Number.POSITIVE_INFINITY;
	maxPressure = Number.NEGATIVE_INFINITY;
	chart.render();
}