package trothly.trothcam.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import trothly.trothcam.domain.Share;
import trothly.trothcam.domain.core.BaseTimeEntity;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@DynamicInsert
@DynamicUpdate
public class Product extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member member;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "views", nullable = false)
    @ColumnDefault("0")
    private int views;

    @Column(name = "likes", nullable = false)
    private int likes;

    @Column(name = "public_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private Share share;

}
