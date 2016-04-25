package ch.hgdev.toposuite.utils;

import android.support.annotation.NonNull;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Useful static method for manipulating String.
 *
 * @author HGdev
 */
public class StringUtils {
    /**
     * This method assumes that the String contains a number.
     *
     * @param str A String.
     * @return The String incremented by 1.
     * @throws IllegalArgumentException Thrown if the input String does not end with a suitable
     *                                  number.
     */
    public static String incrementAsNumber(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("The input String must not be null!");
        }

        Pattern p = Pattern.compile("([0-9]+)$");
        Matcher m = p.matcher(str);

        if (!m.find()) {
            throw new IllegalArgumentException(
                    "Invalid input argument! The input String must end with a valid number");
        }

        String number = m.group(1);
        String prefix = str.substring(0, str.length() - number.length());

        return prefix + String.valueOf(Integer.valueOf(number) + 1);
    }

    /**
     * Tentatively onvert all non ASCII code from string str to their equivalent ASCII value when
     * possible or to the empty string otherwise.
     * For instance, "rösti" will be converted to "rosti".
     *
     * @param str Input string that needs to be "converted" to ASCII chars only.
     * @return An ASCII representation of str.
     */
    public static String toASCII(@NonNull String str) {
        String ret = Normalizer.normalize(str, Normalizer.Form.NFD);
        return ret.replaceAll("[^\\x00-\\x7F]", "");
    }
}
