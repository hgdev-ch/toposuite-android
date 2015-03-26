package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.linecircleintersection.LineCircleIntersectionActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

import com.google.common.base.Preconditions;

public class LineCircleIntersection extends Calculation {
    private static final String LINE_CIRCLE_INTERSECTION   = "Line-Circle intersection: ";

    private static final String LINE_POINT_ONE_NUMBER      = "line_point_one_number";
    private static final String LINE_POINT_TWO_NUMBER      = "line_point_two_number";
    private static final String LINE_DISPLACEMENT          = "line_displacement";
    private static final String LINE_GISEMENT              = "line_gisement";
    private static final String LINE_DISTANCE              = "line_distance";
    private static final String CIRCLE_POINT_CENTER_NUMBER = "circle_point_center_number";
    private static final String CIRCLE_RADIUS              = "circle_radius";

    private Point               p1L;
    private Point               p2L;
    private double              displacementL;
    private double              gisementL;
    private double              distanceL;

    private Point               centerC;
    private double              radiusC;

    /**
     * Point on the first intersection.
     */
    private Point               firstIntersection;
    /**
     * Point on the second intersection (if relevant).
     */
    private Point               secondIntersection;

    public LineCircleIntersection(long id, Date lastModification) {
        super(id,
                CalculationType.LINECIRCINTERSEC,
                App.getContext().getString(R.string.title_activity_line_circle_intersection),
                lastModification,
                true);
    }

