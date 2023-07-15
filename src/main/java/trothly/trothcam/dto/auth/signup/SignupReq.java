package trothly.trothcam.dto.auth.signup;

import lombok.*;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignupReq {

    private String webToken;
    private String webId;
    private String webPassword;
    private String name;
    private String phone;
    private String email;
}
