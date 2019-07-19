package it.eng.openreq.updatemanager.registration;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import it.eng.openreq.updatemanager.management.Management;


public class VerifyRule implements Runnable{

	private String addressTrain;
	private String addressNewData;
	private String addressUpdate;
	private Rule rule;
//	private LocalDate startDate;
//	private LocalTime startTime;

	public VerifyRule(Rule rule, String addressTrain , String addressNewData, String addressUpdate){
		this.rule = rule;	
		this.addressTrain = addressTrain;
		this.addressNewData =addressNewData ;
		this.addressUpdate=addressUpdate;
		//		this.startDate = LocalDate.parse(rule.getStartDate(),rule.getStartDateFormat());
		//		this.startTime = LocalTime.parse(rule.getStartTime(),rule.getStartTimeFormat());
	}



	public ZonedDateTime calculateNextRun() {

		ZonedDateTime nextDateTime = ZonedDateTime.parse(rule.getStartDateTime());
		ZonedDateTime currentDateTime = ZonedDateTime.now();
//		System.out.println("nextDateTime " +nextDateTime);
//	    System.out.println("currentDateTime "+currentDateTime);
		long period = TimeUnit.SECONDS.convert(rule.getPeriod(), TimeUnit.valueOf(rule.getTimeUnit()));
//		System.out.println("calculateNextRun period = "+period);

		while(nextDateTime.isBefore(currentDateTime)) {
			//			System.out.println("sto nel while");
			//			System.out.println(
			//					System.out.println("nextDateTime = "+nextDateTime);
			//							System.out.println(
			nextDateTime = nextDateTime.plusSeconds(period);		
		}
		return nextDateTime;
	}

	//	public long calculateDelay() {
	//		long timeDelay = 0;
	//		long dateDelay = 0;
	//
	//
	//		if (rule.getStartTime()!= null) {
	//			timeDelay = LocalTime.now().until(rule.getStartTime(), ChronoUnit.SECONDS );
	//			if (timeDelay<0) 
	//				timeDelay += 24*60*60+1;
	//		}
	//
	//
	//
	//		return timeDelay + dateDelay ;
	//	}

	public long calculateDelayFromNext(ZonedDateTime next) {
		return ZonedDateTime.now().until(next, ChronoUnit.SECONDS )+1;
	}



	public void run() {
		Management management = new Management();
		boolean verifyCondition = management.verifyCondition(rule,addressNewData);
//		System.out.println("verify condition = " + verifyCondition);
		if (verifyCondition== true) {
			management.trainModel(addressTrain, addressUpdate);
		}
//		return 0;
	}


}
