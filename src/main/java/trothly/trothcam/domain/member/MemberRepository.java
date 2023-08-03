package trothly.trothcam.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByWebId(String webId);
    boolean existsByWebId(String webId);
    Optional<Member> findByWebToken(String webToken);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findByAppleSub(String sub);
}
