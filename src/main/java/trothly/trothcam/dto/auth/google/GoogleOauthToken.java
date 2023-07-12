package trothly.trothcam.dto.auth.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoogleOauthToken {
    // 응답 객체가 JSON 형식으로 되어 있으므로, 이를 deserialization 해서 자바 객체에 담음
    // -> 여기서 자바 객체에 해당하는 클래스
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
