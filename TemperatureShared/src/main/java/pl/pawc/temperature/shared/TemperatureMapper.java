package pl.pawc.temperature.shared;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;

public class TemperatureMapper implements RowMapper<Temperature> {

	public Temperature mapRow(ResultSet resultSet, int rowNum) throws SQLException{
		
		Temperature temperature = new Temperature();
		
		temperature.setId(resultSet.getLong("id"));
		GregorianCalendar calendar = new GregorianCalendar(new Locale("pl"));
		temperature.setTimestamp((resultSet.getTimestamp("time", calendar)));
		temperature.setTempC(resultSet.getDouble("tempC"));
		
		return temperature;
		
	}
	
}