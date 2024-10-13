package com.example.drsystemserver.util;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void testGenerateSalt() {
        byte[] salt1 = PasswordUtil.generateSalt();
        byte[] salt2 = PasswordUtil.generateSalt();

        assertNotNull(salt1);
        assertNotNull(salt2);
        assertEquals(16, salt1.length);
        assertEquals(16, salt2.length);
        assertNotEquals(salt1, salt2, "Generated salts should not be the same");
    }

    @Test
    void testHashPassword() throws NoSuchAlgorithmException {
        String password = "mypassword";
        byte[] salt = PasswordUtil.generateSalt();
        byte[] hashedPassword = PasswordUtil.hashPassword(password, salt);

        assertNotNull(hashedPassword);
        assertEquals(32, hashedPassword.length, "Hash should be 32 bytes long");
    }

    @Test
    void testVerifyPasswordSuccessful() throws NoSuchAlgorithmException {
        String password = "mypassword";
        byte[] salt = PasswordUtil.generateSalt();
        byte[] hashedPassword = PasswordUtil.hashPassword(password, salt);

        boolean isValid = PasswordUtil.verifyPassword(password, salt, hashedPassword);
        assertTrue(isValid, "Password verification should succeed with correct password");
    }

    @Test
    void testVerifyPasswordFailure() throws NoSuchAlgorithmException {
        String password = "mypassword";
        String wrongPassword = "wrongpassword";
        byte[] salt = PasswordUtil.generateSalt();
        byte[] hashedPassword = PasswordUtil.hashPassword(password, salt);

        boolean isValid = PasswordUtil.verifyPassword(wrongPassword, salt, hashedPassword);
        assertFalse(isValid, "Password verification should fail with incorrect password");
    }

    @Test
    void testHashPasswordWithNullSalt() {
        String password = "mypassword";
        
        assertThrows(NullPointerException.class, () -> {
            PasswordUtil.hashPassword(password, null);
        }, "Hashing with null salt should throw NullPointerException");
    }

    @Test
    void testVerifyPasswordWithNullParameters() {
        String password = "mypassword";
        
        assertThrows(NullPointerException.class, () -> {
            PasswordUtil.verifyPassword(password, null, null);
        }, "Password verification with null parameters should throw NullPointerException");
    }
}
