package com.oop.referendumserver.db.model;

import jakarta.persistence.*;

import java.util.Map;

/**
 * Represents a referendum form for collecting voter responses to questions in a referendum
 */
@Entity
public class ReferendumForm {
    @Id
    @GeneratedValue
    private Long id;


    // aggregation
    @ManyToOne(fetch = FetchType.EAGER)
    private Voter voter;

    // aggregation
    @ManyToOne(fetch = FetchType.EAGER)
    private Referendum referendum;


    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Boolean> questionAnswers;


    /**
     * Function to get the question answers provided in the referendum form
     * @return The question answers provided in the referendum form
     */
    public Map<String, Boolean> getQuestionAnswers() {
        return questionAnswers;
    }

    /**
     * Function to set the question answers for the referendum form
     * @param questionAnswers The question answers to set for the referendum form
     */
    public void setQuestionAnswers(Map<String, Boolean> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    /**
     * Function to get the id of the referendum form
     * @return The id of the referendum form
     */
    public Long getId() {
        return id;
    }


    /**
     * Function to set the ID of the referendum form
     * @param id The ID to set for the referendum form
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Function to get the voter associated with the referendum form
     * @return The voter associated with the referendum form
     */
    public Voter getVoter() {
        return voter;
    }

    /**
     * Function to set the voter associated with the referendum form
     * @param voter The voter to set for the referendum form
     */
    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    /**
     * Function to get the referendum associated with the referendum form
     * @return The referendum associated with the referendum form
     */
    public Referendum getReferendum() {
        return referendum;
    }

    /**
     * Function to set the referendum associated with the referendum form
     * @param referendum The referendum to set for the referendum form
     */
    public void setReferendum(Referendum referendum) {
        this.referendum = referendum;
    }

    /**
     * This Function checks if all answers in the referendum form are "Yes"
     * @return True if all answers are "Yes", otherwise false
     */
    public boolean areAllAnswersYes() {
        Map<String, Boolean> questionAnswers = this.questionAnswers;
        for (Boolean answer : questionAnswers.values()) {
            if (!answer) {
                return false;
            }
        }
        return true;
    }
}
