package trothly.trothcam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private static String APPLE_REQUEST_URL = "https://appleid.apple.com/auth/keys";
    public LoginResDto appleLogin(LoginReqDto loginReqDto) {

    }

}
