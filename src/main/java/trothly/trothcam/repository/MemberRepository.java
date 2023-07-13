package trothly.trothcam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import trothly.trothcam.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsMemberByEmail(String email);
}
