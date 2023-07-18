package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.web.CheckIdResDto;
import trothly.trothcam.dto.auth.web.SignupReqDto;
import trothly.trothcam.dto.auth.web.SignupResDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;

    /* 회원 가입 */
    @Transactional
    public SignupResDto signup(SignupReqDto req) {
        System.out.println("web token: " + req.getWebToken());
        Member findMember = memberRepository.findByWebToken(req.getWebToken());
        System.out.println("findMember: " + findMember.toString());
        findMember.updateMember(req.getWebId(), req.getWebPassword(), req.getName(), req.getPhone(), req.getEmail());

        return new SignupResDto(findMember.getWebId());
    }

    /* 아이디 중복 확인 */
    @Transactional(readOnly = true)
    public CheckIdResDto checkDuplicateId(String webId) {
        boolean isDuplicated = false;
        System.out.println("아이디 중복 확인" + webId + memberRepository.existsByWebId(webId));
        if (memberRepository.existsByWebId(webId)) {
            isDuplicated = true;
        }
        return new CheckIdResDto(webId, isDuplicated);
    }
}
