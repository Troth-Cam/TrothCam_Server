package trothly.trothcam.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import trothly.trothcam.domain.core.BaseTimeEntity;
import trothly.trothcam.domain.image.Image;
import trothly.trothcam.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "title")
    private String title;

    @Column(name = "tags", nullable = false)
    private int tags;

    @Column(name = "price")
    private Long price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "views", nullable = false)
    @ColumnDefault("0")
    private int views;

    @Column(name = "public_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private PublicYn publicYn;

    public void updateViews(int views) {
        this.views = views;
    }

    public void updatePublicYn(PublicYn publicYn) {
        this.publicYn = publicYn;
    }
}
