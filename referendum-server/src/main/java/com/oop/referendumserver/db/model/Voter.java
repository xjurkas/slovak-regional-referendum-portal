package com.oop.referendumserver.db.model;

import jakarta.persistence.*;

import java.sql.Ref;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a voter in the AppUser system.
 */
@Entity
public abstract class Voter extends Viewer {

    //aggregation
    @ManyToMany(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    Set<Referendum> votedReferendums = new HashSet<>();


    /**
     * Function to get the referendums that the voter has voted in
     * @return The set of referendums that the voter has voted in
     */
    public Set<Referendum> getVotedReferendums() {
        return votedReferendums;
    }

    /**
     * Function to set the referendums that the voter has voted in
     * @param votedReferendums The set of referendums to set as voted by the voter
     */
    public void setVotedReferendums(Set<Referendum> votedReferendums) {
        this.votedReferendums = votedReferendums;
    }

    // polymorphism
    /**
     * Function to get the weight of the vote for the voter in the specified referendum region
     * @param referendumRegion The region of the referendum
     * @return The weight of the vote
     */
    abstract public Float getVoteWeight(Region referendumRegion);

    /**
     * This function checks if the voter has already voted in the given referendum
     * @param referendum The referendum to check
     * @return True if the voter has already voted in the referendum, otherwise false
     */
    public boolean hasAlreadyVoted(Referendum referendum) {
        // have to check it like this because contains is not working on PersistentSet created via JPA
        for (Referendum ref: getVotedReferendums()) {
            if (ref.getId().equals(referendum.id)) {
                return true;
            }
        }
        return false;
    }

}
