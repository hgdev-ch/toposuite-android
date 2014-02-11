package ch.hgdev.toposuite.calculation;

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
    private static final String CALCULATION_NAME = "LevePolaire";

    /**
     * Horizontal angle read on the orientation point (alpha).
     */
    private double              horizAngleOrientation;
    /**
     * Horizontal distance between station and thrown point (AP).
     */
    private double              horizDist;
    /**
     * East attribute of the thrown point, which is computed by this class.
     */
    private double              thrownPointEast;
    /**
     * North attribute of the thrown point, which is computed by this class.
     */
    private double              thrownPointNorth;
    /**
     * Horizontal angle read on the thrown point (Hz).
     */
    private double              horizAngleThrownPoint;
    private Point               station;
    private Point               orientation;

    public LevePolaire(long id, Date lastModification) {
        super(id, CalculationType.LEVEPOLAIRE, LevePolaire.CALCULATION_NAME, lastModification, true);
    }

    public LevePolaire(Point _station, Point _orientation, double _horizAngleOrientation,
            double _horizAngleThrownPoint, double _horizDist, boolean hasDAO) {
        super(CalculationType.LEVEPOLAIRE, LevePolaire.CALCULATION_NAME, hasDAO);
        this.station = _station;
        this.orientation = _orientation;
        this.horizAngleOrientation = MathUtils.gradToRad(MathUtils
                .modulo400(_horizAngleOrientation));
        this.horizAngleThrownPoint = MathUtils.gradToRad(MathUtils
                .modulo400(_horizAngleThrownPoint));
        this.horizDist = _horizDist;
    }

    public void compute() {
        Gisement g = new Gisement(this.station, this.orientation);
        double z0StationOrientation = g.getGisement();
        double z0Orientation = z0StationOrientation - this.horizAngleOrientation;
        double z0StationThrownPoint = z0Orientation + this.horizAngleThrownPoint;

        this.thrownPointEast = this.station.getEast()
                + (this.horizDist * Math.sin(z0StationThrownPoint));
        this.thrownPointNorth = this.station.getNorth()
                + (this.horizDist * Math.cos(z0StationThrownPoint));
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

    public double getThrownPointEast() {
        return this.thrownPointEast;
    }

    public double getThrownPointNorth() {
        return this.thrownPointNorth;
    }

}