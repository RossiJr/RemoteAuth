package com.rossijr.remoteauth.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Properties;

public class LogConfigurator {
    public static void init(Properties properties) {
        // Get the root logger context
        LoggerContext context = LoggerContext.getContext(false);

        // Set all the logs to a specific level
        Configurator.setRootLevel(Level.valueOf(properties.getProperty("log.level", "OFF")));

        // Loading the configurations
        context.updateLoggers();
    }
}
