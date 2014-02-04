package ch.hgdev.toposuite.utils;

public class MathUtils {
    /**
     * The machine precision used to perform logical operation on doubles.
     */
    public final static double EPSILON = Double.MIN_VALUE;

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
     * Convert radian to gradian
     * 
     * @param rad
     *            angle in radian
     * @return angle in gradian
     */
    public static double radToGrad(double rad) {
        return (rad / Math.PI) * 200;
    }

    /**
     * Convert gradian to radian
     * 
     * @param grad
     *            angle in gradian
     * @return angle in radian
     */
    public static double gradToRad(double grad) {
        return (grad * Math.PI) / 200;
    }
}