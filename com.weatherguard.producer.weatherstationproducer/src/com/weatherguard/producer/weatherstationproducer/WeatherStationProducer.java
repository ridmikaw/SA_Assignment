package com.weatherguard.producer.weatherstationproducer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.emergency.api.AlertMessage;
import com.emergency.api.AlertProducer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherStationProducer implements BundleActivator, AlertProducer {
    private BundleContext context;
    private ServiceRegistration<AlertProducer> registration;
    private Scanner scanner;
    private Timer timer;
    private Random random;
    
    // Producer configuration
    private String stationId;
    private String location;
    private int reportingInterval;
    private Map<String, Double> eventThresholds;
    
    public void start(BundleContext context) {
        this.context = context;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.eventThresholds = new HashMap<>();
        
        // Setup producer configuration through user input
        setupProducer();
        
        // Register as an AlertProducer service
        Dictionary<String, Object> props = new Hashtable<>();
        props.put("producer.type", "weather.station");
        props.put("station.id", stationId);
        props.put("producer.location", location);
        registration = context.registerService(AlertProducer.class, this, props);
        
        System.out.println("\nWeather Station Producer started for " + stationId);
        System.out.println("Location: " + location);
        System.out.println("Reporting interval: " + reportingInterval + " minutes");
        System.out.println("Collecting data...\n");
        
        // Start periodic data collection
        startDataCollection();
    }
    
    public void stop(BundleContext context) {
        if (registration != null) {
            registration.unregister();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("Weather Station Producer stopped.");
    }
    
    private void setupProducer() {
        System.out.println("\n==== WEATHER STATION PRODUCER SETUP ====");
        System.out.print("Enter weather station ID: ");
        stationId = scanner.nextLine();
        
        System.out.print("Enter station location (city, region): ");
        location = scanner.nextLine();
        
        System.out.print("Reporting interval in minutes: ");
        try {
            reportingInterval = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            reportingInterval = 15;  // Default to 15 minutes
            System.out.println("Invalid input. Using default interval: " + reportingInterval + " minutes");
        }
        
        System.out.println("\nSet up event thresholds:");
        setupThresholds();
    }
    
    private void setupThresholds() {
        System.out.println("\nConfigure alert thresholds:");
        
        // Rainfall threshold (mm/hour)
        System.out.print("Rainfall threshold for flood warning (mm/hour): ");
        try {
            double rainfall = Double.parseDouble(scanner.nextLine());
            eventThresholds.put("Flood", rainfall);
        } catch (NumberFormatException e) {
            eventThresholds.put("Flood", 25.0);  // Default
            System.out.println("Invalid input. Using default: " + eventThresholds.get("Flood") + " mm/hour");
        }
        
        // Wind speed threshold (km/h)
        System.out.print("Wind speed threshold for high wind warning (km/h): ");
        try {
            double wind = Double.parseDouble(scanner.nextLine());
            eventThresholds.put("Hurricane", wind);
            eventThresholds.put("Tornado", wind * 1.5);  // Higher threshold for tornado
        } catch (NumberFormatException e) {
            eventThresholds.put("Hurricane", 119.0);  // Default
            eventThresholds.put("Tornado", 178.0);    // Default
            System.out.println("Invalid input. Using defaults: Hurricane: " + 
                eventThresholds.get("Hurricane") + " km/h, Tornado: " + 
                eventThresholds.get("Tornado") + " km/h");
        }
        
        System.out.println("\nThresholds configured:");
        for (Map.Entry<String, Double> entry : eventThresholds.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }
    
    private void startDataCollection() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                collectAndPublishData();
            }
        }, 5000, reportingInterval * 60 * 1000);  // Convert minutes to milliseconds
        
        // For immediate simulation, also collect data once now
        collectAndPublishData();
    }
    
    private void collectAndPublishData() {
        // Collect weather data
        Map<String, Object> weatherData = collectWeatherData();
        
        // Analyze for potential alerts
        List<AlertMessage> alerts = analyzeData(weatherData);
        
        // Publish data and alerts
        publishData(weatherData, alerts);
    }
    
    private Map<String, Object> collectWeatherData() {
        // In a real system, this would connect to actual weather sensors
        // For this example, we'll generate random weather data
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        
        Map<String, Object> weatherData = new HashMap<>();
        weatherData.put("timestamp", timestamp);
        weatherData.put("station_id", stationId);
        weatherData.put("location", location);
        weatherData.put("temperature", Math.round(random.nextDouble() * 25 + 10) / 10.0);  // 10-35°C
        weatherData.put("humidity", Math.round(random.nextDouble() * 65 + 30) / 10.0);     // 30-95%
        weatherData.put("pressure", Math.round(random.nextDouble() * 100 + 950) / 10.0);  // 950-1050 hPa
        weatherData.put("wind_speed", Math.round(random.nextDouble() * 200) / 10.0);     // 0-200 km/h
        
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        weatherData.put("wind_direction", directions[random.nextInt(directions.length)]);
        
        weatherData.put("rainfall", Math.round(random.nextDouble() * 500) / 10.0);      // 0-50 mm/hour
        
        return weatherData;
    }
    
    private List<AlertMessage> analyzeData(Map<String, Object> weatherData) {
        List<AlertMessage> alerts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = (String) weatherData.get("timestamp");
        
        // Check for flood conditions
        double rainfall = (Double) weatherData.get("rainfall");
        double floodThreshold = eventThresholds.getOrDefault("Flood", 25.0);
        
        if (rainfall >= floodThreshold) {
            int severity = (int) Math.min(5, rainfall / floodThreshold);
            alerts.add(new AlertMessage(
                "com/weather/producer",
                "Flood",
                severity,
                (String) weatherData.get("location"),
                "Heavy rainfall detected. Current rate: " + rainfall + " mm/hour.",
                timestamp
            ));
        }
        
        // Check for hurricane conditions
        double windSpeed = (Double) weatherData.get("wind_speed");
        double hurricaneThreshold = eventThresholds.getOrDefault("Hurricane", 119.0);
        double tornadoThreshold = eventThresholds.getOrDefault("Tornado", 178.0);
        
        if (windSpeed >= hurricaneThreshold) {
            int severity = (int) Math.min(5, windSpeed / (hurricaneThreshold / 2));
            
            // Determine if it's a hurricane or tornado based on wind speed
            if (windSpeed >= tornadoThreshold) {
                alerts.add(new AlertMessage(
                    "com/weather/producer",
                    "Tornado",
                    severity,
                    (String) weatherData.get("location"),
                    "Extreme wind speeds detected. Current speed: " + windSpeed + " km/h.",
                    timestamp
                ));
            } else {
                alerts.add(new AlertMessage(
                    "com/weather/producer",
                    "Hurricane",
                    severity,
                    (String) weatherData.get("location"),
                    "Hurricane-force winds detected. Current speed: " + windSpeed + " km/h.",
                    timestamp
                ));
            }
        }
        
        return alerts;
    }
    
    private void publishData(Map<String, Object> weatherData, List<AlertMessage> alerts) {
        // First, publish regular weather data
        System.out.println("\n[" + weatherData.get("timestamp") + "] Weather data collected from " + stationId + ":");
        System.out.println("Temperature: " + weatherData.get("temperature") + "°C");
        System.out.println("Humidity: " + weatherData.get("humidity") + "%");
        System.out.println("Wind: " + weatherData.get("wind_speed") + " km/h, " + weatherData.get("wind_direction"));
        System.out.println("Rainfall: " + weatherData.get("rainfall") + " mm/hour");
        
        // Then, publish any alerts using both direct service and EventAdmin
        for (AlertMessage alert : alerts) {
            System.out.println("\n[ALERT] " + alert.getEventType() + " - Severity: " + alert.getSeverity() + "/5");
            System.out.println("Message: " + alert.getMessage());
            
            // Publish using EventAdmin service if available
            ServiceReference<EventAdmin> ref = context.getServiceReference(EventAdmin.class);
            if (ref != null) {
                EventAdmin eventAdmin = context.getService(ref);
                if (eventAdmin != null) {
                    Dictionary<String, Object> props = new Hashtable<>();
                    props.put("producer", alert.getProducer());
                    props.put("event.type", alert.getEventType());
                    props.put("severity", alert.getSeverity());
                    props.put("location", alert.getLocation());
                    props.put("message", alert.getMessage());
                    props.put("timestamp", alert.getTimestamp());
                    
                    Event event = new Event(alert.getProducer(), props);
                    eventAdmin.postEvent(event);
                    
                    context.ungetService(ref);
                }
            }
        }
        
        if (alerts.isEmpty()) {
            System.out.println("No alerts triggered - normal weather conditions");
        }
    }
    
    @Override
    public AlertMessage[] getLatestAlerts() {
        // This method would be called by services directly accessing this producer
        // For demonstration, we'll just return an empty array
        return new AlertMessage[0];
    }
