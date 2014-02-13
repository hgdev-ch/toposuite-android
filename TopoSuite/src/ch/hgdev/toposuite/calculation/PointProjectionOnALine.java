package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Calculate the projection of a point on a line.
 * 
 * @author HGdev
 */
public class PointProjectionOnALine extends Calculation {
    public static final String  CALCULATION_NAME = "Proj. pt. sur droite";

    private static final double DISTANCE         = 20.0;

    private int                 number;
    private Point               p1;
    private Point               p2;
    private Point               ptToProj;
    private double              displacement;
    private double              gisement;
    private Mode                mode;

    private Point               projPt;
    private double              distPtToLine;
    private double              distPtToP1;
    private double              distPtToP2;

    public PointProjectionOnALine(int _number, Point _p1, Point _p2, Point _ptToProj,
            double _displacement,
            PointProjectionOnALine.Mode _mode, boolean hasDAO) {
        super(CalculationType.PROJPT, PointProjectionOnALine.CALCULATION_NAME, hasDAO);

        this.number = _number;
        this.p1 = _p1;
        this.p2 = _p2;
        this.ptToProj = _ptToProj;
        this.displacement = _displacement;
        this.mode = _mode;

        if (hasDAO) {
            // TODO add into history!
        }
    }

    public PointProjectionOnALine(int _number, Point _p1, double gisement, Point _ptToProj,
            double _displacement, boolean hasDAO) {
        this(
                _number,
                _p1,
                PointProjectionOnALine.pointFromGisement(_p1, gisement),
                _ptToProj,
                _displacement,
                PointProjectionOnALine.Mode.LINE,
                hasDAO);
    }

    public PointProjectionOnALine(int _number, Point _p1, Point _p2, Point _ptToProj,
            double _displacement, boolean hasDAO) {
        this(
                _number,
                _p1,
                _p2,
                _ptToProj,
                _displacement,
                PointProjectionOnALine.Mode.LINE, hasDAO);
    }

    public PointProjectionOnALine(int _number, Point _p1, Point _p2, Point _ptToProj,
            boolean hasDAO) {
        this(_number, _p1, _p2, _ptToProj, 0.0, hasDAO);
    }

    public PointProjectionOnALine(long id, Date lastModification) {
        super(id, CalculationType.PROJPT, PointProjectionOnALine.CALCULATION_NAME,
                lastModification, true);
    }

