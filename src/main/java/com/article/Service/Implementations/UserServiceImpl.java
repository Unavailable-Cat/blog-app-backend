package com.article.Service.Implementations;

import com.article.DTO.LoginRequest;
import com.article.DTO.RegisterRequest;
import com.article.DTO.UserResponseDTO;
import com.article.Exceptions.NullPasswordException;
import com.article.Exceptions.UserAlreadyExistsException;
import com.article.Exceptions.UserNotFoundException;
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
    public String registerUser(RegisterRequest registerRequest) {
        if(registerRequest.getPassword()==null){
            throw new NullPasswordException("Password is Required");
        }
        User user;
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            user=userRepository.findByEmail(registerRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            if(user.getPassword()==null){
                user.setPassword(registerRequest.getPassword());
                user.setAuthorizer(Authorizer.INAPP);
                userRepository.save(user);
            }
            else{
                throw new UserAlreadyExistsException("User with username: "+registerRequest.getUsername()+" already exists");
            }
        }
        else{
            user= RegisterDTOToUserMapper.registerRequestToUser(registerRequest);
            userRepository.save(user);
        }
        return loginUser(new LoginRequest(user.getEmail(),registerRequest.getPassword()));
    }

    @Override
    public String loginUser(LoginRequest loginUserRequest) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(), loginUserRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return token;
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
    public UserResponseDTO getCurrentUserDetails() {
        Authentication authentication=getauthentication();
        if(authentication!=null && authentication.isAuthenticated() && !(authentication.getPrincipal().equals("anonymousUser"))){
            return UserToUserResponseDTO.mapUserToUserResponse(userRepository.findByEmail(authentication.getName()).get());
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public Void updateUserDescription(String description) {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setDescription(description);
        userRepository.save(user);
        return null;
    }

    @Override
    public Void updateUserUsername(String username) {
        if(userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with Username: "+username+" already exists");
        }
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(username);
        userRepository.save(user);
        return null;
    }

    @Override
    public Void addBlog(Blog blog) {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getBlogs().add(blog);
        userRepository.save(user);
        return null;
    }

    @Override
    public Void deleteUser() {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
        return null;
    }

    @Override
    public List<Blog> getMyBlogs() {
        Authentication authentication=getauthentication();
        User user=userRepository.findByEmail(authentication.getName())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        return user.getBlogs();
    }

    @Override
    public List<Blog> getBlogsByUsername(String username) {
        User user=userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username "+ username +" not found"));
        return user.getBlogs();
    }

    @Override
    public Blog addUserToBlog(Blog blog) {
        Authentication authentication = getauthentication();
        blog.setUser(userRepository.findByEmail(authentication.getName()).get());
        return blog;
    }



}
