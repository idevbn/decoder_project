package com.ead.notification.repositories;

import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {

    Page<NotificationModel> findAllByUserIdAndNotificationStatus(
            final UUID userId, final NotificationStatus notificationStatus,
            final Pageable pageable
    );

    Optional<NotificationModel> findByNotificationIdAndUserId(final UUID notificationId,
                                                              final UUID userId);

}
