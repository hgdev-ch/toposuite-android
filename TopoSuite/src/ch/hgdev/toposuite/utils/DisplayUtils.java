package ch.hgdev.toposuite.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.points.Point;

/**
 * Utility functions to format things to display.
 * 
 * @author HGdev
 * 
 */
public class DisplayUtils {

    /**
     * Convert a value of type int to a String.
     * 
     * @param value
     *            The value to convert to a String.
     * @return Value as a String.
     */
    public static String toString(int value) {
        return Integer.toString(value);
    }

    /**
     * Convert a value of type double to a String according to a given number of
     * decimals.
     * 
     * TODO check rounding mode of String.format
     * 
     * @param value
     *            The value to convert to a String.
     * @param precision
     *            The number of decimal. (eg. "%.1f")
     * @return Value as a String.
     */
    public static String toString(double value, String precision) {
        return String.format(precision, value);
    }

    /**
     * Convert a value of type double to a String according to the number of
     * decimals to display which are set in the application settings.
     * 
     * TODO check rounding mode of String.format
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
     * Format a date using the global date format defined in
     * {@link App#dateFormat}
     * 
     * @param d
     *            a date
     * @return a formatted date
     */
    public static String formatDate(Date d) {
        SimpleDateFormat df = new SimpleDateFormat(App.dateFormat, App.locale);
        return df.format(d);
    }

    /**
     * Convert dp to pixels
     * 
     * @param dp
     *            the number of dp
     * @return the number of pixels
     */
    public static int dpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * Format a point in order to display it in a TextView.
     * 
     * @param context
     *            the context
     * @param pt
     *            a Point
     * @return formatted Point
     */
    public static String formatPoint(Context context, Point pt) {
        return String.format("%s: %s, %s: %s, %s: %s", context.getString(R.string.east),
                DisplayUtils.toString(pt.getEast()), context.getString(R.string.north),
                DisplayUtils.toString(pt.getNorth()), context.getString(R.string.altitude),
                DisplayUtils.toString(pt.getAltitude()));
    }

    /**
     * Format a 2D point in order to display it in a TextView.
     * 
     * @param context
     *            the context
     * @param pt
     *            a 2D Point (altitude = 0.0)
     * @return formatted Point
     */
    public static String format2DPoint(Context context, Point pt) {
        return String.format("%s: %s, %s: %s", context.getString(R.string.east),
                DisplayUtils.toString(pt.getEast()), context.getString(R.string.north),
                DisplayUtils.toString(pt.getNorth()));
    }

    /**
     * Format given centimeters in order to display them in a TextView.
     * 
     * @param cm
     *            centimeters.
     * @return formatted centimeters.
     */
    public static String formatDifferences(double cm) {
        return String.format("%.1f", cm);
    }
}