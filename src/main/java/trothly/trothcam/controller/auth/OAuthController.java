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
import trothly.trothcam.exception.custom.base.BaseResponse;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
import trothly.trothcam.service.auth.OAuthService;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final OAuthService oauthService;

    // 애플 로그인
    @PostMapping("/login/apple")
    public BaseResponse<LoginResDto> appleLogin(@RequestBody @Validated LoginReqDto loginReqDto, BindingResult bindingResult){
        logger.info("loginReqDto : " + loginReqDto.getIdToken());

        // BindingResult = 검증 오류가 발생할 경우 오류 내용을 보관하는 객체
        if(bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return new BaseResponse<>(400, objectError.getDefaultMessage(), null);
        }

        LoginResDto result = oauthService.appleLogin(loginReqDto);
        return new BaseResponse<>(result);
    }
}
