package com.project.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrytedPasswordUtils {

    // Encryte Password with BCryptPasswordEncoder
	private static BCryptPasswordEncoder passwordEcorder = new BCryptPasswordEncoder();

    public static String encrytePassword(String password) {
        return passwordEcorder.encode(password);
    }
    
    public static Boolean verifyPassword(String rawPassword,String encodedPassword) {
        return passwordEcorder.matches(rawPassword, encodedPassword);
     }
}
