package com.oop.referendumserver.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading resources such as properties files.
 */
@Component
public class ResourceReader {


    @Autowired
    private ResourceLoader resourceLoader;


    /**
     * Reads properties from the specified resource file
     * @param resourceName the name of the resource file to read
     * @return a Properties object containing the properties read from the resource file
     */
    public Properties readProperties(String resourceName) {
        final Properties properties = new Properties();
        try (InputStream resourceStream = resourceLoader.getClassLoader().getResourceAsStream(resourceName)) {
            properties.load(resourceStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return properties;
    }
}
