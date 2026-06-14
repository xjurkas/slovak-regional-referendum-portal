package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.AppUser;
import com.oop.referendumserver.db.model.Notification;
import com.oop.referendumserver.db.model.Referendum;
import com.oop.referendumserver.db.repository.NotificationRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing notifications
 */
@Service
public class NotificationService {
    @Autowired
    private final NotificationRepository notificationRepository;

    /**
     * Constructs a new NotificationService with the specified notification repository.
     * @param notificationRepository the repository for managing notifications
     */
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Function to retrieves notifications for the specified user
     * @param appUser the user whose notifications are to be retrieved
     * @return a list of notifications for the user
     */
    public List<Notification> getNotificationsForUser(AppUser appUser){
        List<Notification> notifications = notificationRepository.findByUser(appUser);
        return notifications;
    }
}
