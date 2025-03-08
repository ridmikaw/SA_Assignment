package com.weatherguard.consumer.emergency;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private WeatherConsumer consumer;

    @Override
    public void start(BundleContext context) throws Exception {
        consumer = new WeatherConsumer();
        consumer.start(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if (consumer != null) {
            consumer.stop(context);
        }
    }
}