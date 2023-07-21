package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenReqDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenResDto;
import trothly.trothcam.exception.custom.SignupException;
import trothly.trothcam.service.JwtService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebTokenService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    /* 웹 토큰 발급 */
    @Transactional
    public String generateWebToken(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당되는 member_id를 찾을 수 없습니다."));

        String webToken = jwtService.encodeJwtToken(new TokenDto(member.getId()));
        member.generateWebToken(webToken); // Dirty checking(변경 감지)로 DB 업데이트
        return webToken;
    }

    /* 웹 토큰 유효성 검증 */
    @Transactional(readOnly = true)
    public ValidateWebTokenResDto validateWebToken(ValidateWebTokenReqDto req) {
        Member findMember = memberRepository.findByWebToken(req.getWebToken())
                .orElseThrow(() -> new SignupException("유효하지 않는 token입니다."));
        return new ValidateWebTokenResDto(findMember.getEmail());
    }
}
