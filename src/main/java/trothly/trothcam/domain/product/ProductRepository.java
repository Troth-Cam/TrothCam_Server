package trothly.trothcam.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByMember_WebIdAndPublicYn(String webId, PublicYn publicYn); // 인증서 조회
    List<Product> findAllByMember_IdAndPublicYn(Long id, PublicYn publicYn); // 인증서 조회
}
