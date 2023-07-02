package trothly.trothcam.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import trothly.trothcam.exception.custom.BadRequestException;
import trothly.trothcam.exception.custom.UnauthorizedException;
import trothly.trothcam.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtService.getToken((HttpServletRequest) request);
        String refreshToken = jwtService.getRefreshToken(request);
        // 토큰이 존재한다면
        if (token != null) {
            // 토큰을 검증
            if (jwtService.validateToken(token)) {
                //권한
                Authentication authentication = jwtService.getAuthentication(token);

                // security 세션에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new UnauthorizedException("유효한 토큰값이 아닙니다.");
            }

        }else if(refreshToken!=null){

            if(jwtService.validateRefreshToken(refreshToken)){
                //권한
                Authentication authentication = jwtService.getAuthentication(refreshToken);

                // security 세션에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                throw new UnauthorizedException("유효하지 않은 Refresh Token 입니다.");
            }
        }else{
            throw new BadRequestException("존재하지 않는 토큰입니다.");
        }
        chain.doFilter(request, response);
    }
}