package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

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
    private static final String      CALCULATION_NAME = "LevePolaire";
    /**
     * East attribute of the new point, which is computed by this class.
     */
    private double                   newPointEast;
    /**
     * North attribute of the new point, which is computed by this class.
     */
    private double                   newPointNorth;

    /**
     * Altitude attribute of the new point, which is computed by this class.
     */
    private double                   newPointAltitude;

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
    }

    /**
     * Compute a new point for each determination and put it in the array of
     * results.
     */
    public void compute() {
        if (this.determinations.size() == 0) {
            return;
        }

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
            double altitude = (this.station.getAltitude() + (m.getDistance() * Math.cos(zenAngle))
                    + m.getI()) - m.getS();

            Result r = new Result(m.getOrientation(), east, north, altitude);
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
        return LevePolaireActivity.class;
    }

    public double getNewPointEast() {
        return this.newPointEast;
    }

    public double getNewPointNorth() {
        return this.newPointNorth;
    }

    public double getNewPointAltitude() {
        return this.newPointAltitude;
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
        private final Point  determination;
        private final double east;
        private final double north;
        private final double altitude;

        public Result(Point _determination, double _east, double _north, double _altitude) {
            this.determination = _determination;
            this.east = _east;
            this.north = _north;
            this.altitude = _altitude;
        }

        public Point getDetermination() {
            return this.determination;
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