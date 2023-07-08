package trothly.trothcam.domain.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trothly.trothcam.domain.core.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;

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

    @Builder
    private Member(String email, Provider provider) {
        this.email = email;
        this.provider = provider;
    }
}
