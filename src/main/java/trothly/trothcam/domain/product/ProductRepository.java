package trothly.trothcam.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.ProductRankDto;

import java.time.LocalDateTime;
import java.util.List;

import static trothly.trothcam.domain.product.PublicYn.N;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByMember_WebIdAndPublicYn(String webId, PublicYn publicYn); // 인증서 조회
    List<Product> findAllByMember_IdAndPublicYn(Long id, PublicYn publicYn); // 인증서 조회

    @Query(value = "select ap.history_id as historyId, ap.product_id as productId, ap.seller_id as sellerId, ap.buyer_id as buyerId, ap.price as price, ap.sold_at as soldAt, p.image_id as imageId, p.title as title, p.tags as tags\n" +
            "from (select *, rank() over (partition by h.product_id order by price desc, sold_at asc) as rank from history h) as ap join product p on ap.product_id = p.product_id\n" +
            "where ap.rank <= 1\n" +
            "order by price desc, sold_at asc\n" +
            "LIMIT 10", nativeQuery = true)
    List<ProductTop> findProductRandDto();

    @Query(value = "select h.history_id as historyId, h.product_id as productId, h.seller_id as sellerId, h.buyer_id as buyerId, h.price as price, h.sold_at as soldAt, p.image_id as imageId, p.title as title, p.tags as tags \n" +
            "from history h join product p on h.product_id = p.product_id order by sold_at desc LIMIT 10", nativeQuery = true)
    List<ProductTop> findProductLatestDto();

    @Query(value = "select ap.history_id as historyId, ap.product_id as productId, ap.seller_id as sellerId, ap.buyer_id as buyerId, ap.price as price, ap.sold_at as soldAt, p.image_id as imageId, p.title as title, p.tags as tags\n" +
            "from (select *, rank() over (partition by h.product_id order by price desc, sold_at asc) as rank from history h) as ap join product p on ap.product_id = p.product_id\n" +
            "where ap.rank <= 1\n" +
            "order by price desc, sold_at asc\n" +
            "LIMIT 30", nativeQuery = true)
    List<ProductTop> findRankViewAllDto();

    @Query(value = "select h.history_id as historyId, h.product_id as productId, h.seller_id as sellerId, h.buyer_id as buyerId, h.price as price, h.sold_at as soldAt, p.image_id as imageId, p.title as title, p.tags as tags \n" +
            "from history h join product p on h.product_id = p.product_id order by sold_at desc LIMIT 30", nativeQuery = true)
    List<ProductTop> findRankLatestViewAllDto();


    // 비공개 인증서 리스트 조회
    List<Product> findAllByMemberAndPublicYn(Member member, PublicYn publicYn);

    interface ProductTop {
        Long getHistoryId();
        Long getProductId();
        Long getSellerId();
        Long getBuyerId();
        Long getPrice();
        LocalDateTime getSoldAt();
        Long getImageId();
        String getTitle();
        int getTags();
    }
}
