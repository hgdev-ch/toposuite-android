package ch.hgdev.toposuite.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.SparseArray;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
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
     * App defined constants to use for requesting permissions.
     */
    public enum Permission {
        // calendar
        READ_CALENDAR(0x100),
        WRITE_CALENDAR(0x101),

        // camera
        CAMERA(0x200),

        // contacts
        READ_CONTACTS(0x300),
        WRITE_CONTACTS(0x301),
        GET_ACCOUNTS(0x302),

        // location
        ACCESS_FINE_LOCATION(0x400),
        ACCESS_COARSE_LOCATION(0x401),

        // microphone
        RECORD_AUDIO(0x500),

        // phone
        READ_PHONE_STATE(0x600),
        CALL_PHONE(0x601),
        READ_CALL_LOG(0x602),
        WRITE_CALL_LOG(0x603),
        ADD_VOICEMAIL(0x604),
        USE_SIP(0x605),
        PROCESS_OUTGOING_CALLS(0x606),

        // sensors
        BODY_SENSORS(0x700),

        // sms
        SEND_SMS(0x800),
        RECEIVE_SMS(0x801),
        READ_SMS(0x802),
        RECEIVE_WAP_PUSH(0x803),
        RECEIVE_MMS(0x804),

        // storage
        READ_EXTERNAL_STORAGE(0x900),
        WRITE_EXTERNAL_STORAGE(0x901);

        private final int value;
        private final String name;
        private static final SparseArray<Permission> lookup = new SparseArray<Permission>();

        static {
            for (Permission p : EnumSet.allOf(Permission.class))
                lookup.put(p.value, p);
        }

        Permission(int value) {
            this.value = value;
            switch (value) {
                case 0x100:
                    name = android.Manifest.permission.READ_CALENDAR;
                    break;
                case 0x101:
                    name = android.Manifest.permission.WRITE_CALENDAR;
                    break;
                case 0x200:
                    name = android.Manifest.permission.CAMERA;
                    break;
                case 0x300:
                    name = android.Manifest.permission.READ_CONTACTS;
                    break;
                case 0x301:
                    name = android.Manifest.permission.WRITE_CONTACTS;
                    break;
                case 0x302:
                    name = android.Manifest.permission.GET_ACCOUNTS;
                    break;
                case 0x400:
                    name = android.Manifest.permission.ACCESS_FINE_LOCATION;
                    break;
                case 0x401:
                    name = android.Manifest.permission.ACCESS_COARSE_LOCATION;
                    break;
                case 0x500:
                    name = android.Manifest.permission.RECORD_AUDIO;
                    break;
                case 0x600:
                    name = android.Manifest.permission.READ_PHONE_STATE;
                    break;
                case 0x601:
                    name = android.Manifest.permission.CALL_PHONE;
                    break;
                case 0x602:
                    name = android.Manifest.permission.READ_CALL_LOG;
                    break;
                case 0x603:
                    name = android.Manifest.permission.WRITE_CALL_LOG;
                    break;
                case 0x604:
                    name = android.Manifest.permission.ADD_VOICEMAIL;
                    break;
                case 0x605:
                    name = android.Manifest.permission.USE_SIP;
                    break;
                case 0x606:
                    name = android.Manifest.permission.PROCESS_OUTGOING_CALLS;
                    break;
                case 0x700:
                    name = android.Manifest.permission.BODY_SENSORS;
                    break;
                case 0x800:
                    name = android.Manifest.permission.SEND_SMS;
                    break;
                case 0x801:
                    name = android.Manifest.permission.RECEIVE_SMS;
                    break;
                case 0x802:
                    name = android.Manifest.permission.READ_SMS;
                    break;
                case 0x803:
                    name = android.Manifest.permission.RECEIVE_WAP_PUSH;
                    break;
                case 0x804:
                    name = android.Manifest.permission.RECEIVE_MMS;
                    break;
                case 0x900:
                    name = android.Manifest.permission.READ_EXTERNAL_STORAGE;
                    break;
                case 0x901:
                    name = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    break;
                default:
                    name = ""; // this cannot happen anyway
            }
        }

        public static Permission valueOf(int value) {
            return lookup.get(value);
        }
    }

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

    /**
     * Helper function to ask the user for permission. On API < 23, this does nothing. On API > 23,
     * the user is dialog to ask for the permission only if it has not already been granted.
     * <p>
     * The user need to override onRequestPermissionsResult() to handle the user's answer to the
     * request. In other words, the calling activity needs to implement
     * ActivityCompat.OnRequestPermissionsResultCallback.
     * <p>
     * The activity is terminated if the user refuses to give permission.
     *
     * @param activity   The activity that needs to get the permission. It needs to implement
     *                   ActivityCompat.OnRequestPermissionsResultCallback to handle user response.
     * @param permission The permission to be requested to the user.
     * @param message    A short message explaining the user why this permission is required.
     */
    public static void requestPermission(final @NonNull Activity activity, final Permission permission, final @NonNull String message) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.name)) {
            new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{permission.name}, permission.value);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission.name}, permission.value);
        }
    }

    /**
     * Allows to verify if the specified permission has been granted.
     *
     * @param activity   The activity from which this method is called.
     * @param permission The permission to check against.
     * @return True of the permission has been granted.
     */
    public static boolean isPermissionGranted(final @NonNull Activity activity, final Permission permission) {
        return ContextCompat.checkSelfPermission(activity, permission.name) == PackageManager.PERMISSION_GRANTED;
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
     * Return path to public data directory used by Toposuite. The path is only returned if it
     * exists and that the application has at least read access to it.
     * If write external storage permission is granted and the path does not yet exist,
     * this function creates the directory.
     *
     * @param activity Calling activity.
     * @return Path to the public data directory or null.
     */
    public static String publicDataDirectory(@NonNull Activity activity) {
        if (AppUtils.isPermissionGranted(activity, Permission.READ_EXTERNAL_STORAGE)) {
            File dir = new File(App.publicDataDirectory);
            if (dir.exists()) {
                return dir.getAbsolutePath();
            }
            if (AppUtils.isPermissionGranted(activity, Permission.WRITE_EXTERNAL_STORAGE)) {
                if (dir.mkdirs()) {
                    return dir.getAbsolutePath();
                }
            }
        }
        return null;
    }
}
