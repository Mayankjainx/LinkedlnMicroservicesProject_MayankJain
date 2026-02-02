package com.Project.LinkedIn.User.Service.DTO;

import lombok.Data;

@Data
public class SignUpRequestDTO {
    private String name;
    private String email;
    private String password;
}
