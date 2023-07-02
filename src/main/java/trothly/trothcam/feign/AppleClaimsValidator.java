package trothly.trothcam.feign;

import io.jsonwebtoken.Claims;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleClaimsValidator {
    private final String iss;
    private final String clientId;

    public AppleClaimsValidator(
            @Value("${oauth.apple.iss}") String iss,
            @Value("${oauth.apple.client-id}") String clientId
    ) {
        this.iss = iss;
        this.clientId = clientId;
    }

    public boolean isValid(Claims claims) {
        return claims.getIssuer().contains(iss) &&
                claims.getAudience().equals(clientId);
    }
}

