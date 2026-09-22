package com.article.Security;

import com.article.Model.Authorizer;
import com.article.Model.Roles;
import com.article.Model.User;
import com.article.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.AuthProvider;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(request);

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not available from Google");
        }

        User user = userRepository.findByEmail(email).get();

        if(user == null) {
            user = createNewOAuthUser(email, name);
        }

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getRole().toString())),
                attributes,
                "email"
        );
    }

    private User createNewOAuthUser(String email, String name) {
        User newUser = User.builder()
                .email(email)
                .username(generateUniqueUsername(name, email))
                .password(null)
                .authorizer(Authorizer.GOOGLE)
                .role(Roles.USER)
                .build();
        return userRepository.save(newUser);
    }

    private String generateUniqueUsername(String name, String email) {
        String base = (name != null ? name : email.split("@")[0])
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "");
        if (base.isBlank()) base = "user";

        String candidate = base;
        int suffix = 0;
        while (userRepository.existsByUsername(candidate)) {
            suffix++;
            candidate = base + suffix;
        }
        return candidate;
    }
}