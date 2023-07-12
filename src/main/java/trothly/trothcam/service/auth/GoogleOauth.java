package trothly.trothcam.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import trothly.trothcam.dto.auth.google.GoogleOauthToken;
import trothly.trothcam.dto.auth.google.GoogleUser;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOauth {
    @Value("${app.google.url}")
    private String GOOGLE_LOGIN_URL;

    @Value("${app.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${app.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${app.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${app.google.token.url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;

    @Value("${app.google.userinfo.url}")
    private String GOOGLE_SNS_USERINFO_URL;

    private final ObjectMapper objectMapper;

    // 사용자 로그인 페이지 제공 단계 - url 생성
    public String getOauthRedirectURL() {
        String reqUrl = GOOGLE_LOGIN_URL + "?client_id=" + GOOGLE_SNS_CLIENT_ID + "&redirect_uri=" + GOOGLE_SNS_CALLBACK_URL
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }

    // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
    public ResponseEntity<String> requestAccessToken(String code) {

        // RestTemplate : 스프링에서 제공하는 http 통신에 유용하게 쓸 수 있는 템플릿
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        // RestTemplate 주요 메서드 postForEntity : POST 요청을 보내고 결과로 ResponseEntity 로 반환
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    // 응답 객체가 JSON 형식으로 되어 있으므로, 이를 deserialization 해서 자바 객체에 담음
    public GoogleOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() = " + response.getBody());
        GoogleOauthToken googleOauthToken = objectMapper.readValue(response.getBody(), GoogleOauthToken.class);
        return googleOauthToken;
    }

    // 액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아옴
    public ResponseEntity<String> requestUserInfo(GoogleOauthToken oAuthToken) {

        RestTemplate restTemplate = new RestTemplate();
        // header 에 accessToken 을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());

        // HttpEntity 를 하나 생성해, 헤더를 담아서 restTemplate 으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_SNS_USERINFO_URL, HttpMethod.GET,request,String.class);
        log.info("response.getHeaders() = " + response.getHeaders());
        log.info("responseEntity.getStatusCode()"+ response.getStatusCode());
        log.info("response.getBody() = " + response.getBody());
        return response;
    }

    // 다시 JSON 형식의 응답 객체를 deserialization 해서 자바 객체에 담음
    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) throws JsonProcessingException {
        log.info("response.getBody() = "+userInfoResponse.getBody());
        GoogleUser googleUser = objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
        return googleUser;
    }
}
