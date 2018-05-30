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

@Path("/course")
public class CourseServices {


    static AmazonDynamoDB user;
    static AmazonSNSClient snsUser;

    private static void snsInit(){
        ProfileCredentialsProvider credentialsProvider =
                new ProfileCredentialsProvider();
        credentialsProvider.getCredentials();

        snsUser = new AmazonSNSClient(credentialsProvider);
        snsUser.setRegion(Region.getRegion(Regions.US_WEST_2));
    }

    private static void init() throws Exception{
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
    public String getCourse(@PathParam("name") String name) throws Exception {
        init();
        DynamoDB dynamoDB = new DynamoDB(user);
        Table table = dynamoDB.getTable("Course");
        Item item = table.getItem("courseName", name);
        if (item == null) {
            return null;
        }
        return item.toJSON();
    }


    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createCourse(String json) throws Exception {
        System.out.println(json);
        init();
        DynamoDB dynamoDB = new DynamoDB(user);
        Table table = dynamoDB.getTable("Course");
        Gson gson = new Gson();
        Course course = gson.fromJson(json, Course.class);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("courseName", new AttributeValue().withS(course.getcourseName()));
        item.put("courseCode", new AttributeValue().withS(course.getcourseCode()));
        item.put("courseDescription", new AttributeValue().withS(course.getcourseDescription()));
        PutItemRequest putItemRequest = new PutItemRequest(table.getTableName(), item);
        System.out.println("PutItemRequest : " + putItemRequest);
        PutItemResult putItemResult = user.putItem(putItemRequest);
        System.out.println("PutItemResult: " + putItemResult);
        snsInit();
        new CreateTopicRequest(course.getcourseName());
    }
}
