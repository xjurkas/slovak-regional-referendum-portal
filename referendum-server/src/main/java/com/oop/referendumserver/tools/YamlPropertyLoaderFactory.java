package com.oop.referendumserver.tools;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;


/**
 * Custom property loader factory for loading YAML files as property sources
 * This is used to address the issue where Spring only loads properties files via PropertySource
 * Implementation reference: https://stackoverflow.com/a/51392715
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {
    /**
     * Create a property source based on the specified YAML resource
     * @param name     the name of the property source
     * @param resource the resource containing the YAML data
     * @return the property source created from the YAML resource
     * @throws IOException if an I/O error occurs
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null){
            return super.createPropertySource(name, resource);
        }

        return new YamlPropertySourceLoader().load(resource.getResource().getFilename(),resource.getResource()).getFirst();
    }
}