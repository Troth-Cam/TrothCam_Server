package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import trothly.trothcam.dto.auth.signup.SignupReqDto;
import trothly.trothcam.service.auth.MemberService;
import trothly.trothcam.validator.signup.CheckIdValidator;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final CheckIdValidator checkIdValidator;

    /* 커스텀 유효성 검증을 위해 추가 */
    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkIdValidator);
    }

    @PostMapping("/auth/signup")
    public String Signup(@Valid SignupReqDto signupReqDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            /* 회원가입 실패시 입력 데이터 값을 유지 */
//            model.addAttribute("signupReqDto", signupReqDto);

            /* 유효성 통과 못한 필드과 메시지를 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            /* 회원가입 페이지로 다시 리턴 */
//            return "";
        }

         memberService.signup(signupReqDto);
//        return "redirect:/";
    }
}
