package trothly.trothcam.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import trothly.trothcam.domain.member.*;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.exception.custom.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final MemberRepository memberRepository;

    public String encodeJwtToken(TokenDto tokenDto) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("trothcam")
                .setIssuedAt(now)
                .setSubject(tokenDto.getMemberId().toString())
//                .setExpiration(new Date(now.getTime()+20000))
                .setExpiration(new Date(now.getTime() + Duration.ofDays(180).toMillis()))
                .claim("memberId", tokenDto.getMemberId())
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    public String encodeJwtRefreshToken(Long userAccountId) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(userAccountId.toString())
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(20160).toMillis()))
                .claim("userAccountId", userAccountId)
                .claim("roles", "USER")
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                                StandardCharsets.UTF_8)))
                .compact();
    }

    public Long getMemberIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                        StandardCharsets.UTF_8)))
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = (UserDetails) memberRepository.findById(this.getMemberIdFromJwtToken(token))
                .orElseThrow(() -> new IllegalArgumentException("해당되는 사용자를 찾을 수 없습니다."));

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }


    public Boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8))).parseClaimsJws(token);

            System.out.println(claims.getBody().getExpiration());

            if (claims.getBody().getExpiration().before(new Date())) {
                throw new UnauthorizedException("만료된 토큰입니다.");
            }
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException("만료된 토큰입니다.");
        }
    }

    public Boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Base64.getEncoder().encodeToString(("" + JWT_SECRET).getBytes(
                            StandardCharsets.UTF_8))).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                throw new UnauthorizedException("만료된 토큰입니다.");
            }

            // 유저 리프레쉬 토큰 확인
            Long memberId = getMemberIdFromJwtToken(token);

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(()-> new IllegalArgumentException("해당되는 사용자를 찾을 수 없습니다."));

            if(member.getRefreshToken().equals(token)){
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new UnauthorizedException("만료된 토큰입니다.");
        }
    }

    // Header X-ACCESS-TOKEN으로 JWT 추출
    public String getToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // Header X-REFRESH-TOKEN으로 JWT 추출
    public String getRefreshToken(HttpServletRequest request){
        return request.getHeader("X-REFRESH-TOKEN");
    }
}