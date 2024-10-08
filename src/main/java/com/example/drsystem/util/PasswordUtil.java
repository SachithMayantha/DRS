package com.example.drsystem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {
    private static final int SALT_LENGTH = 16; // in bytes
    private static final int ITERATIONS = 10000; // iterations for PBKDF2

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());
        
        // Perform PBKDF2
        for (int i = 0; i < ITERATIONS; i++) {
            digest.reset();
            hash = digest.digest(hash);
        }
        
        return hash;
    }

    public static boolean verifyPassword(String password, byte[] salt, byte[] expectedHash) throws NoSuchAlgorithmException {
        byte[] hash = hashPassword(password, salt);
        return MessageDigest.isEqual(hash, expectedHash);
    }
}
