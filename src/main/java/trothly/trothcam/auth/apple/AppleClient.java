package trothly.trothcam.auth.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import trothly.trothcam.config.FeignClientConfig;
import trothly.trothcam.dto.auth.apple.ApplePublicKeys;
import trothly.trothcam.dto.auth.apple.AppleToken;

@FeignClient(
        name = "apple-public-key-client",
        url = "https://appleid.apple.com/auth",
        configuration = FeignClientConfig.class
)
public interface AppleClient {
    @GetMapping(value = "/keys")
    ApplePublicKeys getApplePublicKeys();

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleToken.Response getToken(AppleToken.Request request);
}
