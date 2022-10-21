package com.ead.authuser.controllers.dtos;

import com.ead.authuser.controllers.AuthController;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

/**
 * DTO passado na requisição, durante a criação de um usuário
 * em {@link AuthController}.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private String oldPassword;
    private String fullName;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
}
