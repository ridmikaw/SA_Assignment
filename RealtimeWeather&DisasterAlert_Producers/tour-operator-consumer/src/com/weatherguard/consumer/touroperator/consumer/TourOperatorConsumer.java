package com.weatherguard.consumer.touroperator.consumer;

import com.weatherguard.producer.disasteralert.model.DisasterAlert;
import com.weatherguard.producer.disasteralert.service.DisasterAlertService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TourOperatorConsumer implements BundleActivator {
    private ServiceReference<DisasterAlertService> serviceReference;
    private DisasterAlertService disasterAlertService;

    @Override
    public void start(BundleContext context) throws Exception {
        // Locate the Disaster Alert Service
        serviceReference = context.getServiceReference(DisasterAlertService.class);
        
        if (serviceReference != null) {
            disasterAlertService = context.getService(serviceReference);
            
            // Simulate checking alerts and adjusting tour schedules
            monitorAlertsAndAdjustSchedules();
        } else {
            System.out.println("Disaster Alert Service not available");
        }
        
        System.out.println("Tour Operator Consumer Bundle Started");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // Unget the service and clean up
        if (serviceReference != null) {
            context.ungetService(serviceReference);
        }
        System.out.println("Tour Operator Consumer Bundle Stopped");
    }

    private void monitorAlertsAndAdjustSchedules() {
        // Example locations to monitor
        String[] locations = {"Kandy", "Colombo", "Galle", "Sigiriya"};
        
        for (String location : locations) {
            DisasterAlert latestAlert = disasterAlertService.getLatestAlert(location);
            
            if (latestAlert != null) {
                adjustTourSchedule(latestAlert);
            }
        }
    }

    private void adjustTourSchedule(DisasterAlert alert) {
        System.out.println("Tour Schedule Adjustment Based on Alert:");
        System.out.println("Location: " + alert.getLocation());
        System.out.println("Alert Type: " + alert.getAlertType());
        System.out.println("Severity: " + alert.getSeverityLevel());
        
        
        // Decision logic for schedule adjustment
        switch (alert.getSeverityLevel()) {
            case "LOW":
            	System.out.println("\n LOW SEVERITY RESPONSE:");
                System.out.println("Minor schedule modifications recommended");
                System.out.println("Continue tours with caution");
                System.out.println("Notify guides and tourists of potential risks");
                // Specific actions for different disaster types
                if (alert.getAlertType().equals("RAIN") || alert.getAlertType().equals("STORM")) {
                    System.out.println("Provide rain gear to all tourists");
                    System.out.println("Move outdoor activities to covered areas where possible");
                } else if (alert.getAlertType().equals("HEAT")) {
                    System.out.println("Add extra water breaks");
                    System.out.println("Reschedule outdoor activities to early morning/evening");
                }
                break;
            case "MEDIUM":
            	  System.out.println("\n MEDIUM SEVERITY RESPONSE:");
                  System.out.println("Significant schedule changes required");
                  System.out.println("Reroute tours away from high-risk areas");
                  System.out.println("Prepare backup indoor activities");
                  System.out.println("Check-in with all guides hourly");
                  
                  if (alert.getAlertType().equals("FLOOD")) {
                      System.out.println("Avoid all low-lying areas and water crossings");
                      System.out.println("Prepare emergency evacuation vehicles");
                  } else if (alert.getAlertType().equals("LANDSLIDE")) {
                      System.out.println("Cancel all hillside and mountain tours");
                      System.out.println("Restrict tours to urban areas only");
                  }
                break;
            case "HIGH":
            case "CRITICAL":
                String urgencyLevel = alert.getSeverityLevel().equals("HIGH") ? "URGENT" : "EMERGENCY";
                System.out.println("\n " + urgencyLevel + " RESPONSE REQUIRED:");
                System.out.println("TOUR CANCELLATION RECOMMENDED");
                break;
        }
    }
}