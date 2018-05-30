package com.amazonaws.repository;

public class Professor {
	 private String professorName;
	    private String courseName;

	    public Professor(String professorName, String courseName){
	        this.professorName = professorName;
	        this.courseName = courseName;
	    }
	    public String getCourseName() {
	        return courseName;
	    }
	    public String getProfessorName() {
	        return professorName;
	    }

	    public void setCourseName(String courseName) {
	        this.courseName = courseName;
	    }

	    public void setProfessorName(String professorName) {
	        this.professorName = professorName;
	    }
}
