package com.weatherguard.producer.disasteralert.model;

import java.time.LocalDateTime;

public class DisasterAlert {
	 	private String location;
	    private String alertType;
	    private String severityLevel;
	    private String description;
	    private LocalDateTime timestamp;

	    public DisasterAlert(String location, String alertType, String severityLevel, String description) {
	        this.location = location;
	        this.alertType = alertType;
	        this.severityLevel = severityLevel;
	        this.description = description;
	        this.timestamp = LocalDateTime.now();
	    }

	    // Getters and setters
	    public String getLocation() { return location; }
	    public String getAlertType() { return alertType; }
	    public String getSeverityLevel() { return severityLevel; }
	    public String getDescription() { return description; }
	    public LocalDateTime getTimestamp() { return timestamp; }

	    @Override
	    public String toString() {
	        return String.format("Disaster Alert: %s in %s - Severity: %s\nDescription: %s\nTime: %s", 
	            alertType, location, severityLevel, description, timestamp);
	    }
}
