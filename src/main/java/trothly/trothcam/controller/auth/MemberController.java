package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.auth.signup.SignupReq;
import trothly.trothcam.dto.auth.signup.SignupRes;
import trothly.trothcam.service.auth.MemberService;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /* 웹 회원 가입 */
    @PostMapping("/auth/signup")
    public SignupRes signup(@RequestBody @Valid SignupReq req) {
        String webId = memberService.signup(req);
        return new SignupRes(webId);
    }
}
