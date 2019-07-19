package it.eng.openreq.updatemanager.registration;

import java.util.ArrayList;
import java.util.List;

public class Policy {
	
	private String ownerId = new String();
	
	private String policyId = new String();
	
	private String addressTraining = new String();
	
	private String addressUpdate = new String();	
	
	private String addressNewData = new String();
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	public void addRule(Rule rule){
		this.rules.add(rule);
	}

	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public String getAddressTraining() {
		return addressTraining;
	}
	public void setAddressTraining(String addressTraining) {
		this.addressTraining = addressTraining;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getAddressNewData() {
		return addressNewData;
	}

	public void setAddressNewData(String addressNewData) {
		this.addressNewData = addressNewData;
	}

	public String getAddressUpdate() {
		return addressUpdate;
	}

	public void setAddressUpdate(String addressUpdate) {
		this.addressUpdate = addressUpdate;
	}



}
