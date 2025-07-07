package com.impetus.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private static ConfigurationManager instance = null;
    private Properties properties;
    private static final String CONFIG_FILE = "/Users/chinta.anusha/Desktop/UVPAutomation/src/main/resources/config.properties";

    private ConfigurationManager() {
        properties = new Properties();
        loadConfig();
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    private void loadConfig() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public void clearProperties() {
        properties.clear();
    }

    public void reloadConfiguration() {
        loadConfig();
    }
}
