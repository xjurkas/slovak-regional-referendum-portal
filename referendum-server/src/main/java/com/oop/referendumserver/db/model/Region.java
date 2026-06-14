package com.oop.referendumserver.db.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

import java.util.List;

/**
 * Represents a region users can vote
 */
@Entity
public class Region {
    @Id
    private String id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> description;

    /**
     * Function to get the description of the region
     * @return The description of the region
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Function to set the description of the region
     * @param description The description to set for the region
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * Constructs a region with the specified ID, name, and description
     * @param id          The unique identifier of the region
     * @param name        The name of the region
     * @param description The description of the region
     */
    public Region(String id, String name, List<String> description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor for JPA
     */
    public Region() {

    }

    /**
     * Function to set the unique identifier of the region
     * @param identifier The unique identifier to set for the region
     */
    public void setId(String identifier) {
        this.id = identifier;
    }

    /**
     * Function to get the unique identifier of the region
     * @return The unique identifier of the region
     */
    public String getId() {
        return id;
    }

    /**
     * Function to get the name of the region
     * @return The name of the region
     */
    public String getName() {
        return name;
    }

    /**
     * Function to set the name of the region
     * @param name The name to set for the region
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function to Returns the name of the region
     * @return The name of the region
     */
    @Override
    public String toString() {
        return name;
    }

}