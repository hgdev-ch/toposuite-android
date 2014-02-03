package ch.hgdev.toposuite.utils;

import ch.hgdev.toposuite.App;

/**
 * Utility functions to format things to display.
 * 
 * @author HGdev
 * 
 */
public class DisplayUtils {

    /**
     * Convert a value of type double to a String according to the number of
     * decimals to display which are set in the application settings.
     * 
     * @param value
     *            The value to convert to a String.
     */
    public static String toString(double value) {
        return String.format(App.numberOfDecimals, value);
    }

}
