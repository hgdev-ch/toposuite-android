package ch.hgdev.toposuite.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.widget.EditText;
import ch.hgdev.toposuite.points.PointsManagerActivity;

/**
 * Useful functions for interacting with View object.
 * 
 * @author HGdev
 */
public class ViewUtils {

    /**
     * Convenient function for easily reading a double value from an EditText.
     * 
     * @param editText
     *            An EditText object
     * @return The value contained in the edit text as double.
     */
    public static double readDouble(EditText editText) {
        return ((editText != null) && (editText.length() > 0)) ? Double.parseDouble(
                editText.getText().toString()) : 0.0;
    }

    /**
     * Convenient function for easily reading a integer value from an EditText.
     * 
     * @param editText
     *            An EditText object
     * @return The value contained in the edit text as int.
     */
    public static int readInt(EditText editText) {
        return ((editText != null) && (editText.length() > 0)) ? Integer.parseInt(
                editText.getText().toString()) : 0;
    }

    /**
     * Start the Points Manager Activity ({@link PointsManagerActivity}).
     * 
     * @param currentActivity
     *            Activity that performs the redirection.
     */
    public static void redirectToPointsManagerActivity(Activity currentActivity) {
        Intent pointsManagerIntent = new Intent(
                currentActivity, PointsManagerActivity.class);
        currentActivity.startActivity(pointsManagerIntent);
    }

    /**
     * Convenient function for locking screen orientation.
     * 
     * @param currentActivity
     *            Activity that request the lock.
     */
    public static void lockScreenOrientation(Activity currentActivity) {
        currentActivity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    /**
     * Convenient function for unlocking screen orientation.
     * 
     * @param currentActivity
     *            Activity that request the unlock.
     */
    public static void unlockScreenOrientation(Activity currentActivity) {
        currentActivity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}
