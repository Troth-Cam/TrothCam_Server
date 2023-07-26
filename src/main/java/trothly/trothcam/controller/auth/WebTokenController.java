package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.auth.web.ValidateWebTokenReqDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.exception.custom.UnauthorizedException;
import trothly.trothcam.service.auth.WebTokenService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class WebTokenController {

    private final WebTokenService webTokenService;

    /* 웹 토큰 유효성 검증 */
    @PostMapping("/validate-token")
    public BaseResponse<ValidateWebTokenResDto> validateWebToken(@RequestBody ValidateWebTokenReqDto req) {
        ValidateWebTokenResDto result = webTokenService.validateWebToken(req);
        return BaseResponse.onSuccess(result);
    }

    /* 웹 토큰 조회 */
    @GetMapping("/web-token")
    public BaseResponse<String> getWebToken(@AuthenticationPrincipal Member member) {
        try {
            String webToken = webTokenService.getWebToken(member.getId());
            return BaseResponse.onSuccess(webToken);
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않거나 만료된 토큰입니다.");
        }
    }
}
