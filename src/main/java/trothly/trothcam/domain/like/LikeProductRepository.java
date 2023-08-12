package trothly.trothcam.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeProductRepository extends JpaRepository<LikeProduct, Long> {

    Optional<LikeProduct> findByProductIdAndMemberId(Long productId, Long memberId);

    Long countById(Long id);
}
