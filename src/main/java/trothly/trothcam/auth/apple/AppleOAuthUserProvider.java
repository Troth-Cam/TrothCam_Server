package trothly.trothcam.auth.apple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import trothly.trothcam.dto.auth.apple.AppleInfo;
import trothly.trothcam.dto.auth.apple.ApplePublicKeys;
import trothly.trothcam.dto.auth.apple.AppleToken;
import trothly.trothcam.exception.custom.InvalidTokenException;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleOAuthUserProvider {

    private final AppleJwtParser appleJwtParser;
    private final AppleClient appleClient;
    private final PublicKeyGenerator publicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;

    @Value("${oauth.apple.client-id}")
    private String clientId;

    public AppleInfo getAppleInfoFromToken(String identityToken) {
        Map<String, String> headers = appleJwtParser.parseHeaders(identityToken);
        ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();

        PublicKey publicKey = publicKeyGenerator.generatePublicKey(headers, applePublicKeys);

        Claims claims = appleJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
        log.info("claims : " + claims.toString());
        validateClaims(claims);

        String email = claims.get("email", String.class);
        String sub = claims.get("sub", String.class);
        log.info("email : " + email + "\nsub : " + sub);

        return new AppleInfo(email, sub);
    }

    private void validateClaims(Claims claims) {
        if (!appleClaimsValidator.isValid(claims)) {
            throw new InvalidTokenException("Apple OAuth Claims 값이 올바르지 않습니다.");
        }
    }

    // authorization code로 token 발급
    public AppleToken.Response getToken(String authorizationCode, String clientSecret) {
        AppleToken.Request request = AppleToken.Request.of(authorizationCode, clientId, clientSecret, "authorization_code");
        return appleClient.getToken(request);
    }
}
