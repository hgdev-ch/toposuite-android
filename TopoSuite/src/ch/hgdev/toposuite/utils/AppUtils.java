package ch.hgdev.toposuite.utils;

import java.util.Calendar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;

/**
 * Provide application specific utilities such a method to provide the
 * application string.
 * 
 * @author HGdev
 * 
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
     * Get current application version number.
     * 
     * @return String version of the application.
     */
    public static String getVersionNumber() {
        String version = "?";
        try {
            PackageInfo pi = App.getContext().getPackageManager()
                    .getPackageInfo(App.getContext().getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Logger.TOPOSUITE_RESSOURCE_NOT_FOUND, "Application name", e);
        }
        return version;
    }
}
