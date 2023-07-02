package trothly.trothcam.auth.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import trothly.trothcam.config.FeignClientConfig;
import trothly.trothcam.dto.auth.apple.ApplePublicKeys;

@FeignClient(
        name = "apple-public-key-client",
        url = "https://appleid.apple.com/auth",
        configuration = FeignClientConfig.class
)
public interface AppleClient {
    @GetMapping("/keys")
    ApplePublicKeys getApplePublicKeys();
}
