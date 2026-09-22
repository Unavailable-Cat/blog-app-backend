package com.article.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "qquztcga",
                "api_key", "531736439158259",
                "api_secret", "yzYO7_uDvWXdlo7QDJuN5POPe0c",
                "secure", true));
        return cloudinary;
    }
}
