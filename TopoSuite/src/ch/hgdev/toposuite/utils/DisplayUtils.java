package ch.hgdev.toposuite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    /**
     * Format a date using the global date format defined in {@link App#dateFormat}
     * @param d
     *            a date
     * @return a formatted date
     */
    public static String formatDate(Date d) {
       SimpleDateFormat df = new SimpleDateFormat(App.dateFormat);    
       return df.format(d);
    }
    
    /**
     * Convert dp to pixels
     * @param dp
     *            the number of dp
     * @return the number of pixels
     */
    public static int dpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}