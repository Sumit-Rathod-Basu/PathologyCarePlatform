package Pathology.Care.Platform.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", "dnxpr5ldm",
                "api_key", "287895687766646",
                "api_secret", "fBgj7GfLt5Te3hRQ98dhAjNSho4"
        ));
    }
}