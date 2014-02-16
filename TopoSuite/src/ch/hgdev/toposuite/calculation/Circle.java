package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class Circle extends Calculation {

    private Point  pointA;
    private Point  pointB;
    private Point  pointC;
    private int    pointNumber;

    private Point  center;
    private double radius;

    public Circle(Point _pointA, Point _pointB, Point _pointC, int _pointNumber,
            boolean hasDAO) {
        super(CalculationType.CIRCLE, "Cercle par 3 points", hasDAO);

        this.pointA = _pointA;
        this.pointB = _pointB;
        this.pointC = _pointC;
        this.pointNumber = _pointNumber;

        if (hasDAO) {
            // TODO add this calculation to the history
        }
    }

    public Circle(long id, Date lastModification) {
        super(id, null, "Cercle par 3 points", lastModification, true);
    }

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
                    0.0, false, false);
            this.radius = MathUtils.euclideanDistance(this.center, this.pointA);
        }
    }

    @Override
    public String exportToJSON() throws JSONException {
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO
    }

    @Override
    public Class<?> getActivityClass() {
        return null;
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

    public int getPointNumber() {
        return this.pointNumber;
    }

    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
    }
}
