package com.article.Repository;

import com.article.Model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    public User findByEmail(String email);

    boolean existsByEmail(@Email String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
