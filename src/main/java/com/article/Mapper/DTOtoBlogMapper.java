package com.article.Mapper;

import com.article.DTO.BlogRequestDTO;
import com.article.Model.Blog;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Component
public class DTOtoBlogMapper {

    @Autowired
    private ImageToURL imageToURL;

//    public static DTOtoBlogMapper getInstance() {
//        return new DTOtoBlogMapper();
//    }

    public Blog mapBlogRequestDTOToBlog(BlogRequestDTO blogRequestDTO) {

        Map data = imageToURL.uploadImageToCloudinary(blogRequestDTO.getImage());

        Blog blog = Blog.builder()
                .title(blogRequestDTO.getTitle())
                .imageUrl((String)data.get("url"))
                .content(blogRequestDTO.getContent())
                .build();
        return blog;
    }
}
