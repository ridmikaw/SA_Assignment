package com.weatherguard.producer.disasteralert.producer;

import com.weatherguard.producer.disasteralert.model.DisasterAlert;
import com.weatherguard.producer.disasteralert.service.DisasterAlertService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;

public class DisasterAlertProducer implements DisasterAlertService, BundleActivator {
    private ServiceRegistration<?> serviceRegistration;
    private Map<String, DisasterAlert> alertRegistry = new HashMap<>();

    @Override
    public void start(BundleContext context) throws Exception {
        // Register the service when the bundle starts
        serviceRegistration = context.registerService(
            DisasterAlertService.class.getName(), 
            this, 
            null
        );
     // Broadcast some sample alerts
        broadcastAlert("Kandy", "FLOOD", "HIGH", "Heavy flooding reported in central areas");
        broadcastAlert("Colombo", "STORM", "MEDIUM", "Tropical storm approaching, heavy rain expected");
        broadcastAlert("Galle", "LANDSLIDE", "LOW", "Minor landslide risk in hill regions");
        
        System.out.println("Disaster Alert Producer Bundle Started");    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // Unregister the service when the bundle stops
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
        }
        System.out.println("Disaster Alert Producer Bundle Stopped");
    }

    @Override
    public void broadcastAlert(String location, String alertType, String severityLevel, String description) {
        DisasterAlert alert = new DisasterAlert(location, alertType, severityLevel, description);
        alertRegistry.put(location, alert);
        System.out.printf("Broadcasted Alert: %s in %s%n", alertType, location);
    }

    @Override
    public DisasterAlert getLatestAlert(String location) {
        return alertRegistry.get(location);
    }
}