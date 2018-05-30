package com.amazonaws.service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class DynamoDbInitializer {
	  static AmazonDynamoDB dynamoDb;

	    private static void init() throws Exception {
	        ProfileCredentialsProvider credentialsProvider =
	                new ProfileCredentialsProvider();
	        credentialsProvider.getCredentials();

	        dynamoDb = AmazonDynamoDBClientBuilder
	                .standard()
	                .withCredentials(credentialsProvider)
	                .withRegion("us-west-2")
	                .build();
	    }

	    private static void createStudentTable() {
	        String tableName = "Student";
	        CreateTableRequest createTableRequest = new CreateTableRequest()
	                .withTableName(tableName)
	                .withKeySchema(
	                        new KeySchemaElement()
	                                .withAttributeName("studentID")
	                                .withKeyType(KeyType.HASH))
	                .withAttributeDefinitions(
	                        new AttributeDefinition()
	                                .withAttributeName("studentID")
	                                .withAttributeType(ScalarAttributeType.S))
	                .withProvisionedThroughput(
	                        new ProvisionedThroughput()
	                                .withReadCapacityUnits(3L)
	                                .withWriteCapacityUnits(3L));
	        TableUtils.createTableIfNotExists(dynamoDb, createTableRequest);
	        try {
	            TableUtils.waitUntilActive(dynamoDb, tableName);
	        } catch (TableUtils.TableNeverTransitionedToStateException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    private static void createAnnouncementTable() {
	        String tableName = "Announcement";
	        CreateTableRequest createTableRequest = new CreateTableRequest()
	                .withTableName(tableName)
	                .withKeySchema(
	                        new KeySchemaElement()
	                                .withAttributeName("announcementID")
	                                .withKeyType(KeyType.HASH))
	                .withAttributeDefinitions(
	                        new AttributeDefinition()
	                                .withAttributeName("announcementID")
	                                .withAttributeType(ScalarAttributeType.S))
	                .withProvisionedThroughput(
	                        new ProvisionedThroughput()
	                                .withReadCapacityUnits(3L)
	                                .withWriteCapacityUnits(3L));
	        TableUtils.createTableIfNotExists(dynamoDb, createTableRequest);
	        try {
	            TableUtils.waitUntilActive(dynamoDb, tableName);
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    private static void createEnrollmentTable() {
	        String tableName = "Enrollment";
	        CreateTableRequest createTableRequest = new CreateTableRequest()
	                .withTableName(tableName)
	                .withKeySchema(
	                        new KeySchemaElement()
	                                .withAttributeName("studentID")
	                                .withKeyType(KeyType.HASH),
	                        new KeySchemaElement()
	                                .withAttributeName("courseName")
	                                .withKeyType(KeyType.RANGE))
	                .withAttributeDefinitions(
	                        new AttributeDefinition()
	                                .withAttributeName("studentID")
	                                .withAttributeType(ScalarAttributeType.S),
	                        new AttributeDefinition()
	                                .withAttributeName("courseName")
	                                .withAttributeType(ScalarAttributeType.S))
	                .withProvisionedThroughput(
	                        new ProvisionedThroughput()
	                                .withReadCapacityUnits(3L)
	                                .withWriteCapacityUnits(3L));
	        TableUtils.createTableIfNotExists(dynamoDb, createTableRequest);
	        try {
	            TableUtils.waitUntilActive(dynamoDb, tableName);
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    private static void createProfessorTable() {
	        String tableName = "Professor";
	        CreateTableRequest createTableRequest = new CreateTableRequest()
	                .withTableName(tableName)
	                .withKeySchema(
	                        new KeySchemaElement()
	                                .withAttributeName("professorName")
	                                .withKeyType(KeyType.HASH))
	                .withAttributeDefinitions(
	                        new AttributeDefinition()
	                                .withAttributeName("professorName")
	                                .withAttributeType(ScalarAttributeType.S))
	                .withProvisionedThroughput(
	                        new ProvisionedThroughput()
	                                .withReadCapacityUnits(3L)
	                                .withWriteCapacityUnits(3L));
	        TableUtils.createTableIfNotExists(dynamoDb, createTableRequest);
	        try {
	            TableUtils.waitUntilActive(dynamoDb, tableName);
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    private static void createCourseTable() {
	        String tableName = "Course";
	        CreateTableRequest createTableRequest = new CreateTableRequest()
	                .withTableName(tableName)
	                .withKeySchema(
	                        new KeySchemaElement()
	                                .withAttributeName("courseName")
	                                .withKeyType(KeyType.HASH))
	                .withAttributeDefinitions(
	                        new AttributeDefinition()
	                                .withAttributeName("courseName")
	                                .withAttributeType(ScalarAttributeType.S))
	                .withProvisionedThroughput(
	                        new ProvisionedThroughput()
	                                .withReadCapacityUnits(3L)
	                                .withWriteCapacityUnits(3L));
	        TableUtils.createTableIfNotExists(dynamoDb, createTableRequest);
	        try {
	            TableUtils.waitUntilActive(dynamoDb, tableName);
	        } catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        init();
	        createStudentTable();
	        createAnnouncementTable();
	        createCourseTable();
	        createProfessorTable();
	        createEnrollmentTable();

	    }
}
