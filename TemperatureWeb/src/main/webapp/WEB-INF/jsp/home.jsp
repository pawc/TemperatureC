<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
 pageEncoding="ISO-8859-1"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">  
	<title>Temperature</title>  
	<%@ page isELIgnored="false" %>
	 <link rel="stylesheet" type="text/css" href="/Temperature/static/css/main.css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
   	<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
 	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
 	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> 
</head>  
<body>  
	<center>
	<script type="text/javascript"> 
	
	function plot(response){	
			
			generatedDataPoints = [];
			len = response.length;
			
			for(var i = 0; i < len; i++){
				generatedDataPoints.push({
					y : response[i].tempC,
					x : new Date(response[i].timestamp)
				})
				
			}
				 
			chart = new CanvasJS.Chart("chartContainer", {
				data: [              
				{
					type: "line",
					showInLegend: true,
					legendText: "temperatura Gdansk, Przymorze",
					dataPoints: generatedDataPoints	
				}
				],
				axisX: {
					valueFormatString: "DDD HH:mm"
				},
				backgroundColor: "#d0f4bc"
			});
			chart.render();
			
	}
	
	window.onload = doAjaxPost;

	function doAjaxPost() { 
		
	var interval = $('#interval').val(); 
	
	$.ajax({  
		type : "Get",   
		url : "get.html",   
		dataType: "json",
		data : "interval=" + interval,
		
		success : function(response) {  
			plot(response);
		}, 
			
		error : function(e) {  
			console.log(e);   
		}  
		});  
	}  
	</script>
		
    <form method="get">  
	<select name="interval" id="interval" onClick="doAjaxPost();">
		<option value="60">60 min.</option>
		<option value="30">30 min.</option>
		<option value="15" selected="selected">15 min.</option>
		<option value="5">5 min.</option>
	</select> 
    </form>
    
	<div id="chartContainer" style="height: 400px; width: 100%;"></div>
  
	</center>  
</body>  
</html>  