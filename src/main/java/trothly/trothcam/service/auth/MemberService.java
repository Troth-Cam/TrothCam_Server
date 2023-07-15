package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.signup.SignupReq;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /* 회원 가입 */
    @Transactional
    public String signup(SignupReq signupReq) {
        checkDuplicateId(signupReq.getWebId());

        Member findMember = memberRepository.findByWebToken(signupReq.getWebToken());
        findMember.updateMember(signupReq.getWebToken(), signupReq.getWebId(), signupReq.getWebPassword(), signupReq.getName(), signupReq.getPhone(), signupReq.getEmail());

        return findMember.getWebId();
    }

    /* 회원가입 시 중복 검사 */
    @Transactional(readOnly = true)
    public void checkDuplicateId(String webId) {
        if (memberRepository.existsByWebId(webId)) {
            throw new IllegalStateException("이미 가입된 아이디입니다.");
        }
    }
}
