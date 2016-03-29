package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.linesintersec.LinesIntersectionActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

import com.google.common.math.DoubleMath;

public class LinesIntersection extends Calculation {
    private static final String P1D1_NUMBER  = "p1d1_number";
    private static final String P2D1_NUMBER  = "p2d1_number";
    private static final String P1D2_NUMBER  = "p1d2_number";
    private static final String P2D2_NUMBER  = "p2d2_number";
    private static final String DISPL_D1     = "displ_d1";
    private static final String DISPL_D2     = "displ_d2";
    private static final String DIST_D1      = "dist_d1";
    private static final String DIST_D2      = "dist_d2";
    private static final String GIS_D1       = "gis_d1";
    private static final String GIS_D2       = "gis_d2";
    private static final String POINT_NUMBER = "point_number";

    private Point               p1D1;
    private Point               p2D1;
    private double              displacementD1;
    private double              gisementD1;
    private double              distanceP1D1;

    private Point               p1D2;
    private Point               p2D2;
    private double              displacementD2;
    private double              gisementD2;
    private double              distanceP1D2;

    private String              pointNumber;

    private Point               intersectionPoint;

    public LinesIntersection(Point _p1D1, Point _p2D1, double _displacementD1,
            double _distanceP1D1, Point _p1D2, Point _p2D2, double _displacementD2,
            double _distanceP1D2, String _pointNumber, boolean hasDAO) {

        this(_p1D1, _p2D1, _displacementD1, 0.0, _distanceP1D1, _p1D2, _p2D2,
                _displacementD2, 0.0, _distanceP1D2, _pointNumber, hasDAO);
    }

    public LinesIntersection(Point _p1D1, Point _p2D1, double _displacementD1,
            double _distanceP1D1, Point _p1D2, double _gisementD2, double _displacementD2,
            double _distanceP1D2, String _pointNumber, boolean hasDAO) {

        this(_p1D1, _p2D1, _displacementD1, 0.0, _distanceP1D1, _p1D2, null,
                _displacementD2, _gisementD2, _distanceP1D2, _pointNumber, hasDAO);
    }

    public LinesIntersection(Point _p1D1, double _gisementD1, double _displacementD1,
            double _distanceP1D1, Point _p1D2, Point _p2D2, double _displacementD2,
            double _distanceP1D2, String _pointNumber, boolean hasDAO) {

        this(_p1D1, null, _displacementD1, _gisementD1, _distanceP1D1, _p1D2, _p2D2,
                _displacementD2, 0.0, _distanceP1D2, _pointNumber, hasDAO);
    }

    public LinesIntersection(Point _p1D1, double _gisementD1, double _displacementD1,
            double _distanceP1D1, Point _p1D2, double _gisementD2, double _displacementD2,
            double _distanceP1D2, String _pointNumber, boolean hasDAO) {

        this(_p1D1, null, _displacementD1, _gisementD1, _distanceP1D1, _p1D2, null,
                _displacementD2, _gisementD2, _distanceP1D2, _pointNumber, hasDAO);
    }

    public LinesIntersection(long id, Date lastModification) {
        super(
                id,
                CalculationType.LINEINTERSEC,
                App.getContext().getString(R.string.title_activity_lines_intersection),
                lastModification,
                true);
    }

