package com.weatherguard.consumer.touroperator.consumer;

import com.weatherguard.producer.disasteralert.model.DisasterAlert;
import com.weatherguard.producer.disasteralert.service.DisasterAlertService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Random;

public class TourOperatorConsumer implements BundleActivator {
    private ServiceReference<DisasterAlertService> serviceReference;
    private DisasterAlertService disasterAlertService;
    private Random random = new Random();

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
                // Generate a random risk level between 0-10
                int riskLevel = generateRandomRiskLevel();
                adjustTourSchedule(latestAlert, riskLevel);
            }
        }
    }

    // Generate a random risk level between 0-10
    private int generateRandomRiskLevel() {
        return random.nextInt(11); // Generates a random number between 0  and 11
    }

    private void adjustTourSchedule(DisasterAlert alert, int riskLevel) {
        System.out.println("=============================================");
        System.out.println("        Tour Schedule Adjustment          ");
        System.out.println("=============================================");
        System.out.printf(" %-12s : %-25s \n", "Location", alert.getLocation());
        System.out.printf(" %-12s : %-25s \n", "Alert Type", alert.getAlertType());
        System.out.printf(" %-12s : %-25s \n", "Severity", alert.getSeverityLevel());
        System.out.printf(" %-12s : %-25s \n", "Description", alert.getDescription());
        System.out.printf(" %-12s : %-25d \n", "Risk Level", riskLevel);
        System.out.println("=============================================");        
        
        // Decision logic based on risk level
        if (riskLevel >= 0 && riskLevel <= 3) {
            System.out.println("\n--------------------------------------");
            System.out.println("       LOW RISK LEVEL (0-3)          ");
            System.out.println("--------------------------------------");
            System.out.println("Standard operations continue         ");
            System.out.println("Routine monitoring in place          ");
            System.out.println("No special actions required          ");
            System.out.println("--------------------------------------");

            // Specific actions for different disaster types at low risk
            if (alert.getAlertType().equals("RAIN") || alert.getAlertType().equals("STORM")) {
                System.out.println("ACTIONS:                              ");
                System.out.println("Remind tourists to bring umbrellas ");
                System.out.println("Check weather updates regularly    ");
            } else if (alert.getAlertType().equals("HEAT")) {
                System.out.println("ACTIONS:                              ");
                System.out.println("Ensure water is readily available  ");
                System.out.println("Remind about sun protection        ");
            }
            System.out.println("--------------------------------------");
        } 
        else if (riskLevel >= 4 && riskLevel <= 6) {
            System.out.println("\n--------------------------------------");
            System.out.println("       MODERATE RISK LEVEL (4-6)      ");
            System.out.println("--------------------------------------");
            System.out.println("Minor schedule adjustments needed     ");
            System.out.println("Regular updates to tourists & guides  ");
            System.out.println("Prepare contingency plans             ");
            System.out.println("--------------------------------------");

            // Specific actions for different disaster types at moderate risk
            if (alert.getAlertType().equals("FLOOD") || alert.getAlertType().equals("RAIN")) {
                System.out.println("ACTIONS:                              ");
                System.out.println("Avoid low-lying areas temporarily  ");
                System.out.println("Provide waterproof gear to tourists");
            } else if (alert.getAlertType().equals("LANDSLIDE")) {
                System.out.println("ACTIONS:                              ");
                System.out.println("Avoid steep terrain & hillsides    ");
                System.out.println("Monitor ground conditions          ");
            }
            System.out.println("--------------------------------------");
        }
        else if (riskLevel >= 7 && riskLevel <= 8) {
            System.out.println("\n--------------------------------------");
            System.out.println("       HIGH RISK LEVEL (7-8)          ");
            System.out.println("--------------------------------------");
            System.out.println("Major schedule changes required       ");
            System.out.println("Relocate tours to safer areas         ");
            System.out.println("Hourly communication with all guides  ");
            System.out.println("Prepare evacuation plans              ");
            System.out.println("--------------------------------------");

            // Specific actions for different disaster types at high risk
            if (alert.getAlertType().equals("STORM") || alert.getAlertType().equals("FLOOD")) {
                System.out.println("ACTIONS:                              ");
                System.out.println("Move all activities indoors        ");
                System.out.println("Prepare emergency shelter options  ");
                System.out.println("Ready transport for quick evacuation");
            } else if (alert.getAlertType().equals("LANDSLIDE")) {
                System.out.println("ACTIONS:                              ");
                System.out.println("Cancel all hill and mountain tours ");
                System.out.println("Relocate tourists from risk areas  ");
            }
            System.out.println("--------------------------------------");
        }
        else if (riskLevel >= 9 && riskLevel <= 10) {
            System.out.println("\n--------------------------------------");
            System.out.println("       CRITICAL RISK LEVEL (9-10)     ");
            System.out.println("--------------------------------------");
            System.out.println("EMERGENCY PROTOCOL ACTIVATED          ");
            System.out.println("IMMEDIATE TOUR CANCELLATION           ");
            System.out.println("COMMENCE EVACUATION PROCEDURES        ");
            System.out.println("CONTACT EMERGENCY SERVICES            ");
            System.out.println("--------------------------------------");

            // Emergency actions regardless of disaster type
            System.out.println("URGENT ACTIONS:                       ");
            System.out.println("Move all tourists to safe zones     ");
            System.out.println("Establish emergency communication   ");
            System.out.println("Coordinate with local authorities   ");
            System.out.println("--------------------------------------");
        }
    }
}