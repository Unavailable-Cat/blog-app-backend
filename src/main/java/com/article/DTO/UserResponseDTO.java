package com.article.DTO;

import com.article.Model.Blog;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class UserResponseDTO {
    String email;
    String username;
    List<Blog> blogs;
    Date createdAt;
}
