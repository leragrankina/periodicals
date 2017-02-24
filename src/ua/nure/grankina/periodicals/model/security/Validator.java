package ua.nure.grankina.periodicals.model.security;

import ua.nure.grankina.periodicals.model.mail.Email;

/**
 * Validators go here
 *
 * Created by Valeriia on 08.01.2017.
 */
public class Validator {
    public static boolean isNumeric(String s){
        return s.matches("-?[0-9]+(\\.[0-9]{1,2})?");
    }

    public static boolean isNotNumeric(String s){
        return !isNumeric(s);
    }

    public static boolean isLessMinValue(double value, double min){
        return value < min;
    }

    public static boolean isMoreMaxValue(double value, double max){
        return value > max;
    }

    public static boolean isValueInRange(double value, double min, double max){
        return !(isLessMinValue(value, min) || isMoreMaxValue(value, max));
    }

    public static boolean isLessMinLength(String s, int len){
        return s.length() < len;
    }

    public static boolean isMoreMaxLength(String s, int len){
        return s.length() >= len;
    }

    public static boolean doesNotMatch(String s1, String s2){
        return !s1.equals(s2);
    }

    public static boolean containsDigitsOnly(String s){
        for (int i = 0; i < s.length(); i++){
            if (!Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean isEmailValid(String email){
        return Email.isEmailValid(email);
    }

}
