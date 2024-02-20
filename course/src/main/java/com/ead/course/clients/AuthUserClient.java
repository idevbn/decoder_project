package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
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

@Log4j2
@Component
public class AuthUserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;

    @Autowired
    public AuthUserClient(final RestTemplate restTemplate,
                          final UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

    public Page<UserDTO> getAllUsersByCourse(final UUID courseId, final Pageable pageable) {
        final List<UserDTO> searchResult;

        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;

        final String url = this.REQUEST_URL_AUTHUSER +
                this.utilsService.createUrlGetAllUsersByCourse(courseId, pageable);

        log.debug("Request URL: {} ", url);

        log.info("Request URL: {} ", url);

        try {
            final ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType
                    = new ParameterizedTypeReference<>() {
            };

            result = this.restTemplate
                    .exchange(url, HttpMethod.GET, null, responseType);

            searchResult = result.getBody().getContent();

            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (
                final HttpStatusCodeException e) {
            log.error("Error request /users {} ", e);
        }
        log.info("Ending request /users courseId {} ", courseId);
        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById(final UUID userId) {
        final String url = this.REQUEST_URL_AUTHUSER + "/users/" + userId;

        return this.restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                UserDTO.class
        );
    }

    public void postSubscriptionUserInCourse(final UUID courseId, final UUID userId) {
        final String url = this.REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";

        final CourseUserDTO courseUserDTO = new CourseUserDTO();
        courseUserDTO.setCourseId(courseId);
        courseUserDTO.setUserId(userId);

        this.restTemplate.postForObject(url, courseUserDTO, String.class);
    }

}
