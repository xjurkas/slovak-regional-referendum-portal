package com.oop.referendumserver.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import java.util.Objects;

/**
 * Represents a main branch of AppUser hierarchy
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AppUser {

    @Id
    private String id;

    private String password;

    /**
     * Function to get id
     * @return the id of the application user
     */
    public String getId() {
        return id;
    }

    /**
     * Function to set id
     * @param username id to set for application user
     */
    public void setId(String username) {
        this.id = username;
    }

    /**
     * Function to get password
     * @return the password of the application user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Function to set Password
     * @param password the password to set for application user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
