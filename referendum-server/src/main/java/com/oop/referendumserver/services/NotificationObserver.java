package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.db.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.View;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Component class representing an Observer in the Observer pattern,
 * responsible for managing notification subscriptions and updates.
 * This class observes changes in ReferendumResults and notifies subscribed Viewers accordingly.
 */
@Component
public class NotificationObserver {

    @Autowired
    private NotificationRepository notificationRepository;

    private Map<Long, Set<Viewer>> subscribers = new HashMap<>();

    /**
     * Subscribes a viewer to receive notifications for a specific referendum
     * @param viewer     the viewer to subscribe
     * @param referendum the referendum for which to subscribe
     */
    public void subscribe(Viewer viewer, Referendum referendum) {
        if (subscribers.containsKey(referendum.getId())) {
            subscribers.get(referendum.getId()).add(viewer);
        } else {
            subscribers.put(referendum.getId(), new HashSet<>());
            subscribers.get(referendum.getId()).add(viewer);
        }
    }

    /**
     * Unsubscribes a viewer from receiving notifications for a specific referendum
     * @param viewer     the viewer to unsubscribe
     * @param referendum the referendum from which to unsubscribe
     */
    public void unsubscribe(Viewer viewer, Referendum referendum) {
        if (subscribers.containsKey(referendum.getId())) {
            subscribers.get(referendum.getId()).remove(viewer);
        }
    }

    /**
     * Updates the subscribers with the result of a referendum evaluation
     * Notifies each subscribed viewer with a notification containing the referendum result
     * @param result the result of the referendum evaluation
     */
    public void update(ReferendumResult result) {
        Referendum referendum = result.getReferendum();
        for (Viewer viewer: subscribers.getOrDefault(referendum.getId(), HashSet.newHashSet(0))) {
            createNotification(referendum, viewer, result.getResult());
        }
    }

    /**
     * Creates a notification for a viewer based on the referendum result
     * @param referendum the evaluated referendum
     * @param viewer     the viewer to notify
     * @param result     the result of the referendum evaluation
     */
    private void createNotification(Referendum referendum, Viewer viewer, ReferendumResult.Result result) {
        Notification notification = new Notification();
        notification.setReferendum(referendum);
        notification.setUser(viewer);
        notification.setText("Referendum " + referendum.getTitle() + " was evaluated with result: " + result.name());
        notificationRepository.save(notification);
    }
}
