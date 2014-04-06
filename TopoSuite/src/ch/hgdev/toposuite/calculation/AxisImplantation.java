package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

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
    private static double                       TOLERANCE = 0.0001;
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
        int resultNumber = 0;

        for (Measure m : this.measures) {
            resultNumber++;
            double gis = MathUtils.modulo400(this.unknownOrientation + m.getHorizDir());

            double east = MathUtils.pointLanceEast(this.station.getEast(), gis, m.getDistance());
            double north = MathUtils.pointLanceNorth(this.station.getNorth(), gis, m.getDistance());

            Point p = new Point(false);
            p.setEast(east);
            p.setNorth(north);
            PointProjectionOnALine projection = new PointProjectionOnALine("",
                    this.orthogonalBase.getExtremity(),
                    this.orthogonalBase.getOrigin(),
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

            if (MathUtils.isBetween(angle, 100.0, 300.0, AxisImplantation.TOLERANCE)) {
                abscissa = -abscissa;
            }
            if (MathUtils.isBetween(angle, 200.0, 400.0, AxisImplantation.TOLERANCE)) {
                ordinate = -ordinate;
            }

            Result r = new Result(resultNumber, p.getEast(), p.getNorth(), abscissa, ordinate);
            this.results.add(r);
        }
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO Implement
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO Implement

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
        private int    number;
        private double east;
        private double north;
        private double abscissa;
        private double ordinate;

        public Result(int _number, double _east, double _north, double _abscissa,
                double _ordinate) {
            this.number = _number;
            this.east = _east;
            this.north = _north;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
        }

        public int getNumber() {
            return this.number;
        }

        public void setNumber(int number) {
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
