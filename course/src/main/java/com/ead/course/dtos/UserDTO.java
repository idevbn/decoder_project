package com.ead.course.dtos;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    final UUID userId;
    final String username;
    final String email;
    final String fullName;
    final UserStatus userStatus;
    final UserType userType;
    final String phoneNumber;
    final String cpf;
    final String imageUrl;

}
