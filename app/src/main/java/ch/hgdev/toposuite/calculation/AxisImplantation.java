package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.axisimpl.AxisImplantationActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * This class implements the axis implantation calculation.
 * 
 * @author HGdev
 * 
 */
public class AxisImplantation extends Calculation {
    public static final String                  STATION_NUMBER    = "station_number";
    public static final String                  ORIGIN_NUMBER     = "origin_number";
    public static final String                  EXTREMITY_NUMBER  = "extremity_number";
    public static final String                  Z0_CALCULATION_ID = "z0_calculation_id";
    public static final String                  MEASURES_LIST     = "measures_list";

    private long                                z0CalculationId;

    private OrthogonalBase                      orthogonalBase;
    private Point                               station;
    private double                              unknownOrientation;

    private final List<Measure>                 measures;

    private final List<AxisImplantation.Result> results;

    public AxisImplantation(long id, Date lastModification) {
        super(id,
                CalculationType.AXISIMPLANTATION,
                App.getContext().getString(R.string.title_activity_axis_implantation),
                lastModification,
                true);
        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<AxisImplantation.Result>();
    }

    public AxisImplantation(boolean hasDAO) {
        this(null, MathUtils.IGNORE_DOUBLE, null, null, hasDAO);
    }

    public AxisImplantation(Point station, double unknownOrientation,
            Point origin, Point extremity, boolean hasDAO) {
        super(CalculationType.AXISIMPLANTATION,
                App.getContext().getString(R.string.title_activity_axis_implantation),
                hasDAO);
        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<AxisImplantation.Result>();
        this.initAttributes(station, unknownOrientation, origin, extremity);

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Initialize class attributes.
     * 
     * @param station
     * @param unknownOrientation
     * @param origin
     * @param extremity
     */
    public void initAttributes(Point station, double unknownOrientation,
            Point origin, Point extremity) {
        this.orthogonalBase = new OrthogonalBase(origin, extremity);
        this.station = station;
        this.unknownOrientation = unknownOrientation;
        this.z0CalculationId = -1;
    }

    @Override
    public void compute() {
        // clear old results
        this.results.clear();

        for (Measure m : this.measures) {
            double gis = MathUtils.modulo400(this.unknownOrientation + m.getHorizDir());

            double east = MathUtils.pointLanceEast(this.station.getEast(), gis, m.getDistance());
            double north = MathUtils.pointLanceNorth(this.station.getNorth(), gis, m.getDistance());

            Point p = new Point(false);
            p.setEast(east);
            p.setNorth(north);
            PointProjectionOnALine projection = new PointProjectionOnALine("",
                    this.orthogonalBase.getOrigin(),
                    this.orthogonalBase.getExtremity(),
                    p,
                    false);
            projection.compute();
            Point projPoint = projection.getProjPt();
            double abscissa = MathUtils.euclideanDistance(
                    this.orthogonalBase.getOrigin(), projPoint);
            double ordinate = MathUtils.euclideanDistance(p, projPoint);

            double angle = MathUtils.angle3Pts(
                    this.orthogonalBase.getExtremity(),
                    this.orthogonalBase.getOrigin(),
                    p);

            if (MathUtils.isBetween(angle, 100.0, 300.0, App.getAngleTolerance())) {
                abscissa = -abscissa;
            }
            if (MathUtils.isBetween(angle, 200.0, 400.0, App.getAngleTolerance())) {
                ordinate = -ordinate;
            }

            Result r = new Result(
                    m.getMeasureNumber(), p.getEast(), p.getNorth(), abscissa, ordinate);
            this.results.add(r);

            this.updateLastModification();
            this.setDescription(this.getCalculationName()
                    + " - " + App.getContext().getString(R.string.station_label) + ": "
                    + this.station.toString() + " / "
                    + App.getContext().getString(R.string.origin_label) + ": "
                    + this.orthogonalBase.getOrigin() + " / "
                    + App.getContext().getString(R.string.extremity_label) + ": "
                    + this.orthogonalBase.getExtremity());
            this.notifyUpdate(this);
        }
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.station != null) {
            json.put(AxisImplantation.STATION_NUMBER, this.station.getNumber());
        }
        if (this.getOrthogonalBase() != null) {
            if (this.getOrthogonalBase().getOrigin() != null) {
                json.put(AxisImplantation.ORIGIN_NUMBER, this.orthogonalBase.getOrigin());
            }
            if (this.getOrthogonalBase().getExtremity() != null) {
                json.put(AxisImplantation.EXTREMITY_NUMBER, this.orthogonalBase.getExtremity());
            }
        }
        json.put(AxisImplantation.Z0_CALCULATION_ID, this.z0CalculationId);

        if (this.measures.size() > 0) {
            JSONArray measuresArray = new JSONArray();
            for (Measure m : this.measures) {
                measuresArray.put(m.toJSONObject());
            }
            json.put(AxisImplantation.MEASURES_LIST, measuresArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        this.station = SharedResources.getSetOfPoints().find(
                json.getString(AxisImplantation.STATION_NUMBER));
        Point origin = SharedResources.getSetOfPoints().find(
                json.getString(AxisImplantation.ORIGIN_NUMBER));
        Point extremity = SharedResources.getSetOfPoints().find(
                json.getString(AxisImplantation.EXTREMITY_NUMBER));
        this.orthogonalBase = new OrthogonalBase(origin, extremity);
        this.z0CalculationId = json.getLong(AxisImplantation.Z0_CALCULATION_ID);

        JSONArray measuresArray = json.getJSONArray(AxisImplantation.MEASURES_LIST);
        for (int i = 0; i < measuresArray.length(); i++) {
            JSONObject jo = (JSONObject) measuresArray.get(i);
            Measure m = new Measure(
                    null,
                    jo.getDouble(Measure.HORIZ_DIR),
                    jo.getDouble(Measure.ZEN_ANGLE),
                    jo.getDouble(Measure.DISTANCE),
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    jo.getString(Measure.MEASURE_NUMBER));
            this.measures.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return AxisImplantationActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_axis_implantation);
    }

    public OrthogonalBase getOrthogonalBase() {
        return this.orthogonalBase;
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

    public List<Measure> getMeasures() {
        return this.measures;
    }

    public long getZ0CalculationId() {
        return this.z0CalculationId;
    }

    public void setZ0CalculationId(long z0CalculationId) {
        this.z0CalculationId = z0CalculationId;
    }

    public List<AxisImplantation.Result> getResults() {
        return this.results;
    }

    public static class Result {
        private String number;
        private double east;
        private double north;
        private double abscissa;
        private double ordinate;

        public Result(String _number, double _east, double _north, double _abscissa,
                double _ordinate) {
            this.number = _number;
            this.east = _east;
            this.north = _north;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
        }

        public String getNumber() {
            return this.number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public double getEast() {
            return this.east;
        }

        public void setEast(double east) {
            this.east = east;
        }

        public double getNorth() {
            return this.north;
        }

        public void setNorth(double north) {
            this.north = north;
        }

        public double getAbscissa() {
            return this.abscissa;
        }

        public void setAbscissa(double abscissa) {
            this.abscissa = abscissa;
        }

        public double getOrdinate() {
            return this.ordinate;
        }

        public void setOrdinate(double ordinate) {
            this.ordinate = ordinate;
        }
    }
}
