package com.article.Repository;

import com.article.Model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);

//    User findByEmail(String email);

    boolean existsByEmail(@Email String email);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
