package it.eng.openreq.updatemanager.registration;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExecutablePolicy {
	private static final Logger LOGGER = LogManager.getLogger(ExecutablePolicy.class.getName());
	private Policy policy;
	private ScheduledExecutorService scheduler ;
	private Boolean working = false;
	private List<ScheduledFuture<?>> handlers = new ArrayList<ScheduledFuture<?>>();

	public ExecutablePolicy(Policy policy, ScheduledExecutorService scheduler) {
		this.policy = policy;
		this.scheduler  = scheduler;
	}

	public void start() {

		for(Rule rule : policy.getRules()) {

			if(rule.getPeriod()!= null&&rule.getPeriod()!= 0) {
				VerifyRule verifyRule = new VerifyRule(rule, policy.getAddressTraining(),policy.getAddressNewData(), policy.getAddressUpdate());
//				long period = TimeUnit.SECONDS.convert(rule.getPeriod(), rule.getTimeUnit());
//				long delay = verifyRule.calculateDelay();
//				System.out.println("Initializing rule");
				LOGGER.info("Initializing rule");
				long period = TimeUnit.SECONDS.convert(rule.getPeriod(), TimeUnit.valueOf(rule.getTimeUnit()));
				ZonedDateTime nextRun = verifyRule.calculateNextRun();
//				System.out.println("Next check at: "+nextRun);
				LOGGER.info("Next check at: "+nextRun);
				long delay = verifyRule.calculateDelayFromNext(nextRun);				
//				System.out.println(nextRun);
//				System.out.println(delay);				
				ScheduledFuture<?> verifyRuleHandle = scheduler.scheduleAtFixedRate(verifyRule, delay, period, TimeUnit.SECONDS);
				handlers.add(verifyRuleHandle);
			}
		}
		
		working =true;

	}

	public void stop() {		
		for(ScheduledFuture<?> handler : handlers) {
			scheduler.schedule(new Runnable() {
				public void run() { handler.cancel(true); }
			}, 0, TimeUnit.SECONDS);
		}
		working = false;
	}

	public String getPolicyId() {
		return policy.getPolicyId();
	}

	public String getOwnerId() {
		return policy.getOwnerId();
	}

	public Boolean getWorking() {
		return working;
	}
	
	public String getAddress() {
		return policy.getAddressTraining();
	}


}
