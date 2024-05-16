package com.mdtlabs.coreplatform.common.util;


import com.mdtlabs.coreplatform.common.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 *   Utility class for generating unique hash-based codes from input strings.
 * </p>
 *
 * @author Yogeshwaran M created on Nov 17, 2023
 */
public class UniqueCodeGenerator {

    private static final Map<String, String> stringToCodeMap = new HashMap<>();

    private UniqueCodeGenerator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Generates a unique hash-based code for the given input string.
     *
     * @param input The input string for which a unique code is to be generated.
     * @return A unique hash-based code generated from the input string, or null if an algorithm is unavailable.
     * @author Yogeshwaran M created on Nov 17, 2023
     */
    public static String generateUniqueCode(String input) {
        if (stringToCodeMap.containsKey(input)) {
            return stringToCodeMap.get(input);
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(Constants.SHA_256);
            byte[] encodedHash = digest.digest(input.getBytes());
            String generatedCode = bytesToHex(encodedHash);
            stringToCodeMap.put(input, generatedCode);
            return generatedCode;
        } catch (NoSuchAlgorithmException e) {
            Logger.logError(Constants.NO_SUCH_ALGORITHM, e);
            return null;
        }
    }

    /**
     * Converts a byte array to a hexadecimal representation.
     *
     * @param hash The byte array to be converted.
     * @return The hexadecimal representation of the byte array.
     * @author Yogeshwaran M created on Nov 17, 2023
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
