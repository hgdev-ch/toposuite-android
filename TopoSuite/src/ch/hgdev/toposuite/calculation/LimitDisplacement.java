package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class LimitDisplacement extends Calculation {
    private Point  pointA;
    private Point  pointB;
    private Point  pointC;
    private Point  pointD;
    private double surface;
    private int    pointXNumber;
    private int    pointYNumber;

    private Point  newPointX;
    private Point  newPointY;
    private double distanceToSouthLimitAD;
    private double distanceToWestLimitAX;
    private double distanceToEastLimitDY;

    public LimitDisplacement(Point _pointA, Point _pointB, Point _pointC,
            Point _pointD, double _surface, int _pointXNumber,
            int _pointYNumber, boolean hasDAO) {
        super(CalculationType.LIMITDISPL, "TODO", hasDAO);

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
        super(id, CalculationType.LIMITDISPL, "TODO",
                lastModification, true);
    }

    @Override
    public void compute() {
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
                0.0, this.pointA, this.pointB, 0.0, 0, this.pointXNumber, false);
        li.compute();
        this.newPointX = li.getIntersectionPoint();

        li = new LinesIntersection(this.pointA, this.pointD, -distD, 0.0,
                this.pointD, this.pointC, 0.0, 0, this.pointYNumber, false);
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
        // TODO
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO
    }

    @Override
    public Class<?> getActivityClass() {
        // TODO
        return null;
    }

    @Override
    public String getCalculationName() {
        // TODO
        return "TODO";
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

    public final int getPointXNumber() {
        return this.pointXNumber;
    }

    public final void setPointXNumber(int pointXNumber) {
        this.pointXNumber = pointXNumber;
    }

    public final int getPointYNumber() {
        return this.pointYNumber;
    }

    public final void setPointYNumber(int pointYNumber) {
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
