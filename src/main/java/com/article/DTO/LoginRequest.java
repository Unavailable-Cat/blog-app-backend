package com.article.DTO;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
    @Email
    String email;
    String password;

    public LoginRequest(String email, String password){
        this.email=email;
        this.password=password;
    }
}
