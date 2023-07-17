package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import trothly.trothcam.dto.auth.signup.CheckIdRes;
import trothly.trothcam.dto.auth.signup.SignupReq;
import trothly.trothcam.dto.auth.signup.SignupRes;
import trothly.trothcam.exception.base.BaseResponse;
import trothly.trothcam.service.auth.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    /* 웹 회원 가입 */ /* 500 에러 왜..? */
    @PostMapping("/signup")
    public BaseResponse<SignupRes> signup(@RequestBody @Validated SignupReq req, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }
        SignupRes result = memberService.signup(req);
        return BaseResponse.onSuccess(result);
    }

    /* 아이디 중복 확인 */ /* 500 에러?! */
    @PostMapping("/check-id/{id}")
    public BaseResponse<CheckIdRes> checkDuplicateId(@PathVariable String id, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }
        CheckIdRes result = memberService.checkDuplicateId(id);
        return BaseResponse.onSuccess(result);
    }
}
