package com.oop.referendumserver.db.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Represents a main branch of referendum hierarchy
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Referendum {

    @Id
    @GeneratedValue
    protected Long id;
    protected String title;
    protected String description;

    // aggregation
    @ManyToOne(fetch = FetchType.EAGER)
    protected Region region;
    protected LocalDate date;

    // composition = strong aggregation
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "referendum_questions", joinColumns = @JoinColumn(name = "referendum_id"))
    protected List<Question> questions;

    /**
     * Function to get the id of the referendum
     * @return The id of the referendum
     */
    public Long getId() {
        return id;
    }

    /**
     * Function to get the title of the referendum
     * @return The title of the referendum
     */
    public String getTitle() {
        return title;
    }

    /**
     * Function to set the title of the referendum
     * @param title The title to set for the referendum
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Function to get the description of the referendum
     * @return The description of the referendum
     */
    public String getDescription() {
        return description;
    }

    /**
     * Function to set the description of the referendum
     * @param description The description to set for the referendum
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Function to get the date of the referendum
     * @return The date of the referendum
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Function to set the date of the referendum
     * @param date The date to set for the referendum
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Function to get the list of questions associated with the referendum
     * @return The list of questions associated with the referendum
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Function to set the list of questions associated with the referendum
     * @param questions The list of questions to set for the referendum
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Returns a string representation of the referendum object
     * @return A string representation of the referendum object
     */
    @Override
    public String toString() {
        return "Referendum{" +
                "title='" + title + '\'' +
                ", regionId='" + region.getId() + '\'' +
                '}';
    }

    /**
     * This Function checks if the referendum has expired based on the current date
     * @return True if the referendum has expired, otherwise false.
     */
    public boolean isExpired() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(this.date);
    }

    /**
     * Function to get the region associated with the referendum
     * @return The region associated with the referendum
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Function to set the region associated with the referendum
     * @param region The region to set for the referendum
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Referendum that = (Referendum) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
