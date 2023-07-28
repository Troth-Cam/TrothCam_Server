package trothly.trothcam.dto.auth.apple;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppleInfo {
    private String email;
    private String sub;

    @Builder
    public AppleInfo(String email, String sub) {
        this.email = email;
        this.sub = sub;
    }
}
