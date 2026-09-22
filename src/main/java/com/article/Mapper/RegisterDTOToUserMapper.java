package com.article.Mapper;

import com.article.DTO.RegisterRequest;
import com.article.Model.Authorizer;
import com.article.Model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;


public class RegisterDTOToUserMapper {
    public static User registerRequestToUser(RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(new BCryptPasswordEncoder(12).encode(registerRequest.getPassword()))
                .role(com.article.Model.Roles.USER)
                .blogs(new ArrayList<>())
                .description(null)
                .authorizer(Authorizer.INAPP)
                .build();
    }
}
