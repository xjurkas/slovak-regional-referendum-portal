package com.oop.referendumserver.db.model;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

/**
 * Represents a notification associated with a specific referendum and user
 */
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Referendum referendum;

    private String text;

    /**
     * Function to retrieves the user to whom the notification is directed
     * @return the user receiving the notification
     */
    public AppUser getUser() {
        return user;
    }

    /**
     * Function to sets the user to whom the notification is directed
     * @param user the user to set as the recipient
     */
    public void setUser(AppUser user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser user;


    /**
     * Function to retrieves the unique identifier of the notification
     * @return the ID of the notification
     */
    public Long getId() {
        return id;
    }

    /**
     * Function to retrieves the referendum associated with the notification
     * @return the associated referendum
     */
    public Referendum getReferendum() {
        return referendum;
    }

    /**
     * Function to sets the referendum associated with the notification
     * @param referendum the referendum to associate
     */
    public void setReferendum(Referendum referendum) {
        this.referendum = referendum;
    }

    /**
     * Function to retrieves the text content of the notification
     * @return the text content of the notification
     */
    public String getText() {
        return text;
    }

    /**
     * Function to sets the text content of the notification
     * @param text the text content to set
     */
    public void setText(String text) {
        this.text = text;
    }

}

