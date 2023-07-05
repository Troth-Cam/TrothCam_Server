package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.dto.auth.apple.RefreshTokenReqDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
import trothly.trothcam.service.auth.OAuthService;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    // H2 : http://localhost:8080/h2-console

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final OAuthService oauthService;

    // 애플 로그인
    @PostMapping("/login/apple")
    public BaseResponse<LoginResDto> appleLogin(@RequestBody @Validated LoginReqDto loginReqDto, BindingResult bindingResult) throws Exception {
        logger.info("loginReqDto : " + loginReqDto.getIdToken());

        // BindingResult = 검증 오류가 발생할 경우 오류 내용을 보관하는 객체
        if(bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return new BaseResponse<>(400, objectError.getDefaultMessage(), null);
        }

        try{
            LoginResDto result = oauthService.appleLogin(loginReqDto);
            return new BaseResponse<>(result);
        } catch(BaseException e) {
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

    // refreshToken으로 accessToken 재발급
    @PostMapping("/refresh")
    public BaseResponse<LoginResDto> regenerateAccessToken(@RequestBody @Validated RefreshTokenReqDto refreshTokenReqDto) throws BaseException {
        try{
            LoginResDto result = oauthService.regenerateAccessToken(refreshTokenReqDto);
            return new BaseResponse<>(result);
        } catch(BaseException e) {
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }
}
