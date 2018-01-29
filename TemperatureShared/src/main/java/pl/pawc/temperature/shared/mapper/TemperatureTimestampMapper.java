package pl.pawc.temperature.shared.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;

import pl.pawc.temperature.shared.model.TemperatureTimestamp;

public class TemperatureTimestampMapper implements RowMapper<TemperatureTimestamp> {

	public TemperatureTimestamp mapRow(ResultSet resultSet, int rowNum) throws SQLException{
		
		TemperatureTimestamp temperatureTimestamp = new TemperatureTimestamp();
	
		GregorianCalendar calendar = new GregorianCalendar(new Locale("pl"));
		Timestamp timestamp = resultSet.getTimestamp("time", calendar);
		temperatureTimestamp.setTimestamp(timestamp);

        temperatureTimestamp.setTempC(resultSet.getDouble("tempC"));
		
		return temperatureTimestamp;
		
	}
	
}