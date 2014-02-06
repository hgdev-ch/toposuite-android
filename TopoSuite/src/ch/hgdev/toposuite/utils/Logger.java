package ch.hgdev.toposuite.utils;

import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.points.Point;

/**
 * Set of TAGS for application logs and useful static method for logging.
 * 
 * @author HGdev
 * 
 */
public class Logger {
    public final static String TOPOSUITE_RESSOURCE_NOT_FOUND = "TOPOSUITE RESSOURCE NOT FOUND: ";
    public final static String TOPOSUITE_SQL_ERROR = "TOPOSUITE SQL ERROR";
    
    public final static String TOPOSUITE_SQL_SUCCESS = "TOPOSUITE SQL SUCCESS";
    
    /**
     * Format point for logging.
     * @param point
     *            a Point
     * @return formatted point
     */
    public static String formatPoint(Point point) {
        return String.format("Point: {No: %d,  E: %f, N: %f, A: %f, BP: %b}",
                point.getNumber(), point.getEast(), point.getNorth(),
                point.getAltitude(), point.isBasePoint());
    }
    
    /**
     * Format calculation for logging.
     * @param calculation
     *            a calculation
     * @return formatted calculation
     */
    public static String formatCalculation(Calculation calculation) {
        return String.format("Calculation: {Type: %s,  Description: %s, LastModification: %s}",
                calculation.getType(), calculation.getDescription(),
                calculation.getLastModification());
    }
}