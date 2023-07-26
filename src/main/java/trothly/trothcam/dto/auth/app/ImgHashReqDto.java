package trothly.trothcam.dto.auth.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImgHashReqDto {
    @NotBlank(message = "해시 값을 넣어주세요.")
    private String imageHash;
}
