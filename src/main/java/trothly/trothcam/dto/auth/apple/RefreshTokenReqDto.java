package trothly.trothcam.dto.auth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenReqDto {
    @NotNull
    @JsonProperty("member_id")
    private Long memberId;

    @NotNull
    @JsonProperty("refresh_token")
    private String refreshToken;
}
