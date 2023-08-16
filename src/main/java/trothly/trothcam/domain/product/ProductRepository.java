package trothly.trothcam.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.dto.web.ProductRankDto;

import java.util.List;

import static trothly.trothcam.domain.product.PublicYn.N;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByMember_WebIdAndPublicYn(String webId, PublicYn publicYn); // 인증서 조회
    List<Product> findAllByMember_IdAndPublicYn(Long id, PublicYn publicYn); // 인증서 조회

    @Query(value = "select ap.history_id, ap.product_id, ap.seller_id, ap.buyer_id, ap.price, ap.sold_at, p.image_id, p.title, p.tags\n" +
            "from (select *, rank() over (partition by h.product_id order by price desc, sold_at asc) as rank from history h) as ap join product p on ap.product_id = p.product_id\n" +
            "where ap.rank <= 1\n" +
            "order by price desc, sold_at asc\n" +
            "LIMIT 10", nativeQuery = true)
    List<ProductRankDto> findProductRandDto();

    // 비공개 인증서 리스트 조회
    List<Product> findAllByMemberAndIdAndPublicYn(Member member, Long productId, PublicYn publicYn);
}
