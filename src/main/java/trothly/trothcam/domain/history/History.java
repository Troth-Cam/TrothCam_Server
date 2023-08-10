package trothly.trothcam.domain.history;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "sold_at", updatable = false, nullable = false)
    private LocalDateTime soldAt;
}
