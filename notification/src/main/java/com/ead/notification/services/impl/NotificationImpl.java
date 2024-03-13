package com.ead.notification.services.impl;

import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.repositories.NotificationRepository;
import com.ead.notification.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Sobre a injeção de dependências via construtor:
     *
     * <p>
     *     As of Spring Framework 4.3, an @Autowired annotation on such a constructor
     *     is no longer necessary if the target bean defines only one constructor to begin with.
     * </p>
     *
     * <br/>
     *
     * <a href="https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired.html"></a>
     */
    @Autowired
    public NotificationImpl(final NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationModel saveNotification(final NotificationModel notificationModel) {
        return this.notificationRepository.save(notificationModel);
    }

    @Override
    public Page<NotificationModel> findAllNotificationsByUser(final UUID userId,
                                                              final Pageable pageable) {
        return this.notificationRepository
                .findAllByUserIdAndNotificationStatus(
                userId,
                NotificationStatus.CREATED,
                pageable
        );
    }

    @Override
    public Optional<NotificationModel> findByNotificationIdAndUserId(
            final UUID notificationId,
            final UUID userId
    ) {
        return this.notificationRepository.findByNotificationIdAndUserId(notificationId, userId);
    }

}
