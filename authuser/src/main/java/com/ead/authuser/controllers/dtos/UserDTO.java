package com.ead.authuser.controllers.dtos;

import com.ead.authuser.controllers.AuthController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * DTO passado na requisição, durante a criação de um usuário
 * em {@link AuthController}.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    public interface UserView {
        public static interface RegistrationPost {}
        public static interface UserPut {}
        public static interface PasswordPut {}
        public static interface ImagePut {}
    }

    private UUID id;

    /**
     * O @NotBlank não permite valores NULOS nem valores VAZIOS
     */
    @Size(min = 4, max = 50)
    @JsonView(UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class)
    private String username;

    @Email
    @JsonView(UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class)
    private String email;

    @Size(min = 6, max = 20)
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @Size(min = 6, max = 20)
    @JsonView(UserView.PasswordPut.class)
    @NotBlank(groups = UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    @NotBlank(groups = UserView.ImagePut.class)
    private String imageUrl;

}
