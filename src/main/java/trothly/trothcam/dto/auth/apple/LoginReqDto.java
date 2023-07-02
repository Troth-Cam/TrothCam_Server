package trothly.trothcam.dto.auth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReqDto {
    @NotBlank(message = "애플 토큰 값이 존재하지 않습니다.")
    private String idToken;
}
