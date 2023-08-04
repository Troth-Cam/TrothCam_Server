package trothly.trothcam.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.auth.apple.RefreshTokenReqDto;
import trothly.trothcam.dto.auth.global.ProfileResDto;
import trothly.trothcam.dto.auth.web.LoginWebReqDto;
import trothly.trothcam.dto.auth.web.LoginWebResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.exception.custom.UnauthorizedException;
import trothly.trothcam.service.auth.OAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

import static trothly.trothcam.exception.base.ErrorCode.REQUEST_ERROR;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {
    // H2 : http://localhost:8080/h2-console

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final OAuthService oauthService;

    /* 웹 로그인 */
    @PostMapping("/login")
    public BaseResponse<LoginWebResDto> webLogin(@RequestBody @Validated LoginWebReqDto req, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        LoginWebResDto result = oauthService.webLogin(req);
        return BaseResponse.onSuccess(result);
    }

    // 애플 로그인
    @PostMapping("/{socialLoginType}")
    public BaseResponse<LoginResDto> appleLogin(@PathVariable(name = "socialLoginType") String socialLoginType, @RequestBody @Validated LoginReqDto loginReqDto, BindingResult bindingResult) throws Exception {
        // BindingResult = 검증 오류가 발생할 경우 오류 내용을 보관하는 객체
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        LoginResDto result = oauthService.appleLogin(loginReqDto);
        return BaseResponse.onSuccess(result);
    }

    // refreshToken으로 accessToken 재발급
    // Authorization : Bearer Token에 refreshToken 담기
    @PostMapping("/regenerate-token")
    public BaseResponse<LoginResDto> regenerateAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            LoginResDto result = oauthService.regenerateAccessToken(refreshToken.substring(7));
            return BaseResponse.onSuccess(result);
        } else
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
    }


    // 구글 로그인
    // 사용자 로그인 페이지 제공 단계 - url
    @GetMapping(value="/{socialLoginType}")
    public void socialLoginType(@PathVariable(name="socialLoginType") String socialLoginType) throws IOException {
        oauthService.request(socialLoginType);
    }

    // code -> accessToken 받아오기
    // accessToken -> 사용자 정보 받아오기
    @GetMapping(value="/{socialLoginType}/callback")
    public BaseResponse<LoginResDto> callback(
            @PathVariable(name="socialLoginType") String socialLoginType,
            @RequestParam(name="code") String code) throws JsonProcessingException {

        LoginResDto result = oauthService.oauthLogin(socialLoginType, code);
        return BaseResponse.onSuccess(result);
    }

    // 로그아웃 -> 토큰 만료
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String result = oauthService.logout(token.substring(7));
            return BaseResponse.onSuccess(result);
        } else
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public BaseResponse<String> withdraw(@AuthenticationPrincipal Member member) {
        String result = oauthService.withdraw(member);
        return BaseResponse.onSuccess(result);
    }

    // 개인 정보 조회
    @GetMapping("/profile")
    public BaseResponse<ProfileResDto> getProfile(@AuthenticationPrincipal Member member) {
        ProfileResDto profile = oauthService.getProfile(member);
        return BaseResponse.onSuccess(profile);
    }
}