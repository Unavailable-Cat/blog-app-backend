package com.article.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Email
    @Id
    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Roles role=Roles.USER;

    @OneToMany(mappedBy = "user")
    private List<Blog> blogs;

    @CreationTimestamp
    private Date createdAt;

    private Authorizer authorizer;
}
