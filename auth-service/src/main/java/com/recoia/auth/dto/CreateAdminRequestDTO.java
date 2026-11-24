
package com.recoia.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
