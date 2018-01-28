package pl.pawc.temperature.shared;

import java.util.ArrayList;
import java.util.List;

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
		String SQL = "insert into temperatures (owner, tempC)"
				+ " values (?, ?)";

		String owner = temperature.getOwner();
		double tempC = temperature.getTempC();
		
		jdbcTemplateObject.update(SQL, owner, tempC);
	}

	public List<Temperature> getLatest(String owner, int intervalMinutes) {
		int interval = intervalMinutes * 60;
		String SQL =
		"select id, owner, avg(tempC) tempC, time " + 
		"from temperatures " + 
		"where owner='"+owner+"' " + 
		"group by UNIX_TIMESTAMP(time) DIV " + interval +
		" order by 1 desc limit 48;";
		
		ArrayList<Temperature> result = new ArrayList<Temperature>();
		result = (ArrayList<Temperature>) jdbcTemplateObject.query(SQL, new TemperatureMapper());
		return result;
	}

	public void insert(double value) {
		String SQL = "insert into osk (val) values (?);";
		jdbcTemplateObject.update(SQL, value);
	}

}