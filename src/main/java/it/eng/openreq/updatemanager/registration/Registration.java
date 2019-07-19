package it.eng.openreq.updatemanager.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Registration{
	private static final Logger LOGGER = LogManager.getLogger(Registration.class.getName());

	private List<ExecutablePolicy> execPolicies = new ArrayList<ExecutablePolicy>();

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

	private PolicyDAO policyDao = new PolicyJSON();

	public boolean registerPolicy(Policy policy) {
		return policyDao.insert(policy);
	}

	public Policy retrivePolicy(String ownerId,String policyId) {
		return policyDao.findByIds(ownerId, policyId) ;
	}

	public void startPolicy(Policy policy){	
//		System.out.println("Starting policy for address: "+ policy.getAddressTraining());
		LOGGER.info("Starting policy for address: "+ policy.getAddressTraining());
		ExecutablePolicy execPolicy = new ExecutablePolicy(policy,scheduler);
		execPolicies.add(execPolicy);
		execPolicy.start();		
	}

	public void deletePolicy(Policy policy) {
		policyDao.delete(policy);
	}


	public boolean updatePolicy(Policy policy) {

		if ( policyDao.update(policy)) {
			stopPolicy(policy.getAddressTraining());
			startPolicy(policy);
			return true;
		}
		else 
			return false;

	}

	public void stopPolicy(String policyId, String ownerId) {
//		System.out.println("Stopping policy ownerId/policyId: "+ownerId+"/"+policyId);
		LOGGER.info("Stopping policy ownerId/policyId: "+ownerId+"/"+policyId);
		for (ExecutablePolicy execPolicy : execPolicies) {
			if(execPolicy.getPolicyId().equals(policyId) && execPolicy.getOwnerId().equals(ownerId)) {
				execPolicy.stop();
//				System.out.println("stopped");
			}
		}
	}

	public void stopPolicy(String address) {
//		System.out.println("Stopping policy for address: "+address);
		LOGGER.info("Stopping policy for address: "+address);
		for (ExecutablePolicy execPolicy : execPolicies) {
			if(execPolicy.getAddress().equals(address)) {
				execPolicy.stop();
				//System.out.println("stopped");
			}
		}		
	}
	
	public void startAllPolicies() {
		List<Policy> policies = policyDao.getAllPolicies();
		for (Policy policy : policies) {
			startPolicy(policy);
		}
	}
	

}
