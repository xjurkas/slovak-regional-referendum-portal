package com.oop.referendumserver.db.repository;

import com.oop.referendumserver.db.model.AppUser;
import com.oop.referendumserver.db.model.Notification;
import jdk.jfr.Event;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for managing notifications in the database
 * Extends ListCrudRepository to provide additional list-related CRUD operations
 */
@Repository
public interface NotificationRepository extends ListCrudRepository<Notification,Long> {

    /**
     * Retrieves a list of notifications associated with the specified user
     * @param user the user whose notifications are to be retrieved
     * @return a list of notifications associated with the user
     */
    List<Notification> findByUser(AppUser user);
}
