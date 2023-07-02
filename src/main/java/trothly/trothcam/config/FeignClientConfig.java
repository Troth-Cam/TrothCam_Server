package trothly.trothcam.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import trothly.trothcam.TrothcamApplication;

@Configuration
@EnableFeignClients(basePackageClasses = TrothcamApplication.class)
public class FeignClientConfig {
}
