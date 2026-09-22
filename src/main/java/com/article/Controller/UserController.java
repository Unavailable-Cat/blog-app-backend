package com.article.Controller;

import com.article.DTO.LoginRequest;
import com.article.DTO.RegisterRequest;
import com.article.DTO.UserResponseDTO;
import com.article.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getCurrentUserDetails() {
        return userService.getCurrentUserDetails();
    }

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerUserRequest) {
        return userService.registerUser(registerUserRequest);
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginUserRequest) {
        return userService.loginUser(loginUserRequest);
    }

    @PatchMapping("/user/username")
    public ResponseEntity<Void> updateUserUsername(@RequestBody String username){
        return userService.updateUserUsername(username);
    }

    @PatchMapping("/user/description")
    public ResponseEntity<Void> updateUserDescription(@RequestBody String description){
        return userService.updateUserDescription(description);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(){
        return userService.deleteUser();
    }
}
