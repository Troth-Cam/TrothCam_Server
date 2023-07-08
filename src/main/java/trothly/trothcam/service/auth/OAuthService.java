package trothly.trothcam.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.dto.auth.apple.LoginReqDto;
import trothly.trothcam.dto.auth.apple.LoginResDto;
//import trothly.trothcam.dto.auth.apple.RefreshTokenReqDto;
import trothly.trothcam.exception.base.*;
import trothly.trothcam.exception.custom.InvalidProviderException;
import trothly.trothcam.auth.apple.AppleOAuthUserProvider;
import trothly.trothcam.domain.member.*;
//import trothly.trothcam.exception.custom.InvalidTokenException;
import trothly.trothcam.service.JwtService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AppleOAuthUserProvider appleOAuthUserProvider;

    private final MemberRepository memberRepository;
//    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;


    // 애플 로그인
    @Transactional
    public LoginResDto appleLogin(LoginReqDto loginReqDto) throws BaseException {
        // identity token으로 email값 얻어오기
        String email = appleOAuthUserProvider.getEmailFromToken(loginReqDto.getIdToken());

        // email이 null인 경우 : email 동의 안한 경우
        if(email == null)
            throw new BaseException(ErrorCode.NOT_AGREE_EMAIL);

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

        // redis에 refreshToken 저장
//        redisTemplate.opsForValue().set(member.getId().toString(), newRefreshToken, 14L, TimeUnit.SECONDS);
//        log.info("redis에 저장된 refreshToken : " + newRefreshToken + "\nmember.getId : " + member.getId().toString());

        return new LoginResDto(newAccessToken, newRefreshToken);
    }

    // refreshToken으로 accessToken 발급하기
//    @Transactional
//    public LoginResDto regenerateAccessToken(RefreshTokenReqDto refreshTokenReqDto) throws BaseException {
//        String getRefreshToken = refreshTokenReqDto.getRefreshToken();
//        Long memberId = jwtService.getMemberIdFromJwtToken(getRefreshToken);
//
//        String redisRefreshToken = redisTemplate.opsForValue().get(memberId.toString());
//        log.info("getRefreshToken : " + getRefreshToken);
//        log.info("redisRefreshToken : " + redisRefreshToken);   // 요 부분이 값이 있었다가 null로 떴다가 그래
//
//        if(!getRefreshToken.equals(redisRefreshToken))
//            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");
//
//        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));
//        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
//
//        redisTemplate.opsForValue().set(memberId.toString(), newRefreshToken, 14L, TimeUnit.SECONDS);
//
//        return new LoginResDto(newAccessToken, newRefreshToken);
//    }
}
