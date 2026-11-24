
package com.recoia.auth.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
}
