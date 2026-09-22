package com.article.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
public class DetailedBlogResponseDTO {
    String id;
    String title;
    String imageUrl;
    String content;
    String author;
    //Username
    Date createdAt;
    boolean canDelete;
}
