package trothly.trothcam.jwt;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.exception.custom.UnauthorizedException;
import trothly.trothcam.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }


    // header에서 token 값을 가져옴
    // header : JWT를 검증하는 암호화 알고리즘, 토큰 타입이 포함된다
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken == null)
            throw new BadRequestException("토큰값이 존재하지 않습니다.");
        else if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return bearerToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtService.getToken();
        String refreshToken = jwtService.getRefreshToken();
        String requestURI = request.getRequestURI();

        // 토큰이 존재 여부 + 토큰 검증
        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            // 권한
            Authentication authentication = jwtService.getAuthentication(token);
            // security 세션에 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else if (StringUtils.hasText(refreshToken) && jwtService.validateToken(refreshToken)) {
            // 권한
            Authentication authentication = jwtService.getAuthentication(refreshToken);
            // security 세션에 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            throw new UnauthorizedException("유효한 토큰값이 아닙니다.");
        }

        chain.doFilter(request, response);
    }
}