package com.oop.referendumserver.db.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for storing data related to regions
 */
@ConfigurationProperties("data")
public class DataConfiguration {
    private List<RegionConfiguration> regions;

    /**
     * Gets the list of region configurations.
     * @return The list of region configurations.
     */
    public List<RegionConfiguration> getRegions() {
        return regions;
    }

    /**
     * Sets the list of region configurations.
     * @param regions The list of region configurations.
     */
    public void setRegions(List<RegionConfiguration> regions) {
        this.regions = regions;
    }

    /**
     * Configuration properties for each region.
     */
    public static class RegionConfiguration {
        private String id;
        private String name;
        private List<String> description;

        /**
         * Gets the ID of the region.
         * @return The ID of the region.
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the ID of the region.
         * @param id The ID of the region.
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets the name of the region.
         * @return The name of the region.
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the region.
         * @param name The name of the region.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the description of the region.
         * @return The description of the region.
         */
        public List<String> getDescription() {
            return description;
        }

        /**
         * Sets the description of the region.
         * @param description The description of the region.
         */
        public void setDescription(List<String> description) {
            this.description = description;
        }
    }
}
