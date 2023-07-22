package trothly.trothcam.controller.auth;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trothly.trothcam.domain.member.Member;

@RestController
@RequestMapping("/test")
public class TestController {
    /*
        * 참고용 API

        - 로그인 이후 모든 요청엔 Authorization : Bearer에 토큰이 들어있음
        - 로그인 한 유저의 정보가 필요할 땐 @AuthenticationPrincipal 을 사용하면 된다.
        - 프론트에서 Bearer 토큰을 넣어주면 사용 가능하므로 로그인 한 사용자의 정보를 얻기 위해 RequestParam 이나 RequestBody가 따로 필요 X
     */
    @PostMapping("")
    public String loginTest(@AuthenticationPrincipal Member member) {
        return "현재 로그인한 유저의 memberId = " + member.getId();
    }
}
