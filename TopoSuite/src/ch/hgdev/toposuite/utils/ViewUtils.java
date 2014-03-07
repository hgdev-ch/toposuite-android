package ch.hgdev.toposuite.utils;

import android.widget.EditText;

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
}
