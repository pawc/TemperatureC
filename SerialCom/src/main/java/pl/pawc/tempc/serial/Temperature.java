package pl.pawc;

import java.sql.Timestamp;
import java.util.Date;

public class Temperature {
	
	private long id;
	private double tempC;
	private Timestamp timestamp;
	
	public Temperature() {}
	
	public long getId() {
		return id;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public double getTempC() {
		return tempC;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public void setTempC(double tempC) {
		this.tempC = tempC;
	}
	
	public String toString() {
		return timestamp + ": " + tempC;
	}
	
}