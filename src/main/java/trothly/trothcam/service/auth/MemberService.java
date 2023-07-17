package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.signup.CheckIdRes;
import trothly.trothcam.dto.auth.signup.SignupReq;
import trothly.trothcam.dto.auth.signup.SignupRes;
import trothly.trothcam.exception.base.BaseException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /* 회원 가입 */
    @Transactional
    public SignupRes signup(SignupReq signupReq) {
        System.out.println("web token: " + signupReq.getWebToken());
        Member findMember = memberRepository.findByWebToken(signupReq.getWebToken());
        System.out.println("findMember: " + findMember.toString());
        findMember.updateMember(signupReq.getWebToken(), signupReq.getWebId(), signupReq.getWebPassword(), signupReq.getName(), signupReq.getPhone(), signupReq.getEmail());

        return new SignupRes(findMember.getWebId());
    }

    /* 아이디 중복 확인 */
    @Transactional(readOnly = true)
    public CheckIdRes checkDuplicateId(String webId) {
        boolean isDuplicated = false;
        System.out.println("아이디 중복 확인" + webId + memberRepository.existsByWebId(webId));
        if (memberRepository.existsByWebId(webId)) {
            isDuplicated = true;
        }
        return new CheckIdRes(webId, isDuplicated);
    }
}
