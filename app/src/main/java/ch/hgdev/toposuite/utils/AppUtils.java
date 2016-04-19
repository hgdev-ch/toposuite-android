package ch.hgdev.toposuite.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
        private static final Map<Integer, Permission> lookup = new HashMap<Integer, Permission>();

        static {
            for (Permission p : EnumSet.allOf(Permission.class))
                lookup.put(p.value, p);
        }

        private Permission(int value) {
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
     *
     * The user need to override onRequestPermissionsResult() to handle the user's answer to the
     * request. In other words, the calling activity needs to implement
     * ActivityCompat.OnRequestPermissionsResultCallback.
     *
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
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
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
     * @param activity The activity from which this method is called.
     * @param permission The permission to check against.
     * @return True of the permission has been granted.
     */
    public static boolean isPermissionGranted(final @NonNull Activity activity, final Permission permission) {
        return ContextCompat.checkSelfPermission(activity, permission.name) == PackageManager.PERMISSION_GRANTED;
    }
}
