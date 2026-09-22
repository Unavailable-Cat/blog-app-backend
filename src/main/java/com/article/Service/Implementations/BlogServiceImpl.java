package com.article.Service.Implementations;

import com.article.DTO.BlogRequestDTO;
import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.Mapper.BlogToDTOMapper;
import com.article.Mapper.DTOtoBlogMapper;
import com.article.Mapper.ImageToURL;
import com.article.Model.Blog;
import com.article.Model.User;
import com.article.Repository.BlogRepository;
import com.article.Service.BlogService;
import com.article.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    DTOtoBlogMapper dtOtoBlogMapper;

    @Autowired
    ImageToURL imageToURL;

    @Override
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        if(blogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(blogs.stream().map(BlogToDTOMapper::mapBlogToBlogResponseDTO).toList());
    }

    @Override
    public ResponseEntity<DetailedBlogResponseDTO> getBlogById(String blogId) {
        Optional<Blog> optionalBlog=blogRepository.findById(blogId);
        if(optionalBlog.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        boolean canDelete=userService.canDelete(optionalBlog.get());
        //System.out.println("user is authenticated?: " + canDelete);
        return ResponseEntity.ok(BlogToDTOMapper.mapBlogToDetailedBlogResponseDTO(optionalBlog.get(),canDelete));
    }

    @Override
    public ResponseEntity<Void> createBlog(BlogRequestDTO blogRequestDTO) {
        if(!(isValidImage(blogRequestDTO.getImage()))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Blog blog=dtOtoBlogMapper.mapBlogRequestDTOToBlog(blogRequestDTO);
        userService.addUserToBlog(blog);
        blogRepository.save(blog);
        userService.addBlog(blog);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> updateBlog(String blogId, BlogRequestDTO blogRequestDTO) {
        if(!(isValidImage(blogRequestDTO.getImage()))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Blog blog = dtOtoBlogMapper.mapBlogRequestDTOToBlog(blogRequestDTO);
        blog.setBlogId(blogId);
        blogRepository.save(blog);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateTitle(String id, String title) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Blog blog = optionalBlog.get();
        blog.setTitle(title);
        blogRepository.save(blog);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateImage(String id, MultipartFile image) {
        if(!(isValidImage(image))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Map data = imageToURL.uploadImageToCloudinary(image);
        // Update the blog with the new image URL
        Blog blog = optionalBlog.get();
        blog.setImageUrl((String) data.get("url"));
        blogRepository.save(blog);
        return ResponseEntity.ok().build();
    }

    private boolean isValidImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            //throw new IllegalArgumentException("Image is required");
            return false;
        }

        // 2. Limit file size
        long maxSize = 10 * 1024 * 1024; // 5 MB

        if (image.getSize() > maxSize) {
            //throw new IllegalArgumentException("Image must be less than 5 MB");
            return false;
        }

        // 3. Check the declared content type
        String contentType = image.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpg") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp"))) {

            //throw new IllegalArgumentException("Only JPG, PNG and WebP images are allowed");
            return false;
        }

        String filename = image.getOriginalFilename();

        if (filename == null ||
                !(filename.toLowerCase().endsWith(".jpg") ||
                        filename.toLowerCase().endsWith(".jpeg") ||
                        filename.toLowerCase().endsWith(".png") ||
                        filename.toLowerCase().endsWith(".webp"))) {

            //throw new IllegalArgumentException("Invalid image format");
            return false;
        }
        return true;
    }

    @Override
    public ResponseEntity<Void> updateContent(String id, String content) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Blog blog = optionalBlog.get();
        blog.setContent(content);
        blogRepository.save(blog);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteBlog(String blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        blogRepository.delete(optionalBlog.get());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<BlogResponseDTO>> getMyBlog() {
        List<Blog> blogs=userService.getMyBlogs();
        return ResponseEntity.ok(blogs.stream().map(BlogToDTOMapper::mapBlogToBlogResponseDTO).toList());
    }

    @Override
    public ResponseEntity<List<BlogResponseDTO>> getBlogByUsername(String username) {
        List<Blog> blogs = userService.getBlogsByUsername(username);
        return ResponseEntity.ok(blogs.stream().map(BlogToDTOMapper::mapBlogToBlogResponseDTO).toList());
    }
}
