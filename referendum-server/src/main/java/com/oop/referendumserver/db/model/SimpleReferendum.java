package com.oop.referendumserver.db.model;

import jakarta.persistence.Entity;

import java.util.List;

/**
 * Represents a simple referendum in the referendum system.
 */
@Entity
public class SimpleReferendum extends Referendum {
    protected Float passThreshold;

    /**
     * Function to get the pass threshold for the simple referendum
     * @return The pass threshold for the simple referendum
     */
    public Float getPassThreshold() {
        return passThreshold;
    }

    /**
     * Function to set the pass threshold for the simple referendum
     * @param passThreshold The pass threshold to set for the simple referendum
     */
    public void setPassThreshold(Float passThreshold) {
        this.passThreshold = passThreshold;
    }
}
