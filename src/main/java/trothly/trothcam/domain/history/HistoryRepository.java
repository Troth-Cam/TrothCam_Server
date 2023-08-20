package trothly.trothcam.domain.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByProductIdOrderBySoldAtDesc(Long productId);
    Optional<History> findTopByProduct_IdOrderBySoldAt(Long productId);
}
