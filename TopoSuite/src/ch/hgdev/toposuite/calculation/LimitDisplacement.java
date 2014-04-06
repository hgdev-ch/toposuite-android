package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.limdispl.LimitDisplacementActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class LimitDisplacement extends Calculation {
    private final static String POINT_A        = "point_a";
    private final static String POINT_B        = "point_b";
    private final static String POINT_C        = "point_c";
    private final static String POINT_D        = "point_d";
    private final static String SURFACE        = "surface";
    private final static String POINT_X_NUMBER = "point_x_number";
    private final static String POINT_Y_NUMBER = "point_y_number";

    private Point               pointA;
    private Point               pointB;
    private Point               pointC;
    private Point               pointD;
    private double              surface;
    private String              pointXNumber;
    private String              pointYNumber;

    private Point               newPointX;
    private Point               newPointY;
    private double              distanceToSouthLimitAD;
    private double              distanceToWestLimitAX;
    private double              distanceToEastLimitDY;

    public LimitDisplacement(Point _pointA, Point _pointB, Point _pointC,
            Point _pointD, double _surface, String _pointXNumber,
            String _pointYNumber, boolean hasDAO) {
        super(
                CalculationType.LIMITDISPL,
                App.getContext().getString(
                        R.string.title_activity_limit_displacement),
                hasDAO);

        this.pointA = _pointA;
        this.pointB = _pointB;
        this.pointC = _pointC;
        this.pointD = _pointD;
        this.surface = _surface;
        this.pointXNumber = _pointXNumber;
        this.pointYNumber = _pointYNumber;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public LimitDisplacement(long id, Date lastModification) {
        super(
                id,
                CalculationType.LIMITDISPL,
                App.getContext().getString(
                        R.string.title_activity_limit_displacement),
                lastModification,
                true);
    }

    @Override
    public void compute() throws CalculationException {
        double distA = MathUtils.euclideanDistance(this.pointA, this.pointD);

        double alphaAngle = new Gisement(this.pointA, this.pointD, false).getGisement() -
                new Gisement(this.pointA, this.pointB, false).getGisement();

        double betaAngle = new Gisement(this.pointD, this.pointC, false).getGisement() -
                new Gisement(this.pointD, this.pointA, false).getGisement();

        double alphaSide = 1 / Math.tan(MathUtils.gradToRad(alphaAngle));
        double betaSide = 1 / Math.tan(MathUtils.gradToRad(betaAngle));

        double distB = Math.sqrt(Math.pow(distA, 2) -
                (2 * this.surface * (alphaSide + betaSide)));

        double distD = (2 * this.surface) / (distA + distB);

        LinesIntersection li = new LinesIntersection(this.pointA, this.pointD, -distD,
                MathUtils.IGNORE_DOUBLE, this.pointA, this.pointB, 0.0,
                MathUtils.IGNORE_DOUBLE, this.pointXNumber, false);
        li.compute();
        this.newPointX = li.getIntersectionPoint();

        li = new LinesIntersection(this.pointA, this.pointD, -distD,
                MathUtils.IGNORE_DOUBLE, this.pointD, this.pointC, 0.0,
                MathUtils.IGNORE_DOUBLE, this.pointYNumber, false);
        li.compute();
        this.newPointY = li.getIntersectionPoint();

        this.distanceToSouthLimitAD = distD;
        this.distanceToWestLimitAX = MathUtils.euclideanDistance(
                this.pointA, this.newPointX);
        this.distanceToEastLimitDY = MathUtils.euclideanDistance(
                this.pointD, this.newPointY);

        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(LimitDisplacement.POINT_A, this.pointA.getNumber());
        jo.put(LimitDisplacement.POINT_B, this.pointB.getNumber());
        jo.put(LimitDisplacement.POINT_C, this.pointC.getNumber());
        jo.put(LimitDisplacement.POINT_D, this.pointD.getNumber());
        jo.put(LimitDisplacement.SURFACE, this.surface);
        jo.put(LimitDisplacement.POINT_X_NUMBER, this.pointXNumber);
        jo.put(LimitDisplacement.POINT_Y_NUMBER, this.pointYNumber);

        return jo.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject jo = new JSONObject(jsonInputArgs);
        this.pointA = SharedResources.getSetOfPoints().find(
                jo.getString(LimitDisplacement.POINT_A));
        this.pointB = SharedResources.getSetOfPoints().find(
                jo.getString(LimitDisplacement.POINT_B));
        this.pointC = SharedResources.getSetOfPoints().find(
                jo.getString(LimitDisplacement.POINT_C));
        this.pointD = SharedResources.getSetOfPoints().find(
                jo.getString(LimitDisplacement.POINT_D));
        this.surface = jo.getDouble(LimitDisplacement.SURFACE);
        this.pointXNumber = jo.getString(LimitDisplacement.POINT_X_NUMBER);
        this.pointYNumber = jo.getString(LimitDisplacement.POINT_Y_NUMBER);
    }

    @Override
    public Class<?> getActivityClass() {
        return LimitDisplacementActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(
                R.string.title_activity_limit_displacement);
    }

    public final Point getPointA() {
        return this.pointA;
    }

    public final void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public final Point getPointB() {
        return this.pointB;
    }

    public final void setPointB(Point pointB) {
        this.pointB = pointB;
    }

    public final Point getPointC() {
        return this.pointC;
    }

    public final void setPointC(Point pointC) {
        this.pointC = pointC;
    }

    public final Point getPointD() {
        return this.pointD;
    }

    public final void setPointD(Point pointD) {
        this.pointD = pointD;
    }

    public final double getSurface() {
        return this.surface;
    }

    public final void setSurface(double surface) {
        this.surface = surface;
    }

    public final String getPointXNumber() {
        return this.pointXNumber;
    }

    public final void setPointXNumber(String pointXNumber) {
        this.pointXNumber = pointXNumber;
    }

    public final String getPointYNumber() {
        return this.pointYNumber;
    }

    public final void setPointYNumber(String pointYNumber) {
        this.pointYNumber = pointYNumber;
    }

    public final Point getNewPointX() {
        return this.newPointX;
    }

    public final Point getNewPointY() {
        return this.newPointY;
    }

    public final double getDistanceToSouthLimitAD() {
        return this.distanceToSouthLimitAD;
    }

    public final double getDistanceToWestLimitAX() {
        return this.distanceToWestLimitAX;
    }

    public final double getDistanceToEastLimitDY() {
        return this.distanceToEastLimitDY;
    }
}
