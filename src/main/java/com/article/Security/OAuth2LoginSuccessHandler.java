package com.article.Security;

import com.article.Model.User;
import com.article.Repository.UserRepository;
import com.article.Service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String jwtToken = jwtService.generateToken(authentication);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");

        // Swap to this once your frontend is ready:
        // response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + jwtToken);
    }
}
