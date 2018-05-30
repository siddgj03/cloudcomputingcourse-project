package com.amazonaws.repository;

public class CourseRegistration {
	 private String studentID;
	    private String courseName;

	    public CourseRegistration(String studentID, String courseName) {
	        this.studentID = studentID;
	        this.courseName = courseName;
	    }

	    public String getRegistrationID() {
	        return studentID;
	    }

	    public String getCourseName(){
	        return courseName;
	    }
}
