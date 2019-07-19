package it.eng.openreq.updatemanager.management;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import it.eng.openreq.updatemanager.registration.Rule;


public class Management {

	//"http://localhost:8090/openreq/trainable"

	private static final Logger LOGGER = LogManager.getLogger(Management.class.getName());
	public void trainModel(String addressTraining, String addressUpdate) {

		//setWorking(true);

		CompletableFuture.supplyAsync(() ->{
			WebClient webClient = WebClient.create(addressTraining);
			//			System.out.println(LocalTime.now());
			//			System.out.println("Requesting training at "+ addressTraining);
			LOGGER.info("Requesting training at "+ addressTraining);
			TrainingResponse result = webClient.get()
					.retrieve()
					.bodyToMono(TrainingResponse.class)
					.block();
			return result;
		}).thenAccept(result -> {
			//			System.out.println(LocalTime.now());
			//			System.out.println("Training response at address " + addressTraining);
			//			System.out.println("Received: " + result);
			LOGGER.info("Training response at address " + addressTraining);
			LOGGER.info("Received result ");
			LOGGER.info("To update = "+result.getToUpdate());
			LOGGER.info("New Model Path = "+result.getNewModelPath());			
			if (result.getToUpdate()==1) {
				WebClient webClient = WebClient.create(addressUpdate);
				LOGGER.info("Requesting update at "+ addressUpdate);
				String response = webClient.put()
				.body(BodyInserters.fromObject(result.getNewModelPath()))
				.retrieve()
				.bodyToMono(String.class)
				.block();
				LOGGER.info("Received response "+ response);
			}
		}).thenRun(() -> {
			//setWorking(false);
		});

	}
	public void updateLastUpdate() throws IOException {
		//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//		Date date = new Date();
		//		dateFormat.format(date);
		//		history.setProperty("last_update", dateFormat.format(date));
		//		OutputStream output = new FileOutputStream(historyPath);
		//		history.store(output, null);

		//		System.out.println("Update last update");
		LOGGER.info("Update last update");

	}

	public boolean verifyCondition(Rule rule) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean verifyCondition(Rule rule, String address) {
		if(rule.getVolume()>0&&rule.getVolume()!=null) {
			WebClient webClient = WebClient.create(address);
			//			System.out.println(LocalTime.now());
			//			System.out.println("Getting new data available at "+ address);
			LOGGER.info("Getting new data available at "+ address);
			Integer result =  webClient.get()
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
			//			System.out.println("Get result: "+result);
			//			System.out.println("Rule volume: "+rule.getVolume());
			LOGGER.info("Get result: "+result);
			LOGGER.info("Rule volume: "+rule.getVolume());

			boolean condition = result>=rule.getVolume();

			return condition;
		}
		else return true;

	}


}
