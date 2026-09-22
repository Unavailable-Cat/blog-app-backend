package com.article.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.generator.values.GeneratedValues;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String blogId;
    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;
    @NotBlank
    private String title;
    @NotBlank
    private String imageUrl;
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;
    @CreationTimestamp
    private Date createdAt;
}
