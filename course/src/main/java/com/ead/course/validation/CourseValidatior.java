package com.ead.course.validation;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Component
public class CourseValidatior implements Validator {

    private final Validator validator;
    private final AuthUserClient authUserClient;

    @Autowired
    public CourseValidatior(final Validator validator,
                            final AuthUserClient authUserClient) {
        this.validator = validator;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final CourseDTO courseDTO = (CourseDTO) target;
        this.validator.validate(courseDTO, errors);

        if (!errors.hasErrors()) {
            this.validateUserInstructor(courseDTO.getUserInstructor(), errors);
        }
     }

     private void validateUserInstructor(final UUID userInstructor, final Errors errors) {
         ResponseEntity<UserDTO> responseUserInstructor;

         try {
             responseUserInstructor = this.authUserClient.getOneUserById(userInstructor);

             if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
                 errors.rejectValue(
                         "userInstructor",
                         "UserInstructorError",
                         "User must be INSTRUCTOR or ADMIN."
                 );
             }

         } catch (final HttpStatusCodeException e) {
             if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                 errors.rejectValue(
                         "userInstructor",
                         "UserInstructorError",
                         "Instructor not found."
                 );
             }
         }
     }

}
