package com.oop.referendumserver.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

/**
 * Represents a local voter in the AppUser system
 */
@Entity
public class LocalVoter extends Voter {
    /**
     * Function to get region
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Function to set region
     * @param region the region to set for local voter
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Region region;

    // polymorphism
    /**
     * Function to get weight of vote for Localvoter, depends on if user is from this region or not
     * @param referendumRegion the region of the referendum
     * @return The weight of the vote. Returns 1.0 if the voter is from the same region as the referendum, otherwise returns 0.25.
     */
    @Override
    public Float getVoteWeight(Region referendumRegion) {
        if(region==referendumRegion){
            return 1F;
        }else{
            return 0.25F;
        }
    }
}
