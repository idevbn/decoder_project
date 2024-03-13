package com.ead.notification.services.impl;

import com.ead.notification.repositories.NotificationRepository;
import com.ead.notification.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
