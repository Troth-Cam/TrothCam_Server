package trothly.trothcam.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.dto.auth.apple.RefreshTokenReqDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
import trothly.trothcam.service.auth.OAuthService;

import java.io.IOException;

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
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        LoginResDto result = oauthService.appleLogin(loginReqDto);
        return BaseResponse.onSuccess(result);
    }

    // refreshToken으로 accessToken 재발급
    @PostMapping("/refresh")
    public BaseResponse<LoginResDto> regenerateAccessToken(@RequestBody @Validated RefreshTokenReqDto refreshTokenReqDto) throws BaseException {
        LoginResDto result = oauthService.regenerateAccessToken(refreshTokenReqDto);
        return BaseResponse.onSuccess(result);
    }

    // 구글 로그인
    // 사용자 로그인 페이지 제공 단계 - url
    @GetMapping(value="/auth/{socialLoginType}")
    public void socialLoginType(@PathVariable(name="socialLoginType") String socialLoginType) throws IOException {
        oauthService.request(socialLoginType);
    }

    // code -> accessToken 받아오기
    // accessToken -> 사용자 정보 받아오기
    @GetMapping(value="/auth/{socialLoginType}/callback")
    public BaseResponse<LoginResDto> callback(
            @PathVariable(name="socialLoginType") String socialLoginType,
            @RequestParam(name="code") String code) throws JsonProcessingException {

        LoginResDto result = oauthService.oauthLogin(socialLoginType, code);
        return BaseResponse.onSuccess(result);
    }
}
