package trothly.trothcam.domain.image;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trothly.trothcam.domain.core.BaseTimeEntity;
import trothly.trothcam.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
public class Image extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", length = 255, nullable = true)
    private String imageUrl;

    @Column(name = "image_hash", length = 255, nullable = false)
    private String imageHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "image_share", nullable = false)
    @Enumerated(EnumType.STRING)
    private Share share;

    @Column(name = "image_lens", nullable = true)
    private String lens;

    @Column(name = "image_location", nullable = true)
    private String location;

    @Column(name = "image_resolution", nullable = true)
    private String resolution;

    @Column(name = "image_size", nullable = true)
    private String size;

    public Image(String imageHash, Member member) {
        this.imageHash = imageHash;
        this.member = member;
    }
}
