package ch.hgdev.toposuite.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Strings;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.points.Point;

/**
 * Utility functions to format things to display.
 *
 * @author HGdev
 */
public class DisplayUtils {

    /**
     * Double values may represent different type of data. They are enumerated
     * here and for each of them, the number of decimal to show may be
     * different.
     *
     * @author HGdev
     */
    private enum valueType {
        ANGLE,
        AVERAGE,
        CC,
        COORDINATE,
        DIFFERENCE,
        DISTANCE,
        GAP,
        SCALE_FACTOR,
        SURFACE
    }

    /**
     * Convert a value of type int to a String. This method is meant to be used
     * to set values in EditText views.
     *
     * @param value The value to convert to a String.
     * @return Value as a String.
     */
    @NonNull
    public static String toStringForEditText(int value) {
        if (MathUtils.isIgnorable(value)) {
            return "";
        }
        return Integer.toString(value);
    }

    /**
     * Convert a value of type double to a String according to the number of
     * decimals to display which are set in the application settings. This
     * method is meant to be used to set values in EditText views.
     *
     * @param value The value to convert to a String.
     * @return Value as a String.
     */
    @NonNull
    public static String toStringForEditText(double value) {
        if (MathUtils.isIgnorable(value)) {
            return "";
        }
        return String.format(Locale.US, App.numberOfDecimals, value);
    }

    /**
     * Format a string for a view, handling the case of null string.
     *
     * @param s A string to display oon a view.
     * @return Formatted string s.
     */
    @NonNull
    public static String format(@Nullable String s) {
        if (s == null) {
            return "-";
        }
        return s;
    }

    /**
     * Utility to format a string to be displayed.
     *
     * @param value The value to format.
     * @param type  The type of the value.
     * @return Value formatted according to type.
     */
    @NonNull
    private static String format(double value, @NonNull DisplayUtils.valueType type) {
        int precision;

        if (MathUtils.isIgnorable(value)) {
            return "-";
        } else {
            switch (type) {
                case COORDINATE:
                    precision = App.getDecimalPrecisionForCoordinate();
                    break;
                case ANGLE:
                    precision = App.getDecimalPrecisionForAngle();
                    break;
                case DIFFERENCE:
                    precision = App.getDecimalPrecisionForDifference();
                    break;
                case DISTANCE:
                    precision = App.getDecimalPrecisionForDistance();
                    break;
                case AVERAGE:
                    precision = App.getDecimalPrecisionForAverage();
                    break;
                case GAP:
                    precision = App.getDecimalPrecisionForGap();
                    break;
                case SCALE_FACTOR:
                    precision = App.getDecimalPrecisionForScaleFactor();
                    break;
                case SURFACE:
                    precision = App.getDecimalPrecisionForSurface();
                    break;
                case CC:
                    precision = App.getDecimalPrecisionForCC();
                    break;
                default:
                    Logger.log(Logger.ErrLabel.SETTINGS_ERROR, "unknown value type");
                    return "-";
            }
            String pattern = precision < 1 ? "#,##0" : "#,##0.";
            String decimalCount = Strings.repeat("0", precision);
            pattern += decimalCount;

            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(App.getLocale());
            DecimalFormat df = new DecimalFormat(pattern, symbols);
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(value);
        }
    }

    /**
     * Format a value of type coordinate.
     *
     * @param value Coordinate to format.
     * @return Formatted coordinate.
     */
    @NonNull
    public static String formatCoordinate(double value) {
        return DisplayUtils.format(value, valueType.COORDINATE);
    }

    /**
     * Format a value of type angle.
     *
     * @param value Angle to format.
     * @return Formatted angle.
     */
    @NonNull
    public static String formatAngle(double value) {
        return DisplayUtils.format(value, valueType.ANGLE);
    }

    /**
     * Format a value of type distance.
     *
     * @param value Distance to format.
     * @return Formatted distance.
     */
    @NonNull
    public static String formatDistance(double value) {
        return DisplayUtils.format(value, valueType.DISTANCE);
    }

    /**
     * Format a value of type average.
     *
     * @param value Average value to format.
     * @return Formatted average value.
     */
    @NonNull
    public static String formatAverage(double value) {
        return DisplayUtils.format(value, valueType.AVERAGE);
    }

