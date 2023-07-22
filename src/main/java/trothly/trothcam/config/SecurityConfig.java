package trothly.trothcam.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import trothly.trothcam.jwt.JwtAuthenticationFilter;
import trothly.trothcam.jwt.JwtExceptionFilter;
import trothly.trothcam.service.JwtService;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;
    private final JwtExceptionFilter jwtExceptionFilter;

    // 스프링시큐리티 앞단 설정
    @Override
    public void configure(WebSecurity web) {
        // 로그인 개발 끝나면 "/**" 경로에서 삭제
        web.ignoring().antMatchers("/auth/**", "/h2-console/**", "/sample/**");
    }

    // 스프링시큐리티 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource()) // 크로스 오리진 정책 안씀 (인증 X) , 시큐리티 필터에 등록 인증 O
                .and()
                // 세션을 사용하지 않기 때문에 STATELESS 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/sample/**").permitAll()
                //.antMatchers("/**").permitAll()     // 로그인 개발 끝나면 삭제
                .anyRequest().authenticated()
                .and()
                // JwtAuthenticationFilter 보다 jwtExceptionFilter를 먼저 실행
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", getDefaultCorsConfiguration());

        return source;
    }

    private CorsConfiguration getDefaultCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:8080"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 header 에 응답을 허용
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 get,post,patch,put,delete 요청 허용
        configuration.setAllowedOrigins(Arrays.asList("*")); // 모든 ip 응답을 허용
        configuration.setAllowCredentials(true); // 내 서버가 응답할 때 json 을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        configuration.setMaxAge(3600L);

        return configuration;
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}