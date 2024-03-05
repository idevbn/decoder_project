package com.ead.course.validation;

import com.ead.course.dtos.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    private final Validator validator;

    @Autowired
    public CourseValidator(final Validator validator) {
        this.validator = validator;
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
//         ResponseEntity<UserDTO> responseUserInstructor;

//         try {
//             responseUserInstructor = this.authUserClient.getOneUserById(userInstructor);
//
//             if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
//                 errors.rejectValue(
//                         "userInstructor",
//                         "UserInstructorError",
//                         "User must be INSTRUCTOR or ADMIN."
//                 );
//             }
//
//         } catch (final HttpStatusCodeException e) {
//             if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
//                 errors.rejectValue(
//                         "userInstructor",
//                         "UserInstructorError",
//                         "Instructor not found."
//                 );
//             }
//         }
     }

}
