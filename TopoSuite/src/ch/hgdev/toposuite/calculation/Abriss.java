package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.abriss.AbrissActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class Abriss extends Calculation {
    public static final String  STATION_NUMBER    = "station_number";
    public static final String  ORIENTATIONS_LIST = "orientations_list";

    private static final String CALCULATION_NAME  = "Orient. st.";

    private Point               station;
    private ArrayList<Measure>  orientations;

    private ArrayList<Result>   results;
    private double              mean;

    /**
     * MSE stands for Mean Squared Error.
     */
    private double              mse;

    private double              meanErrComp;

    public Abriss(boolean hasDAO) {
        super(CalculationType.ABRISS, Abriss.CALCULATION_NAME, hasDAO);

        this.orientations = new ArrayList<Measure>();
        this.results = new ArrayList<Abriss.Result>();
        this.mean = 0.0;
        this.mse = 0.0;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public Abriss(Point station, boolean hasDAO) {
        this(hasDAO);
        this.station = station;
    }

    public Abriss(long id, Date lastModification) {
        super(id, CalculationType.ABRISS, Abriss.CALCULATION_NAME, lastModification, true);

        this.orientations = new ArrayList<Measure>();
        this.results = new ArrayList<Abriss.Result>();
    }

    /**
     * Perform the the computation.
     */
    public void compute() {
        if (this.orientations.size() == 0) {
            return;
        }

        this.results.clear();

        for (Measure m : this.orientations) {
            Gisement g = new Gisement(this.station, m.getPoint(), false);

            double z0 = MathUtils.modulo400(g.getGisement() - m.getHorizDir());
            double calcDist = MathUtils.euclideanDistance(this.station, m.getPoint());

            Result r = new Result(m.getPoint(), g.getHorizDist(),
                    z0, 0.0, g.getGisement(), calcDist, 0.0, 0.0, 0.0);

            this.results.add(r);
            this.mean += z0;
        }

        this.mean = MathUtils.modulo400(this.mean / this.orientations.size());

        int index = 0;
        for (Measure m : this.orientations) {
            double orientDir = MathUtils.modulo400(this.mean + m.getHorizDir());
            this.results.get(index).setOrientedDirection(orientDir);

            // [cc]
            double errAngle = (this.results.get(index).getGisement() - orientDir) * 10000;
            this.results.get(index).setErrAngle(errAngle);

            double calcDist = this.results.get(index).getCalculatedDistance();

            // [cm]
            double errTrans = (calcDist * (errAngle / 6366.2));
            this.results.get(index).setErrTrans(errTrans);

            this.mse += Math.pow(errAngle, 2);

            index++;
        }

        this.mse = Math.sqrt(this.mse / (index - 1));
        this.meanErrComp = this.mse / Math.sqrt(index);

        // update the calculation last modification date
        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.station != null) {
            json.put(Abriss.STATION_NUMBER, this.station.getNumber());
        }

        if (this.orientations.size() > 0) {
            JSONArray orientationsArray = new JSONArray();
            for (Measure m : this.orientations) {
                orientationsArray.put(m.toJSONObject());
            }

            json.put(Abriss.ORIENTATIONS_LIST, orientationsArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        this.station = SharedResources.getSetOfPoints().find(
                json.getInt(Abriss.STATION_NUMBER));

        JSONArray orientationsArray = json.getJSONArray(Abriss.ORIENTATIONS_LIST);

        for (int i = 0; i < orientationsArray.length(); i++) {
            JSONObject jo = (JSONObject) orientationsArray.get(i);
            Measure m = new Measure(
                    SharedResources.getSetOfPoints().find(
                            jo.getInt(Measure.ORIENTATION_NUMBER)),
                    jo.getDouble(Measure.HORIZ_DIR),
                    jo.getDouble(Measure.ZEN_ANGLE),
                    jo.getDouble(Measure.DISTANCE),
                    jo.getDouble(Measure.S));
            this.orientations.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return AbrissActivity.class;
    }

    public Point getStation() {
        return this.station;
    }

    public void setStation(Point station) {
        this.station = station;
    }

    public ArrayList<Measure> getMeasures() {
        return this.orientations;
    }

    public ArrayList<Result> getResults() {
        return this.results;
    }

    public double getMean() {
        return this.mean;
    }

    /**
     * Getter for the Mean Squared Error.
     * 
     * @return
     */
    public double getMSE() {
        return this.mse;
    }

    public double getMeanErrComp() {
        return this.meanErrComp;
    }

    public class Result {
        private Point  orientation;
        private double distance;
        private double unknownOrientation;
        private double orientatedDirection;
        private double gisement;
        private double calculatedDistance;
        private double errAngle;
        private double errTrans;
        private double errLong;

        public Result(Point _orientation, double _distance, double _unknownOrientation,
                double _orientationDirection, double _gisement, double _calculatedDistance,
                double _errAngle, double _errTrans, double _errLong) {
            this.orientation = _orientation;
            this.distance = _distance;
            this.unknownOrientation = _unknownOrientation;
            this.orientatedDirection = _orientationDirection;
            this.gisement = _gisement;
            this.calculatedDistance = _calculatedDistance;
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

        public double getGisement() {
            return this.gisement;
        }

        public double getCalculatedDistance() {
            return this.calculatedDistance;
        }

        public double getErrAngle() {
            return this.errAngle;
        }

        public void setErrAngle(double _errAngle) {
            this.errAngle = _errAngle;
        }

        public double getErrTrans() {
            return this.errTrans;
        }

        public void setErrTrans(double _errTrans) {
            this.errTrans = _errTrans;
        }

        public double getErrLong() {
            return this.errLong;
        }
    }
}
