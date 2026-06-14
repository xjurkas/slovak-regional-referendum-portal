package com.oop.referendumserver.db.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

/**
 * Represents a question in the referendum system
 */
@Embeddable
public class Question {
    private String id;
    private String text;

    /**
     * Constructs a question with the specified ID and text
     *
     * @param id   The ID of the question
     * @param text The text of the question
     */
    public Question(String id, String text) {
        this.id = id;
        this.text = text;
    }

    /**
     * Default constructor required by JPA
     */
    public Question() {

    }

    /**
     * Function to get the ID of the question
     * @return The ID of the question
     */
    public String getId() {
        return id;
    }

    /**
     * Function to set the id of the question
     * @param id The id to set for the question
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Function to get the text of the question
     * @return The text of the question
     */
    public String getText() {
        return text;
    }

    /**
     * Functio to set the text of the question
     * @param text The text to set for the question
     */
    public void setText(String text) {
        this.text = text;
    }
}
