package it.eng.openreq.updatemanager.registration;


import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class Rule {
	
	private Integer period = 1;
	private String timeUnit = TimeUnit.DAYS.toString();
	/** Accepted: 
	  * SECONDS, HOURS, DAYS. */
	private Integer volume = 0;
	private String startDateTime = ZonedDateTime.now().toString();
	 /** ZonedDateTime format.
	  * Example: 
	  * 2018-05-24T00:00:00+02:00[Europe/Rome]*/
		
	
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	

}
