package com.mdtlabs.coreplatform.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Throwaway RSA keypair generated per test run so no key material is committed.
 */
public final class TestKeys {

    /** {@code file:} URL of the generated PKCS#8 private key. */
    public static final String PRIVATE_KEY_URI;

    /** {@code file:} URL of the generated X.509 public key. */
    public static final String PUBLIC_KEY_URI;

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            PRIVATE_KEY_URI = write("test-private-key", keyPair.getPrivate().getEncoded());
            PUBLIC_KEY_URI = write("test-public-key", keyPair.getPublic().getEncoded());
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static String write(String prefix, byte[] encoded) throws java.io.IOException {
        Path file = Files.createTempFile(prefix, ".der");
        file.toFile().deleteOnExit();
        Files.write(file, encoded);
        return file.toUri().toString();
    }

    private TestKeys() {
    }
}
