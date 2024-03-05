package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID userId;

}