    public LinesIntersection(Point _p1D1, Point _p2D1, double _displacementD1,
            double _gisementD1, double _distanceP1D1, Point _p1D2, Point _p2D2,
            double _displacementD2, double _gisementD2, double _distanceP1D2,
            String _pointNumber, boolean hasDAO) {

        super(
                CalculationType.LINEINTERSEC,
                App.getContext().getString(R.string.title_activity_lines_intersection),
                hasDAO);

        this.setP1D1(_p1D1);
        this.setGisementD1(_gisementD1);

        if (_p2D1 == null) {
            this.setP2D1(_gisementD1);
        } else {
            this.setP2D1(_p2D1);
        }

        this.setDisplacementD1(_displacementD1);
        this.setDistanceP1D1(_distanceP1D1);

        this.setP1D2(_p1D2);
        this.setGisementD2(_gisementD2);

        if (_p2D2 == null) {
            this.setP2D2(_gisementD2);
        } else {
            this.setP2D2(_p2D2);
        }

        this.setDisplacementD2(_displacementD2);
        this.setDistanceP1D2(_distanceP1D2);

        this.pointNumber = _pointNumber;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    @Override
    public void compute() throws CalculationException {
        // check for points equality
        if ((MathUtils.equals(this.p1D1, this.p1D2, App.getCoordinateTolerance())
                && MathUtils.equals(this.p2D1, this.p2D2, App.getCoordinateTolerance()))
                || (MathUtils.equals(this.p1D1, this.p2D2, App.getCoordinateTolerance())
                && MathUtils.equals(this.p2D1, this.p1D2, App.getCoordinateTolerance()))) {
            throw new CalculationException(App.getContext().getString(
                    R.string.error_impossible_calculation));
        }

        double alphaAngle, gammaAngle, pAngle, displGis;

        if (!MathUtils.isZero(this.displacementD1)) {
            displGis = new Gisement(this.p1D1, this.p2D1, false).getGisement();
            displGis += (MathUtils.isNegative(this.displacementD1)) ? -100 : 100;

            this.p1D1.setEast(MathUtils.pointLanceEast(this.p1D1.getEast(),
                    displGis, Math.abs(this.displacementD1)));
            this.p1D1.setNorth(MathUtils.pointLanceNorth(this.p1D1.getNorth(),
                    displGis, Math.abs(this.displacementD1)));

            this.p2D1.setEast(MathUtils.pointLanceEast(this.p2D1.getEast(),
                    displGis, Math.abs(this.displacementD1)));
            this.p2D1.setNorth(MathUtils.pointLanceNorth(this.p2D1.getNorth(),
                    displGis, Math.abs(this.displacementD1)));
        }

        if (!MathUtils.isZero(this.displacementD2)) {
            displGis = new Gisement(this.p1D2, this.p2D2, false).getGisement();
            displGis += (MathUtils.isNegative(this.displacementD2)) ? -100 : 100;

            this.p1D2.setEast(MathUtils.pointLanceEast(this.p1D2.getEast(),
                    displGis, Math.abs(this.displacementD2)));
            this.p1D2.setNorth(MathUtils.pointLanceNorth(this.p1D2.getNorth(),
                    displGis, Math.abs(this.displacementD2)));

            this.p2D2.setEast(MathUtils.pointLanceEast(this.p2D2.getEast(),
                    displGis, Math.abs(this.displacementD2)));
            this.p2D2.setNorth(MathUtils.pointLanceNorth(this.p2D2.getNorth(),
                    displGis, Math.abs(this.displacementD2)));
        }

        // calculation of the triangle angles according to the following schema:
        // @formatter:off
        //
        //  D         B
        //   \       /
        //    \     /
        //     \   /
        //      \P/
        //       X
        //      / \
        //     /   \
        //    /     \
        //   /       \
        // A/_________\C
        //
        // @formatter:on
        //
        // Let <alpha be the angle AC-AB and
        // Let <gamma be the angle CD-CA and
        // Let <P be the angle AB-DC, which is 200 - <alpha - <gamma

        alphaAngle = new Gisement(this.p1D1, this.p1D2, false).getGisement() -
                new Gisement(this.p1D1, this.p2D1, false).getGisement();

        gammaAngle = new Gisement(this.p1D2, this.p2D2, false).getGisement() -
                new Gisement(this.p1D2, this.p1D1, false).getGisement();

        pAngle = 200 - alphaAngle - gammaAngle;

        double stPtIntersecDist = (MathUtils.euclideanDistance(this.p1D1, this.p1D2) *
                Math.sin(MathUtils.gradToRad(gammaAngle))) /
                Math.sin(MathUtils.gradToRad(pAngle));

        double stPtIntersecGis = new Gisement(this.p1D1, this.p2D1, false)
                .getGisement();

        // check if the lines are coincident
        if (DoubleMath.fuzzyEquals(alphaAngle, -gammaAngle, App.getAngleTolerance()) &&
                MathUtils.isZero(stPtIntersecDist)) {
            throw new CalculationException(
                    App.getContext().getString(R.string.error_impossible_calculation));
        }

        double east = MathUtils.pointLanceEast(this.p1D1.getEast(),
                stPtIntersecGis, stPtIntersecDist);
        double north = MathUtils.pointLanceNorth(this.p1D1.getNorth(),
                stPtIntersecGis, stPtIntersecDist);

        this.intersectionPoint = new Point(this.pointNumber, east, north, MathUtils.IGNORE_DOUBLE,
                false, false);

        this.updateLastModification();
        this.setDescription(this.getCalculationName() + " - "
                + App.getContext().getString(R.string.line_1) + " "
                + App.getContext().getString(R.string.origin_label) + ": "
                + this.p1D1.toString()
                + " / " + App.getContext().getString(R.string.line_2) + " "
                + App.getContext().getString(R.string.origin_label) + ": "
                + this.p1D2.toString());
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject jo = new JSONObject();

        jo.put(LinesIntersection.P1D1_NUMBER, this.p1D1.getNumber());
        jo.put(LinesIntersection.P2D1_NUMBER, this.p2D1.getNumber());
        jo.put(LinesIntersection.P1D2_NUMBER, this.p1D2.getNumber());
        jo.put(LinesIntersection.P2D2_NUMBER, this.p2D2.getNumber());
        jo.put(LinesIntersection.DISPL_D1, this.displacementD1);
        jo.put(LinesIntersection.DISPL_D2, this.displacementD2);
        jo.put(LinesIntersection.DIST_D1, this.distanceP1D1);
        jo.put(LinesIntersection.DIST_D2, this.distanceP1D2);
        jo.put(LinesIntersection.GIS_D1, this.gisementD1);
        jo.put(LinesIntersection.GIS_D2, this.gisementD2);
        jo.put(LinesIntersection.POINT_NUMBER, this.pointNumber);

        return jo.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject jo = new JSONObject(jsonInputArgs);

        this.setP1D1(this.p1D1 = SharedResources.getSetOfPoints().find(
                jo.getString(LinesIntersection.P1D1_NUMBER)));

        String p2D1Position = jo.getString(LinesIntersection.P2D1_NUMBER);
        if (!p2D1Position.isEmpty()) {
            this.setP2D1(SharedResources.getSetOfPoints().find(p2D1Position));
        } else {
            this.setGisementD1(jo.getDouble(LinesIntersection.GIS_D1));
        }

        this.setP1D2(SharedResources.getSetOfPoints().find(
                jo.getString(LinesIntersection.P1D2_NUMBER)));

        String p2D2Position = jo.getString(LinesIntersection.P2D2_NUMBER);
        if (!p2D2Position.isEmpty()) {
            this.setP2D2(SharedResources.getSetOfPoints().find(p2D2Position));
        } else {
            this.setGisementD2(jo.getDouble(LinesIntersection.GIS_D2));
        }

        this.setDisplacementD1(jo.getDouble(LinesIntersection.DISPL_D1));
        this.setDisplacementD2(jo.getDouble(LinesIntersection.DISPL_D2));
        this.setDistanceP1D1(jo.getDouble(LinesIntersection.DIST_D1));
        this.setDistanceP1D2(jo.getDouble(LinesIntersection.DIST_D2));
        this.setPointNumber(jo.getString(LinesIntersection.POINT_NUMBER));
    }

    @Override
    public Class<?> getActivityClass() {
        return LinesIntersectionActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_lines_intersection);
    }

