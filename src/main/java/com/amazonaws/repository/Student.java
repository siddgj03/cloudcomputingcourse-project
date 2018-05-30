package com.amazonaws.repository;

public class Student {
	 private String studentID;
	    private String studentName;
	    private String email;

	    Student(String studentID, String studentName, String email){
	        this.studentID = studentID;
	        this.studentName = studentName;
	        this.email = email;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public String getStudentName() {
	        return studentName;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public void setStudentID(String studentID) {
	        this.studentID = studentID;
	    }

	    public void setStudentName(String studentName) {
	        this.studentName = studentName;
	    }

	    public String getStudentID() {
	        return studentID;
	    }
}
