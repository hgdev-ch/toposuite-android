package ch.hgdev.toposuite.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;

import com.google.common.base.Strings;
import com.google.common.math.DoubleMath;

/**
 * MathUtils provides static helpers for mathematical operation/conversion.
 *
 * @author HGdev
 */
public class MathUtils {

    /**
     * The earth equatorial radius in meters.
     */
    public static final double EARTH_RADIUS  = 6378100.0;

    /**
     * The machine precision used to perform logical operation on doubles.
     */
    public static final double EPSILON       = Double.MIN_NORMAL;

    /**
     * Use this value when you need to set a variable which needs to be ignored
     * later in some calculations.
     */
    public static final double IGNORE_DOUBLE = Double.MIN_VALUE;

    /**
     * Use this value when you need to set a variable which needs to be ignored
     * later in some calculations.
     */
    public static final int    IGNORE_INT    = Integer.MIN_VALUE;

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
     * Check if a double has the value of the largest possible value for a
     * double.
     *
     * @param d
     *            a double
     * @return true if d is equal to the maximum value, false otherwise.
     */
    public static boolean isMax(double d) {
        return d == Double.MAX_VALUE;
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
     * Check if a double has the value of the smallest possible value for a
     * double.
     *
     * @param d
     *            a double
     * @return true if d is equal to the minimum value, false otherwise.
     */
    public static boolean isMin(double d) {
        return d == Double.MIN_VALUE;
    }

    /**
     * Check that a double is between the given interval.
     *
     * @param d
     *            A double.
     * @param min
     *            The lowest value of the interval.
     * @param max
     *            The highest value of the interval.
     * @param tolerance
     *            The minimal accepted tolerance.
     * @return True if d is in the interval, false otherwise.
     */
    public static boolean isBetween(double d, double min, double max, double tolerance) {
        if (DoubleMath.fuzzyCompare(min, max, tolerance) >= 0) {
            return false;
        }
        if (DoubleMath.fuzzyCompare(d, min, tolerance) < 0) {
            return false;
        }
        if (DoubleMath.fuzzyCompare(d, max, tolerance) > 0) {
            return false;
        }
        return true;
    }

    /**
     * Check the two inputs for equality.
     *
     * @param a
     *            First double to compare.
     * @param b
     *            Second double to compare.
     * @return True if they are the same, false otherwise.
     */
    public static boolean equals(double a, double b) {
        return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
    }

    /**
     * Check the two input points for equality. This method does not take the
     * point number into account.
     *
     * @param p1
     *            First point to compare
     * @param p2
     *            Second point to compare
     * @param tolerance
     *            The tolerance used for the comparison
     * @param altitude
     *            Set to true if you want to consider the altitude for the
     *            comparison
     * @return True if they are the same, false otherwise.
     */
    public static boolean equals(Point p1, Point p2, double tolerance, boolean altitude) {
        if ((p1 == null) || (p2 == null)) {
            return false;
        }

        if (DoubleMath.fuzzyEquals(p1.getEast(), p2.getEast(), tolerance)
                && DoubleMath.fuzzyEquals(p1.getNorth(), p2.getNorth(), tolerance)) {
            if (altitude && !MathUtils.isIgnorable(p1.getAltitude())
                    && !MathUtils.isIgnorable(p2.getAltitude())) {
                if (!DoubleMath.fuzzyEquals(p1.getAltitude(), p2.getAltitude(), tolerance)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Check the two input points for equality. This method does not take the
     * point number into account.
     *
     * @param p1
     *            First point to compare
     * @param p2
     *            Second point to compare
     * @param tolerance
     *            The tolerance used for the comparison
     * @return True if they are the same, false otherwise.
     */
    public static boolean equals(Point p1, Point p2, double tolerance) {
        return MathUtils.equals(p1, p2, tolerance, true);
    }

    /**
     * Determine if the given value could be ignored or not. This is useful in
     * some calculations. For instance, some value may be optional and in this
     * case, it should be ignored. In order to be ignored, a value of type
     * double must be set to {@link MathUtils.IGNORE_DOUBLE}.
     *
     * @param d
     *            a double
     * @return True if the value can be ignored, false otherwise.
     */
    public static boolean isIgnorable(double d) {
        if ((d == MathUtils.IGNORE_DOUBLE)
                || MathUtils.isMax(d)
                || MathUtils.isMin(d)
                || Double.isInfinite(d)
                || Double.isNaN(d)) {
            return true;
        }
        return false;
    }

    /**
     * Check if a int has the value of the largest possible value for a int.
     *
     * @param value
     *            an int
     * @return true if d is equal to the maximum value, false otherwise.
     */
    public static boolean isMax(int value) {
        return value == Integer.MAX_VALUE;
    }

    /**
     * Check if a int has the value of the smallest possible value for a int.
     *
     * @param value
     *            an int
     * @return true if d is equal to the minimum value, false otherwise.
     */
    public static boolean isMin(int value) {
        return value == Integer.MIN_VALUE;
    }

    /**
     * Determine if the given value could be ignored or not. This is useful in
     * some calculations. For instance, some value may be optional and in this
     * case, it should be ignored. In order to be ignored, a value of type
     * double must be set to {@link MathUtils.IGNORE_INT}.
     *
     * @param value
     *            an integer
     * @return True if the value can be ignored, false otherwise.
     */
    public static boolean isIgnorable(int value) {
        if ((value == MathUtils.IGNORE_INT)
                || MathUtils.isMax(value)
                || MathUtils.isMin(value)) {
            return true;
        }
        return false;
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

    /**
     * Convert a scale factor into PPM (Part Per Million).
     *
     * @param scaleFactor
     *            a scale factor
     * @return scale factor in PPM
     */
    public static int scaleToPPM(double scaleFactor) {
        return (int) (1000000 * scaleFactor) - 1000000;
    }

    /**
     * Round a value of type coordinate to the number of decimals set in the
     * settings (3 by default). If values are "ignorable", they are not rounded.
     *
     * @param coordinate
     *            Coordinate to round.
     * @return Coordinate rounded.
     */
    public static double roundCoordinate(double coordinate) {
        if (MathUtils.isIgnorable(coordinate)) {
            return MathUtils.IGNORE_DOUBLE;
        }
        int precision = App.getCoordinateDecimalRounding();
        String pattern = precision < 1 ? "#" : "#.";
        String decimalCount = Strings.repeat("#", precision);
        pattern += decimalCount;
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat(pattern, symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        try {
            return df.parse(df.format(coordinate)).doubleValue();
        } catch (ParseException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR, e.toString());
            return MathUtils.IGNORE_DOUBLE;
        }
    }

    /**
     * Round up a given number only if it is close enough according to a given
     * tolerance.
     *
     * @param n
     *            a number
     * @param tolerance
     *            a tolerance
     * @return The number rounded up or not
     */
    public static double roundWithTolerance(double n, double tolerance) {
        double nSup = Math.ceil(n);
        return DoubleMath.fuzzyEquals(nSup - n, 0.0d, tolerance) ? nSup : n;
    }
}