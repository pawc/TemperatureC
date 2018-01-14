package pl.pawc.temperature.shared;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class TemperatureJdbcTemplate implements TemperatureDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void insert(Temperature temperature){
		String SQL = "insert into temperatures (tempC)"
				+ " values (?)";

		double tempC = temperature.getTempC();
		
		jdbcTemplateObject.update(SQL, tempC);
	}

}