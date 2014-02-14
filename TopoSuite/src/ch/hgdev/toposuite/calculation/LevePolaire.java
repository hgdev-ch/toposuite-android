package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.levepolaire.LevePolaireActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Implementation of the "leve polaire" calculation. It computes the north and
 * east coordinates of the "thrown point", accessible by getters after a call to
 * the compute() method.
 * 
 * @author HGdev
 * 
 */
public class LevePolaire extends Calculation {
    public static final String       STATION_NUMBER      = "station_number";
    public static final String       DETERMINATIONS_LIST = "determinations_list";
    private static final String      CALCULATION_NAME    = "Leve Polaire";

    private Point                    station;
    private final ArrayList<Measure> determinations;
    private final ArrayList<Result>  results;

    public LevePolaire(long id, Date lastModification) {
        super(id, CalculationType.LEVEPOLAIRE, LevePolaire.CALCULATION_NAME, lastModification, true);

        this.determinations = new ArrayList<Measure>();
        this.results = new ArrayList<Result>();
    }

    public LevePolaire(Point _station, boolean hasDAO) {
        super(CalculationType.LEVEPOLAIRE, LevePolaire.CALCULATION_NAME, hasDAO);

        this.determinations = new ArrayList<Measure>();
        this.results = new ArrayList<Result>();
        this.station = _station;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Compute a new point for each determination and put it in the array of
     * results.
     */
    public void compute() {
        if (this.determinations.size() == 0) {
            return;
        }

        // clear any previously computed results
        this.results.clear();

        for (Measure m : this.determinations) {
            double zenAngle = MathUtils.gradToRad(MathUtils.modulo400(m.getZenAngle()));
            double z0 = MathUtils.gradToRad(MathUtils.modulo400(m.getUnknownOrientation()));
            double hz = MathUtils.gradToRad(MathUtils.modulo400(m.getHorizDir()));

            double horizDist = Math.sin(zenAngle) * m.getDistance();
            horizDist += m.getLonDepl();
            hz = hz + Math.atan(m.getLatDepl() / horizDist);
            horizDist = MathUtils.pythagoras(horizDist, m.getLatDepl());

            double east = this.station.getEast() + (Math.sin(z0 + hz) * horizDist);
            double north = this.station.getNorth() + (Math.cos(z0 + hz) * horizDist);

            double altitude;
            if ((m.getI() != 0.0) && (m.getS() != 0.0)) {
                altitude = (this.station.getAltitude() + (m.getDistance() * Math.cos(zenAngle))
                        + m.getI()) - m.getS();
            } else {
                altitude = 0.0;
            }

            Result r = new Result(m.getMeasureNumber(), east, north, altitude);
            this.results.add(r);
        }

        // update the calculation last modification date
        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.station != null) {
            json.put(LevePolaire.STATION_NUMBER, this.station.getNumber());
        }

        if (this.determinations.size() > 0) {
            JSONArray determinationsArray = new JSONArray();
            for (Measure m : this.determinations) {
                determinationsArray.put(m.toJSONObject());
            }

            json.put(LevePolaire.DETERMINATIONS_LIST, determinationsArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        this.station = SharedResources.getSetOfPoints().find(
                json.getInt(LevePolaire.STATION_NUMBER));

        JSONArray determinationsArray = json.getJSONArray(LevePolaire.DETERMINATIONS_LIST);

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
                    jo.getInt(Measure.MEASURE_NUMBER));
            this.determinations.add(m);
        }

    }

    @Override
    public Class<?> getActivityClass() {
        return LevePolaireActivity.class;
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

    /**
     * Class to store the results of the leve polaire calculation on each
     * determination.
     * 
     * @author HGdev
     * 
     */
    public class Result {
        private final int    determinationNumber;
        private final double east;
        private final double north;
        private final double altitude;

        public Result(int _determinationNumber, double _east, double _north, double _altitude) {
            this.determinationNumber = _determinationNumber;
            this.east = _east;
            this.north = _north;
            this.altitude = _altitude;
        }

        public int getDeterminationNumber() {
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