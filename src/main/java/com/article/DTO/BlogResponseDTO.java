package com.article.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@AllArgsConstructor
@Data
@Builder
public class BlogResponseDTO {
    String id;
    String title;
    String imageUrl;
    String author;
    //Username
    Date createdAt;
}
