package com.article.Controller;

import com.article.DTO.ErrorResponse;
import com.article.DTO.LoginRequest;
import com.article.DTO.RegisterRequest;
import com.article.DTO.UserResponseDTO;
import com.article.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Get current user details")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User details retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/user/my")
    public ResponseEntity<UserResponseDTO> getCurrentUserDetails() {
        return ResponseEntity.ok(userService.getCurrentUserDetails());
    }

    @Operation(summary = "Get user by username")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/user/username")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @Operation(summary = "Register a user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Password is required",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("register")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterRequest registerUserRequest) {

        String token = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok(token);
    }


    @Operation(summary = "Login user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful and JWT token returned"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("login")
    public ResponseEntity<String> loginUser(
            @RequestBody LoginRequest loginUserRequest) {

        String token = userService.loginUser(loginUserRequest);
        return ResponseEntity.ok(token);
    }


    @Operation(summary = "Update current user's username")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Username updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/user/username")
    public ResponseEntity<Void> updateUserUsername(
            @RequestBody String username) {

        userService.updateUserUsername(username);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Update current user's description")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Description updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/user/description")
    public ResponseEntity<Void> updateUserDescription(
            @RequestBody String description) {

        userService.updateUserDescription(description);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete current user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok().build();
    }
}