package com.weatherguard.producer.weatherstation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration<WeatherProducer> registration;

    @Override
    public void start(BundleContext context) throws Exception {
        // Create an instance of the WeatherProducer service
        WeatherProducer weatherProducer = new WeatherProducerImpl();

        // Register the service with the OSGi framework
        registration = context.registerService(WeatherProducer.class, weatherProducer, null);

        System.out.println("WeatherProducer service registered successfully!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // Unregister the service when the bundle stops
        if (registration != null) {
            registration.unregister();
            System.out.println("WeatherProducer service unregistered.");
        }
    }
}
