package com.oop.referendumserver.db.model;

import jakarta.persistence.Entity;

import java.util.List;

/**
 * Represents a conditional referendum in the referendum system
 */
@Entity
public class ConditionalReferendum extends QuotaReferendum {
    private Float localVoteQuota;

    /**
     * Function to get quota for local voters
     * @return quota for local voters
     */
    public Float getLocalVoteQuota() {
        return localVoteQuota;
    }

    /**
     * Function to set quota for local voters
     * @param localVoteQuota the quota to set for local voters
     */
    public void setLocalVoteQuota(Float localVoteQuota) {
        this.localVoteQuota = localVoteQuota;
    }

}
