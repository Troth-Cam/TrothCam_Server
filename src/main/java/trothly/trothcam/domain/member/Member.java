package trothly.trothcam.domain.member;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import trothly.trothcam.domain.core.BaseTimeEntity;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.service.JwtService;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "apple_sub", length = 255, nullable = true)
    private String appleSub;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "image", length = 255, nullable = true)
    private String image;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at", nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    @Builder
    private Member(String email, String appleSub, Provider provider) {
        this.email = email;
        this.appleSub = appleSub;
        this.provider = provider;
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
        this.webToken = "";
    }

    // refreshToken 재발급
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    // 로그아웃 시 토큰 만료
    public void refreshTokenExpires() {
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    /* 웹 */
    @Column(name = "web_id")
    private String webId;

    @Column(name = "web_password")
    private String webPassword;

    @Column(name = "web_token")
    private String webToken;

    @Column(name = "phone")
    private String phone;

    public void updateMember(String webId, String webPassword, String name, String phone, String email) {
        this.webId = webId;
        this.webPassword = webPassword;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public void generateWebToken(String webToken) {
        this.webToken = webToken;
    }


    /* implements UserDetails 시 구현해야하는 메소드 */

    // UserDetails = 사용자 정보를 담는 인터페이스 -> 아래 메소드는 필요시 변형해서 사용!

    // 계정 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 계정 비밀번호
    @Override
    public String getPassword() {
        return null;
    }

    // 계정 이름
    @Override
    public String getUsername() {
        return null;
    }

    // 계정의 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return false; // 만료
    }

    // 계정의 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return false;    // 잠김
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;    // 만료
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return false;    // 비활성화
    }
}
