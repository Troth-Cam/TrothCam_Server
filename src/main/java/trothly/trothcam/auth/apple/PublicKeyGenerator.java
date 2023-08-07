package trothly.trothcam.auth.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import trothly.trothcam.dto.auth.apple.ApplePublicKey;
import trothly.trothcam.dto.auth.apple.ApplePublicKeys;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PublicKeyGenerator {

    private static final String SIGN_ALGORITHM_HEADER_KEY = "alg";
    private static final String KEY_ID_HEADER_KEY = "kid";
    private static final int POSITIVE_SIGN_NUMBER = 1;

    @Value("${oauth.apple.iss}")
    private String iss;

    @Value("${oauth.apple.client-id}")
    private String clientId;

    @Value("${oauth.apple.key.path}")
    private String path;

    // Public Key 생성
    public PublicKey generatePublicKey(Map<String, String> headers, ApplePublicKeys applePublicKeys) {
        ApplePublicKey applePublicKey =
                applePublicKeys.getMatchesKey(headers.get(SIGN_ALGORITHM_HEADER_KEY), headers.get(KEY_ID_HEADER_KEY));

        log.info("publickey : " + generatePublicKeyWithApplePublicKey(applePublicKey).toString());
        return generatePublicKeyWithApplePublicKey(applePublicKey);
    }

    // Public Key 속 정보로 Public Key 생성
    private PublicKey generatePublicKeyWithApplePublicKey(ApplePublicKey publicKey) {
        byte[] nBytes = Base64Utils.decodeFromUrlSafeString(publicKey.getN());
        byte[] eBytes = Base64Utils.decodeFromUrlSafeString(publicKey.getE());

        BigInteger n = new BigInteger(POSITIVE_SIGN_NUMBER, nBytes);
        BigInteger e = new BigInteger(POSITIVE_SIGN_NUMBER, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.getKty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new IllegalStateException("Apple OAuth 로그인 중 public key 생성에 문제가 발생했습니다.");
        }
    }

    // Client Secret 발급
    public String createClientSecret() throws IOException {
        // 애플에서 유효기간 최대 30일 권고
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

        try {
            return Jwts.builder()
                    .setHeaderParam(KEY_ID_HEADER_KEY, "9LA3NLSR6X")
                    .setHeaderParam(SIGN_ALGORITHM_HEADER_KEY, "ES256")
                    .setIssuer("5JQS3FU5R6")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience(iss)
                    .setSubject(clientId)
                    .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                    .compact();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path); // Auth.p8 파일의 경로에 맞게 수정
        String privateKey = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}
