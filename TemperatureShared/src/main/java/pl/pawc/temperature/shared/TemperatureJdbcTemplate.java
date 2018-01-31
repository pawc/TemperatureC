package pl.pawc.temperature.shared;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import pl.pawc.temperature.shared.mapper.TemperatureTimestampMapper;
import pl.pawc.temperature.shared.model.Temperature;
import pl.pawc.temperature.shared.model.TemperatureResponse;
import pl.pawc.temperature.shared.model.TemperatureTimestamp;

public class TemperatureJdbcTemplate implements TemperatureDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void insert(Temperature temperature, String table){
		String SQL = "insert into " + table +" (owner, "+getColumnName(table) +")"
				+ " values (?, ?)";

		String owner = temperature.getOwner();
		double tempC = temperature.getTempC();
		
		jdbcTemplateObject.update(SQL, owner, tempC);
	}
	
	private String getColumnName(String table) {
		switch(table) {
			case "temperatures" : return "tempC";
			case "humidity" : return "hum";
			case "pressure" : return "press";
			default : return null;
		}
	}

	public TemperatureResponse getLatest(String owner, int intervalMinutes) {
		int interval = intervalMinutes * 60;
		
		String SQL =
		"select avg(tempC) tempC, time " + 
		"from temperatures " + 
		"where owner='"+owner+"' " + 
		"group by UNIX_TIMESTAMP(time) DIV " + interval +
		" order by 2 desc limit 48;";
		
		ArrayList<TemperatureTimestamp> result = new ArrayList<TemperatureTimestamp>();
		result = (ArrayList<TemperatureTimestamp>) jdbcTemplateObject.query(SQL, new TemperatureTimestampMapper());
		
		TemperatureResponse temperatureResponse = new TemperatureResponse(owner, result);
		
		return temperatureResponse;
	}

	public void insert(double value) {
		String SQL = "insert into osk (val) values (?);";
		jdbcTemplateObject.update(SQL, value);
	}

}