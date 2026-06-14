package com.oop.referendumserver.db.model;

import com.oop.referendumserver.Application;
import jakarta.persistence.Entity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

/**
 * Represents an administrator in the AppUser system.
 */
@Entity
public class Admin extends AppUser {

}
