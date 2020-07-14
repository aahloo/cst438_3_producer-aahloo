package cst438hw2.domain;

// Helper class to return multiple values from the getTimeAndTemp method
public class TempAndTime {
	
	public double temp;
	public long time;
	public int timezone;
	
	public TempAndTime(double temp, long time, int timezone) {
		
		this.temp = temp;
		this.time = time;
		this.timezone = timezone;
		
	}


}
