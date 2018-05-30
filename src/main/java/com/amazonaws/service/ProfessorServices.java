package com.amazonaws.service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.google.gson.Gson;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.repository.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/professor")
public class ProfessorServices {
	 static AmazonDynamoDB user;
	    static AmazonSNSClient snsUser;

	    private static void init() throws Exception {
	        ProfileCredentialsProvider credentialsProvider =
	                new ProfileCredentialsProvider();
	        credentialsProvider.getCredentials();

	        user = AmazonDynamoDBClientBuilder
	                .standard()
	                .withCredentials(credentialsProvider)
	                .withRegion("us-west-2")
	                .build();

	    }
	    @GET
	    @Path("/{name}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public String getProfessorDetails(@PathParam("name") String professorName) throws Exception {
	        init();
	        DynamoDB dynamoDB = new DynamoDB(user);
	        Table table = dynamoDB.getTable("Professor");
	        Item item = table.getItem("professorName", professorName);
	        if (item == null) {
	            return null;
	        }
	        return item.toJSON();
	    }

	    @POST
	    @Path("/create")
	    @Consumes(MediaType.APPLICATION_JSON)
	    public void createProfessor(String json) throws Exception {
	        init();
	        DynamoDB dynamoDB = new DynamoDB(user);
	        Table table = dynamoDB.getTable("Professor");
	        Gson gson = new Gson();
	        Professor prof = gson.fromJson(json, Professor.class);

	        Map<String, AttributeValue> item = new HashMap<>();
	        item.put("professorName", new AttributeValue().withS(prof.getProfessorName()));
	        item.put("courseName", new AttributeValue().withS(prof.getCourseName()));

	        PutItemRequest putItemRequest = new PutItemRequest(table.getTableName(), item);
	        System.out.println("PutItemRequest : " + putItemRequest);
	        PutItemResult putItemResult = user.putItem(putItemRequest);
	        System.out.println("PutItemResult: " + putItemResult);
	        init();
	    }

	    @POST
	    @Path("/{name}/announcement")
	    @Consumes(MediaType.APPLICATION_JSON)
	    public void makeAnnouncement(String json,@PathParam("name") String name)throws Exception{
	        init();
	        DynamoDB dynamoDB = new DynamoDB(user);
	        Table announcementTable = dynamoDB.getTable("Announcement");
	        Table courseTable = dynamoDB.getTable("Course");
	        Gson gson = new Gson();

	        Announcement newAnnouncement = gson.fromJson(json, Announcement.class);

	        Map<String, AttributeValue> item = new HashMap<>();
	        item.put("announcementID", new AttributeValue().withS(newAnnouncement.getAnnouncementID()));
	        item.put("professorName", new AttributeValue().withS(name));
	        item.put("message", new AttributeValue().withS(newAnnouncement.getMessage()));

	        PutItemRequest putItemRequest = new PutItemRequest(announcementTable.getTableName(), item);
	        System.out.println("PutItemRequest : " + putItemRequest);
	        PutItemResult putItemResult = user.putItem(putItemRequest);
	        System.out.println("PutItemResult: " + putItemResult);
	        init();
	        new CreateTopicRequest(course.getcourseName());
	        //PutItemOutcome outcome = announcementTable.putItem(item);
	    }
}
