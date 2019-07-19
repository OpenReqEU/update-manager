package it.eng.openreq.updatemanager.registration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class RegistrationRest {

	private Registration registration = new Registration();
	private static final Logger LOGGER = LogManager.getLogger(RegistrationRest.class.getName());


	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ResponseEntity<String> registryAndStart(@RequestBody Policy policy){

		LOGGER.info("Received request to POST policy for address: "+policy.getAddressTraining());
		Boolean registered = registration.registerPolicy(policy);
		
		if(registered) {
			LOGGER.info("Registered policy for address: "+policy.getAddressTraining());
			registration.startPolicy(policy);
			return new ResponseEntity<String>("POST response: Policy registered and started", HttpStatus.OK);
		}
		else {
			LOGGER.info("Impossible to register policy for address: "+policy.getAddressTraining());
			return new ResponseEntity<String>("POST response: impossible to register policy", HttpStatus.BAD_REQUEST);
		}
	}


	@RequestMapping(value = "/registration", method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePolicy(@RequestBody Policy policy){
		registration.stopPolicy(policy.getAddressTraining());
		registration.deletePolicy(policy);
		return new ResponseEntity<String>("DELETE response: ok", HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/registration", method = RequestMethod.PUT)
	public ResponseEntity<String> updatePolicy(@RequestBody Policy policy){
		if(registration.updatePolicy(policy))
			return new ResponseEntity<String>("PUT response: Policy updated", HttpStatus.OK);
		else 
			return new ResponseEntity<String>("PUT response: impossible to update policy", HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ResponseEntity<String> greetings(){
	
			return new ResponseEntity<String>("Welcome to Registration Service!", HttpStatus.OK);
		}



}
