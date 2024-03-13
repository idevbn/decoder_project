package com.ead.notification.services;

import com.ead.notification.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface NotificationService {

    NotificationModel saveNotification(final NotificationModel notificationModel);

    Page<NotificationModel> findAllNotificationsByUser(final UUID userId, final Pageable pageable);

    Optional<NotificationModel> findByNotificationIdAndUserId(
            final UUID notificationId,
            final UUID userId
    );

}
