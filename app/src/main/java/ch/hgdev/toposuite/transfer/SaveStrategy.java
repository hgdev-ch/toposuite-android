package ch.hgdev.toposuite.transfer;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Interface implementing the Strategy design pattern in order to provide an
 * easy way to save an object into a file.
 *
 * @author HGdev
 */
public interface SaveStrategy {

    /**
     * Save the content of the object into a file stored in the default app
     * directory.
     *
     * @param context  The current Android Context.
     * @param filename The file name.
     * @return The number of line written in the target file.
     */
    int saveAsCSV(Context context, String filename) throws IOException;

    /**
     * Save the content of the object into a file identified by its path.
     *
     * @param context  The current Android Context.
     * @param path     The path where to store the file.
     * @param filename The file name.
     * @return The number of line written in the target file.
     */
    int saveAsCSV(Context context, String path, String filename) throws IOException;

    /**
     * Save the content of the object into a file identified by its output
     * stream.
     *
     * @param context      The current Android Context.
     * @param outputStream An opened output stream. This method must close the output
     *                     stream.
     * @return The number of line written in the target file.
     */
    int saveAsCSV(Context context, FileOutputStream outputStream) throws IOException;

    /**
     * @param context The Current Android Context.
     * @param file    The file to which to save
     * @return The number of lines written in the target file.
     */
    int saveAsCSV(Context context, File file) throws IOException;
}
