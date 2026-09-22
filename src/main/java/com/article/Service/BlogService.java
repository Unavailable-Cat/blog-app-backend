package com.article.Service;

import com.article.DTO.BlogRequestDTO;
import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.Model.Blog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {
    List<BlogResponseDTO> getAllBlogs();

    DetailedBlogResponseDTO getBlogById(String blogId);
    
    Void createBlog(BlogRequestDTO blogRequestDTO);

    Void updateBlog(String id, BlogRequestDTO blogRequestDTO);

    Void updateTitle(String id, String title);

    Void updateImage(String id, MultipartFile image);

    Void updateContent(String id, String content);

    Void deleteBlog(String blogId);

    List<BlogResponseDTO> getMyBlog();

    List<BlogResponseDTO> getBlogByUsername(String username);
}
