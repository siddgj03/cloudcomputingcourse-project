package com.amazonaws.repository;

public class Course {
    private String courseName;
    private String courseCode;
    private String courseDescription;
    
    Course(String courseName, String coursecode, String courseDesc){
        super();
        this.courseName = courseName;
        this.courseCode = coursecode;
        this.courseDescription = courseDesc;
    }
    
    public void setcourseCode(String courseCode){
        this.courseCode = courseCode;
    }

    public String getcourseCode(){
        return courseCode;
    }
    
    public void setcourseDescription(String courseDescription){
        this.courseDescription = courseDescription;
    }

    public String getcourseDescription(){
        return courseDescription;
    }

    public void setcourseName(String courseName){
        this.courseName = courseName;
    }

    public String getcourseName(){
        return courseName;
    }

}
