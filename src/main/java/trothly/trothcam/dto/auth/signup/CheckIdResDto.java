package trothly.trothcam.dto.auth.signup;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckIdResDto {
    String id;
    boolean isDuplicated;
}
