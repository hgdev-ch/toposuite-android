package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.circle.CircleActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class Circle extends Calculation {
    private static final String POINT_A      = "point_a";
    private static final String POINT_B      = "point_b";
    private static final String POINT_C      = "point_c";
    private static final String POINT_NUMBER = "point_number";

    private Point               pointA;
    private Point               pointB;
    private Point               pointC;
    private String              pointNumber;

    private Point               center;
    private double              radius;

    public Circle(Point _pointA, Point _pointB, Point _pointC, String _pointNumber,
            boolean hasDAO) {
        super(CalculationType.CIRCLE,
                App.getContext().getString(R.string.title_activity_circle),
                hasDAO);

        this.pointA = _pointA;
        this.pointB = _pointB;
        this.pointC = _pointC;
        this.pointNumber = _pointNumber;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public Circle(long id, Date lastModification) {
        super(id, CalculationType.CIRCLE,
                App.getContext().getString(R.string.title_activity_circle),
                lastModification, true);
    }

    @Override
    public void compute() {
        if ((this.pointA == null) || (this.pointB == null)
                || (this.pointC == null)) {
            return;
        }

        double deltaEastAB = this.pointA.getEast() - this.pointB.getEast();
        double deltaNorthAB = this.pointA.getNorth() - this.pointB.getNorth();
        double deltaEastBC = this.pointB.getEast() - this.pointC.getEast();
        double deltaNorthBC = this.pointB.getNorth() - this.pointC.getNorth();

        double deltaSquareEastAB = Math.pow(this.pointA.getEast(), 2)
                - Math.pow(this.pointB.getEast(), 2);
        double deltaSquareNorthAB = Math.pow(this.pointA.getNorth(), 2)
                - Math.pow(this.pointB.getNorth(), 2);
        double deltaSquareEastBC = Math.pow(this.pointB.getEast(), 2)
                - Math.pow(this.pointC.getEast(), 2);
        double deltaSquareNorthBC = Math.pow(this.pointB.getNorth(), 2)
                - Math.pow(this.pointC.getNorth(), 2);

        double factNorm = 2 * ((deltaEastAB * deltaNorthBC) - (deltaNorthAB * deltaEastBC));

        if (!MathUtils.isZero(factNorm)) {
            this.center = new Point(
                    this.pointNumber,
                    ((deltaNorthBC * (deltaSquareEastAB + deltaSquareNorthAB)) -
                            (deltaNorthAB * (deltaSquareEastBC + deltaSquareNorthBC))) / factNorm,
                    ((deltaEastAB * (deltaSquareEastBC + deltaSquareNorthBC)) -
                            (deltaEastBC * (deltaSquareEastAB + deltaSquareNorthAB))) / factNorm,
                    MathUtils.IGNORE_DOUBLE, false, false);
            this.radius = MathUtils.euclideanDistance(this.center, this.pointA);
        }

        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(Circle.POINT_A, this.pointA.getNumber());
        jo.put(Circle.POINT_B, this.pointB.getNumber());
        jo.put(Circle.POINT_C, this.pointC.getNumber());
        jo.put(Circle.POINT_NUMBER, this.pointNumber);

        return jo.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject jo = new JSONObject(jsonInputArgs);
        this.pointA = SharedResources.getSetOfPoints().find(
                jo.getString(Circle.POINT_A));
        this.pointB = SharedResources.getSetOfPoints().find(
                jo.getString(Circle.POINT_B));
        this.pointC = SharedResources.getSetOfPoints().find(
                jo.getString(Circle.POINT_C));
        this.pointNumber = jo.getString(Circle.POINT_NUMBER);
    }

    @Override
    public Class<?> getActivityClass() {
        return CircleActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_circle);
    }

    public Point getPointA() {
        return this.pointA;
    }

    public void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public Point getPointB() {
        return this.pointB;
    }

    public void setPointB(Point pointB) {
        this.pointB = pointB;
    }

    public Point getPointC() {
        return this.pointC;
    }

    public void setPointC(Point pointC) {
        this.pointC = pointC;
    }

    public Point getCenter() {
        return this.center;
    }

    public double getRadius() {
        return this.radius;
    }

    public String getPointNumber() {
        return this.pointNumber;
    }

    public void setPointNumber(String pointNumber) {
        this.pointNumber = pointNumber;
    }
}
