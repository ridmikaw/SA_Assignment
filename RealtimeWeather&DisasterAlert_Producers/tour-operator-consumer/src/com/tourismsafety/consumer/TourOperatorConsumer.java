package com.tourismsafety.consumer;

import com.tourismsafety.service.DisasterAlertService;
import com.tourismsafety.model.DisasterAlert;
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
                System.out.println("Minor schedule modifications recommended");
                break;
            case "MEDIUM":
                System.out.println("Significant schedule changes required");
                break;
            case "HIGH":
            case "CRITICAL":
                System.out.println("TOUR CANCELLATION RECOMMENDED");
                break;
        }
    }
}