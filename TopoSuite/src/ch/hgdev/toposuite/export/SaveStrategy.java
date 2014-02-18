package ch.hgdev.toposuite.export;

import java.io.IOException;

import android.content.Context;

/**
 * Interface implementing the Strategy design pattern in order to provide an
 * easy way to save an object into a file.
 * 
 * @author HGdev
 */
public interface SaveStrategy {

    /**
     * Save the content of the object into a file identified by its path.
     * 
     * @param context
     *            The current Android Context.
     * @param filename
     *            The file name.
     * @return The number of line written in the target file.
     */
    int saveAsCSV(Context context, String filename) throws IOException;
}
