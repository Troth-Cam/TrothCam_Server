package trothly.trothcam.dto.auth.signup;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignupReqDto {

    private String webToken;
    private String webId;
    private String webPassword;
    private String name;
    private String phone;
    private String email;
}
