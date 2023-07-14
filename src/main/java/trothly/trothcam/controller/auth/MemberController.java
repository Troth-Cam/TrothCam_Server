package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import trothly.trothcam.dto.auth.signup.SignupReq;
import trothly.trothcam.service.auth.MemberService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/auth/signup")
    public String Signup(@Valid SignupReq signupReq) {

    }
}
