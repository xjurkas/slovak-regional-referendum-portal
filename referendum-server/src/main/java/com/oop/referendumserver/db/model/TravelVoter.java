package com.oop.referendumserver.db.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a travel voter in the AppUser system.
 */
@Entity
public class TravelVoter extends Voter {

    // aggregation
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Region> regions = new HashSet<>();

    /**
     * Function to get the regions associated with the travel voter
     * @return The set of regions associated with the travel voter
     */
    public Set<Region> getRegions() {
        return regions;
    }

    /**
     * Function to set the regions associated with the travel voter
     * @param regions The set of regions to set for the travel voter
     */
    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    // Polymorphism
    /**
     * Function to get the weight of the vote for the travel voter based on the given referendum region
     * @param referendumRegion The region of the referendum
     * @return The weight of the vote
     */
    @Override
    public Float getVoteWeight(Region referendumRegion) {
        if (regions.contains(referendumRegion)) {
            return 0.5F;
        } else {
            return 0.25F;
        }
    }
}
