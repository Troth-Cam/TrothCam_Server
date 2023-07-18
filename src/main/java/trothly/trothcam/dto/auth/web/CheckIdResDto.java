package trothly.trothcam.dto.auth.web;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckIdResDto {
    String id;
    boolean isDuplicated;
}
