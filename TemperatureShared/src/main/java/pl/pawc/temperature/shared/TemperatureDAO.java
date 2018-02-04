package pl.pawc.temperature.shared;

import pl.pawc.temperature.shared.model.Temperature;
import pl.pawc.temperature.shared.model.TemperatureResponse;

public interface TemperatureDAO {
	
	public void insert(Temperature temperature, String table);
	public TemperatureResponse getLatest(String owner, int intervalMinutes, String table);

}