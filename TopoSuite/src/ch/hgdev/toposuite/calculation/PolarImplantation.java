package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Implement the polar implantation calculation, which does the opposite of the
 * polar survey. It finds back measures that were used to compute a point.
 * Theses values are accessible by getters after a call to the compute() method.
 * 
 * @author HGdev
 * 
 */
public class PolarImplantation extends Calculation {
    private static final String                       CALCULATION_NAME = "Polar Implantation";

    private Point                                     station;
    private final ArrayList<Measure>                  measures;
    private final ArrayList<PolarImplantation.Result> results;

    public PolarImplantation(long id, Date lastModification) {
        super(id, CalculationType.POLARIMPLANT, PolarImplantation.CALCULATION_NAME,
                lastModification, true);
        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<PolarImplantation.Result>();
    }

    public PolarImplantation(Point _station, boolean hasDAO) {
        super(CalculationType.POLARIMPLANT, PolarImplantation.CALCULATION_NAME, hasDAO);

        this.station = _station;
        this.measures = new ArrayList<Measure>();
        this.results = new ArrayList<PolarImplantation.Result>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Compute measure values from points and put it in the array of results.
     */
    public void compute() {
        if (this.measures.isEmpty()) {
            return;
        }

        for (Measure d : this.measures) {
            Gisement g = new Gisement(this.station, d.getPoint());

            double horizDir = MathUtils.modulo400(g.getGisement() - d.getUnknownOrientation());
            double horizDist = MathUtils.euclideanDistance(this.station, d.getPoint());

            double altitude = d.getPoint().getAltitude() - this.station.getAltitude();
            if (!MathUtils.isZero(d.getI()) && !MathUtils.isZero(d.getS())) {
                altitude = (altitude - d.getI()) + d.getS();
            }

            double zenAngle, distance;
            if (!MathUtils.isZero(altitude)) {
                zenAngle = MathUtils.modulo200(MathUtils.radToGrad(
                        (Math.atan(horizDist / altitude))));
                distance = (horizDist * (MathUtils.EARTH_RADIUS + d.getPoint().getAltitude()))
                        / MathUtils.EARTH_RADIUS;
            } else {
                zenAngle = 100.0;
                distance = horizDist;
            }

            distance /= Math.sin(MathUtils.gradToRad(zenAngle));

            Result r = new Result(d.getPoint().getNumber(), horizDir, horizDist, distance,
                    zenAngle, g.getGisement());
            this.results.add(r);
        }

        // update the calculation last modification date
        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO implement
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO implement

    }

    @Override
    public Class<?> getActivityClass() {
        // TODO uncomment once the activity has been created
        // return ImplantationPolaireActivity.class;
        return null;
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

    /**
     * Class to store the results of the polar implantation calculation for each
     * points.
     * 
     * @author HGdev
     * 
     */
    public class Result {
        private final int    pointNumber;
        private final double horizDir;
        private final double horizDist;
        private final double distance;
        private final double zenAngle;
        private final double gisement;

        public Result(int _pointNumber, double _horizDir, double _horizDist, double _distance,
                double _zenAngle, double _gisement) {
            this.pointNumber = _pointNumber;
            this.horizDist = _horizDist;
            this.horizDir = _horizDir;
            this.distance = _distance;
            this.zenAngle = _zenAngle;
            this.gisement = _gisement;
        }

        public int getPointNumber() {
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

    }
}