    /**
     * Format a value of type gap.
     *
     * @param value Gap value to format.
     * @return Formatted gap value.
     */
    @NonNull
    public static String formatGap(double value) {
        return DisplayUtils.format(value, valueType.GAP);
    }

    /**
     * Format a value of type surface.
     *
     * @param value Surface value to format.
     * @return Formatted surface value.
     */
    @NonNull
    public static String formatSurface(double value) {
        return DisplayUtils.format(value, valueType.SURFACE);
    }

    /**
     * Format a scale factor.
     *
     * @param value scale factor to format.
     * @return Formatted scale factor.
     */
    @NonNull
    public static String formatScaleFactor(double value) {
        return DisplayUtils.format(value, valueType.SCALE_FACTOR);
    }

    /**
     * Format a value in CC (1/10000 Grad). This removes any decimal precision
     * and rounds the value half up. If the value appears to be either infinite
     * or NaN, a simple dash is returned.
     *
     * @param value Input value in CC.
     * @return Formatted CC value.
     */
    @NonNull
    public static String formatCC(double value) {
        return DisplayUtils.format(value, valueType.CC);
    }

    /**
     * Format given centimeters in order to display them in a TextView.
     *
     * @param cm centimeters.
     * @return formatted centimeters.
     */
    public static String formatDifferences(double cm) {
        return DisplayUtils.format(cm, valueType.DIFFERENCE);
    }

    /**
     * Convert a value of type boolean to a string. If value is true, if will
     * return "yes" in the appropriate language, otherwise "no" in the
     * appropriate language.
     *
     * @param context Calling activity.
     * @param value   The value to convert to a String.
     * @return Value as a String.
     */
    public static String toString(@NonNull Context context, boolean value) {
        return value ? context.getString(R.string.yes) : context.getString(R.string.no);
    }

    /**
     * Format a date using the global date format defined in
     * {@link App#DATE_FORMAT}
     *
     * @param d a date
     * @return a formatted date
     */
    @NonNull
    public static String formatDate(@Nullable Date d) {
        if (d == null) {
            return "-";
        }
        SimpleDateFormat df = new SimpleDateFormat(App.DATE_FORMAT, App.getLocale());
        return df.format(d);
    }

    /**
     * Format a date using the global date format defined in
     * {@link App#DATE_FORMAT}
     *
     * @param d a date
     * @return a formatted date
     */
    @NonNull
    public static String formatDate(long d) {
        if (MathUtils.isIgnorable(d)) {
            return "-";
        }
        SimpleDateFormat df = new SimpleDateFormat(App.DATE_FORMAT, App.getLocale());
        return df.format(d);
    }

    /**
     * Convert dp to pixels
     *
     * @param dp the number of dp
     * @return the number of pixels
     */
    public static int dpToPx(@NonNull Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    /**
     * Format a point in order to display it in a TextView.
     *
     * @param context the context
     * @param pt      a Point
     * @return formatted Point
     */
    @NonNull
    public static String formatPoint(@NonNull Context context, Point pt) {
        if (pt == null) {
            return String.format("%s: -, %s: -, %s: -",
                    context.getString(R.string.east),
                    context.getString(R.string.north),
                    context.getString(R.string.altitude));
        }
        return String.format("%s: %s, %s: %s, %s: %s", context.getString(R.string.east),
                DisplayUtils.formatCoordinate(pt.getEast()), context.getString(R.string.north),
                DisplayUtils.formatCoordinate(pt.getNorth()),
                context.getString(R.string.altitude),
                DisplayUtils.formatCoordinate(pt.getAltitude()));
    }

    /**
     * Format a 2D point in order to display it in a TextView.
     *
     * @param context the context
     * @param pt      a 2D Point (altitude = 0.0)
     * @return formatted Point
     */
    @NonNull
    public static String format2DPoint(@NonNull Context context, Point pt) {
        if (pt == null) {
            return String.format("%s: -, %s: -",
                    context.getString(R.string.east),
                    context.getString(R.string.north));
        }
        return String.format("%s: %s, %s: %s", context.getString(R.string.east),
                DisplayUtils.formatCoordinate(pt.getEast()), context.getString(R.string.north),
                DisplayUtils.formatCoordinate(pt.getNorth()));
    }
}