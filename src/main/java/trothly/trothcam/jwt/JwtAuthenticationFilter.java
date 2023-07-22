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

// Jwt 토큰으로 인증 + SecurityContextHolder에 추가하는 필터 설정
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 토큰 가져오기
        String token = jwtService.resolveToken(request);
        String requestURI = request.getRequestURI();

        // 토큰이 존재 여부 + 토큰 검증
        if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
            // 권한
            Authentication authentication = jwtService.getAuthentication(token);
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