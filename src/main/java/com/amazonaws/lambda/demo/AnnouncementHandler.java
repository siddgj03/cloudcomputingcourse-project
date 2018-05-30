package com.amazonaws.lambda.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;

public class AnnouncementHandler  implements RequestHandler<DynamodbEvent, String> {
    private AmazonSNS SNS_Client = AmazonSNSClientBuilder.standard()
            .withRegion(Regions.US_WEST_2).build();
    private static String COURSE_SNS_TOPIC = "arn:aws:sns:us-west-2:243824163312:StudentNotification";
    
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.US_WEST_2)
			.build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    //@Override
    public String handleRequest(DynamodbEvent input, Context context) {
       
        for(DynamodbEvent.DynamodbStreamRecord record : input.getRecords()) {
        	   if (record == null) {
                   continue;
               }
               context.getLogger().log("Input: " + record);
               String content = " ";
               try {
                   content = getMessage(record);
               } catch (Exception e) {
                   e.printStackTrace();
               }
              
               String output = "Hello students ";
               sendEmailNotification(content, output);
           }
           return new String(" ");
    }

    private void sendEmailNotification(String content, String output) {
        PublishRequest publishRequest = new PublishRequest(COURSE_SNS_TOPIC ,content, output);
        SNS_Client.publish(publishRequest);
    }

    
    private String getMessage(DynamodbEvent.DynamodbStreamRecord record) throws Exception {
        String message = "";
        message = record.getDynamodb().getNewImage().get("message").getS();
        return message;
    }
}
