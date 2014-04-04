package ch.hgdev.toposuite.utils;

import org.json.JSONException;

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
    public final static String TOPOSUITE_RESSOURCE_NOT_FOUND         = "TOPOSUITE RESSOURCE NOT FOUND: ";
    public final static String TOPOSUITE_SQL_ERROR                   = "TOPOSUITE SQL ERROR";
    public final static String TOPOSUITE_CALCULATION_IMPORT_ERROR    = "TOPOSUITE CALCULATION IMPORT ERROR";
    public final static String TOPOSUITE_CALCULATION_NOT_IMPLEMENTED = "TOPOSUITE CALCULATION NOT IMPLEMENTED";
    public final static String TOPOSUITE_CALCULATION_IMPOSSIBLE      = "TOPOSUITE CALCULATION IMPOSSIBLE";
    public final static String TOPOSUITE_CALCULATION_INVALID_TYPE    = "TOPOSUITE CALCULATION INVALID TYPE";
    public final static String TOPOSUITE_PARSE_ERROR                 = "TOPOSUITE PARSE ERROR";
    public final static String TOPOSUITE_INPUT_ERROR                 = "TOPOSUITE INPUT ERROR";
    public final static String TOPOSUITE_IO_ERROR                    = "TOPOSUITE IO ERROR";

    public final static String TOPOSUITE_SQL_SUCCESS                 = "TOPOSUITE SQL SUCCESS";

    public final static String TOPOSUITE_SETTINGS_ERROR              = "TOPOSUITE SETTINGS ERROR";

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
}