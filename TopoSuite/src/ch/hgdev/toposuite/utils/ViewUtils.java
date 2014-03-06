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
     * @return
     */
    public static double readDouble(EditText editText) {
        return ((editText != null) && (editText.length() > 0)) ? Double.parseDouble(
                editText.getText().toString()) : 0.0;
    }
}
