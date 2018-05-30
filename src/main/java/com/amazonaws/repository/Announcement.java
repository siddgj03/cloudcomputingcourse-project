package com.amazonaws.repository;

public class Announcement {
	   String announcementID;
	    String message;

	    public Announcement(String id, String content) {
	        super();
	        this.announcementID = id;

	        this.message = content;
	    }
	    public String getAnnouncementID() {
	        return announcementID;
	    }
	    public void setannouncementID(String id) {
	        this.announcementID = id;
	    }
	    public String getMessage() {
	        return message;
	    }
	    public void setMessage(String message) {
	        this.message = message;
	    }
}
