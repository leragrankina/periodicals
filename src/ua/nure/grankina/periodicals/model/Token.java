package ua.nure.grankina.periodicals.model;

import ua.nure.grankina.periodicals.model.security.Security;

/**
 * Class representing remember-me token
 *
 * Created by Valeriia on 09.01.2017.
 */
public class Token {
    private String value;

    public String getValue() {
        return value;
    }

    public Token(int len){
        value = generate(len);
    }

    private static String generate(int len){
        return Security.getRandomString(len);
    }

    @Override
    public String toString() {
        return value;
    }
}
