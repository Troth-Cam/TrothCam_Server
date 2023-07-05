package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
import trothly.trothcam.exception.custom.InvalidProviderException;
import trothly.trothcam.auth.apple.AppleOAuthUserProvider;
import trothly.trothcam.domain.member.*;
import trothly.trothcam.service.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AppleOAuthUserProvider appleOAuthUserProvider;

    private final MemberRepository memberRepository;
    private final JwtService jwtService;


    // 애플 로그인
    public LoginResDto appleLogin(LoginReqDto loginReqDto) {
        // identity token으로 email값 얻어오기
        String email = appleOAuthUserProvider.getEmailFromToken(loginReqDto.getIdToken());
        logger.info("Apple Login Request Email : " + email);

        Optional<Member> getMember = memberRepository.findByEmail(email);
        Member member;
        if(getMember.isPresent()){  // 이미 회원가입한 회원인 경우
            member = getMember.get();
            if(!member.getProvider().equals("APPLE"))   // 이미 회원가입했지만 APPLE이 아닌 다른 소셜 로그인 사용
                throw new InvalidProviderException("GOOGLE로 회원가입한 회원입니다.");
        } else {    // 아직 회원가입 하지 않은 회원인 경우
            member = memberRepository.save(
                    Member.builder()
                        .email(email)
                        .provider(Provider.APPLE)
                        .build()
            );
        }

        // accessToken, refreshToken 발급
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(member.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(member.getId());

        logger.info("accessToken : " + newAccessToken);
        logger.info("refreshToken : " + newRefreshToken);

        member.updateRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new LoginResDto(newAccessToken, newRefreshToken);
    }
}
