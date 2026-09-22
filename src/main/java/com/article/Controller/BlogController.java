package com.article.Controller;

import com.article.DTO.BlogRequestDTO;
import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.Model.Blog;
import com.article.Service.BlogService;
import com.article.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs(){
        return blogService.getAllBlogs();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<DetailedBlogResponseDTO> getBlogById(@PathVariable String id){
        return blogService.getBlogById(id);
    }

    @GetMapping("my")
    public ResponseEntity<List<BlogResponseDTO>> getMyBlog(){
        return blogService.getMyBlog();
    }

    @GetMapping("username/{username}")
    public ResponseEntity<List<BlogResponseDTO>> getBlogByUsername(@PathVariable String username){
        return blogService.getBlogByUsername(username);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createBlog(@ModelAttribute BlogRequestDTO blogRequestDTO){
            return blogService. createBlog(blogRequestDTO);
    }

    @PutMapping(value = "{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateBlog(@PathVariable String id,@ModelAttribute BlogRequestDTO blogRequestDTO){
        return blogService.updateBlog(id,blogRequestDTO);
    }

    @PatchMapping("title/{id}")
    public ResponseEntity<Void> updateTitle(@PathVariable String id, @RequestBody String title){
        return blogService.updateTitle(id, title);
    }

    @PatchMapping(value = "image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImage(@PathVariable String id, @RequestParam("image") MultipartFile image){

        return blogService.updateImage(id, image);
    }

    @PatchMapping("content/{id}")
    public ResponseEntity<Void> updateContent(@PathVariable String id, @RequestBody String content){
        return blogService.updateContent(id, content);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable String id){
        return blogService.deleteBlog(id);
    }

}
