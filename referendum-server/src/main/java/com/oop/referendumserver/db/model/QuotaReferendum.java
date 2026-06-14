package com.oop.referendumserver.db.model;

import jakarta.persistence.Entity;

import java.util.List;

/**
 * Represents a quota referendum in the referendum system
 */
@Entity
public class QuotaReferendum extends SimpleReferendum {
    protected Long minimalParticipation;

    /**
     * Function to get the minimal participation required for the referendum to be successfull
     * @return The minimal participation required
     */
    public Long getMinimalParticipation() {
        return minimalParticipation;
    }

    /**
     * Function to set the minimal participation required for the referendum
     * @param totalExpectedParticipation The minimal participation required
     */
    public void setMinimalParticipation(Long totalExpectedParticipation) {
        this.minimalParticipation = totalExpectedParticipation;
    }
}


