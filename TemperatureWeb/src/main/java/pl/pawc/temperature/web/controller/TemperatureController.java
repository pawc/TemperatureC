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

import pl.pawc.temperature.shared.Temperature;
import pl.pawc.temperature.shared.TemperatureJdbcTemplate;

@Controller
public class TemperatureController {
	
	
	@RequestMapping("/")
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response){		
		
		ModelMap model = new ModelMap();
		
		return new ModelAndView("home", "model", model);
	}
	
	@RequestMapping("/ajax")  
	public @ResponseBody  
	String ajax(@RequestParam(value = "targetCurrency") String targetCurrency,
			HttpServletResponse response){ 
	  
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	    TemperatureJdbcTemplate temperatureJdbcTemplate = null;
	    
		temperatureJdbcTemplate = (TemperatureJdbcTemplate) context.getBean("temperatureJdbcTemplate");
		
		ArrayList<Temperature> result = (ArrayList<Temperature>) temperatureJdbcTemplate.getLast10();
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String str = null;
		try{
			str = objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException e){
			e.printStackTrace();
		}
	  
		return str;  
	  
	}  

}
