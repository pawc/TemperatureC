package pl.pawc.temperature.shared.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.jdbc.core.RowMapper;

import pl.pawc.temperature.shared.model.Temperature;

public class TemperatureMapper implements RowMapper<Temperature> {

	public Temperature mapRow(ResultSet resultSet, int rowNum) throws SQLException{
		
		Temperature temperature = new Temperature();
		
		temperature.setId(resultSet.getLong("id"));
		temperature.setOwner(resultSet.getString("owner"));
		GregorianCalendar calendar = new GregorianCalendar(new Locale("pl"));
		Timestamp timestamp = resultSet.getTimestamp("time", calendar);
		//Timestamp hourBack = new Timestamp(timestamp.getTime() - (1000 * 60 * 60));
		temperature.setTimestamp(timestamp);
		
		//DecimalFormat df = new DecimalFormat("#.###");
        //String d = df.format(resultSet.getDouble("tempC"));
        temperature.setTempC(resultSet.getDouble("tempC"));
		
		return temperature;
		
	}
	
}