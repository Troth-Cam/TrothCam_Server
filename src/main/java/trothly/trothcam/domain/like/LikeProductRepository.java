package trothly.trothcam.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trothly.trothcam.domain.member.Member;
import trothly.trothcam.domain.product.Product;

import java.util.Optional;

@Repository
public interface LikeProductRepository extends JpaRepository<LikeProduct, Long> {

    Optional<LikeProduct> findByProductIdAndMemberId(Long productId, Long memberId);

    Long countByProductId(Long productId);

    Optional<LikeProduct> findByProductAndMember(Product product, Member member);
}
