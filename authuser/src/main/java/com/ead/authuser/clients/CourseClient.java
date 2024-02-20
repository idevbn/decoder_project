package com.ead.authuser.clients;

import com.ead.authuser.controllers.dtos.CourseDTO;
import com.ead.authuser.controllers.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * Classe que faz chamadas à API do microsserviço course
 */
@Log4j2
@Component
public class UserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @Value("$(ead.api.url.course)")
    private String REQUEST_URL_COURSE;

    @Autowired
    public UserClient(final RestTemplate restTemplate, final UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

    public Page<CourseDTO> getAllCoursesByUser(final UUID userId, final Pageable pageable) {
        final List<CourseDTO> searchResult;

        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;

        final String url = this.REQUEST_URL_COURSE + this.utilsService.createUrl(userId, pageable);

        log.debug("Request URL: {} ", url);

        log.info("Request URL: {} ", url);

        try {
            final ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType
                    = new ParameterizedTypeReference<>() {};

            result = this.restTemplate
                    .exchange(url, HttpMethod.GET, null, responseType);

            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (final HttpStatusCodeException e){
            log.error("Error request /courses {} ", e);
        }
        log.info("Ending request /courses userId {} ", userId);
        return result.getBody();
    }

}
