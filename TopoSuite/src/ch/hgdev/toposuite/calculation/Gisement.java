package ch.hgdev.toposuite.calculation;

import ch.hgdev.toposuite.points.Point;

/**
 * Gisement provides methods for the calculation of a gisement/distance.
 * 
 * @author HGdev
 */
public class Gisement extends Calculation {
    /**
     * The machine precision used to perform logical operation on doubles.
     */
    private final static double EPSILON = Double.MIN_VALUE;

    /**
     * The origin.
     */
    private Point               origin;

    /**
     * The orientation.
     */
    private Point               orientation;

    /**
     * The "gisement", also called Z0.
     */
    private double              gisement;

    /**
     * The horizontal distance.
     */
    private double              horizDist;

    /**
     * The altitude.
     */
    private double              altitude;

    /**
     * The slope.
     */
    private double              slope;

    /**
     * Constructs a new Gisement object. It also calls the
     * {@link Gisement#compute()} method which computes the gisement, the
     * distance, the altitude and the slope.
     * 
     * @param _description
     *            the calculation description
     * @param _origin
     *            the origin
     * @param _orientation
     *            the orientation
     */
    public Gisement(String _description, Point _origin, Point _orientation) {
        super("Gisement", _description);

        this.origin = _origin;
        this.orientation = _orientation;

        this.compute();
    }

    /**
     * See {@link Gisement#Gisement(String, Point, Point)}
     * 
     * @param _origin
     *            the origin
     * @param _orientation
     *            the orientation
     */
    public Gisement(Point _origin, Point _orientation) {
        this("", _origin, _orientation);
    }

    /**
     * Perform the gisement, distance, altitude and slope calculations.
     */
    public final void compute() {
        double deltaY = this.orientation.getEast() - this.origin.getEast();
        double deltaX = this.orientation.getNorth() - this.origin.getNorth();

        // complement remains 0.0 if deltaY is positive and deltaX is positive
        // and if deltaY is 0.0 and deltaX is positive.
        double complement = 0.0;

        if (this.isPositive(deltaY) && this.isZero(deltaX)) {
            complement = 100.0;
        } else if (this.isNegative(deltaY) && this.isZero(deltaX)) {
            complement = 300.0;
        } else if ((this.isZero(deltaY) && this.isNegative(deltaX))
                || (this.isPositive(deltaY) && this.isNegative(deltaX))
                || (this.isNegative(deltaY) && this.isNegative(deltaX))) {
            complement = 200.0;
        } else if (this.isNegative(deltaY) && this.isPositive(deltaX)) {
            complement = 400.0;
        }

        // handle division by zero
        double tmp = 0.0;
        if (!this.isZero(deltaX)) {
            tmp = Math.atan(deltaY / deltaX);
        }
        // TODO create a separate helper for converting rad to grad
        this.gisement = ((tmp / Math.PI) * 200) + complement;

        // TODO create a separate helper for converting grad to rad
        this.horizDist = deltaY / Math.sin((this.gisement * Math.PI) / 200);

        // update the calculation last modification date
        this.updateLastModification();
    }

    /**
     * Check if a double is zero.
     * 
     * @param d
     *            a double
     * @return true if d is equal to 0, false otherwise.
     */
    private boolean isZero(double d) {
        return (d < Gisement.EPSILON) && (d > -Gisement.EPSILON);
    }

    /**
     * Check if a double is strictly positive.
     * 
     * @param d
     *            a double
     * @return true if d is bigger than 0, false otherwise.
     */
    private boolean isPositive(double d) {
        return d > Gisement.EPSILON;
    }

    /**
     * Check if a double is strictly negative.
     * 
     * @param d
     *            a double
     * @return true if d is smaller than 0, false otherwise.
     */
    private boolean isNegative(double d) {
        return d < -Gisement.EPSILON;
    }

    /**
     * Getter for the origin.
     * 
     * @return the origin
     */
    public Point getOrigin() {
        return this.origin;
    }

    /**
     * Setter for the origin. Whenever this method is called, it triggers the
     * {@link Gisement#compute()} method.
     * 
     * @param _origin
     *            the new origin
     */
    public void setOrigin(Point _origin) {
        this.origin = _origin;
        this.compute();
    }

    /**
     * Getter for the orientation.
     * 
     * @return the orientation
     */
    public Point getOrientation() {
        return this.orientation;
    }

    /**
     * Setter for the orientation. Whenever this method is called, it triggers
     * the {@link Gisement#compute()} method.
     * 
     * @param _orientation
     *            the new orientation
     */
    public void setOrientation(Point _orientation) {
        this.orientation = _orientation;
        this.compute();
    }

    /**
     * Getter the gisement.
     * 
     * @return the gisement
     */
    public double getGisement() {
        return this.gisement;
    }

    /**
     * Getter for the distance.
     * 
     * @return the distance
     */
    public double getHorizDist() {
        return this.horizDist;
    }

    /**
     * Getter for the altitude.
     * 
     * @return the altitude
     */
    public double getAltitude() {
        return this.altitude;
    }

    /**
     * Getter for the slope.
     * 
     * @return the slope
     */
    public double getSlope() {
        return this.slope;
    }
}