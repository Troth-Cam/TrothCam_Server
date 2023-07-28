package trothly.trothcam.auth.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import trothly.trothcam.dto.auth.apple.AppleInfo;
import trothly.trothcam.dto.auth.apple.ApplePublicKeys;
import trothly.trothcam.exception.custom.InvalidTokenException;

import java.security.PublicKey;
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
}
