package ch.hgdev.toposuite.points;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.transfer.InvalidFormatException;
import ch.hgdev.toposuite.transfer.SupportedPointsFileTypes;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.UnicodeReader;

public class PointsImporter {

    /**
     * Import a set of points from a file. This function assumes that the
     * supplied extension is valid and supported by the importer.
     *
     * @param inputStream An input stream.
     * @param ext         The file extension.
     * @return A list of pair <line number, errors>. The list is empty if no
     * error occurred.
     * @throws IOException
     */
    public static List<Pair<Integer, String>> importFromFile(InputStream inputStream, final String ext)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new UnicodeReader(inputStream, "UTF-8"));
        SupportedPointsFileTypes type = SupportedPointsFileTypes.fileTypeOf(ext);

        // List of errors
        List<Pair<Integer, String>> errors = new ArrayList<>();

        int nbLines = 0;

        for (String line = ""; (line = bufferedReader.readLine()) != null;) {
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
                errors.add(new Pair<>(nbLines, e.getMessage()));
                Logger.log(Logger.ErrLabel.INPUT_ERROR, "line #" + nbLines + " => " + e.getMessage());
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
     * @param filename File name.
     * @param errors   Errors list.
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
