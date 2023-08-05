package trothly.trothcam.dto.auth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationReqDto {
    @NotNull
    @JsonProperty("authorization_code")
    private String authorizationCode;
}
