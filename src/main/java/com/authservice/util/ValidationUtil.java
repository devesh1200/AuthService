package com.authservice.util;

import com.authservice.model.UserInfoDto;

import java.util.regex.Pattern;

public class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class - instantiation not allowed");
    }

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static boolean isValidEmail(UserInfoDto userInfoDto) {
        if (userInfoDto == null || userInfoDto.getEmail() == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(userInfoDto.getEmail()).matches();
    }

    public static boolean isValidPassword(UserInfoDto userInfoDto) {
        if (userInfoDto == null || userInfoDto.getPassword() == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(userInfoDto.getPassword()).matches();
    }
}
