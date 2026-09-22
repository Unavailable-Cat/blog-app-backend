package com.article.Service.Implementations;

import com.article.DTO.BlogRequestDTO;
import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.Exceptions.BlogNotFoundException;
import com.article.Exceptions.InvalidImageException;
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
    public List<BlogResponseDTO> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        if(blogs.isEmpty()) {
            return List.of();
        }
        return blogs.stream().map(BlogToDTOMapper::mapBlogToBlogResponseDTO).toList();
    }

    @Override
    public DetailedBlogResponseDTO getBlogById(String blogId) {
        Optional<Blog> optionalBlog=blogRepository.findById(blogId);
        if(optionalBlog.isEmpty()){
            throw new BlogNotFoundException("Blog with id: "+blogId+" not found");
        }
        boolean canDelete=userService.canDelete(optionalBlog.get());
        return BlogToDTOMapper.mapBlogToDetailedBlogResponseDTO(optionalBlog.get(),canDelete);
    }

    @Override
    public Void createBlog(BlogRequestDTO blogRequestDTO) {
        isValidImage(blogRequestDTO.getImage());

        Blog blog=dtOtoBlogMapper.mapBlogRequestDTOToBlog(blogRequestDTO);
        userService.addUserToBlog(blog);
        blogRepository.save(blog);
        userService.addBlog(blog);
        return null;
    }

    @Override
    public Void updateBlog(String blogId, BlogRequestDTO blogRequestDTO) {
        isValidImage(blogRequestDTO.getImage());
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isEmpty()) {
            throw new BlogNotFoundException("Blog with id: "+blogId+" not found");
        }
        Blog blog = dtOtoBlogMapper.mapBlogRequestDTOToBlog(blogRequestDTO);
        blog.setBlogId(blogId);
        blogRepository.save(blog);
        return null;
    }

    @Override
    public Void updateTitle(String id, String title) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isEmpty()) {
            throw new BlogNotFoundException("Blog with id: "+id+" not found");
        }
        Blog blog = optionalBlog.get();
        blog.setTitle(title);
        blogRepository.save(blog);
        return null;
    }

    @Override
    public Void updateImage(String id, MultipartFile image) {
        isValidImage(image);
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isEmpty()) {
            throw new BlogNotFoundException("Blog with id: "+id+" not found");
        }
        Map data = imageToURL.uploadImageToCloudinary(image);
        Blog blog = optionalBlog.get();
        blog.setImageUrl((String) data.get("url"));
        blogRepository.save(blog);
        return null;
    }

    private Void isValidImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new InvalidImageException("Image is required");
        }


        long maxSize = 10 * 1024 * 1024; // 10 MB

        if (image.getSize() > maxSize) {
            throw new InvalidImageException("Image must be less than 10 MB");
        }

        String contentType = image.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpg") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp"))) {

            throw new InvalidImageException("Only JPG, PNG and WebP images are allowed");
        }

        String filename = image.getOriginalFilename();

        if (filename == null ||
                !(filename.toLowerCase().endsWith(".jpg") ||
                        filename.toLowerCase().endsWith(".jpeg") ||
                        filename.toLowerCase().endsWith(".png") ||
                        filename.toLowerCase().endsWith(".webp"))) {

            throw new InvalidImageException("Invalid image format");
        }
        return null;
    }

    @Override
    public Void updateContent(String id, String content) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if(optionalBlog.isEmpty()) {
            throw new BlogNotFoundException("Blog with id: "+id+" not found");
        }
        Blog blog = optionalBlog.get();
        blog.setContent(content);
        blogRepository.save(blog);
        return null;
    }

    @Override
    public Void deleteBlog(String blogId) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if(optionalBlog.isEmpty()) {
            throw new BlogNotFoundException("Blog with id: "+blogId+" not found");
        }
        blogRepository.delete(optionalBlog.get());
        return null;
    }

    @Override
    public List<BlogResponseDTO> getMyBlog() {
        List<Blog> blogs=userService.getMyBlogs();
        return blogs.stream().map(BlogToDTOMapper::mapBlogToBlogResponseDTO).toList();
    }

    @Override
    public List<BlogResponseDTO> getBlogByUsername(String username) {
        List<Blog> blogs = userService.getBlogsByUsername(username);
        return blogs.stream().map(BlogToDTOMapper::mapBlogToBlogResponseDTO).toList();
    }
}
