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
		String SQL = "insert into " + table +" (owner, tempC)"
				+ " values (?, ?)";

		String owner = temperature.getOwner();
		double tempC = temperature.getTempC();
		
		jdbcTemplateObject.update(SQL, owner, tempC);
	}

	public TemperatureResponse getLatest(String owner, int intervalMinutes, String table) {
		int interval = intervalMinutes * 60;
		
		String SQL =
		"select avg(tempC) tempC, time " + 
		"from "+ table + 
		" where owner='"+owner+"' " + 
		"group by UNIX_TIMESTAMP(time) DIV " + interval +
		" order by 2 desc limit 48;";
		
		ArrayList<TemperatureTimestamp> result = new ArrayList<TemperatureTimestamp>();
		result = (ArrayList<TemperatureTimestamp>) jdbcTemplateObject.query(SQL, new TemperatureTimestampMapper());
		
		TemperatureResponse temperatureResponse = new TemperatureResponse(table, owner, result);
		
		return temperatureResponse;
	}

}