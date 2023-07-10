package trothly.trothcam.dto.auth.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoogleUser {
    // 다시 JSON 형식의 응답 객체를 deserialization 해서 자바 객체에 담음
    // -> 여기서 자바 객체에 해당하는 클래스
    private String id;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String picture;
    private String locale;
}
