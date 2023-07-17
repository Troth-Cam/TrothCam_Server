package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.dto.auth.signup.*;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.auth.SignupService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class SignupController { // TODO: 2023/07/17 전체적으로 500에러가 뜨는데 왜 이럴까~~~~~&&^&^~

    private final SignupService signupService;

    /* 아이디 중복 확인 */
    @PostMapping("/check-id/{id}")
    public BaseResponse<CheckIdRes> checkDuplicateId(@PathVariable String id, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }
        CheckIdRes result = signupService.checkDuplicateId(id);
        return BaseResponse.onSuccess(result);
    }

    /* 웹 회원 가입 */
    @PostMapping("/signup")
    public BaseResponse<SignupRes> signup(@RequestBody @Validated SignupReq req, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }
        SignupRes result = signupService.signup(req);
        return BaseResponse.onSuccess(result);
    }
}
