package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class FreeStation extends Calculation {
    public static final String      CALCULATION_NAME = "Station libre";

    private int                     stationNumber;
    private ArrayList<Measure>      measures;
    /** Height of the instrument (I). */
    private double                  i;

    private Point                   stationResult;
    private final ArrayList<Result> results;
    private double                  mse;
    private double                  unknownOrientation;

    public FreeStation(int _stationNumber, boolean hasDAO) {
        super(CalculationType.FREESTATION, FreeStation.CALCULATION_NAME, hasDAO);

        this.stationNumber = _stationNumber;
        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<Result>();

        if (hasDAO) {
            // TODO uncomment when the calculation works
            // SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public FreeStation(long id, Date lastModification) {
        super(id, CalculationType.FREESTATION, FreeStation.CALCULATION_NAME,
                lastModification, true);

        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<Result>();
    }

    @Override
    public void compute() {
        if (this.measures.size() < 1) {
            return;
        }

        this.results.clear();

        double unknOrient = 0.0;

        for (Measure m : this.measures) {
            Point res = new Point(m.getPoint().getNumber(), m.getPoint().getEast(),
                    m.getPoint().getNorth(), m.getPoint().getAltitude(),
                    false, false);

            double horizDist = m.getDistance() * Math.sin(
                    MathUtils.gradToRad(m.getZenAngle()));
            double gis = MathUtils.modulo400(unknOrient + m.getHorizDir());

            if (!MathUtils.isZero(m.getLatDepl())) {
                double angle = Math.asin(Math.abs(m.getLatDepl() / horizDist));
                horizDist = Math.abs(m.getLatDepl()) / Math.tan(angle);

                // correction of the direction [g]
                gis += (MathUtils.isPositive(m.getLatDepl()))
                        ? MathUtils.radToGrad(angle) : -MathUtils.radToGrad(angle);
            }

            res.setEast(MathUtils.pointLanceEast(0, gis, horizDist));
            res.setNorth(MathUtils.pointLanceNorth(0, gis, horizDist));

            if (MathUtils.isPositive(m.getPoint().getAltitude())) {
                res.setAltitude(res.getAltitude() - MathUtils.nivellTrigo(
                        horizDist, m.getZenAngle(), this.i, m.getS(), 0.0));
                // TODO calculate the mean altitude
            }

            this.results.add(new Result(res));
        }

        // TODO finish the calculation...
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

    public int getStationNumber() {
        return this.stationNumber;
    }

    public void setStationNumber(int _stationNumber) {
        this.stationNumber = _stationNumber;
    }

    public ArrayList<Measure> getMeasures() {
        return this.measures;
    }

    public void setMeasures(ArrayList<Measure> _measures) {
        this.measures = _measures;
    }

    public Point getStationResult() {
        return this.stationResult;
    }

    public void setStationResult(Point _stationResult) {
        this.stationResult = _stationResult;
    }

    public double getMse() {
        return this.mse;
    }

    public void setMse(double _mse) {
        this.mse = _mse;
    }

    public double getUnknownOrientation() {
        return this.unknownOrientation;
    }

    public void setUnknownOrientation(double _unknownOrientation) {
        this.unknownOrientation = _unknownOrientation;
    }

    public static class Result {
        /** The target point. */
        private Point  point;

        /**
         * Difference between the east coordinate of the original point and the
         * new one.
         */
        private double vE;

        /**
         * Difference between the north coordinate of the original point and the
         * new one.
         */
        private double vN;

        /**
         * Difference between the altitude of the original point and the new
         * one.
         */
        private double vA;

        public Result(Point _point, double _vE, double _vN, double _vA) {
            this.point = _point;
            this.vE = _vE;
            this.vN = _vN;
            this.vA = _vA;
        }

        public Result(Point _point) {
            this(_point, 0.0, 0.0, 0.0);
        }

        public Point getPoint() {
            return this.point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public double getvE() {
            return this.vE;
        }

        public void setvE(double vE) {
            this.vE = vE;
        }

        public double getvN() {
            return this.vN;
        }

        public void setvN(double vN) {
            this.vN = vN;
        }

        public double getvA() {
            return this.vA;
        }

        public void setvA(double vA) {
            this.vA = vA;
        }
    }
}