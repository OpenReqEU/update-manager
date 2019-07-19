package it.eng.openreq.updatemanager.registration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class PolicyJSON implements PolicyDAO {
	private static final Logger LOGGER = LogManager.getLogger(PolicyJSON.class.getName());
	private ObjectMapper mapper = new ObjectMapper()
			.registerModule(new ParameterNamesModule())
			.registerModule(new Jdk8Module())
			.registerModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


	private String folder = "Policies\\";

	public PolicyJSON() {
		File rootDir = new File(folder);
		if(!rootDir.exists())
			rootDir.mkdirs();
	}
	private String createAbsoluteName(Policy policy) {
		return folder+"policy_"+policy.getOwnerId()+"_"+policy.getPolicyId()+policy.getAddressTraining().hashCode()+".json";
		//		File ownerDirectory = new File(this.folder+"\\"+policy.getOwnerId()+"\\");
		//		ownerDirectory.mkdirs();
		//		return ownerDirectory.getAbsolutePath()+"\\"+"policy_"+policy.getPolicyId()
		//		+policy.getAddress().hashCode()+".json";
		//		return folder+"policy_"+policy.getAddress().replaceAll("\\", "_")+".json";
	}


	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#insert(openreq.wp2.analyticsBackend.updateManager.registration.Policy)
	 */
	@Override
	public boolean insert(Policy policy)  {
		boolean alreadyExist = false;
		boolean inserted = false;
		List<Policy> policies = getAllPolicies();
		for(Policy forPolicy : policies) {
			//					System.out.println("forPolicy.getAddress() = " + forPolicy.getAddress());
			//					System.out.println("policy.getAddress() = " + policy.getAddress());
			if (forPolicy.getAddressTraining().equals(policy.getAddressTraining())){
				//				System.out.println("Policy for the given address already exist");
				LOGGER.error("Policy for the given address already exist");
				alreadyExist = true;
			}
			//					System.out.println(alreadyExist);
		}
		if (alreadyExist==false) {

				try {
					mapper.writeValue(new File(createAbsoluteName(policy)), policy);
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inserted = true;						

		}
		return inserted;
	}



	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#update(openreq.wp2.analyticsBackend.updateManager.registration.Policy)
	 */
	@Override
	public boolean update(Policy policy) {
		List<Policy> policies = getAllPolicies();
		boolean result = false;
		for(Policy forPolicy : policies) {
			if (forPolicy.getAddressTraining().equals(policy.getAddressTraining())){
				//				System.out.println("Updating policy for address: "+policy.getAddressTraining());
				LOGGER.info("Updating policy for address: "+policy.getAddressTraining());
				delete(forPolicy);
				result = insert(policy);
			}			
		}
		return result;
	}



	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#delete(openreq.wp2.analyticsBackend.updateManager.registration.Policy)
	 */
	@Override
	public void delete(Policy policy) {
		List<Policy> policies = getAllPolicies();
		for(Policy forPolicy : policies) {
			if (forPolicy.getAddressTraining().equals(policy.getAddressTraining())){
				//				System.out.println("Deleting policy for address: "+policy.getAddressTraining());
				LOGGER.info("Deleting policy for address: "+policy.getAddressTraining());
				File file = new File(createAbsoluteName(forPolicy));
				System.out.println("with File name"+file);
				file.delete();
			}
		}
	}


	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#findByIds(java.lang.String, java.lang.String)
	 */
	@Override
	public Policy findByIds(String ownerId, String policyId) {
		// TODO Auto-generated method stub
		try {
			return mapper.readValue(new File(folder+ownerId+"_"+policyId+".json"), Policy.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}


	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#findByAddress(java.lang.String)
	 */
	@Override
	public List<Policy> findByAddress(String address) {
		return null;
	}


	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#getAllPolicies()
	 */
	@Override
	public List<Policy> getAllPolicies() {
		// TODO Auto-generated method stub

		List<Policy> policies = new ArrayList<Policy>();
		File folderFile = new File(folder);
		File[] listOfFiles = folderFile.listFiles();
		//				for (File file :listOfFiles){
		//					System.out.println(file);
		//				}

		//		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		//		if (listOfFiles!= null) {
		//		System.out.println("Retrieving existing policies");
		LOGGER.info("Retrieving existing policies");
		for (File file :listOfFiles){
			if(file.isFile()&&file.getName().matches("policy.*\\.json")) {
				try {	
					//					System.out.println(file.getName());
					LOGGER.info("retrived "+file.getName());
					Policy policy = mapper.readValue(file, Policy.class);
					policies.add(policy);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//		}
		return policies;

	}



	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#policiesCount()
	 */
	@Override
	public int policiesCount() {
		// TODO Auto-generated method stub
		return 0;
	}


	/* (non-Javadoc)
	 * @see openreq.wp2.analyticsBackend.updateManager.registration.PolicyDAO#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String ownerId, String policyId) {
		// TODO Auto-generated method stub

	}


}
