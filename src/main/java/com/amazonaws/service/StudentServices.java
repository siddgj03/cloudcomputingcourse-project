package com.amazonaws.service;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.google.gson.Gson;
import com.amazonaws.repository.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/student")
public class StudentServices {
	static AmazonDynamoDB user;
    static String arn = "arn:aws:sns:us-west-2:243824163312:StudentNotification";
    static AmazonSNSClient snsUser;

    private static void snsInit() {
        ProfileCredentialsProvider credentialsProvider =
                new ProfileCredentialsProvider();
        credentialsProvider.getCredentials();

        snsUser = new AmazonSNSClient(credentialsProvider);
        snsUser.setRegion(Region.getRegion(Regions.US_WEST_2));
    }

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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStudent(@PathParam("id") String studentID) throws Exception {
        init();
        DynamoDB dynamoDB = new DynamoDB(user);
        Table table = dynamoDB.getTable("Student");
        Item item = table.getItem("studentID", studentID);
        if (item == null) {
            return null;
        }
        return item.toJSON();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createStudent(String json) throws Exception {
        init();
        DynamoDB dynamoDB = new DynamoDB(user);
        Table table = dynamoDB.getTable("Student");
        Gson gson = new Gson();
        Student student = gson.fromJson(json, Student.class);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("studentID", new AttributeValue().withS(student.getStudentID()));
        item.put("studentName", new AttributeValue().withS(student.getStudentName()));
        item.put("email", new AttributeValue().withS(student.getEmail()));

        PutItemRequest putItemRequest2 = new PutItemRequest(table.getTableName(), item);
        System.out.println("PutItemRequest : " + putItemRequest2);
        PutItemResult putItemResult2 = user.putItem(putItemRequest2);
        System.out.println("PutItemResult: " + putItemResult2);
        snsInit();
        new CreateTopicRequest(student.getStudentName());
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public void register(String json) throws Exception {
        init();
        snsInit();
        DynamoDB dynamoDB = new DynamoDB(user);
        Gson gson = new Gson();

        Table enrollmentTable = dynamoDB.getTable("Enrollment");

        CourseRegistration course = gson.fromJson(json, CourseRegistration.class);

        Map<String, AttributeValue> enrolItem = new HashMap<>();
        enrolItem.put("studentID", new AttributeValue().withS(course.getRegistrationID()));
        enrolItem.put("courseName", new AttributeValue().withS(course.getCourseName()));

        PutItemRequest putItemRequest1 = new PutItemRequest(enrollmentTable.getTableName(), enrolItem);
        System.out.println("PutItemRequest : " + putItemRequest1);
        PutItemResult putItemResult1 = user.putItem(putItemRequest1);
        System.out.println("PutItemResult: " + putItemResult1);
        snsInit();
    }

    // Delete a particular student
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public void deleteStudent(@PathParam("id") String studentID) throws Exception {
        init();
        DynamoDB dynamoDB = new DynamoDB(user);
        Table table = dynamoDB.getTable("Student");
        Gson gson = new Gson();
        Item item = table.getItem("studentID", studentID);
        if (item == null) {
            return;
        }
    }
}
