package ch.hgdev.toposuite.points;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Pair;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.export.InvalidFormatException;
import ch.hgdev.toposuite.export.SupportedFileTypes;
import ch.hgdev.toposuite.utils.Logger;

public class PointsImporter {

    /**
     * Import a set of points from a file. This function assumes that the
     * supplied extension is valid and supported by the importer.
     *
     * @param inputStream
     *            An input stream.
     * @param ext
     *            The file extension.
     * @return A list of pair <line number, errors>. The list is empty if no
     *         error occurred.
     * @throws IOException
     */
    public static List<Pair<Integer, String>> importFromFile(InputStream inputStream,
            final String ext)
                    throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        SupportedFileTypes type = SupportedFileTypes.fileTypeOf(ext);

        // List of errors
        List<Pair<Integer, String>> errors = new ArrayList<Pair<Integer, String>>();

        int nbLines = 0;

        while ((line = bufferedReader.readLine()) != null) {
            ++nbLines;

            Point newPt = new Point();

            try {
                switch (type) {
                case CSV:
                    newPt.createPointFromCSV(line);
                    break;
                case COO:
                case KOO:
                case LTOP:
                    if (nbLines == 1) {
                        continue;
                    }

                    if (line.matches("^\\*\\*.*")) {
                        continue;
                    }

                    newPt.createPointFromLTOP(line);
                    break;
                case PTP:
                    newPt.createPointFromPTP(line);
                    break;
                }
            } catch (InvalidFormatException e) {
                errors.add(new Pair<Integer, String>(nbLines, e.getMessage()));
                Logger.log(Logger.ErrLabel.INPUT_ERROR, e.getMessage() + " => " + errors.size());
                continue;
            }

            SharedResources.getSetOfPoints().add(newPt);
        }
        inputStream.close();

        return errors;
    }

    /**
     * Format errors list as a String.
     *
     * @param filename
     *            File name.
     * @param errors
     *            Errors list.
     * @return A formatted string, ready to be displayed in an alert dialog.
     */
    public static String formatErrors(String filename, List<Pair<Integer, String>> errors) {
        StringBuilder builder = new StringBuilder();
        builder.append(App.getContext().getString(R.string.error_points_import));
        builder.append("\n\n");

        for (Pair<Integer, String> p : errors) {
            builder.append(filename + ":" + p.first.toString());
            builder.append(" → ");
            builder.append(p.second);
            builder.append("\n");
        }

        return builder.toString();
    }
}
