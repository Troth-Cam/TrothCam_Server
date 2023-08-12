package trothly.trothcam.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    List<Product> findAllByMember_WebIdAndPublicYn_Y(String webId); // 공개 인증서 조회
//    List<Product> findAllByMember_WebIdAndPublicYn_N(String webId); // 비공개 인증서 조회
}
