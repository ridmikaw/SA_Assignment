package com.weatherguard.producer.weatherstation;

import java.util.Random;

public class WeatherProducerImpl implements WeatherProducer {

    private Random random = new Random();

    @Override
    public String getWeatherData() {
        // Simulate weather data
        double temperature = 20 + random.nextDouble() * 10; // Temperature in °C
        double rainfall = random.nextDouble() * 5; // Rainfall in mm
        double humidity = 40 + random.nextDouble() * 30; // Humidity in %

        return String.format("Temperature: %.2f°C, Rainfall: %.2f mm, Humidity: %.2f%%",
                temperature, rainfall, humidity);
    }
}