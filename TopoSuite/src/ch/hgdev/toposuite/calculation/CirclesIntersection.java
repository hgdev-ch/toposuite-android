package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import android.util.Log;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

import com.google.common.base.Preconditions;

public class CirclesIntersection extends Calculation {

    private static final String CIRCLE_INTERSECTION = "Circle intersection: ";

    /**
     * Center of the first circle.
     */
    private Point               centerFirst;
    /**
     * Radius of the first circle.
     */
    private double              radiusFirst;
    /**
     * Center of the second circle.
     */
    private Point               centerSecond;
    /**
     * Radius of the second circle.
     */
    private double              radiusSecond;

    /**
     * Point on the first intersection.
     */
    private Point               firstIntersection;
    /**
     * Point on the second intersection (if relevant).
     */
    private Point               secondIntersection;

    public CirclesIntersection(long id, Date lastModification) {
        super(id,
                CalculationType.CIRCLEINTERSEC,
                App.getContext().getString(R.string.title_activity_circles_intersection),
                lastModification,
                true);
    }

    public CirclesIntersection(Point _centerFirst, double _radiusFirst,
            Point _centerSecond, double _radiusSecond, boolean hasDAO)
            throws IllegalArgumentException {
        super(CalculationType.CIRCLEINTERSEC,
                "Circle intersection",
                hasDAO);
        Preconditions.checkArgument(
                !_centerFirst.equals(_centerSecond),
                "The two provided points must be different.");

        this.initAttributes(_centerFirst, _radiusFirst, _centerSecond, _radiusSecond);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public CirclesIntersection(Point _centerFirst, Point _borderFirst,
            Point _centerSecond, Point _borderSecond, boolean hasDAO)
            throws IllegalArgumentException {
        super(CalculationType.CIRCLEINTERSEC,
                App.getContext().getString(R.string.title_activity_circles_intersection),
                hasDAO);

        this.initAttributes(_centerFirst, MathUtils.euclideanDistance(_centerFirst, _borderFirst),
                _centerSecond, MathUtils.euclideanDistance(_centerSecond, _borderSecond));

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Initialize class attributes.
     * 
     * @param _centerFirst
     *            Center of the first circle.
     * @param _radiusFirst
     *            Radius of the first circle.
     * @param _centerSecond
     *            Center of the second circle.
     * @param _radiusSecond
     *            Radius of the second circle.
     * @throws IllegalArgumentException
     */
    private void initAttributes(Point _centerFirst, double _radiusFirst,
            Point _centerSecond, double _radiusSecond) throws IllegalArgumentException {
        Preconditions.checkArgument(
                !_centerFirst.equals(_centerSecond),
                "The two provided points must be different.");
        Preconditions.checkNotNull(_centerFirst, "The first point must no be null");
        Preconditions.checkNotNull(_centerSecond, "The second point must no be null");
        Preconditions.checkArgument(MathUtils.isPositive(_radiusFirst),
                "The first radius must be positive.");
        Preconditions.checkArgument(MathUtils.isPositive(_radiusSecond),
                "The second radius must be positive.");

        this.centerFirst = _centerFirst;
        this.radiusFirst = _radiusFirst;
        this.centerSecond = _centerSecond;
        this.radiusSecond = _radiusSecond;

        this.firstIntersection = new Point(0, 0.0, 0.0, 0.0, false);
        this.secondIntersection = new Point(0, 0.0, 0.0, 0.0, false);
    }

    @Override
    public void compute() {
        double distCenters = MathUtils.euclideanDistance(this.centerFirst, this.centerSecond);
        double alpha = ((Math.pow(distCenters, 2) + Math.pow(this.radiusFirst, 2)) - Math.pow(
                this.radiusSecond, 2)) / (2 * this.radiusFirst * distCenters);
        // make sure there is an intersection
        if (((-alpha * alpha) + 1) <= 0) {
            // radius to small => circles are next to each another
            if ((this.radiusFirst + this.radiusSecond) < distCenters) {
                Log.w(Logger.TOPOSUITE_CALCULATION_IMPOSSIBLE,
                        CirclesIntersection.CIRCLE_INTERSECTION
                                + "the circles are next to each another (no intersection).");
            } else {
                Log.w(Logger.TOPOSUITE_CALCULATION_IMPOSSIBLE,
                        CirclesIntersection.CIRCLE_INTERSECTION
                                + "one of the circle is included in the other one (no intersection).");
            }
            this.setZeros();
            return;
        }
        alpha = Math.atan(-alpha / Math.sqrt((-alpha * alpha) + 1)) + (2 * Math.atan(1));

        double gisement = MathUtils.gradToRad(
                new Gisement(this.centerFirst, this.centerSecond, false).getGisement());

        this.firstIntersection.setEast(
                this.centerFirst.getEast()
                        + (this.radiusFirst * Math.sin(gisement + alpha)));
        this.firstIntersection.setNorth(
                this.centerFirst.getNorth()
                        + (this.radiusFirst * Math.cos(gisement + alpha)));
        this.secondIntersection.setEast(
                this.centerFirst.getEast()
                        + (this.radiusFirst * Math.sin(gisement - alpha)));
        this.secondIntersection.setNorth(
                this.centerFirst.getNorth()
                        + (this.radiusFirst * Math.cos(gisement - alpha)));
    }

    /**
     * Set resulting points coordinate to 0.0. This usually indicates that there
     * was an error.
     */
    private void setZeros() {
        this.firstIntersection.setEast(0.0);
        this.firstIntersection.setNorth(0.0);
        this.secondIntersection.setEast(0.0);
        this.secondIntersection.setNorth(0.0);
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO implement
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO implement

    }

    @Override
    public Class<?> getActivityClass() {
        // TODO implement
        return null;
    }

    public Point getFirstIntersection() {
        return this.firstIntersection;
    }

    public Point getSecondIntersection() {
        return this.secondIntersection;
    }
}
