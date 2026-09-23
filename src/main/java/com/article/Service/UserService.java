package com.article.Service;

import com.article.DTO.BlogResponseDTO;
import com.article.DTO.LoginRequest;
import com.article.DTO.RegisterRequest;
import com.article.DTO.UserResponseDTO;
import com.article.Model.Blog;
import com.article.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String registerUser(RegisterRequest registerRequest);

    Void updateUserUsername(String username);

    Void addBlog(Blog blog);

    Void deleteUser();

    List<Blog> getMyBlogs();

    List<Blog> getBlogsByUsername(String username);

    Blog addUserToBlog(Blog blog);

    String loginUser(LoginRequest loginUserRequest);

    boolean canDelete(Blog blog);

    UserResponseDTO getCurrentUserDetails();

    Void updateUserDescription(String description);

    UserResponseDTO getUserByUsername(String username);
}
