package trothly.trothcam.auth.apple;

import io.jsonwebtoken.Claims;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppleClaimsValidator {
    private final String iss;
    private final String clientId;

    // iss = apple 개발자 계정 team id (발급자)
    // client-id = app bundle id
    public AppleClaimsValidator(
            @Value("${oauth.apple.iss}") String iss,
            @Value("${oauth.apple.client-id}") String clientId
    ) {
        this.iss = iss;
        this.clientId = clientId;
    }

    public boolean isValid(Claims claims) {
        log.info("Apple 서버에서 받아와 저장한 iss : " + claims.getIssuer());
        log.info("Apple 서버에서 받아와 저장한 aud : " + claims.getAudience());

        log.info("application-local에 저장한 iss : " + iss + "\napplication-local에 저장한 clientId : " + clientId);
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId);
    }
}

