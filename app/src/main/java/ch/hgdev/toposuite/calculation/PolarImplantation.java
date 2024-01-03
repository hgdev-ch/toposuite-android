package ch.hgdev.toposuite.calculation;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.polarimplantation.PolarImplantationActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Implement the polar implantation calculation, which does the opposite of the
 * polar survey. It finds back measures that were used to compute a point.
 * Theses values are accessible by getters after a call to the compute() method.
 *
 * @author HGdev
 */
public class PolarImplantation extends Calculation {
    private static final String STATION_NUMBER = "station_number";
    private static final String POINT_NUMBER = "station_number";
    private static final String POINT_WITH_S_LIST = "points_with_s_list";

    private Point station;
    private Point point;
    private final ArrayList<Measure> measures;
    private final ArrayList<PolarImplantation.Result> results;

    public PolarImplantation(long id, Date lastModification) {
        super(id,
                CalculationType.POLARIMPLANT,
                App.getContext().getString(R.string.title_activity_polar_implantation),
                lastModification,
                true);
        this.measures = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public PolarImplantation(Point _station, boolean hasDAO) {
        super(CalculationType.POLARIMPLANT,
                App.getContext().getString(R.string.title_activity_polar_implantation),
                hasDAO);

        this.station = _station;
        this.measures = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    /**
     * Compute measure values from points and put it in the array of results.
     */
    @Override
    public void compute() throws CalculationException {
        if (this.measures.isEmpty()) {
            throw new CalculationException("no measures provided");
        }
        if (this.station == null) {
            throw new CalculationException("no station provided");
        }

        this.results.clear();
        for (Measure d : this.measures) {
            Gisement g = new Gisement(this.station, d.getPoint(), false);

            double horizDir = MathUtils.modulo400(g.getGisement() - d.getUnknownOrientation());
            double horizDist = MathUtils.euclideanDistance(this.station, d.getPoint());

            double altitude = d.getPoint().getAltitude() - this.station.getAltitude();
            if (!MathUtils.isIgnorable(d.getI()) && !MathUtils.isIgnorable(d.getS())) {
                altitude = (altitude - d.getI()) + d.getS();
            }

            double zenAngle, distance;
            if (!MathUtils.isZero(altitude)) {
                zenAngle = MathUtils.modulo200(MathUtils.radToGrad((Math.atan(horizDist / altitude))));
                distance = (horizDist * (MathUtils.EARTH_RADIUS + d.getPoint().getAltitude())) / MathUtils.EARTH_RADIUS;
            } else {
                zenAngle = 100.0;
                distance = horizDist;
            }

            distance /= Math.sin(MathUtils.gradToRad(zenAngle));

            Result r = new Result(d.getPoint().getNumber(), horizDir, horizDist, distance, zenAngle, g.getGisement(), d.getS());
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
            json.put(PolarImplantation.STATION_NUMBER, this.station.getNumber());
        }
        if (this.point != null) {
            json.put(PolarImplantation.POINT_NUMBER, this.point.getNumber());
        }

        if (this.measures.size() > 0) {
            JSONArray measuresArray = new JSONArray();
            for (Measure m : this.measures) {
                measuresArray.put(m.toJSONObject());
            }

            json.put(PolarImplantation.POINT_WITH_S_LIST, measuresArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        this.station = SharedResources.getSetOfPoints().find(
                json.getString(PolarImplantation.STATION_NUMBER));
        this.point = SharedResources.getSetOfPoints().find(
                json.getString(PolarImplantation.POINT_NUMBER));

        JSONArray measuresArray = json.getJSONArray(PolarImplantation.POINT_WITH_S_LIST);

        for (int i = 0; i < measuresArray.length(); i++) {
            JSONObject jo = (JSONObject) measuresArray.get(i);
            Measure m = new Measure(
                    this.point,
                    0.0,
                    0.0,
                    0.0,
                    jo.getDouble(Measure.S),
                    0.0,
                    0.0,
                    jo.getDouble(Measure.I),
                    jo.getDouble(Measure.UNKNOWN_ORIENTATION));
            this.measures.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return PolarImplantationActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_polar_implantation);
    }

    public ArrayList<Measure> getMeasures() {
        return this.measures;
    }

    public ArrayList<PolarImplantation.Result> getResults() {
        return this.results;
    }

    public Point getStation() {
        return this.station;
    }

    public void setStation(@NonNull Point station) {
        this.station = station;
    }

    /**
     * Class to store the results of the polar implantation calculation for each
     * points.
     *
     * @author HGdev
     */
    public class Result implements Serializable {
        private final String pointNumber;
        private final double horizDir;
        private final double horizDist;
        private final double distance;
        private final double zenAngle;
        private final double gisement;
        private final double s;

        public Result(String _pointNumber, double _horizDir, double _horizDist, double _distance,
                      double _zenAngle, double _gisement, double _s) {
            this.pointNumber = _pointNumber;
            this.horizDist = _horizDist;
            this.horizDir = _horizDir;
            this.distance = _distance;
            this.zenAngle = _zenAngle;
            this.gisement = _gisement;
            this.s = _s;
        }

        public String getPointNumber() {
            return this.pointNumber;
        }

        public double getHorizDir() {
            return this.horizDir;
        }

        public double getHorizDist() {
            return this.horizDist;
        }

        public double getDistance() {
            return this.distance;
        }

        public double getZenAngle() {
            return this.zenAngle;
        }

        public double getGisement() {
            return this.gisement;
        }

        public double getS() {
            return this.s;
        }
    }
}