    public LineCircleIntersection(Point _p1L, Point _p2L, double _displacementL, double _gisement,
            double _distance, Point _centerC, double _radiusC,
            boolean hasDAO) throws IllegalArgumentException {
        super(CalculationType.LINECIRCINTERSEC,
                App.getContext().getString(R.string.title_activity_line_circle_intersection),
                hasDAO);

        this.initAttributes(_p1L, _p2L, _displacementL, _gisement, _distance, _centerC, _radiusC);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public LineCircleIntersection(Point _p1L, Point _p2L, double _displacementL, double _distance,
            Point _centerC, double _radiusC) {
        this(_p1L, _p2L, _displacementL, 0.0, _distance, _centerC, _radiusC, false);
    }

    public LineCircleIntersection(Point _p1L, double _displacementL, double _gisement,
            double _distance,
            Point _centerC, double _radiusC) {
        this(_p1L, null, _displacementL, _gisement, _distance, _centerC, _radiusC, false);
    }

    public LineCircleIntersection(Point _p1L, Point _p2L, double _displacementL, Point _centerC,
            double _radiusC) {
        this(_p1L, _p2L, _displacementL, 0.0, _centerC, _radiusC);
    }

    public LineCircleIntersection(boolean hasDAO) {
        super(CalculationType.LINECIRCINTERSEC,
                App.getContext().getString(R.string.title_activity_line_circle_intersection),
                hasDAO);

        this.p1L = new Point(false);
        this.p2L = new Point(false);
        this.displacementL = 0.0;
        this.gisementL = 0.0;
        this.distanceL = 0.0;

        this.centerC = new Point(false);
        this.radiusC = 0.0;

        this.firstIntersection = new Point(false);
        this.secondIntersection = new Point(false);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public LineCircleIntersection() {
        this(true);
    }

    /**
     * Initialize class attributes.
     *
     * @param _p1L
     *            First point on the line. Must NOT be null.
     * @param _p2L
     *            Second point on the line. May be null.
     * @param _displacementL
     *            Displacement.
     * @param _gisement
     *            Gisement. Not used of _p2L is not null.
     * @param _centerC
     *            Center of the circle.
     * @param _radiusC
     *            Radius of the circle.
     * @throws IllegalArgumentException
     */
    public void initAttributes(Point _p1L, Point _p2L, double _displacementL, double _gisement,
            double _distance, Point _centerC, double _radiusC) throws IllegalArgumentException {
        Preconditions.checkNotNull(_p1L, "The first point must not be null");

        this.p1L = _p1L.clone();
        this.p1L.setNumber(_p1L.getNumber());
        if (_p2L == null) {
            this.p2L = new Point(
                    "",
                    MathUtils.pointLanceEast(_p1L.getEast(), _gisement, 100),
                    MathUtils.pointLanceNorth(_p1L.getNorth(), _gisement, 100.0),
                    MathUtils.IGNORE_DOUBLE,
                    false);
        } else {
            this.p2L = _p2L.clone();
            this.p2L.setNumber(_p2L.getNumber());
        }
        this.displacementL = _displacementL;
        this.gisementL = _gisement;
        if (!MathUtils.isZero(_distance)) {
            this.distanceL = _distance;
            double gis = new Gisement(this.p1L, this.p2L, false).getGisement();
            this.p1L.setEast(
                    MathUtils.pointLanceEast(this.p1L.getEast(), gis, this.distanceL));
            this.p1L.setNorth(
                    MathUtils.pointLanceNorth(this.p1L.getNorth(), gis, this.distanceL));
            gis += 100;
            this.p2L.setEast(
                    MathUtils.pointLanceEast(this.p1L.getEast(), gis, 100));
            this.p2L.setNorth(
                    MathUtils.pointLanceNorth(this.p1L.getNorth(), gis, 100));
        }

        this.centerC = _centerC;
        this.radiusC = _radiusC;

        this.firstIntersection = new Point(false);
        this.secondIntersection = new Point(false);
    }

    @Override
    public void compute() throws CalculationException {
        if (!MathUtils.isZero(this.displacementL)) {
            double displGis = new Gisement(this.p1L, this.p2L, false).getGisement();
            displGis += MathUtils.isNegative(this.displacementL) ? -100 : 100;
            this.p1L.setEast(MathUtils.pointLanceEast(
                    this.p1L.getEast(), displGis, Math.abs(this.displacementL)));
            this.p1L.setNorth(MathUtils.pointLanceNorth(
                    this.p1L.getNorth(), displGis, Math.abs(this.displacementL)));
            this.p2L.setEast(MathUtils.pointLanceEast(
                    this.p2L.getEast(), displGis, Math.abs(this.displacementL)));
            this.p2L.setNorth(MathUtils.pointLanceNorth(
                    this.p2L.getNorth(), displGis, Math.abs(this.displacementL)));
        }

        double alpha = new Gisement(this.p1L, this.p2L, false).getGisement() -
                new Gisement(this.p1L, this.centerC, false).getGisement();

        double minRadius = MathUtils.euclideanDistance(
                this.p1L, this.centerC) * Math.sin(MathUtils.gradToRad(alpha));
        double proj = minRadius / this.radiusC;
        double beta = 0.0;

        // check that the circle crosses the line
        if (MathUtils.isPositive((-proj * proj) + 1)) {
            beta = MathUtils.radToGrad(Math.atan(proj / Math.sqrt((-proj * proj) + 1)));
        } else {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    LineCircleIntersection.LINE_CIRCLE_INTERSECTION
                            + "No line-circle crossing. The radius should be longer than "
                            + DisplayUtils.formatDistance(minRadius)
                            + " (" + DisplayUtils.formatDistance(this.radiusC) + " given).");
            this.setIgnorableResults();
            throw new CalculationException(App.getContext().getString(
                    R.string.error_impossible_calculation));
        }

        double stPtIntersecGis1 = new Gisement(this.p1L, this.p2L, false).getGisement();
        double stPtIntersecGis2 = stPtIntersecGis1;
        double distAP1, distAP2;

        // center of the circle on first point of the line
        if (MathUtils.equals(this.centerC.getEast(), this.p1L.getEast())
                && MathUtils.equals(this.centerC.getNorth(), this.p1L.getNorth())) {
            distAP1 = this.radiusC;
            distAP2 = this.radiusC;
            stPtIntersecGis2 = stPtIntersecGis2 - 200;

            // center of the circle on the second point of the line
        } else if (MathUtils.equals(this.centerC.getEast(), this.p2L.getEast())
                && MathUtils.equals(this.centerC.getNorth(), this.p2L.getNorth())) {
            distAP1 = MathUtils.euclideanDistance(this.p1L, this.centerC) + this.radiusC;
            distAP2 = MathUtils.euclideanDistance(this.p2L, this.p1L) - this.radiusC;

            // center of the circle aligned with the two points of the line
        } else if (MathUtils.isZero(Math.sin(MathUtils.gradToRad(alpha)))) {
            double dist = MathUtils.euclideanDistance(this.p1L, this.centerC);
            distAP1 = dist + this.radiusC;
            distAP2 = dist - this.radiusC;

            // center of the circle elsewhere
        } else {
            distAP1 = (this.radiusC * Math.sin(MathUtils.gradToRad(200 - alpha - beta)))
                    / Math.sin(MathUtils.gradToRad(alpha));
            distAP2 = distAP1 + ((this.radiusC * Math.sin(MathUtils.gradToRad((2 * beta) - 200)))
                    / Math.sin(MathUtils.gradToRad(200 - beta)));
        }

        this.firstIntersection.setEast(
                MathUtils.pointLanceEast(this.p1L.getEast(), stPtIntersecGis1, distAP1));
        this.firstIntersection.setNorth(
                MathUtils.pointLanceNorth(this.p1L.getNorth(), stPtIntersecGis1, distAP1));
        this.secondIntersection.setEast(
                MathUtils.pointLanceEast(this.p1L.getEast(), stPtIntersecGis2, distAP2));
        this.secondIntersection.setNorth(
                MathUtils.pointLanceNorth(this.p1L.getNorth(), stPtIntersecGis2, distAP2));

        this.updateLastModification();
        this.setDescription(this.getCalculationName() + " - "
                + App.getContext().getString(R.string.line) + " "
                + App.getContext().getString(R.string.origin_label) + ": "
                + this.p1L.toString()
                + " / " + App.getContext().getString(R.string.circle_label) + " "
                + App.getContext().getString(R.string.center_label) + ": "
                + this.centerC.toString());
        this.notifyUpdate(this);
    }

    /**
     * Set resulting points coordinate to ignorable values. This usually
     * indicates that there was an error or that the calculation was impossible.
     */
    private void setIgnorableResults() {
        this.firstIntersection.setEast(MathUtils.IGNORE_DOUBLE);
        this.firstIntersection.setNorth(MathUtils.IGNORE_DOUBLE);
        this.secondIntersection.setEast(MathUtils.IGNORE_DOUBLE);
        this.secondIntersection.setNorth(MathUtils.IGNORE_DOUBLE);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(LineCircleIntersection.LINE_POINT_ONE_NUMBER, this.p1L.getNumber());
        json.put(LineCircleIntersection.LINE_POINT_TWO_NUMBER, this.p2L.getNumber());
        json.put(LineCircleIntersection.LINE_DISPLACEMENT, this.displacementL);
        json.put(LineCircleIntersection.LINE_GISEMENT, this.gisementL);
        json.put(LineCircleIntersection.LINE_DISTANCE, this.distanceL);
        json.put(LineCircleIntersection.CIRCLE_POINT_CENTER_NUMBER, this.centerC.getNumber());
        json.put(LineCircleIntersection.CIRCLE_RADIUS, this.radiusC);

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);

        Point p1L = SharedResources.getSetOfPoints().find(
                json.getString(LineCircleIntersection.LINE_POINT_ONE_NUMBER));
        Point p2L = SharedResources.getSetOfPoints().find(
                json.getString(LineCircleIntersection.LINE_POINT_TWO_NUMBER));
        double displacement = json.getDouble(LineCircleIntersection.LINE_DISPLACEMENT);
        double gisement = json.getDouble(LineCircleIntersection.LINE_GISEMENT);
        double distance = json.getDouble(LineCircleIntersection.LINE_DISTANCE);

        Point centerC = SharedResources.getSetOfPoints().find(
                json.getString(LineCircleIntersection.CIRCLE_POINT_CENTER_NUMBER));
        double radiusC = json.getDouble(LineCircleIntersection.CIRCLE_RADIUS);

        this.initAttributes(p1L, p2L, displacement, gisement, distance, centerC, radiusC);
    }

    @Override
    public Class<?> getActivityClass() {
        return LineCircleIntersectionActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_line_circle_intersection);
    }

    public Point getP1L() {
        return this.p1L;
    }

    public Point getP2L() {
        return this.p2L;
    }

    public double getDisplacementL() {
        return this.displacementL;
    }

    public double getGisementL() {
        return this.gisementL;
    }

    public double getDistanceL() {
        return this.distanceL;
    }

    public Point getCenterC() {
        return this.centerC;
    }

    public double getRadiusC() {
        return this.radiusC;
    }

    public Point getFirstIntersection() {
        return this.firstIntersection;
    }

    public Point getSecondIntersection() {
        return this.secondIntersection;
    }
}