    public final Point getP1D1() {
        return this.p1D1;
    }

    public final void setP1D1(Point _p1d1) {
        if (_p1d1 == null) {
            this.p1D1 = null;
            return;
        }

        this.p1D1 = _p1d1.clone();
        this.p1D1.setNumber(_p1d1.getNumber());
    }

    public final Point getP2D1() {
        return this.p2D1;
    }

    public final void setP2D1(Point _p2d1) {
        if (_p2d1 == null) {
            this.p2D1 = null;
            return;
        }

        this.p2D1 = _p2d1.clone();
        this.p2D1.setNumber(_p2d1.getNumber());
    }

    public final void setP2D1(double gisement) {
        if (this.p1D1 == null) {
            this.p2D1 = null;
            return;
        }

        double east = MathUtils.pointLanceEast(
                this.p1D1.getEast(), gisement, 100);
        double north = MathUtils.pointLanceNorth(
                this.p1D1.getNorth(), gisement, 100);
        this.p2D1 = new Point("", east, north, 0.0, false, false);
    }

    public final double getDisplacementD1() {
        return this.displacementD1;
    }

    public final void setDisplacementD1(double _displacementD1) {
        this.displacementD1 = _displacementD1;
    }

    public final double getGisementD1() {
        return this.gisementD1;
    }

