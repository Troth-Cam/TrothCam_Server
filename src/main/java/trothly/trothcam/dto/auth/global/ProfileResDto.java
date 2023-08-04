package trothly.trothcam.dto.auth.global;

import lombok.Builder;
import lombok.Getter;
import trothly.trothcam.domain.member.Member;

@Getter
public class ProfileResDto {
    private Long memberId;
    private String email;
    private String name;
    private String image;

    @Builder
    public ProfileResDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.image = member.getImage();
    }
}
