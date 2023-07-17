package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.dto.auth.signup.ValidateWebTokenReq;
import trothly.trothcam.dto.auth.signup.ValidateWebTokenRes;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.auth.WebTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class WebTokenController {

    private final WebTokenService webTokenService;

    /* 웹 토큰 유효성 검증 */
    @PostMapping("/validate-token")
    public BaseResponse<ValidateWebTokenRes> validateWebToken(@RequestBody ValidateWebTokenReq req) {
        ValidateWebTokenRes result = webTokenService.validateWebToken(req);
        return BaseResponse.onSuccess(result);
    }
}