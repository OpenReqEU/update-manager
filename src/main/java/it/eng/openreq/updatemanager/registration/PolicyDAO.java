package it.eng.openreq.updatemanager.registration;

import java.util.List;

public interface PolicyDAO {

	boolean insert(Policy policy);

	boolean update(Policy policy);

	void delete(Policy policy);

	Policy findByIds(String ownerId, String policyId);

	List<Policy> findByAddress(String address);

	List<Policy> getAllPolicies();

	int policiesCount();

	void delete(String ownerId, String policyId);

}