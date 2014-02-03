package ch.hgdev.toposuite.utils;

import android.content.Context;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;

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
     * @return Value as a String.
     */
    public static String toString(double value) {
        return String.format(App.numberOfDecimals, value);
    }

    /**
     * Convert a value of type boolean to a string. If value is true, if will
     * return "yes" in the appropriate language, otherwise "no" in the
     * appropriate language.
     * 
     * @param context
     *            Calling activity.
     * @param value
     *            The value to convert to a String.
     * @return Value as a String.
     */
    public static String toString(Context context, boolean value) {
        return value ? context.getString(R.string.yes) : context.getString(R.string.no);
    }
}
