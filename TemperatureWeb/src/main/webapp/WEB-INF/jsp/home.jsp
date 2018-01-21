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
					legendText: "temperature",
					dataPoints: generatedDataPoints	
				}
				],
				axisX: {
					valueFormatString: "DDD HH:mm"
				},
				backgroundColor: "#a1fcd0"
			});
			chart.render();
			
	}
	
	window.onload = doAjaxPost;

	function doAjaxPost() {  
	
	$.ajax({  
		type : "Get",   
		url : "ajax.html",   
		dataType: "json",
		data : "targetCurrency=ok",
		
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
	<input type="button" value="update" onclick="doAjaxPost();" />  
    </form>
    
	<div id="chartContainer" style="height: 400px; width: 100%;"></div>
  
	</center>  
</body>  
</html>  