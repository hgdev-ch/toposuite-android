package ch.hgdev.toposuite.utils;

import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;

/**
 * MathUtils provides static helpers for mathematical operation/conversion.
 * 
 * @author HGdev
 */
public class MathUtils {

    /**
     * The earth equatorial radius in meters.
     */
    public static final double EARTH_RADIUS = 6378100.0;

    /**
     * The machine precision used to perform logical operation on doubles.
     */
    public static final double EPSILON      = Double.MIN_VALUE;

    /**
     * Check if a double is zero.
     * 
     * @param d
     *            a double
     * @return true if d is equal to 0, false otherwise.
     */
    public static boolean isZero(double d) {
        return (d < MathUtils.EPSILON) && (d > -MathUtils.EPSILON);
    }

    /**
     * Check if a double is strictly positive.
     * 
     * @param d
     *            a double
     * @return true if d is bigger than 0, false otherwise.
     */
    public static boolean isPositive(double d) {
        return d > MathUtils.EPSILON;
    }

    /**
     * Check if a double is strictly negative.
     * 
     * @param d
     *            a double
     * @return true if d is smaller than 0, false otherwise.
     */
    public static boolean isNegative(double d) {
        return d < -MathUtils.EPSILON;
    }

    /**
     * Convert an angle in radian to its equivalent in gradian.
     * 
     * @param rad
     *            angle in radian
     * @return angle in gradian
     */
    public static double radToGrad(double rad) {
        return (rad / Math.PI) * 200;
    }

    /**
     * Convert an angle in gradian to its equivalent radian.
     * 
     * @param grad
     *            angle in gradian
     * @return angle in radian
     */
    public static double gradToRad(double grad) {
        return (grad * Math.PI) / 200;
    }

    /**
     * Convert an angle in gradian to its equivalent in degree.
     * 
     * @param grad
     *            The angle to convert.
     * @return The angle in degree.
     */
    public static double gradToDeg(double grad) {
        return grad * ((Math.PI / 200) / (Math.PI / 180));
    }

    /**
     * Convert an angle in degree to its equivalent in gradian.
     * 
     * @param deg
     *            The angle to convert.
     * @return The angle in gradian.
     */
    public static double degToGrad(double deg) {
        return deg * ((Math.PI / 180) / (Math.PI / 200));
    }

    /**
     * Convert meters to centimeters.
     * 
     * @param m
     *            The meters.
     * @return The distance in centimeters.
     */
    public static double mToCm(double m) {
        return m * 100;
    }

    /**
     * Modulate an angle in gradian. This ensures that the angle is between 0
     * and 200 gradian.
     * 
     * @param angle
     *            Angle in gradian unit.
     * @return The angle with a value between 0 and 200 gradians.
     */
    public static double modulo200(double angle) {
        double m = angle;
        if (m < 0) {
            while (m < 0) {
                m += 200;
            }
        } else if (m >= 200) {
            while (m >= 200) {
                m -= 200;
            }
        }
        return m;
    }

    /**
     * Modulate an angle in gradian. This ensures that the angle is between 0
     * and 400 gradian.
     * 
     * @param angle
     *            Angle in gradian unit.
     * @return The angle with a value between 0 and 400 gradians.
     */
    public static double modulo400(double angle) {
        double m = angle;
        if (m < 0) {
            while (m < 0) {
                m += 400;
            }
        } else if (m >= 400) {
            while (m >= 400) {
                m -= 400;
            }
        }
        return m;
    }

    /**
     * Euclidean distance between 2 points in 2D.
     * 
     * @param p1
     *            a Point
     * @param p2
     *            a Point
     * @return the euclidean distance between p1 and p2
     */
    public static double euclideanDistance(Point p1, Point p2) {
        double deltaY = p2.getEast() - p1.getEast();
        double deltaX = p2.getNorth() - p1.getNorth();

        return MathUtils.pythagoras(deltaY, deltaX);
    }

    /**
     * Pythagoras...
     * 
     * @param a
     *            a double value
     * @param b
     *            another double value
     * @return sqrt(a^2 + b^2)
     */
    public static double pythagoras(double a, double b) {
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    /**
     * Compute a "point lancé" for a east coordinate.
     * 
     * @param east
     *            a east coordinate
     * @param gisement
     *            a gisement
     * @param distance
     *            a distance
     * @return new east coordinate
     */
    public static double pointLanceEast(double east, double gisement, double distance) {
        return east + (distance * Math.sin(
                MathUtils.gradToRad(MathUtils.modulo400(gisement))));
    }

    /**
     * Compute a "point lancé" for a north coordinate.
     * 
     * @param north
     *            a north coordinate
     * @param gisement
     *            a gisement
     * @param distance
     *            a distance
     * @return new north coordinate
     */
    public static double pointLanceNorth(double north, double gisement, double distance) {
        return north + (distance * Math.cos(
                MathUtils.gradToRad(MathUtils.modulo400(gisement))));
    }

    /**
     * Calculate an angle defined by 3 points.
     * 
     * @param p1
     *            First point
     * @param p2
     *            Second point
     * @param p3
     *            Third point
     * @return an angle
     */
    public static double angle3Pts(Point p1, Point p2, Point p3) {
        double gis1 = new Gisement(p2, p1, false).getGisement();
        double gis2 = new Gisement(p2, p3, false).getGisement();
        return MathUtils.modulo400(gis2 - gis1);
    }

    /**
     * TODO fill the description of this function.
     * 
     * @param distance
     *            Distance in meter.
     * @param zenAngle
     *            Zenithal angle.
     * @param i
     *            Height of the instrument (I).
     * @param s
     *            Height of the prism (S).
     * @param altitude
     *            Altitude.
     * @return
     */
    public static double nivellTrigo(double distance, double zenAngle, double i,
            double s, double altitude) {
        double radius = MathUtils.EARTH_RADIUS + altitude;
        double e = Math.pow(distance, 2) / (2 * radius);
        double r = (0.13 * Math.pow(distance, 2)) / (2 * radius);

        return ((((distance / Math.tan(MathUtils.gradToRad(zenAngle)))
                + i) - s) + e) - r;
    }
}