package com.ead.authuser.services.impl;

import com.ead.authuser.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    public static final String REQUEST_URI = "http://localhost:8082";

    @Override
    public String createUrl(final UUID userId, final Pageable pageable) {
        final String url = REQUEST_URI
                + "/courses?userId=" + userId
                + "&page=" + pageable.getPageNumber()
                + "&size=" + pageable.getPageSize()
                + "&sort=" + pageable.getSort().toString().replaceAll(":", ",");

        return url;
    }

}
