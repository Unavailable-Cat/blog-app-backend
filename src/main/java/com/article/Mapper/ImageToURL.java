package com.article.Mapper;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
@Scope(scopeName = "singleton")
public class ImageToURL {
    @Autowired
    private Cloudinary cloudinary;

    public Map uploadImageToCloudinary(MultipartFile image) {
        try {
            Map data = cloudinary.uploader().upload(image.getBytes(), Map.of());
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
