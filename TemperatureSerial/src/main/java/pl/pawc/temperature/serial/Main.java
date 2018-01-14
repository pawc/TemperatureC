package pl.pawc.temperature.serial;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.pawc.temperature.shared.TemperatureJdbcTemplate;

public class Main {
	
	public static void main(String args[]){
		
		ApplicationContext context = null;
	    TemperatureJdbcTemplate temperatureJdbcTemplate = null;
	    
    	context = new ClassPathXmlApplicationContext("beans.xml");
    	temperatureJdbcTemplate = (TemperatureJdbcTemplate) context.getBean("temperatureJdbcTemplate");
		
		Communicator comm = new Communicator(temperatureJdbcTemplate);
		
		Scanner sc = new Scanner(System.in);
		String input = "";
		
		comm.searchForPorts();
		
		System.out.println("Choose port: ");
		input = sc.nextLine();
		
		comm.connect(input);
		comm.initIOStream();
		comm.initListener();
		
		while(true){
			input = sc.nextLine();
			if("quit".equals(input)) break;
		    
		}
		comm.disconnect();
		sc.close();
	}

}
