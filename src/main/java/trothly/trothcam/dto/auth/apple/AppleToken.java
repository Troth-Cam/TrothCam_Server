package trothly.trothcam.dto.auth.apple;

import lombok.Getter;
import lombok.Setter;

public class AppleToken {
    @Setter
    public static class Request {
        private String code;
        private String client_id;
        private String client_secret;
        private String grant_type;

        public static Request of(String code, String clientId, String clientSecret, String grantType) {
            Request request = new Request();
            request.code = code;
            request.client_id = clientId;
            request.client_secret = clientSecret;
            request.grant_type = grantType;
            return request;
        }
    }

    @Setter
    @Getter
    public static class Response {
        private String access_token;
        private String expires_in;
        private String id_token;
        private String refresh_token;
        private String token_type;
        private String error;
    }
}
