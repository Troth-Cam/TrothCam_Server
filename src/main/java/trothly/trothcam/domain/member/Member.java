package trothly.trothcam.domain.member;

import lombok.*;
import trothly.trothcam.domain.core.BaseTimeEntity;
import trothly.trothcam.dto.auth.TokenDto;
import trothly.trothcam.service.JwtService;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email
    @Column(name = "email", length = 255, nullable = false)
    private String email;

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
    private Member(String email, Provider provider) {
        this.email = email;
        this.provider = provider;
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
        this.webToken = "";
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

}
