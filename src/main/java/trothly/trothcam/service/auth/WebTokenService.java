package trothly.trothcam.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.member.MemberRepository;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenReqDto;
import trothly.trothcam.dto.auth.web.ValidateWebTokenResDto;
import trothly.trothcam.exception.base.BaseException;
import trothly.trothcam.exception.base.ErrorCode;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.exception.custom.SignupException;
import trothly.trothcam.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static trothly.trothcam.exception.base.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebTokenService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    /* 웹 토큰 발급 */
    @Transactional
    public String encodeWebToken(Long memberId, LocalDateTime createdAt) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당되는 member_id를 찾을 수 없습니다."));

        String webToken = jwtService.encodeWebToken(memberId, createdAt);

        // 해당 WEB TOKEN으로 가입한 적이 있는지 검사
        // 같은 WEB TOKEN으로 1번을 초과하여 가입하지 못하도록
        Optional<Member> duplicatedMember = memberRepository.findByWebToken(webToken);
        if(duplicatedMember.isPresent())
            throw new BaseException(ErrorCode.DUPLICATED_MEMBER);

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

    /* 웹 토큰 조회 */
    public String getWebToken(Long memberId) {
        // 토큰 만료 에러 처리
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty())
            throw new BadRequestException(MEMBER_NOT_FOUND);
        return member.get().getWebToken();
    }
}
