package com.article.Mapper;

import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.Model.Blog;

public class BlogToDTOMapper {
    public static DetailedBlogResponseDTO mapBlogToDetailedBlogResponseDTO(Blog blog,boolean canDelete) {

        return DetailedBlogResponseDTO.builder()
                .id(blog.getBlogId())
                .title(blog.getTitle())
                .imageUrl(blog.getImageUrl())
                .createdAt(blog.getCreatedAt())
                .content(blog.getContent())
                .author(blog.getUser().getUsername())
                .canDelete(canDelete)
                .build();
    }

    public static BlogResponseDTO mapBlogToBlogResponseDTO(Blog blog) {
        return BlogResponseDTO.builder()
                .id(blog.getBlogId())
                .title(blog.getTitle())
                .imageUrl(blog.getImageUrl())
                .author(blog.getUser().getUsername())
                .createdAt(blog.getCreatedAt())
                .build();
    }
}
