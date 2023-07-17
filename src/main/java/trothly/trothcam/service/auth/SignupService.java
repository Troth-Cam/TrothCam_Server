package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.signup.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;

    /* 회원 가입 */
    @Transactional
    public SignupRes signup(SignupReq req) {
        System.out.println("web token: " + req.getWebToken());
        Member findMember = memberRepository.findByWebToken(req.getWebToken());
        System.out.println("findMember: " + findMember.toString());
        findMember.updateMember(req.getWebToken(), req.getWebId(), req.getWebPassword(), req.getName(), req.getPhone(), req.getEmail());

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
