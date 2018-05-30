package com.amazonaws.lambda.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;

public class EnrollmentFunctionHandler implements RequestHandler<DynamodbEvent, String> {
	  private AmazonSNS SNS_Client = AmazonSNSClientBuilder.standard()
	            .withRegion(Regions.US_WEST_2).build();
	    private static String COURSE_SNS_TOPIC = "arn:aws:sns:us-west-2:243824163312:StudentNotification"; 
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
				.build();
	    static DynamoDB dynamoDB = new DynamoDB(client);
    @Override
    public String handleRequest(DynamodbEvent input, Context context) {

    	for(DynamodbEvent.DynamodbStreamRecord record : input.getRecords()) {
    	if (record == null) {
            continue;
        }
        context.getLogger().log("Input: " + record);
        String courseName = " ";
        try {
            courseName = getCourseName(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // context.getLogger().log("Hello " + studentName);
       
        String output = " You have successfully registerd for the " + courseName + " course.";
        sendEmailNotification(output);
    }
    return null;
    }
    
    private void sendEmailNotification(String content) {
        PublishRequest publishRequest = new PublishRequest(COURSE_SNS_TOPIC ,content);
        SNS_Client.publish(publishRequest);
    }
    private String getCourseName(DynamodbEvent.DynamodbStreamRecord record) throws Exception {
        String courseName = "";
        courseName = record.getDynamodb().getNewImage().get("courseName").getS();
        return courseName;
    }
}