    public final void setGisementD1(double _gisementD1) {
        this.gisementD1 = _gisementD1;
        if (!MathUtils.isZero(this.gisementD1)) {
            this.setP2D1(this.gisementD1);
        }
    }

    public final double getDistanceP1D1() {
        return this.distanceP1D1;
    }

    public final void setDistanceP1D1(double _distanceP1D1) {
        this.distanceP1D1 = _distanceP1D1;
        if (!MathUtils.isIgnorable(this.distanceP1D1)) {
            if ((this.p1D1 == null) || (this.p2D1 == null)) {
                return;
            }

            double gis = new Gisement(
                    this.p1D1, this.p2D1, false).getGisement();

            this.p1D1.setEast(
                    MathUtils.pointLanceEast(this.p1D1.getEast(),
                            gis, this.distanceP1D1));
            this.p1D1.setNorth(
                    MathUtils.pointLanceNorth(this.p1D1.getNorth(),
                            gis, this.distanceP1D1));

            gis += 100;
            this.p2D1.setEast(
                    MathUtils.pointLanceEast(this.p1D1.getEast(),
                            gis, 100));
            this.p2D1.setNorth(
                    MathUtils.pointLanceNorth(this.p1D1.getNorth(),
                            gis, 100));
        }
    }

    public final Point getP1D2() {
        return this.p1D2;
    }

    public final void setP1D2(Point _p1d2) {
        if (_p1d2 == null) {
            this.p1D2 = null;
            return;
        }

        this.p1D2 = _p1d2.clone();
        this.p1D2.setNumber(_p1d2.getNumber());
    }

    public final Point getP2D2() {
        return this.p2D2;
    }

    public final void setP2D2(Point _p2d2) {
        if (_p2d2 == null) {
            this.p2D2 = null;
            return;
        }

        this.p2D2 = _p2d2.clone();
        this.p2D2.setNumber(_p2d2.getNumber());
    }

    public final void setP2D2(double gisement) {
        if (this.p1D2 == null) {
            this.p2D2 = null;
            return;
        }

        double east = MathUtils.pointLanceEast(
                this.p1D2.getEast(), gisement, 100);
        double north = MathUtils.pointLanceNorth(
                this.p1D2.getNorth(), gisement, 100);
        this.p2D2 = new Point("", east, north, MathUtils.IGNORE_DOUBLE, false, false);
    }

    public final double getDisplacementD2() {
        return this.displacementD2;
    }

    public final void setDisplacementD2(double _displacementD2) {
        this.displacementD2 = _displacementD2;
    }

    public final double getGisementD2() {
        return this.gisementD2;
    }

    public final void setGisementD2(double _gisementD2) {
        this.gisementD2 = _gisementD2;
        if (!MathUtils.isZero(this.gisementD2)) {
            this.setP2D2(this.gisementD2);
        }
    }

    public final double getDistanceP1D2() {
        return this.distanceP1D2;
    }

    public final void setDistanceP1D2(double _distanceP1D2) {
        this.distanceP1D2 = _distanceP1D2;
        if (!MathUtils.isIgnorable(this.distanceP1D2)) {
            if ((this.p1D2 == null) || (this.p2D2 == null)) {
                return;
            }

            double gis = new Gisement(
                    this.p1D2, this.p2D2, false).getGisement();

            this.p1D2.setEast(
                    MathUtils.pointLanceEast(this.p1D2.getEast(),
                            gis, this.distanceP1D2));
            this.p1D2.setNorth(
                    MathUtils.pointLanceNorth(this.p1D2.getNorth(),
                            gis, this.distanceP1D2));

            gis += 100;
            this.p2D2.setEast(
                    MathUtils.pointLanceEast(this.p1D2.getEast(),
                            gis, 100));
            this.p2D2.setNorth(
                    MathUtils.pointLanceNorth(this.p1D2.getNorth(),
                            gis, 100));
        }
    }

    public String getPointNumber() {
        return this.pointNumber;
    }

    public void setPointNumber(String _pointNumber) {
        // also update the intersection point
        if ((this.intersectionPoint != null) && (!this.pointNumber.equals(_pointNumber))) {
            this.intersectionPoint = new Point(false);
            this.intersectionPoint.setNumber(_pointNumber);
        }

        this.pointNumber = _pointNumber;
    }

    public final Point getIntersectionPoint() {
        return this.intersectionPoint;
    }
}
