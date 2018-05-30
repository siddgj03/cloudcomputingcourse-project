package com.amazonaws.lambda.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;

public class LambdaFunctionHandler implements RequestHandler<DynamodbEvent, String> {
	
	private AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
			.withRegion(Regions.US_WEST_2).build();
	
	private static String RESTURANTS_SNS_TOPIC = "arn:aws:sns:us-west-2:243824163312:Restraunt";
    
    @Override
	public String handleRequest(DynamodbEvent input, Context context) {
    	String output = " ";
		  // Read DDB Records
			for (DynamodbStreamRecord record : input.getRecords()) {

	            if (record == null) {
	                continue;
	            }
	            String inputString = record.toString();	
	            context.getLogger().log("Input: " + input);
	            output = "Hello" + input + "!";
	            String outputBody = output + " A new announcement for your class";
	            sendEmailNotification(output, outputBody);
	           
	        }
	      return output;
	  }

	  	private void sendEmailNotification(final String subject, final String message) {  		
	  		// Message object 
	  		PublishRequest publishRequest 
	  				= new PublishRequest(RESTURANTS_SNS_TOPIC, message);
	  		// Call Client.publishMessage
	  		SNS_CLIENT.publish(publishRequest);
	  	}
	  	
	  	private String getResturantName(String record) {
	  		String resturantName = " ";
	  		// JSON Parser Logic
	  		return resturantName;
	  	}
	  	
	 	private String getLocation(String location) {
	  		String restrauntLocation = " ";
	  		// JSON Parser Logic
	  		return location;
	  	}
}