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
import ch.hgdev.toposuite.calculation.activities.polarsurvey.PolarSurveyActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Implementation of the polar survey calculation. It computes the north and
 * east coordinates of the "thrown point", accessible by getters after a call to
 * the compute() method.
 *
 * @author HGdev
 */
public class PolarSurvey extends Calculation {
    private static final String STATION_NUMBER = "station_number";
    private static final String DETERMINATIONS_LIST = "determinations_list";
    private static final String UNKNOWN_ORIENTATION = "unknown_orientation";
    private static final String Z0_CALCULATION_ID = "z0_calculation_id";
    private static final String INSTRUMENT_HEIGHT = "instrument_height";

    private Point station;
    private double unknownOrientation;
    private double instrumentHeight;
    /**
     * A user can either provide a Z0 (unknown orientation) or either retrieve
     * it from another calculation (abriss or free station typically). In the
     * latter case, we need to store the calculation ID in order to be able to
     * retrieve the correct z0 from within a call from the calculation history.
     */
    private long z0CalculationId;

    private final ArrayList<Measure> determinations;
    private final ArrayList<Result> results;

    public PolarSurvey(long id, Date lastModification) {
        super(id, CalculationType.POLARSURVEY,
                App.getContext().getString(R.string.title_activity_polar_survey),
                lastModification,
                true);

        this.determinations = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public PolarSurvey(boolean hasDAO) {
        this(null, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_LONG, hasDAO);
    }

    public PolarSurvey(Point _station, double _unknownOrientation, double _instrumentHeight,
                       long _z0CalculationId,
                       boolean hasDAO) {
        this(_station, _unknownOrientation, _instrumentHeight, hasDAO);
        this.z0CalculationId = _z0CalculationId;
    }

    public PolarSurvey(Point _station, double _unknownOrientation, double _instrumentHeight,
                       boolean hasDAO) {
        super(CalculationType.POLARSURVEY,
                App.getContext().getString(R.string.title_activity_polar_survey),
                hasDAO);

        this.determinations = new ArrayList<>();
        this.results = new ArrayList<>();
        this.station = _station;
        this.unknownOrientation = _unknownOrientation;
        this.instrumentHeight = _instrumentHeight;
    }

    /**
     * Compute a new point for each determination and put it in the array of
     * results.
     */
    @Override
    public void compute() throws CalculationException {
        if (this.determinations.size() == 0) {
            throw new CalculationException("no measures provided");
        }
        if (this.station == null) {
            throw new CalculationException("no station provided");
        }
        if (MathUtils.isIgnorable(this.unknownOrientation)) {
            throw new CalculationException("no unknown orientation provided");
        }

        // clear any previously computed results
        this.results.clear();

        double z0 = MathUtils.gradToRad(MathUtils.modulo400(this.unknownOrientation));

        for (Measure m : this.determinations) {
            // zenithal angle value is optional, needs to be 100.0 by default
            if (MathUtils.isIgnorable(m.getZenAngle())) {
                m.setZenAngle(100.0); // default value for zenithal angle
            }

            double zenAngle = MathUtils.gradToRad(MathUtils.modulo400(m.getZenAngle()));
            double hz = MathUtils.gradToRad(MathUtils.modulo400(m.getHorizDir()));

            double horizDist = Math.sin(zenAngle) * m.getDistance();
            if (!MathUtils.isIgnorable(m.getLonDepl())) {
                horizDist += m.getLonDepl();
            }
            if (!MathUtils.isIgnorable(m.getLatDepl())) {
                hz += Math.atan(m.getLatDepl() / horizDist);
                horizDist = MathUtils.pythagoras(horizDist, m.getLatDepl());
            }

            double east = this.station.getEast() + (Math.sin(z0 + hz) * horizDist);
            double north = this.station.getNorth() + (Math.cos(z0 + hz) * horizDist);

            double altitude;
            if (!MathUtils.isIgnorable(this.instrumentHeight) && !MathUtils.isIgnorable(m.getS())) {
                altitude = (this.station.getAltitude() + (m.getDistance() * Math.cos(zenAngle)) + this.instrumentHeight) - m.getS();
            } else {
                altitude = MathUtils.IGNORE_DOUBLE;
            }

            Result r = new Result(m.getMeasureNumber(), east, north, altitude);
            this.results.add(r);
        }

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
            json.put(PolarSurvey.STATION_NUMBER, this.station.getNumber());
        } else {
            json.put(PolarSurvey.STATION_NUMBER, null);
        }

        json.put(PolarSurvey.Z0_CALCULATION_ID, this.z0CalculationId);
        json.put(PolarSurvey.INSTRUMENT_HEIGHT, this.instrumentHeight);
        json.put(PolarSurvey.UNKNOWN_ORIENTATION, this.unknownOrientation);

        if (this.determinations.size() > 0) {
            JSONArray determinationsArray = new JSONArray();
            for (Measure m : this.determinations) {
                determinationsArray.put(m.toJSONObject());
            }

            json.put(PolarSurvey.DETERMINATIONS_LIST, determinationsArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        this.station = SharedResources.getSetOfPoints().find(json.getString(PolarSurvey.STATION_NUMBER));

        this.z0CalculationId = json.getLong(PolarSurvey.Z0_CALCULATION_ID);
        this.instrumentHeight = json.getDouble(PolarSurvey.INSTRUMENT_HEIGHT);
        this.unknownOrientation = json.getDouble(PolarSurvey.UNKNOWN_ORIENTATION);

        JSONArray determinationsArray = json.getJSONArray(PolarSurvey.DETERMINATIONS_LIST);

        for (int i = 0; i < determinationsArray.length(); i++) {
            JSONObject jo = (JSONObject) determinationsArray.get(i);
            Measure m = new Measure(
                    null,
                    jo.getDouble(Measure.HORIZ_DIR),
                    jo.getDouble(Measure.ZEN_ANGLE),
                    jo.getDouble(Measure.DISTANCE),
                    jo.getDouble(Measure.S),
                    jo.getDouble(Measure.LAT_DEPL),
                    jo.getDouble(Measure.LON_DEPL),
                    jo.getDouble(Measure.I),
                    jo.getDouble(Measure.UNKNOWN_ORIENTATION),
                    jo.getString(Measure.MEASURE_NUMBER));
            this.determinations.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return PolarSurveyActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_polar_survey);
    }

    public ArrayList<Measure> getDeterminations() {
        return this.determinations;
    }

    public ArrayList<Result> getResults() {
        return this.results;
    }

    public Point getStation() {
        return this.station;
    }

    public void setStation(Point station) {
        this.station = station;
    }

    public double getUnknownOrientation() {
        return this.unknownOrientation;
    }

    public void setUnknownOrientation(double unknownOrientation) {
        this.unknownOrientation = unknownOrientation;
    }

    public long getZ0CalculationId() {
        return this.z0CalculationId;
    }

    public void setZ0CalculationId(long z0CalculationId) {
        this.z0CalculationId = z0CalculationId;
    }

    public double getInstrumentHeight() {
        return this.instrumentHeight;
    }

    public void setInstrumentHeight(double instrumentHeight) {
        this.instrumentHeight = instrumentHeight;
    }

    /**
     * Class to store the results of the polar survey calculation on each
     * determination.
     *
     * @author HGdev
     */
    public class Result implements Serializable {
        private final String determinationNumber;
        private final double east;
        private final double north;
        private final double altitude;

        public Result(String _determinationNumber, double _east, double _north, double _altitude) {
            this.determinationNumber = _determinationNumber;
            this.east = _east;
            this.north = _north;
            this.altitude = _altitude;
        }

        public String getDeterminationNumber() {
            return this.determinationNumber;
        }

        public double getEast() {
            return this.east;
        }

        public double getNorth() {
            return this.north;
        }

        public double getAltitude() {
            return this.altitude;
        }
    }
}