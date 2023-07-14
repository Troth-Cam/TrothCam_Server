package trothly.trothcam.dto.auth.signup;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
