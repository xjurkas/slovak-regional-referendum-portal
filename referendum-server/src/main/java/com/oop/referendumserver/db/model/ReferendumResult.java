package com.oop.referendumserver.db.model;

import jakarta.persistence.*;

/**
 * Represents the result of a referendum
 */
@Entity
public class ReferendumResult {
    /**
     * Function to get the id of the referendum result
     * @return The id of the referendum result
     */
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    private Referendum referendum;

    private Result result;

    private Integer numberOfParticipants;

    private Integer numberOfYesVotes;

    private Integer numberOfNoVotes;

    /**
     * Function to get the referendum associated with the referendum result
     * @return The referendum associated with the referendum result
     */
    public Referendum getReferendum() {
        return referendum;
    }

    /**
     * Function to set the referendum associated with the referendum result
     * @param referendum The referendum to set for the referendum result
     */
    public void setReferendum(Referendum referendum) {
        this.referendum = referendum;
    }

    /**
     * Function to get the number of participants in the referendum result
     * @return The number of participants in the referendum result
     */
    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * Function to set the number of participants in the referendum result
     * @param numberOfParticipants The number of participants to set for the referendum result
     */
    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    /**
     * Function to get the result of the referendum
     * @return The result of the referendum
     */
    public Result getResult() {
        return result;
    }

    /**
     * Function to set the result of the referendum
     * @param result The result to set for the referendum
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Function to get the number of "Yes" votes in the referendum result
     * @return The number of "Yes" votes in the referendum result
     */
    public Integer getNumberOfYesVotes() {
        return numberOfYesVotes;
    }

    /**
     * Function to set the number of "Yes" votes in the referendum result
     * @param numberOfYesVotes The number of "Yes" votes to set for the referendum result
     */
    public void setNumberOfYesVotes(Integer numberOfYesVotes) {
        this.numberOfYesVotes = numberOfYesVotes;
    }

    /**
     * Function to get the number of "No" votes in the referendum result
     * @return The number of "No" votes in the referendum result
     */
    public Integer getNumberOfNoVotes() {
        return numberOfNoVotes;
    }

    /**
     * Function to set the number of "No" votes in the referendum result
     * @param numberOfNoVotes The number of "No" votes to set for the referendum result
     */
    public void setNumberOfNoVotes(Integer numberOfNoVotes) {
        this.numberOfNoVotes = numberOfNoVotes;
    }

    /**
     * Enum representing the possible results of a referendum
     */
    public enum Result {
        SUCCESSFUL,
        FAILED,
        INVALID
    }
}
