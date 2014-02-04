package ch.hgdev.toposuite.calculation;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Gisement provides methods for the calculation of a gisement/distance.
 * 
 * @author HGdev
 */
public class Gisement extends Calculation {
    /**
     * The origin.
     */
    private Point  origin;

    /**
     * The orientation.
     */
    private Point  orientation;

    /**
     * The "gisement", also called Z0.
     */
    private double gisement;

    /**
     * The horizontal distance.
     */
    private double horizDist;

    /**
     * The altitude.
     */
    private double altitude;

    /**
     * The slope given in percent.
     */
    private double slope;

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

        // gisement
        double complement = this.computeComplement(deltaY, deltaX);
        this.gisement = this.computeGisement(deltaY, deltaX, complement);

        // distance
        this.horizDist = this.computeHorizDist(deltaY, deltaX, this.gisement);

        // altitude
        this.altitude = this.computeAltitude();

        // slope in percent
        this.slope = this.computeSlope(this.altitude, this.horizDist);

        // update the calculation last modification date
        this.updateLastModification();
    }

    /**
     * Calculate the complement using the following rules:
     * 
     * <pre>
     * +==========+==========+============+
     * |  DeltaY  | Delta X  | Complement |
     * +==========+==========+============+
     * | Positive | Positive |      0     |
     * +----------+----------+------------+
     * | Positive | Negative |     200    |
     * +----------+----------+------------+
     * | Negative | Negative |     200    |
     * +----------+----------+------------+
     * | Negative | Positive |     400    |
     * +----------+----------+------------+
     * | Zero     | Positive |      0     |
     * +----------+----------+------------+
     * | Zero     | Negative |     200    |
     * +----------+----------+------------+
     * | Positive | Zero     |     100    |
     * +----------+----------+------------+
     * | Negative | Zero     |     300    |
     * +----------+----------+------------+
     * </pre>
     * 
     * @param deltaY
     *            delta Y
     * @param deltaX
     *            delta X
     * @return the complement
     */
    private double computeComplement(double deltaY, double deltaX) {
        // complement remains 0.0 if deltaY is positive and deltaX is positive
        // and if deltaY is 0.0 and deltaX is positive.
        double complement = 0.0;

        if (MathUtils.isPositive(deltaY) && MathUtils.isZero(deltaX)) {
            complement = 100.0;
        } else if (MathUtils.isNegative(deltaY) && MathUtils.isZero(deltaX)) {
            complement = 300.0;
        } else if ((MathUtils.isZero(deltaY) && MathUtils.isNegative(deltaX))
                || (MathUtils.isPositive(deltaY) && MathUtils.isNegative(deltaX))
                || (MathUtils.isNegative(deltaY) && MathUtils.isNegative(deltaX))) {
            complement = 200.0;
        } else if (MathUtils.isNegative(deltaY) && MathUtils.isPositive(deltaX)) {
            complement = 400.0;
        }

        return complement;
    }

    /**
     * Calculate the "gisement" using the following formula:
     * <i>atan(deltaY/deltaX)</i>
     * 
     * @param deltaY
     *            delta Y
     * @param deltaX
     *            delta X
     * @param complement
     *            the complement
     * @return the gisement
     */
    private double computeGisement(double deltaY, double deltaX, double complement) {
        // handle division by zero
        double tmp = 0.0;
        if (!MathUtils.isZero(deltaX)) {
            tmp = Math.atan(deltaY / deltaX);
        }

        return MathUtils.radToGrad(tmp) + complement;
    }

    /**
     * Calculate the horizontal distance using the following formula: <i>deltaY
     * / sin(gisement)</i>
     * 
     * @param deltaY
     *            delta Y
     * @param deltaX
     *            delta X
     * @param gisement
     *            the gisement
     * @return the horizontal distance
     */
    private double computeHorizDist(double deltaY, double deltaX, double gisement) {
        if (MathUtils.isZero(this.gisement) || MathUtils.isZero(deltaY)) {
            // TODO check if it's a correct assumption...
            return Math.abs(deltaX);
        }
        return deltaY / Math.sin(MathUtils.gradToRad(this.gisement));
    }

    /**
     * Calculate the altitude.
     * 
     * @return the altitude
     */
    private double computeAltitude() {
        return this.orientation.getAltitude() - this.origin.getAltitude();
    }

    /**
     * Calculate the slope in percent using the following formula: <i>(altitude
     * / distance) * 100</i>
     * 
     * @param altitude
     *            the altitude
     * @param horizDist
     *            the horizontal distance
     * @return the slope in percent
     */
    private double computeSlope(double altitude, double horizDist) {
        if (MathUtils.isZero(horizDist)) {
            return 0.0;
        }
        return (altitude / horizDist) * 100;
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