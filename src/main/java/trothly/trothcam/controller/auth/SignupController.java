package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.dto.auth.web.CheckIdResDto;
import trothly.trothcam.dto.auth.web.SignupReqDto;
import trothly.trothcam.dto.auth.web.SignupResDto;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.auth.SignupService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class SignupController { // TODO: JPA 결과로 자꾸 null을 반환하는데 왜 이럴까~~~~~@~@!~!@

    private final SignupService signupService;

    /* 아이디 중복 확인 */
    @PostMapping("/check-id/{id}")
    public BaseResponse<CheckIdResDto> checkDuplicateId(@PathVariable String id) throws Exception {
        CheckIdResDto result = signupService.checkDuplicateId(id);
        return BaseResponse.onSuccess(result);
    }

    /* 웹 회원 가입 */
    @PostMapping("/signup")
    public BaseResponse<SignupResDto> signup(@RequestBody @Validated SignupReqDto req, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        SignupResDto result = signupService.signup(req);
        return BaseResponse.onSuccess(result);
    }
}
