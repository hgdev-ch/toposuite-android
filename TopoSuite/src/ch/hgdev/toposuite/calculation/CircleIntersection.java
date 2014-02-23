package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import android.util.Log;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

public class CircleIntersection extends Calculation {

    /**
     * Center of the first circle.
     */
    private Point  centerFirst;
    /**
     * Radius of the first circle.
     */
    private double radiusFirst;
    /**
     * Center of the second circle.
     */
    private Point  centerSecond;
    /**
     * Radius of the second circle.
     */
    private double radiusSecond;

    /**
     * Point on the first intersection.
     */
    private Point  firstIntersection;
    /**
     * Point on the second intersection (if relevant).
     */
    private Point  secondIntersection;

    public CircleIntersection(long id, Date lastModification) {
        super(id,
                CalculationType.CIRCLEINTERSEC,
                "Circle intersection",
                lastModification,
                true);
    }

    public CircleIntersection(Point _centerFirst, double _radiusFirst,
            Point _centerSecond, double _radiusSecond, boolean hasDAO) {
        super(CalculationType.CIRCLEINTERSEC,
                "Circle intersection",
                hasDAO);
        this.centerFirst = _centerFirst;
        this.radiusFirst = _radiusFirst;
        this.centerSecond = _centerSecond;
        this.radiusSecond = _radiusSecond;

        this.firstIntersection = new Point(0, 0.0, 0.0, 0.0, false);
        this.secondIntersection = new Point(0, 0.0, 0.0, 0.0, false);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public CircleIntersection(Point _centerFirst, Point _borderFirst,
            Point _centerSecond, Point _borderSecond, boolean hasDAO) {
        super(CalculationType.CIRCLEINTERSEC,
                "Circle intersection",
                hasDAO);
        this.centerFirst = _centerFirst;
        this.radiusFirst = MathUtils.euclideanDistance(_centerFirst, _borderFirst);
        this.centerSecond = _centerSecond;
        this.radiusSecond = MathUtils.euclideanDistance(_centerSecond, _borderSecond);

        this.firstIntersection = new Point(0, 0.0, 0.0, 0.0, false);
        this.secondIntersection = new Point(0, 0.0, 0.0, 0.0, false);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
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
                Log.e(Logger.TOPOSUITE_CALCULATION_IMPOSSIBLE,
                        "Circle intersection: the circles are next to each another (no intersection).");
            } else {
                Log.e(Logger.TOPOSUITE_CALCULATION_IMPOSSIBLE,
                        "Circle intersection: one of the circle is included in the other one (no intersection).");
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
                        + (this.radiusFirst * Math.sin(gisement + alpha)));
        this.secondIntersection.setEast(
                this.centerFirst.getEast()
                        + (this.radiusFirst * Math.sin(gisement - alpha)));
        this.secondIntersection.setNorth(
                this.centerFirst.getNorth()
                        + (this.radiusFirst * Math.sin(gisement - alpha)));
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
