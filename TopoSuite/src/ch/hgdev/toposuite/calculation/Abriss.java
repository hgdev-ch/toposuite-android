package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;

import org.json.JSONException;

import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class Abriss extends Calculation {
    private static final String CALCULATION_NAME = "Orient. st.";

    private Point               station;
    private ArrayList<Measure>  measures;

    private ArrayList<Result>   results;
    private double              mean;
    private double              meanError;

    public Abriss() {
        super(CalculationType.ABRISS, Abriss.CALCULATION_NAME);

        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<Abriss.Result>();
        this.mean = 0.0;
        this.meanError = 0.0;
    }

    public Abriss(Point station) {
        this();
        this.station = station;
    }

    /**
     * Perform the the computation.
     */
    public void compute() {
        for (Measure m : this.measures) {
            Gisement g = new Gisement(this.station, m.getOrientation());
            double z0 = MathUtils.modulo400(g.getGisement() - m.getHorizDir());

            Result r = new Result(m.getOrientation(), g.getHorizDist(),
                    z0, 0.0, 0.0, 0.0, 0.0);

            this.results.add(r);
            this.mean += z0;
        }

        this.mean /= this.measures.size();

        int index = 0;
        for (Measure m : this.measures) {
            double orientDir = MathUtils.modulo400(this.mean + m.getHorizDir());
            this.results.get(index).setOrientedDirection(orientDir);
            index++;
        }
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

    public Point getStation() {
        return this.station;
    }

    public void setStation(Point station) {
        this.station = station;
    }

    public ArrayList<Measure> getMeasures() {
        return this.measures;
    }

    public ArrayList<Result> getResults() {
        return this.results;
    }

    public double getMean() {
        return this.mean;
    }

    public double getMeanError() {
        return this.meanError;
    }

    public class Result {
        private Point  orientation;
        private double distance;
        private double unknownOrientation;
        private double orientatedDirection;
        private double errAngle;
        private double errTrans;
        private double errLong;

        public Result(Point _orientation, double _distance, double _unknownOrientation,
                double _orientationDirection, double _errAngle, double _errTrans, double _errLong) {
            this.orientation = _orientation;
            this.distance = _distance;
            this.unknownOrientation = _unknownOrientation;
            this.orientatedDirection = _orientationDirection;
            this.errAngle = _errAngle;
            this.errTrans = _errTrans;
            this.errLong = _errLong;
        }

        public Point getOrientation() {
            return this.orientation;
        }

        public double getDistance() {
            return this.distance;
        }

        public double getUnknownOrientation() {
            return this.unknownOrientation;
        }

        public double getOrientedDirection() {
            return this.orientatedDirection;
        }

        public void setOrientedDirection(double _orientedDirection) {
            this.orientatedDirection = _orientedDirection;
        }

        public double getErrAngle() {
            return this.errAngle;
        }

        public double getErrTrans() {
            return this.errTrans;
        }

        public double getErrLong() {
            return this.errLong;
        }
    }
}
