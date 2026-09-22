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

    ResponseEntity<String> registerUser(RegisterRequest registerRequest);

    ResponseEntity<Void> updateUserUsername(String username);

    ResponseEntity<Void> addBlog(Blog blog);

    ResponseEntity<Void> deleteUser();

    List<Blog> getMyBlogs();

    List<Blog> getBlogsByUsername(String username);

    Blog addUserToBlog(Blog blog);

    ResponseEntity<String> loginUser(LoginRequest loginUserRequest);

    boolean canDelete(Blog blog);

    ResponseEntity<UserResponseDTO> getCurrentUserDetails();

    ResponseEntity<Void> updateUserDescription(String description);
}
