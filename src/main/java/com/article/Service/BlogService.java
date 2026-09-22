package com.article.Service;

import com.article.DTO.BlogRequestDTO;
import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.Model.Blog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {
    ResponseEntity<List<BlogResponseDTO>> getAllBlogs();

    ResponseEntity<DetailedBlogResponseDTO> getBlogById(String blogId);
    
    ResponseEntity<Void> createBlog(BlogRequestDTO blogRequestDTO);

    ResponseEntity<Void> updateBlog(String id, BlogRequestDTO blogRequestDTO);

    ResponseEntity<Void> updateTitle(String id, String title);

    ResponseEntity<Void> updateImage(String id, MultipartFile image);

    ResponseEntity<Void> updateContent(String id, String content);

    ResponseEntity<Void> deleteBlog(String blogId);

    ResponseEntity<List<BlogResponseDTO>> getMyBlog();

    ResponseEntity<List<BlogResponseDTO>> getBlogByUsername(String username);
}
