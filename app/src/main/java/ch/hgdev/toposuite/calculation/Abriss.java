package ch.hgdev.toposuite.calculation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.abriss.AbrissActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class Abriss extends Calculation {
    private static final String STATION_NUMBER = "station_number";
    private static final String ORIENTATIONS_LIST = "orientations_list";

    private Point station;
    private final ArrayList<Measure> orientations;

    private final ArrayList<Result> results;
    private double mean;

    /**
     * MSE stands for Mean Squared Error.
     */
    private double mse;

    private double meanErrComp;

    public Abriss(boolean hasDAO) {
        this(null, hasDAO);
    }

    public Abriss(Point station, boolean hasDAO) {
        super(CalculationType.ABRISS,
                App.getContext().getString(R.string.title_activity_abriss),
                hasDAO);
        this.station = station;
        this.orientations = new ArrayList<>();
        this.results = new ArrayList<>();
        this.mean = 0.0;
        this.mse = 0.0;
    }

    public Abriss(long id, Date lastModification) {
        super(id,
                CalculationType.ABRISS,
                App.getContext().getString(R.string.title_activity_abriss),
                lastModification,
                true);

        this.orientations = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    /**
     * Perform the the computation.
     */
    @Override
    public void compute() throws CalculationException {
        if (this.orientations.size() == 0) {
            throw new CalculationException("no measures provided");
        }
        if (this.station == null) {
            throw new CalculationException("no station provided");
        }

        // if some measures have been deactivated, then we have to keep them
        // and avoid to clear the previous results list
        if (!this.hasDeactivatedMeasure()) {
            this.results.clear();
        }
        this.mean = 0.0;
        this.mse = 0.0;

        // Small trick to take the deactivated measures into account during some
        // of the computations.
        int numberOfDeactivatedOrientations = 0;
        int index = -1;
        double adjZ0 = 0.0;
        double z0Sum = 0.0;

        for (Measure m : this.orientations) {
            index++;

            // zenithal angle value is optional, needs to be 100.0 by default
            if (MathUtils.isIgnorable(m.getZenAngle())) {
                m.setZenAngle(100.0); // default value for zenithal angle
            }

            // skip deactivated orientations
            if (m.isDeactivated()) {
                numberOfDeactivatedOrientations++;
                continue;
            }

            Gisement g = new Gisement(this.station, m.getPoint(), false);

            double z0 = MathUtils.modulo400(g.getGisement() - m.getHorizDir());
            double calcDist = MathUtils.euclideanDistance(this.station, m.getPoint());

            // special case for when angle values cross the 400 grad for some
            // see issue #625
            if (this.results.isEmpty()) {
                adjZ0 = z0;
                z0Sum = adjZ0;
            } else {
                // check diff with previous adjusted z0
                double diff = z0 - adjZ0;
                if (diff < -200) {
                    adjZ0 = z0 + 400;
                } else if (diff > 200) {
                    adjZ0 = z0 - 400;
                } else {
                    adjZ0 = z0;
                }
                z0Sum += adjZ0;
            }

            Result r = new Result(m.getPoint(), g.getHorizDist(),
                    z0, 0.0, g.getGisement(), calcDist, 0.0, 0.0, 0.0);

            if (!this.hasDeactivatedMeasure()) {
                this.results.add(r);
            } else {
                // just used as tmp variable for modifying the pointed value of the
                // current result
                @SuppressWarnings("unused")
                Result oldResult = this.results.get(index);
                oldResult = r;
            }
        }

        this.mean = MathUtils.modulo400(z0Sum / (
                this.orientations.size() - numberOfDeactivatedOrientations));

        index = 0;
        for (Measure m : this.orientations) {
            // skip deactivated orientations
            if (m.isDeactivated()) {
                index++;
                continue;
            }

            double orientDir = MathUtils.modulo400(this.mean + m.getHorizDir());
            this.results.get(index).setOrientedDirection(orientDir);

            // [cc]
            double errAngle = (this.results.get(index).getGisement() - orientDir) * 10000;
            this.results.get(index).setErrAngle(errAngle);

            double calcDist = this.results.get(index).getCalculatedDistance();

            // [cm]
            double errTrans = (calcDist * (errAngle / 6366.2));
            this.results.get(index).setErrTrans(errTrans);

            // [cm] => measured distance - calculated distance
            double errLong = MathUtils.IGNORE_DOUBLE;
            // if no distance is provided, then there cannot be any error with regard to the distance
            if (!MathUtils.isIgnorable(m.getDistance())) {
                errLong = MathUtils.mToCm(calcDist - (Math.sin(MathUtils.gradToRad(m.getZenAngle())) * m.getDistance()));
            }
            this.results.get(index).setErrLong(errLong);

            this.mse += Math.pow(errAngle, 2);

            index++;
        }

        this.mse = Math.sqrt(this.mse / (index - numberOfDeactivatedOrientations - 1));
        this.meanErrComp = this.mse / Math.sqrt(index - numberOfDeactivatedOrientations);

        this.postCompute();
    }

    @Override
    protected void postCompute() {
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.station_label) + ": "
                + this.getStation().toString());
        super.postCompute();
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
                json.getString(Abriss.STATION_NUMBER));

        JSONArray orientationsArray = json.getJSONArray(Abriss.ORIENTATIONS_LIST);

        for (int i = 0; i < orientationsArray.length(); i++) {
            JSONObject jo = (JSONObject) orientationsArray.get(i);
            Measure m = new Measure(
                    SharedResources.getSetOfPoints().find(
                            jo.getString(Measure.ORIENTATION_NUMBER)),
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

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_abriss);
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

    private boolean hasDeactivatedMeasure() {
        for (Measure m : this.orientations) {
            if (m.isDeactivated()) {
                return true;
            }
        }
        return false;
    }

    public class Result implements Serializable {
        private final Point orientation;
        private final double distance;
        private final double unknownOrientation;
        private double orientatedDirection;
        private final double gisement;
        private final double calculatedDistance;
        private double errAngle;
        private double errTrans;
        private double errLong;
        private boolean deactivated;

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
            this.deactivated = false;
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

        public void setErrLong(double _errLong) {
            this.errLong = _errLong;
        }

        public final boolean isDeactivated() {
            return this.deactivated;
        }

        public final void toggle() {
            this.deactivated = !this.deactivated;
        }
    }
}
