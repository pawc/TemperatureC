package pl.pawc.temperature.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.pawc.temperature.shared.TemperatureJdbcTemplate;
import pl.pawc.temperature.shared.model.Temperature;
import pl.pawc.temperature.shared.model.TemperatureResponse;

@Controller
public class TemperatureController {
		
	@RequestMapping("/")
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response){		
		
		ModelMap model = new ModelMap();
		
		return new ModelAndView("home", "model", model);
	}
	
	@RequestMapping("/post")  
	public @ResponseBody  
	void post(@RequestParam(value = "owner") String owner, 
			@RequestParam(value = "temperature") String temperatureParam, 
			HttpServletResponse response){
		
		if(!validateOwner(owner)) {
			throw400(response);
			return;
		}
		
		double temperatureVal;
		
		try {
			temperatureVal = Double.parseDouble(temperatureParam);
		}
		catch(NumberFormatException e) {
			throw400(response);
			return;
		}
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	    TemperatureJdbcTemplate temperatureJdbcTemplate = null;
	    
		temperatureJdbcTemplate = (TemperatureJdbcTemplate) context.getBean("temperatureJdbcTemplate");
		
		Temperature temperature = new Temperature();
		temperature.setOwner(owner);
		temperature.setTempC(temperatureVal);
		
		temperatureJdbcTemplate.insert(temperature, "temperatures");
		
		response.setStatus(HttpServletResponse.SC_OK);
	  
	}
	
	@RequestMapping("/bme280")  
	public @ResponseBody  
	void bme280(@RequestParam(value = "owner") String owner, 
			@RequestParam(value = "temperature") String temperatureParam, 
			@RequestParam(value = "humidity") String humidityParam,
			@RequestParam(value = "pressure") String pressureParam,
			HttpServletResponse response){
		
		if(!validateOwner(owner)) {
			throw400(response);
			return;
		}
		
		double temperatureVal;
		double humidityVal;
		double pressureVal;
		
		try {
			temperatureVal = Double.parseDouble(temperatureParam);
			humidityVal = Double.parseDouble(humidityParam);
			pressureVal = Double.parseDouble(pressureParam);
		}
		catch(NumberFormatException e) {
			throw400(response);
			return;
		}
		
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	    TemperatureJdbcTemplate temperatureJdbcTemplate = null;
	    
		temperatureJdbcTemplate = (TemperatureJdbcTemplate) context.getBean("temperatureJdbcTemplate");
		
		Temperature temperature = new Temperature();
		temperature.setOwner(owner);
		temperature.setTempC(temperatureVal);
		temperatureJdbcTemplate.insert(temperature, "temperatures");
		
		Temperature humidity = new Temperature();
		temperature.setOwner(owner);
		temperature.setTempC(humidityVal);
		temperatureJdbcTemplate.insert(temperature, "humidity");
		
		Temperature pressure = new Temperature();
		temperature.setOwner(owner);
		temperature.setTempC(pressureVal);
		temperatureJdbcTemplate.insert(temperature, "pressure");
		
		response.setStatus(HttpServletResponse.SC_OK);
	  
	} 	
	
	@RequestMapping("/get")  
	public @ResponseBody  
	String get(
			@RequestParam(value = "owner") String owner,
			@RequestParam(value = "interval") String intervalParam, 
			HttpServletResponse response){
		
		if(!validateOwner(owner)) {
			throw400(response);
		}
		
		if(!validateParam(intervalParam)) throw400(response);
		
		int interval = Integer.parseInt(intervalParam);
	  
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	    TemperatureJdbcTemplate temperatureJdbcTemplate = null;
	    
		temperatureJdbcTemplate = (TemperatureJdbcTemplate) context.getBean("temperatureJdbcTemplate");
		
		TemperatureResponse result = (TemperatureResponse) temperatureJdbcTemplate.getLatest(owner, interval);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String str = null;
		try{
			str = objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException e){
			e.printStackTrace();
		}
	  
		return str;  
	  
	} 
	
	private boolean validateParam(String intervalParam) {
		
		int interval;
		
		try {
			interval = Integer.parseInt(intervalParam);
		}
		catch(NumberFormatException e) {
			return false;
		}
		
		if(interval<=0) return false;
		return true;
	}
	
	private boolean validateOwner(String owner) {
		switch (owner) {
			case "pawc" : {
				return true;
			}
			case "osk1" : {
				return true;
			}
			case "osk2" : {
				return true;
			}
			case "osk3" : {
				return true;
			}
			default : {
				return false;
			}
		}
	}
	
	private void throw400(HttpServletResponse response){
		try {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}