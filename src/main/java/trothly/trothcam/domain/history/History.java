package trothly.trothcam.domain.history;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "history")
public class History {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @Column(name = "price", nullable = false)
    private Long price;

    @CreatedDate
    @Column(name = "sold_at", updatable = false, nullable = false)
    private LocalDateTime soldAt;

    public History(Product product, Member seller, Member buyer, Long price) {
        this.product = product;
        this.seller = seller;
        this.buyer = buyer;
        this.price = price;
    }
}
