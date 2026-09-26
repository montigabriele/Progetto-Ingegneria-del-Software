package it.foodmood.utils;

import java.util.regex.Pattern;

public final class Validator {
    
    private Validator() {}

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    public static boolean isValidEmail(String email){
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(char[] password){
        return password != null && PASSWORD_PATTERN.matcher(new String(password)).matches();
    }

}
