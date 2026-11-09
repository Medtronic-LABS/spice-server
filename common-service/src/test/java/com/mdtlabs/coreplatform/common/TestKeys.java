package com.mdtlabs.coreplatform.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

/**
 * Throwaway RSA keypair and matching token, generated per test run so no key material is committed.
 */
public final class TestKeys {

    /** {@code file:} URL of the generated PKCS#8 private key, for the filter's private-key property. */
    public static final String PRIVATE_KEY_URI;

    /** Encrypted token for superuser@test.com that the generated key can decrypt. */
    public static final String JWT_TOKEN;

    private static final String USER_DATA = """
            {"accountLocked":false,"lastName":"user","country":{"unitMeasurement":"unitMeasurement",
            "countryCode":"+199","name":"India","id":1},"gender":"Male","timezone":{"offset":"+05:30",
            "description":"(UTC+05:30) Chennai, Kolkata, Mumbai, New Delhi","id":1},
            "roles":[{"name":"PROVIDER","id":3},{"name":"SUPER_USER","id":1}],"isBlocked":false,"active":true,
            "firstName":"test","phoneNumber":"0000000000","organizations":[{"active":true,"deleted":false,
            "formName":"country","tenantId":1,"name":"India","formDataId":1,"id":1}],"tenantId":1,
            "accountExpired":false,"middleName":"m","isSuperUser":true,"id":1,"credentialsExpired":false,
            "username":"superuser@test.com"}""";

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            Path file = Files.createTempFile("test-private-key", ".der");
            file.toFile().deleteOnExit();
            Files.write(file, keyPair.getPrivate().getEncoded());
            PRIVATE_KEY_URI = file.toUri().toString();

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .claim("userData", USER_DATA)
                    .claim("tenantId", List.of(1))
                    .build();
            EncryptedJWT jwt = new EncryptedJWT(new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM),
                    claims);
            jwt.encrypt(new RSAEncrypter((RSAPublicKey) keyPair.getPublic()));
            JWT_TOKEN = jwt.serialize();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private TestKeys() {
    }
}
