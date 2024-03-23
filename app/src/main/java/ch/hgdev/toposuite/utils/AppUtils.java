package ch.hgdev.toposuite.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;

/**
 * Provide application specific utilities such a method to provide the
 * application string.
 *
 * @author HGdev
 */
public class AppUtils {
    /**
     * Return the current year.
     *
     * @return Year as an integer.
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Return the application name.
     *
     * @return Application name.
     */
    public static String getAppName() {
        return App.getContext().getString(R.string.app_name);
    }

    /**
     * Get current application version name.
     *
     * @return String version of the application.
     */
    public static String getVersionName() {
        String version = "?";
        try {
            PackageInfo pi = App.getContext().getPackageManager()
                    .getPackageInfo(App.getContext().getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.log(Logger.ErrLabel.RESOURCE_NOT_FOUND, "Application name version" + e);
        }
        return version;
    }

    /**
     * Get current application version code.
     *
     * @return String code version number of the application.
     */
    public static String getVersionCode() {
        String version = "?";
        try {
            PackageInfo pi = App.getContext().getPackageManager()
                    .getPackageInfo(App.getContext().getPackageName(), 0);
            version = String.valueOf(pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.log(Logger.ErrLabel.RESOURCE_NOT_FOUND, "Application code version " + e);
        }
        return version;
    }

    public static String serializeDate(final @NonNull Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat(App.ISO8601_DATE_FORMAT, Locale.US);
        return sdf.format(d);
    }

    /**
     * Attempt to parse a date. At first, attempt to parse ISO8601 formatted date. If this fails,
     * fallback to parsing using the old date format. If it still fails, the returned value is the
     * date of EPOCH. This function is meant to be used when deserializing dates, typically from
     * JSON files. Hence, it does not care about locale.
     *
     * @param date A date formatted as {@link App#ISO8601_DATE_FORMAT} or {@link App#DATE_FORMAT}.
     * @return A new date object corresponding to the date string.
     */
    public static Date parseSerializedDate(final @NonNull String date) {
        Date d;
        try {
            // try ISO 8601 date first
            SimpleDateFormat sdfISO = new SimpleDateFormat(App.ISO8601_DATE_FORMAT, Locale.US);
            d = sdfISO.parse(date);
        } catch (ParseException e) {
            try {
                Logger.log(Logger.WarnLabel.PARSE_ERROR, "old date format (" + date + ")");
                // well, try old format ten
                SimpleDateFormat sdf = new SimpleDateFormat(App.DATE_FORMAT, Locale.US);
                d = sdf.parse(date);
            } catch (ParseException p) {
                // meh, use default EPOCH then
                Logger.log(Logger.ErrLabel.PARSE_ERROR, p.getMessage());
                d = new Date(0);
            }
        }
        return d;
    }

    /**
     * Return path to public data directory used by Toposuite. If the path does not yet exist, this
     * function creates the directory.
     *
     * @return Path to the public data directory or null.
     */
    public static String publicDataDirectory() {
        File dir = new File(App.publicDataDirectory);
        if (dir.exists()) {
            return dir.getAbsolutePath();
        }
        if (dir.mkdirs()) {
            return dir.getAbsolutePath();
        }
        return null;
    }
}
