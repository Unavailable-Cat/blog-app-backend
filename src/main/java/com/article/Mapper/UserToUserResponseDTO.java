package com.article.Mapper;

import com.article.DTO.UserResponseDTO;
import com.article.Model.User;

public class UserToUserResponseDTO {

    public static UserResponseDTO mapUserToUserResponse(User user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .blogs(user.getBlogs())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
