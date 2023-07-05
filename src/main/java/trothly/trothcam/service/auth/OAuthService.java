package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
import trothly.trothcam.dto.auth.apple.RefreshTokenReqDto;
import trothly.trothcam.exception.base.*;
import trothly.trothcam.exception.custom.InvalidProviderException;
import trothly.trothcam.auth.apple.AppleOAuthUserProvider;
import trothly.trothcam.domain.member.*;
import trothly.trothcam.exception.custom.InvalidTokenException;
import trothly.trothcam.service.JwtService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AppleOAuthUserProvider appleOAuthUserProvider;

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;


    // 애플 로그인
    public LoginResDto appleLogin(LoginReqDto loginReqDto) throws BaseException {
        // identity token으로 email값 얻어오기
        String email = appleOAuthUserProvider.getEmailFromToken(loginReqDto.getIdToken());
        logger.info("Apple Login Request Email : " + email);

        // email이 null인 경우 : email 동의 안한 경우!!!!
        if(email == null)
            throw new BaseException(BaseResponseStatus.NOT_AGREE_EMAIL);

        Optional<Member> getMember = memberRepository.findByEmail(email);
        Member member;
        if(getMember.isPresent()){  // 이미 회원가입한 회원인 경우
            member = getMember.get();
            if(!member.getProvider().equals(Provider.APPLE))   // 이미 회원가입했지만 APPLE이 아닌 다른 소셜 로그인 사용
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

        // redis에 refreshToken 저장
        try {
            redisTemplate.opsForValue().set(newRefreshToken, member.getId().toString(), 14L, TimeUnit.SECONDS);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Redis에 저장 (key: userId, value: refreshToken)
        return new LoginResDto(newAccessToken, newRefreshToken);
    }

    // refreshToken으로 accessToken 발급하기
    public LoginResDto regenerateAccessToken(RefreshTokenReqDto refreshTokenReqDto) throws BaseException {
        Long memberId = refreshTokenReqDto.getMemberId();

        String getRefreshToken = refreshTokenReqDto.getRefreshToken();
        String redisRefreshToken = redisTemplate.opsForValue().get(memberId.toString());

        if(getRefreshToken.equals(redisRefreshToken))
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");

        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
        String newAcessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

        try {
            redisTemplate.opsForValue().set(newRefreshToken, memberId.toString(), 14L, TimeUnit.SECONDS);
            logger.info("성공");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new LoginResDto(newAcessToken, newRefreshToken);
    }
}
