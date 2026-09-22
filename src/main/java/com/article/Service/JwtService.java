package com.article.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService{
    String generateToken(Authentication authentication);

    String extractUsername(String token);

    boolean isValid(String token, UserDetails userDetails);
}
