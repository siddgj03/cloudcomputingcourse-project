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
import com.amazonaws.services.sns.model.SubscribeResult;

public class StudentFunctionHandler implements RequestHandler<DynamodbEvent, String> {
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
        //context.getLogger().log("Input: " + record);
        String studentName = " ";
        String studentEmail = "";
        try {
            studentName = getStudentName(record);
            studentEmail = getEmail(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // context.getLogger().log("Hello " + studentName);
       String output1 = "Hello, "+ studentName;
        String output2 = " Thank you for registering with our System! Please register for a course now";
        sendEmailNotification(output2, output1,studentEmail);
    }
    return null;
}
    private void sendEmailNotification(String studentName, String content,String email) {
        PublishRequest publishRequest = new PublishRequest(COURSE_SNS_TOPIC ,studentName,content);
       // SubscribeResult result = new SubscribeResult();
        SNS_Client.publish(publishRequest);
    }
    private String getStudentName(DynamodbEvent.DynamodbStreamRecord record) throws Exception {
        String studentName = "";
        studentName = record.getDynamodb().getNewImage().get("studentName").getS();
        return studentName;
    }
    private String getEmail(DynamodbEvent.DynamodbStreamRecord record) throws Exception {
        String email = "";
        email = record.getDynamodb().getNewImage().get("email").getS();
        return email;
    }
}