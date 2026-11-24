package com.recoia.auth.dto;

import com.recoia.auth.enums.Role;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private Boolean enabled;
}