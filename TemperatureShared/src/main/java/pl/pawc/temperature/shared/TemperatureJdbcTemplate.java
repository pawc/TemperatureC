package pl.pawc.temperature.shared;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

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

	public List<Temperature> getLast10() {
		String SQL =
		"select id, avg(tempC) tempC, time " + 
		"from temperatures " + 
		"where date(time) >='2018-01-21' " + 
		"group by UNIX_TIMESTAMP(time) DIV 300 " + 
		"order by 1 desc limit 24;";
		
		ArrayList<Temperature> result = new ArrayList<Temperature>();
		result = (ArrayList<Temperature>) jdbcTemplateObject.query(SQL, new TemperatureMapper());
		return result;
	}

}