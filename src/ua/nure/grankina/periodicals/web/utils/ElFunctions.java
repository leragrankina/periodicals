package ua.nure.grankina.periodicals.web.utils;

import java.util.regex.Pattern;

/**
 * Class containing EL custom functions
 *
 * Created by Valeriia on 03.01.2017.
 */
public class ElFunctions {
    public static String replaceAll(String input, String regex, String replacement){
        return Pattern.compile("(" + regex + ")", Pattern.CASE_INSENSITIVE).matcher(input).replaceAll(replacement);
    }

}
