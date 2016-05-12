package ch.hgdev.toposuite.transfer;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.App;

/**
 * This enum contains the enumeration of all supported format.
 * 
 * @author HGdev
 */
public enum SupportedPointsFileTypes {
    CSV,
    LTOP, // LTOP custom MIME-type
    COO, // LTOP with a *.coo/*.COO extension
    KOO, // LTOP with a *.koo/*.KOO extension
    PTP;

    /**
     * Return the enum values as a list of strings.
     * 
     * @return a list
     */
    public static List<String> toList() {
        List<String> list = new ArrayList<>();
        for (SupportedPointsFileTypes sft : SupportedPointsFileTypes.values()) {
            list.add(sft.toString());
        }

        return list;
    }

    /**
     * Check if a given format exists in the enum.
     * 
     * @param format
     *            a file format such as CSV, LTOP, etc.
     * @return true if the format is supported, false otherwise
     */
    public static boolean isSupported(String format) {
        try {
            SupportedPointsFileTypes.valueOf(format.toUpperCase(App.getLocale()));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Return the file type corresponding to a given file format. This method is
     * case insensitive.
     * 
     * @param format
     *            A file format such as CSV, LTOP, etc.
     * @return the file format if it exists, null otherwise
     */
    public static SupportedPointsFileTypes fileTypeOf(String format) {
        SupportedPointsFileTypes type = null;

        try {
            type = SupportedPointsFileTypes.valueOf(format.toUpperCase(App.getLocale()));
        } catch (IllegalArgumentException e) {
            // nothing
        }
        return type;
    }
}
