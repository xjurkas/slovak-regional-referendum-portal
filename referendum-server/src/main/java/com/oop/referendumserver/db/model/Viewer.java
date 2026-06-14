package com.oop.referendumserver.db.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a viewer in the AppUser system.
 */
@Entity
public class Viewer extends AppUser {

    // aggregation
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Referendum> likedReferendums = new HashSet<>();

    /**
     * Function to get the referendums that the viewer has liked
     * @return The set of referendums that the viewer has liked
     */
    public Set<Referendum> getLikedReferendums() {
        return likedReferendums;
    }

    /**
     * Function to set the referendums that the viewer has liked
     * @param likedReferendums The set of referendums to set as liked by the viewer
     */
    public void setLikedReferendums(Set<Referendum> likedReferendums) {
        this.likedReferendums = likedReferendums;
    }

    /**
     * This function checks if the viewer has already liked the given referendum
     * @param referendum The referendum to check
     * @return True if the viewer has already liked the referendum, otherwise false
     */
    public boolean hasAlreadyLiked(Referendum referendum) {
        // have to check it like this because contains is not working on PersistentSet created via JPA
        for (Referendum ref: getLikedReferendums()) {
            if (ref.getId().equals(referendum.id)) {
                return true;
            }
        }
        return false;
    }

    public void removeLikedReferendum(Referendum referendum) {
        // remove referendum from the set
        this.likedReferendums.remove(referendum);

        // need to create new instance of set otherwise JPA didn't delete entries from link table
        this.likedReferendums = new HashSet<>(this.likedReferendums);

    }
}