    public void compute() {
        // displacement gisement
        double displGis = 0.0;

        Gisement g;

        // if a displacement is supplied by the used, we need to update the
        // points p1 and p2. New points will be created to ensure that the
        // original ones are not overwritten.
        if (!MathUtils.isZero(this.displacement)) {
            g = new Gisement(this.p1, this.p2, false);
            g.compute();

            displGis = g.getGisement();
            displGis += (MathUtils.isNegative(this.displacement)) ? -100 : 100;

            // create the points
            this.p1 = new Point(
                    this.p1.getNumber(),
                    MathUtils.pointLanceEast(this.p1.getEast(), displGis,
                            Math.abs(this.displacement)),
                    MathUtils.pointLanceNorth(this.p1.getNorth(), displGis,
                            Math.abs(this.displacement)),
                    0.0, false, false);
            this.p2 = new Point(
                    this.p2.getNumber(),
                    MathUtils.pointLanceEast(this.p2.getEast(), displGis,
                            Math.abs(this.displacement)),
                    MathUtils.pointLanceNorth(this.p2.getNorth(), displGis,
                            Math.abs(this.displacement)),
                    0.0, false, false);
        }

        g = new Gisement(this.p1, this.p2, false);
        displGis = g.getGisement() + 100;

        Point tmpPt = new Point(
                42,
                MathUtils.pointLanceEast(this.ptToProj.getEast(), displGis,
                        PointProjectionOnALine.DISTANCE),
                MathUtils.pointLanceNorth(this.ptToProj.getNorth(), displGis,
                        PointProjectionOnALine.DISTANCE),
                0.0, false, false);

        // calculation of the triangle angles according to the following schema:
        //   D         B
        //    \       /
        //     \     /
        //      \   /
        //       \P/
        //        X
        //       / \
        //      /   \
        //     /     \
        //    /       \
        // A /_________\ C
        //
        // Let <alpha be the angle AC-AB and
        // Let <gamma be the angle CD-CA and
        // Let <P be the angle AB-DC, which is 200 - <alpha - <gamma

        double alphaAngle = new Gisement(this.p1, this.ptToProj, false).getGisement() -
                new Gisement(this.p1, this.p2, false).getGisement();
        double gammaAngle = new Gisement(this.ptToProj, tmpPt, false).getGisement() -
                new Gisement(this.ptToProj, this.p1, false).getGisement();
        double pAngle = 200 - alphaAngle - gammaAngle;

        // calculation of the intersection point
        double dist = (MathUtils.euclideanDistance(this.p1, this.ptToProj) *
                Math.sin(MathUtils.gradToRad(gammaAngle))) /
                Math.sin(MathUtils.gradToRad(pAngle));

        double gis = new Gisement(this.p1, this.p2).getGisement();

        // projected point aka the one we want :)
        this.projPt = new Point(
                this.number,
                MathUtils.pointLanceEast(this.p1.getEast(), gis, dist),
                MathUtils.pointLanceNorth(this.p1.getNorth(), gis, dist),
                0.0, false, false);

        // distance point to line
        this.distPtToLine = MathUtils.euclideanDistance(this.ptToProj, this.projPt);

        // distance point to p1
        this.distPtToP1 = MathUtils.euclideanDistance(this.projPt, this.p1);

        // distance point to p2
        this.distPtToP2 = MathUtils.euclideanDistance(this.projPt, this.p2);

        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO Auto-generated method stub

    }

    @Override
    public Class<?> getActivityClass() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Create a point from a given gisement and a point. The new point is
     * determined using the "point lancé".
     * 
     * Note that the created point is not stored in the global list of points.
     * 
     * @param p1
     *            a point
     * @param gisement
     *            a gisement
     * @return a new point
     */
    public static Point pointFromGisement(Point p1, double gisement) {
        double east = MathUtils.pointLanceEast(p1.getEast(), gisement,
                PointProjectionOnALine.DISTANCE);
        double north = MathUtils.pointLanceNorth(p1.getNorth(), gisement,
                PointProjectionOnALine.DISTANCE);
        return new Point(4242, east, north, 0.0, false, false);
    }

    public Point getP1() {
        return this.p1;
    }

    public void setP1(Point _p1) {
        this.p1 = _p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public void setP2(Point _p2) {
        this.p2 = _p2;
    }

    public double getDisplacement() {
        return this.displacement;
    }

    public void setDisplacement(double _displacement) {
        this.displacement = _displacement;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode _mode) {
        this.mode = _mode;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int _number) {
        this.number = _number;
    }

    public Point getPtToProj() {
        return this.ptToProj;
    }

    public void setPtToProj(Point _ptToProj) {
        this.ptToProj = _ptToProj;
    }

    public double getGisement() {
        return this.gisement;
    }

    public void setGisement(double _gisement) {
        this.gisement = _gisement;
    }

    public Point getProjPt() {
        return this.projPt;
    }

    public void setProjPt(Point _projPt) {
        this.projPt = _projPt;
    }

    public double getDistPtToLine() {
        return this.distPtToLine;
    }

    public void setDistPtToLine(double _distPtToLine) {
        this.distPtToLine = _distPtToLine;
    }

    public double getDistPtToP1() {
        return this.distPtToP1;
    }

    public void setDistPtToP1(double _distPtToP1) {
        this.distPtToP1 = _distPtToP1;
    }

    public double getDistPtToP2() {
        return this.distPtToP2;
    }

    public void setDistPtToP2(double _distPtToP2) {
        this.distPtToP2 = _distPtToP2;
    }

    public enum Mode {
        LINE,
        GISEMENT
    }
}