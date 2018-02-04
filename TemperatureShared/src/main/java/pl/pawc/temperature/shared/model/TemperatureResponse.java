package pl.pawc.temperature.shared.model;

import java.util.ArrayList;
import java.util.Comparator;

public class TemperatureResponse {
	
	private String type;
	private String owner;
	private double min;
	private double max;
	private ArrayList<TemperatureTimestamp> result;
	
	public TemperatureResponse(String type, String owner, ArrayList<TemperatureTimestamp> result) {
		this.type = type;
		this.owner = owner;
		this.result = result;
		result.sort(new Comparator<TemperatureTimestamp>() {

			public int compare(TemperatureTimestamp o1, TemperatureTimestamp o2) {
				return Double.compare(o1.getTempC(), o2.getTempC());
			}
			
		});
		this.min = result.get(0).getTempC();
		this.max = result.get(result.size()-1).getTempC();
		
		result.sort(new Comparator<TemperatureTimestamp>() {

			public int compare(TemperatureTimestamp o1, TemperatureTimestamp o2) {
				return o1.getTimestamp().compareTo(o2.getTimestamp());
			}
			
		});	
	}
	
	public String getType() {
		return type;
	}
	
	public String getOwner() {
		return owner;
	}
	public double getMin() {
		return min;
	}
	public double getMax() {
		return max;
	}
	public ArrayList<TemperatureTimestamp> getResults() {
		return result;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public void setResults(ArrayList<TemperatureTimestamp> result) {
		this.result = result;
	}

}