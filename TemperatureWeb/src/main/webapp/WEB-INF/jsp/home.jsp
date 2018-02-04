<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
 pageEncoding="ISO-8859-1"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">  
	<title>Temperature</title>  
	<%@ page isELIgnored="false" %>
	<script type="text/javascript" src="/Temperature/static/js/plotter.js"></script>
	<link rel="stylesheet" type="text/css" href="/Temperature/static/css/main.css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
   	<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
 	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
 	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> 
</head>  
<body>  
	<center>
	<script type="text/javascript"> 
	
	window.onload = draw(true);
	
	function draw(init){
		
		if(init){
			owner = 'osk3';
			interval = '15';
			type = 'temperature';
		}
		else{
			owner = $('#owner').val();
			interval = $('#interval').val();
			type = $('#type').val();
		}

		$.ajax({  
			type : "Get",   
			url : "get.html",   
			dataType: "json",
			data : "owner=" + owner +"&interval=" + interval +"&type=" + type,
			success : function(response) {  
				if(init) {
					plot(response);
				}
				else {
					updateChart(response);
				}
			}, 	
			error : function(e) {  
				console.log(e);   
			}  
		});  
	}

	</script>
		
    <form method="get">
    <select name="type" id="type">
		<option value="temperature" selected="selected">temperature</option>
		<option value="humidity">humidity</option>
		<option value="pressure">pressure</option>
	</select>  
   	<select name="owner" id="owner">
		<option value="pawc">pawc</option>
		<option value="osk1">osk1</option>
		<option value="osk2">osk2</option>
		<option value="osk3" selected="selected">osk3</option>
	</select>   
	<select name="interval" id="interval">
		<option value="60">60 min.</option>
		<option value="30">30 min.</option>
		<option value="15" selected="selected">15 min.</option>
		<option value="5">5 min.</option>
	</select> 
	<input type="button" class="button button1" value="add" onclick="draw(false)" />  
	<input type="button" class="button button1" value="clear" onclick="clearChart();" />  
    </form>
    
	<div id="chartContainer" style="height: 400px; width: 100%;"></div>
  
	</center>  
</body>  
</html>  