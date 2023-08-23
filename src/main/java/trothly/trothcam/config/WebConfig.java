package trothly.trothcam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:8080","http://localhost:3000", "https://trothly.com") // 허용할 출처
                .allowedMethods("*") // 허용할 HTTP method
                .allowCredentials(true) // 쿠키 인증 요청 허용
                .allowedHeaders("*")
                .allowedOrigins("*")
                .maxAge(3600L); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
}