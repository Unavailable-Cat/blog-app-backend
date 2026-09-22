package com.article.Service.Implementations;

import com.article.DTO.LoginRequest;
import com.article.DTO.RegisterRequest;
import com.article.DTO.UserResponseDTO;
import com.article.Mapper.RegisterDTOToUserMapper;
import com.article.Mapper.UserToUserResponseDTO;
import com.article.Model.Authorizer;
import com.article.Model.Blog;
import com.article.Model.User;
import com.article.Repository.UserRepository;
import com.article.Service.JwtService;
import com.article.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtService jwtService;

    private Authentication getauthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @Override
    public ResponseEntity<String> registerUser(RegisterRequest registerRequest) {
        if(registerRequest.getPassword()==null){
            return new ResponseEntity("Password is required", HttpStatusCode.valueOf(400));
        }
        User user;
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            user=userRepository.findByEmail(registerRequest.getEmail());
            if(user.getPassword()==null){
                user.setPassword(registerRequest.getPassword());
                user.setAuthorizer(Authorizer.INAPP);
                userRepository.save(user);
            }
            else{
                return new ResponseEntity("User already exists", HttpStatusCode.valueOf(409));
            }
        }
        else{
            user= RegisterDTOToUserMapper.registerRequestToUser(registerRequest);
            userRepository.save(user);
        }
        return loginUser(new LoginRequest(user.getEmail(),registerRequest.getPassword()));
        //return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> loginUser(LoginRequest loginUserRequest) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(), loginUserRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

    @Override
    public boolean canDelete(Blog blog) {
        Authentication authentication=getauthentication();
        if(authentication!=null && authentication.isAuthenticated() && !(authentication.getPrincipal().equals("anonymousUser"))){
            //System.out.println(authentication.getAuthorities().toString());
            if(authentication.getAuthorities().iterator().next().getAuthority().toString().equals("ADMIN")){
                return true;
            }
            if(authentication.getName().equals(blog.getUser().getEmail())){
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<UserResponseDTO> getCurrentUserDetails() {
        Authentication authentication=getauthentication();
        if(authentication!=null && authentication.isAuthenticated() && !(authentication.getPrincipal().equals("anonymousUser"))){
            return ResponseEntity.ok(UserToUserResponseDTO.mapUserToUserResponse(userRepository.findByEmail(authentication.getName())));
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<Void> updateUserDescription(String description) {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName());
        user.setDescription(description);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateUserUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            return new ResponseEntity("Username already exists", HttpStatusCode.valueOf(409));
        }
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName());
        user.setUsername(username);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addBlog(Blog blog) {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName());
        user.getBlogs().add(blog);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser() {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName());
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<Blog> getMyBlogs() {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName());
        return user.getBlogs();
    }

    @Override
    public List<Blog> getBlogsByUsername(String username) {
        User user=userRepository.findByUsername(username);
        if(user==null){
            return null;
        }
        return user.getBlogs();
    }

    @Override
    public Blog addUserToBlog(Blog blog) {
        Authentication authentication = getauthentication();
        blog.setUser(userRepository.findByEmail(authentication.getName()));
        return blog;
    }



}
