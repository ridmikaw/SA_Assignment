package com.weatherguard.consumer.emergency;

import com.weatherguard.producer.weatherstation.WeatherProducer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class WeatherConsumer implements BundleActivator {

    private ServiceReference<WeatherProducer> serviceReference;

    @Override
    public void start(BundleContext context) throws Exception {
        // Get reference to the WeatherProducer service
        serviceReference = context.getServiceReference(WeatherProducer.class);

        if (serviceReference != null) {
            WeatherProducer weatherProducer = context.getService(serviceReference);
            if (weatherProducer != null) {
                // Get weather data
                String weatherData = weatherProducer.getWeatherData();
                System.out.println("Weather Data Received: " + weatherData);

                // Analyze weather data for emergencies
                analyzeWeather(weatherData);
            }
        } else {
            System.out.println("WeatherProducer service not found.");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (serviceReference != null) {
            context.ungetService(serviceReference);
        }
        System.out.println("WeatherConsumer stopped.");
    }

    private void analyzeWeather(String weatherData) {
        // Extract temperature and rainfall from the data
        if (weatherData.contains("Temperature:")) {
            String[] parts = weatherData.split(",");
            double temperature = Double.parseDouble(parts[0].split(": ")[1].replace("°C", "").trim());
            double rainfall = Double.parseDouble(parts[1].split(": ")[1].replace("mm", "").trim());

            // Check for emergency conditions
            if (temperature > 35) {
                System.out.println("[ALERT] Extreme Heat! Stay hydrated and avoid outdoor activities.");
            }
            if (rainfall > 4.0) {
                System.out.println("[ALERT] Heavy Rainfall! Risk of flooding, take precautions.");
            }
        }
    }
}