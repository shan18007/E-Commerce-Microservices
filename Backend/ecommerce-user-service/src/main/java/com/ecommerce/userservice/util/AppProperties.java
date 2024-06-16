package com.ecommerce.userservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private static final Logger LOG = LoggerFactory.getLogger(AppProperties.class);

    private static AppProperties instance;
    private Properties properties;
    private String basePath;

    private AppProperties() {
        loadProperties();
    }

    public static synchronized AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                LOG.info("Unable to find application.properties file");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            LOG.error("Error loading application.properties file: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getBasePath() {
        return this.getProperty("shared.path");
    }

}

