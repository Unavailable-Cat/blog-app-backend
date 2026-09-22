package com.article.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
public class BlogRequestDTO {
    String title;
    MultipartFile image;
    String content;

}
