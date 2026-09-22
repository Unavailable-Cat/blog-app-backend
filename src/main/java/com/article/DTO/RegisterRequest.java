package com.article.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email
    private String email;
    private String username;
    private String password;
}
