package pl.pawc.temperature.shared.model;

import java.sql.Timestamp;

public class TemperatureTimestamp {

	private Timestamp timestamp;
	private double tempC;
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public double getTempC() {
		return tempC;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public void setTempC(double tempC) {
		this.tempC = tempC;
	}

}