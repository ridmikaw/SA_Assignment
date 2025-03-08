package com.tourismsafety.service;

import com.tourismsafety.model.DisasterAlert;

public interface DisasterAlertService {
	/**
     * Broadcasts an emergency alert for a specific location
     * 
     * @param location The geographical location of the alert
     * @param alertType Type of disaster (FLOOD, LANDSLIDE, STORM, etc.)
     * @param severityLevel Severity of the disaster (LOW, MEDIUM, HIGH, CRITICAL)
     * @param description Detailed description of the alert
     */
    void broadcastAlert(String location, String alertType, String severityLevel, String description);
    
    /**
     * Retrieves the most recent alert for a specific location
     * 
     * @param location The geographical location to check for alerts
     * @return Most recent disaster alert or null if no alerts exist
     */
    DisasterAlert getLatestAlert(String location);
}
