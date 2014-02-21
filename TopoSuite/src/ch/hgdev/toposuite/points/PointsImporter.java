package ch.hgdev.toposuite.points;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.export.SupportedFileTypes;

public class PointsImporter {

    /**
     * Import a set of points from a file. This function assumes that the
     * supplied extension is valid and supported by the importer.
     * 
     * @param inputStream
     *            An input stream.
     * @param ext
     *            The file extension.
     * @throws IOException
     */
    public static void importFromFile(InputStream inputStream, final String ext) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        SupportedFileTypes type = SupportedFileTypes.fileTypeOf(ext);

        while ((line = bufferedReader.readLine()) != null) {
            Point newPt = new Point();

            switch (type) {
            case CSV:
                newPt.createPointFromCSV(line);
                break;
            }

            SharedResources.getSetOfPoints().add(newPt);
        }
        inputStream.close();
    }
}
