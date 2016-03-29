package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * 
 */
public class PolarSurvey extends Calculation {
    public static final String       STATION_NUMBER      = "station_number";
    public static final String       DETERMINATIONS_LIST = "determinations_list";
    public static final String       Z0_CALCULATION_ID   = "z0_calculation_id";

    private Point                    station;
    private double                   unknownOrientation;
    private double                   instrumentHeight;
    /**
     * A user can either provide a Z0 (unknown orientation) or either retrieve
     * it from another calculation (abriss or free station typically). In the
     * latter case, we need to store the calculation ID in order to be able to
     * retrieve the correct z0 from within a call from the calculation history.
     */
    private long                     z0CalculationId;

    private final ArrayList<Measure> determinations;
    private final ArrayList<Result>  results;

    public PolarSurvey(long id, Date lastModification) {
        super(id, CalculationType.POLARSURVEY,
                App.getContext().getString(R.string.title_activity_polar_survey),
                lastModification,
                true);

        this.determinations = new ArrayList<Measure>();
        this.results = new ArrayList<Result>();
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

        this.determinations = new ArrayList<Measure>();
        this.results = new ArrayList<Result>();
        this.station = _station;
        this.unknownOrientation = MathUtils.gradToRad(MathUtils.modulo400(_unknownOrientation));
        this.instrumentHeight = _instrumentHeight;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Compute a new point for each determination and put it in the array of
     * results.
     */
    @Override
    public void compute() {
        if (this.determinations.size() == 0) {
            return;
        }

        // clear any previously computed results
        this.results.clear();

        for (Measure m : this.determinations) {
            double zenAngle = MathUtils.gradToRad(MathUtils.modulo400(m.getZenAngle()));
            double hz = MathUtils.gradToRad(MathUtils.modulo400(m.getHorizDir()));

            double horizDist = Math.sin(zenAngle) * m.getDistance();
            horizDist += m.getLonDepl();
            hz = hz + Math.atan(m.getLatDepl() / horizDist);
            horizDist = MathUtils.pythagoras(horizDist, m.getLatDepl());

            double east = this.station.getEast()
                    + (Math.sin(this.unknownOrientation + hz) * horizDist);
            double north = this.station.getNorth()
                    + (Math.cos(this.unknownOrientation + hz) * horizDist);

            double altitude;
            if (!MathUtils.isIgnorable(this.instrumentHeight) && !MathUtils.isIgnorable(m.getS())) {
                altitude = (this.station.getAltitude() + (m.getDistance() * Math.cos(zenAngle))
                        + this.instrumentHeight) - m.getS();
            } else {
                altitude = MathUtils.IGNORE_DOUBLE;
            }

            Result r = new Result(m.getMeasureNumber(), east, north, altitude);
            this.results.add(r);
        }

        // update the calculation last modification date
        this.updateLastModification();
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.station_label) + ": "
                + this.getStation().toString());
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.station != null) {
            json.put(PolarSurvey.STATION_NUMBER, this.station.getNumber());
        }

        json.put(PolarSurvey.Z0_CALCULATION_ID, this.z0CalculationId);

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
        this.station = SharedResources.getSetOfPoints().find(
                json.getString(PolarSurvey.STATION_NUMBER));

        this.z0CalculationId = json.getLong(PolarSurvey.Z0_CALCULATION_ID);

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

    public double getUnknownOrientation() {
        return this.unknownOrientation;
    }

    public long getZ0CalculationId() {
        return this.z0CalculationId;
    }

    public double getInstrumentHeight() {
        return this.instrumentHeight;
    }

    /**
     * Class to store the results of the polar survey calculation on each
     * determination.
     * 
     * @author HGdev
     * 
     */
    public class Result {
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