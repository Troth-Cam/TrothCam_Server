package trothly.trothcam.dto.auth.web;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignupReqDto {
    @NotBlank(message = "토큰 값을 입력해주세요.")
    private String webToken;

    @NotBlank(message = "아이디를 입력해주세요.")
    private String webId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 영문자, 숫자, 특수문자 조합을 입력해야합니다.")
    private String webPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private String phone;
    private String email;
}
