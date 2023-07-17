package trothly.trothcam.controller.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.auth.signup.CheckIdRes;
import trothly.trothcam.dto.auth.signup.SignupReq;
import trothly.trothcam.dto.auth.signup.SignupRes;
import trothly.trothcam.service.auth.MemberService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /* 웹 회원 가입 */ /* 500 에러 왜..? */
    @PostMapping("/auth/signup")
    public SignupRes signup(@RequestBody SignupReq req) {
        String webId = memberService.signup(req);
        return new SignupRes(webId);
    }

    /* 아이디 중복 확인 */ /* 근데 왜 모든 값에 대해 false 가 뜨는지? */
    @PostMapping("/auth/check-id/{id}")
    public CheckIdRes checkDuplicateId(@PathVariable String id) {
        boolean isDuplicated = memberService.checkDuplicateId(id);
        return new CheckIdRes(id, isDuplicated);
    }
}
