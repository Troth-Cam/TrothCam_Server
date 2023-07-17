package trothly.trothcam.dto.auth.signup;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckIdRes {
    String id;
    boolean isDuplicated;
}
