package ch.hgdev.toposuite.utils;

import org.json.JSONException;

import android.util.Log;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.points.Point;

/**
 * Set of TAGS for application logs and useful static method for logging.
 *
 * @author HGdev
 *
 */
public class Logger {
    private final static String TOPOSUITE_LABEL = "TopoSuite";

    /**
     * Error labels for log messages.
     */
    public enum ErrLabel {
        RESOURCE_NOT_FOUND("RESOURCE NOT FOUND"),
        SQL_ERROR("SQL ERROR"),
        CALCULATION_IMPORT_ERROR("CALCULATION IMPORT ERROR"),
        CALCULATION_NOT_IMPLEMENTED("CALCULATION NOT IMPLEMENTED"),
        CALCULATION_INVALID_TYPE("CALCULATION INVALID TYPE"),
        PARSE_ERROR("PARSE ERROR"),
        INPUT_ERROR("INPUT ERROR"),
        IO_ERROR("IO ERROR"),
        SETTINGS_ERROR("SETTINGS ERROR");

        private final String label;

        private ErrLabel(final String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Warning labels for log messages.
     */
    public enum WarnLabel {
        CALCULATION_IMPOSSIBLE("CALCULATION IMPOSSIBLE");

        private final String label;

        private WarnLabel(final String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Informative labels for log messages.
     */
    public enum InfoLabel {
        SQL_SUCCESS("SQL SUCCESS");

        private final String label;

        private InfoLabel(final String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    /**
     * Format point for logging.
     *
     * @param point
     *            a Point
     * @return formatted point
     */
    public static String formatPoint(Point point) {
        return String.format(App.locale,
                "Point: {No: %s,  E: %f, N: %f, A: %f, BP: %b}",
                point.getNumber(), point.getEast(), point.getNorth(),
                point.getAltitude(), point.isBasePoint());
    }

    /**
     * Format calculation for logging.
     *
     * @param calculation
     *            a calculation
     * @return formatted calculation
     */
    public static String formatCalculation(Calculation calculation) {
        String json = "";
        try {
            json = calculation.exportToJSON();

        } catch (JSONException e) {
            json = "ERROR";
        }

        return String
                .format(App.locale,
                        "Calculation: {ID: %d, Type: %s,  Description: %s, LastModification: %s, Input: '%s'}",
                        calculation.getId(), calculation.getType(),
                        calculation.getDescription(),
                        calculation.getLastModification(), json);
    }

    /**
     * Log informative messages.
     *
     * @param label
     *            Information label.
     * @param message
     *            Actual message to log.
     */
    public static void log(Logger.InfoLabel label, String message) {
        Log.i(Logger.TOPOSUITE_LABEL, label + ": " + message);
    }

    /**
     * Log warning messages.
     *
     * @param label
     *            Warning label.
     * @param message
     *            Actual message to log.
     */
    public static void log(Logger.WarnLabel label, String message) {
        Log.w(Logger.TOPOSUITE_LABEL, label + ": " + message);
    }

    /**
     * Log error messages.
     *
     * @param label
     *            Error label.
     * @param message
     *            Actual message to log.
     */
    public static void log(Logger.ErrLabel label, String message) {
        Log.e(Logger.TOPOSUITE_LABEL, label + ": " + message);
    }
}