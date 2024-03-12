package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe que faz chamadas à API do microsserviço course
 */
@Log4j2
@Component
public class CourseClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @Value("${ead.api.url.course}")
    private String REQUEST_URL_COURSE;

    @Autowired
    public CourseClient(final RestTemplate restTemplate, final UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

//    @Retry(name = "retryInstance", fallbackMethod = "retryfallback")
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

    public Page<CourseDTO> retryfallback(final UUID userId, final Pageable pageable, final Throwable t) {
        log.error("Inside retry retryfallback, cause - {}", t.toString());
        final List<CourseDTO> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

}
