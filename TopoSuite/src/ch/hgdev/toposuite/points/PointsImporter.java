package ch.hgdev.toposuite.points;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.hgdev.toposuite.SharedResources;

public class PointsImporter {

    /**
     * Import a set of points from a file.
     * 
     * @param inputStream
     *            An input stream.
     * @param ext
     *            The file extension.
     * @throws IOException
     */
    public static void importFromFile(InputStream inputStream, String ext) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";

        while ((line = bufferedReader.readLine()) != null) {
            Point newPt = new Point();

            if (ext.equalsIgnoreCase("CSV")) {
                newPt.createPointFromCSV(line);
            }

            SharedResources.getSetOfPoints().add(newPt);
        }
        inputStream.close();
    }
}
