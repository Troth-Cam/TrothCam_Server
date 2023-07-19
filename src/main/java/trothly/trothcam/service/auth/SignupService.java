package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.web.CheckIdResDto;
import trothly.trothcam.dto.auth.web.SignupReqDto;
import trothly.trothcam.dto.auth.web.SignupResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.SignupException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;

    // 비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    /* 회원 가입 */
    @Transactional
    public SignupResDto signup(SignupReqDto req) throws BaseException {
        System.out.println("web token: " + req.getWebToken());
        // 유효하지 않은 웹 토큰의 경우
        Member findMember = memberRepository.findByWebToken(req.getWebToken())
                .orElseThrow(() -> new SignupException("유효하지 않는 token입니다."));
        System.out.println("findMember: " + findMember.toString());

        String rawPw = req.getWebPassword();
        String encodePw = passwordEncoder.encode(rawPw); // 비밀번호 암호화
        findMember.updateMember(req.getWebId(), encodePw, req.getName(), req.getPhone(), req.getEmail());

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
