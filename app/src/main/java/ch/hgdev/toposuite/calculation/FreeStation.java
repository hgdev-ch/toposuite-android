package ch.hgdev.toposuite.calculation;

import com.google.common.math.DoubleMath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.freestation.FreeStationActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class FreeStation extends Calculation {
    private static final String STATION_NUMBER = "station_number";
    private static final String MEASURES = "measures";
    private static final String INSTRUMENT = "instrument";

    private String stationNumber;
    private ArrayList<Measure> measures;
    /**
     * Height of the instrument (I).
     */
    private double i;

    private Point stationResult;
    private final ArrayList<Result> results;
    private double unknownOrientation;
    private double scaleFactor;

    /**
     * Mean east error.
     */
    private double sE;

    /**
     * Mean north error.
     */
    private double sN;

    /**
     * Mean altitude error.
     */
    private double sA;

    /**
     * Mean FS.
     */
    private double meanFS;

    public FreeStation(String _stationNumber, double _i, boolean hasDAO) {
        super(CalculationType.FREESTATION, App.getContext().getString(
                R.string.title_activity_free_station), hasDAO);

        this.stationNumber = _stationNumber;
        this.i = _i;
        this.measures = new ArrayList<>();
        this.results = new ArrayList<>();
        this.scaleFactor = MathUtils.IGNORE_DOUBLE;
    }

    public FreeStation(String _stationNumber, boolean hasDAO) {
        this(_stationNumber, MathUtils.IGNORE_DOUBLE, hasDAO);
    }

    public FreeStation(boolean hasDAO) {
        this("", hasDAO);
    }

    public FreeStation(long id, Date lastModification) {
        super(id, CalculationType.FREESTATION, App.getContext().getString(
                R.string.title_activity_free_station),
                lastModification, true);

        this.measures = new ArrayList<>();
        this.results = new ArrayList<>();
        this.scaleFactor = MathUtils.IGNORE_DOUBLE;
    }

    @Override
    public void compute() throws CalculationException {
        if (this.measures.size() < 3) {
            throw new CalculationException("not enough measures provided");
        }

        // since we need to adjust some measures for the computation, use a copy
        // this prevents future calls to compute() to modify the measures (see #731)
        ArrayList<Measure> measuresCopy = new ArrayList<>(this.measures.size());
        for (Measure m : this.measures) {
            // zenithal angle value is optional, needs to be 100.0 by default
            if (MathUtils.isIgnorable(m.getZenAngle())) {
                m.setZenAngle(100.0); // default value for zenithal angle
            }
            measuresCopy.add(new Measure(m));
        }

        if (!this.hasDeactivatedMeasure(measuresCopy)) {
            this.results.clear();
        }

        this.sE = 0.0;
        this.sN = 0.0;
        this.sA = 0.0;
        this.meanFS = 0.0;

        // fictive coordinates centroid
        double centroidYFict = 0.0;
        double centroidXFict = 0.0;
        Point centroidFict;

        // cadastral coordinates centroid
        double centroidYCadast = 0.0;
        double centroidXCadast = 0.0;
        Point centroidCadast;

        // altitude related stuff
        double totalWeights = 0.0;
        double meanAltitude = 0.0;
        int nbAltitudes = 0;

        int numberOfDeactivatedOrientations = 0;
        int index = -1;

        for (Measure m : measuresCopy) {
            index++;

            if (m.isDeactivated()) {
                numberOfDeactivatedOrientations++;
                continue;
            }
            Point res = new Point(m.getPoint().getNumber(), m.getPoint().getEast(),
                    m.getPoint().getNorth(), m.getPoint().getAltitude(),
                    false, false);

            double hz = MathUtils.modulo400(m.getHorizDir());
            double zenAngle = MathUtils.gradToRad(MathUtils.modulo400(m.getZenAngle()));
            double horizDist = m.getDistance() * Math.sin(zenAngle);
            if (!MathUtils.isIgnorable(m.getLonDepl())) {
                horizDist += m.getLonDepl();
            }
            if (!MathUtils.isIgnorable(m.getLatDepl())) {
                hz += MathUtils.radToGrad(Math.atan(m.getLatDepl() / horizDist));
                horizDist = MathUtils.pythagoras(horizDist, m.getLatDepl());
            }
            // the distance, here horizontal distance, is used later on
            m.setDistance(horizDist);
            m.setHorizDir(hz);

            res.setEast(MathUtils.pointLanceEast(0, hz, horizDist));
            res.setNorth(MathUtils.pointLanceNorth(0, hz, horizDist));

            double weight = 0.0;
            if (!MathUtils.isIgnorable(m.getPoint().getAltitude())
                    && MathUtils.isPositive(m.getPoint().getAltitude())
                    && MathUtils.isIgnorable(m.getLatDepl())
                    && MathUtils.isIgnorable(m.getLonDepl())
                    && !MathUtils.isIgnorable(this.i)
                    && MathUtils.isPositive(this.i)) {
                res.setAltitude(res.getAltitude() - MathUtils.nivellTrigo(
                        horizDist, m.getZenAngle(), this.i, m.getS(), res.getAltitude()));
                weight = 1 / Math.pow((horizDist / 1000), 2);

                totalWeights += weight;
                meanAltitude += weight * res.getAltitude();
                nbAltitudes++;
            } else {
                res.setAltitude(MathUtils.IGNORE_DOUBLE);
            }

            if (!this.hasDeactivatedMeasure(measuresCopy)) {
                this.results.add(new Result(res, weight));
            } else {
                // just used as tmp variable for modifying the pointed value of
                // the current result
                Result oldResult = this.results.get(index);
                oldResult = new Result(res, weight);
            }

            // centroid calculation
            centroidYFict += res.getEast();
            centroidXFict += res.getNorth();

            centroidYCadast += m.getPoint().getEast();
            centroidXCadast += m.getPoint().getNorth();
        }

        int n = this.results.size() - numberOfDeactivatedOrientations;
        centroidFict = new Point("", centroidYFict / n, centroidXFict / n,
                MathUtils.IGNORE_DOUBLE, false, false);
        centroidCadast = new Point("", centroidYCadast / n, centroidXCadast / n,
                MathUtils.IGNORE_DOUBLE, false, false);

        List<IntermediateResults> intermRes = new ArrayList<>();
        double meanRotation = 0.0;
        this.scaleFactor = 0.0;

        for (int i = 0; i < this.results.size(); i++) {
            if (measuresCopy.get(i).isDeactivated()) {
                // dummy intermediate results in order to avoid index problems
                intermRes.add(new IntermediateResults(
                        MathUtils.IGNORE_DOUBLE,
                        MathUtils.IGNORE_DOUBLE,
                        MathUtils.IGNORE_DOUBLE,
                        MathUtils.IGNORE_DOUBLE));
                continue;
            }
            Gisement g1 = new Gisement(centroidFict, this.results.get(i).getPoint(), false);
            Gisement g2 = new Gisement(centroidCadast, measuresCopy.get(i).getPoint(), false);
            intermRes.add(
                    new IntermediateResults(
                            g1.getGisement(), g1.getHorizDist(),
                            g2.getGisement(), g2.getHorizDist()));

            // calculation of the rotation between fictive and cadastral
            // coordinates according to the following formula:
            // mod400(gis_cadastral - gis_fictive)
            intermRes.get(i).rotation = MathUtils.modulo400(g2.getGisement() - g1.getGisement());

            // calculation of the multiplication constants between fictive and
            // cadastral coordinates according to the following formula:
            // dist_cadastral / dist_fictive
            double constant = g2.getHorizDist() / g1.getHorizDist();
            intermRes.get(i).constant = constant;
            this.scaleFactor += constant;
        }
        double minRotation = this.getMinRotation(intermRes);
        for (IntermediateResults res : intermRes) {
            double rotation = res.rotation;
            // skip values to ignore (deactivated measures)
            if (MathUtils.isIgnorable(rotation)) {
                continue;
            }
            if (DoubleMath.fuzzyCompare(rotation - minRotation, -15.0, App.getAngleTolerance()) < 0) {
                rotation += 400.0;
            } else if (DoubleMath.fuzzyCompare(rotation - minRotation, 15.0,
                    App.getAngleTolerance()) > 0) {
                rotation -= 400.0;
            }
            meanRotation += rotation;
        }

        meanRotation = MathUtils.modulo400(meanRotation / n);
        this.scaleFactor /= n;

        // calculation of the gisement/distance between the fictive centroid and
        // the station (which is actually at the coordinates 0;0).
        this.stationResult = new Point(this.stationNumber, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, false, false);
        Gisement g = new Gisement(centroidFict, this.stationResult, false);
        double gisFictiveGToSt = g.getGisement(); // gisement g-St
        double distFictiveGToSt = g.getHorizDist(); // distance g-St

        // calculation of the station coordinates
        double tmp1 = MathUtils.gradToRad(gisFictiveGToSt + meanRotation);
        double tmp2 = distFictiveGToSt * this.scaleFactor;
        this.stationResult.setEast((Math.sin(tmp1) * tmp2) + centroidCadast.getEast());
        this.stationResult.setNorth((Math.cos(tmp1) * tmp2) + centroidCadast.getNorth());
        double altitude = MathUtils.IGNORE_DOUBLE;
        if (!MathUtils.isIgnorable(meanAltitude) && (nbAltitudes > 0)) {
            altitude = meanAltitude / totalWeights;
        }
        this.stationResult.setAltitude(altitude);

        double diffAlt = 0.0;

        double u = 0.0;
        double v = 0.0;

        for (int i = 0; i < this.results.size(); i++) {
            if (measuresCopy.get(i).isDeactivated()) {
                continue;
            }

            double newGis = MathUtils.modulo400(meanRotation + measuresCopy.get(i).getHorizDir());
            double newDist = this.scaleFactor * measuresCopy.get(i).getDistance();

            // vE [cm]
            double newE = MathUtils.pointLanceEast(this.stationResult.getEast(), newGis, newDist);
            double vE = (newE - measuresCopy.get(i).getPoint().getEast()) * 100;
            this.results.get(i).setvE(vE);

            // vN [cm]
            double newN = MathUtils.pointLanceNorth(this.stationResult.getNorth(), newGis, newDist);
            double vN = (newN - measuresCopy.get(i).getPoint().getNorth()) * 100;
            this.results.get(i).setvN(vN);

            // vA [cm]
            double resAlt = this.results.get(i).getPoint().getAltitude();
            double vA = (altitude - resAlt) * 100;
            if (MathUtils.isIgnorable(resAlt)) {
                vA = MathUtils.IGNORE_DOUBLE;
            }
            this.results.get(i).setvA(vA);

            // FS [cm]
            double fS = MathUtils.pythagoras(vE, vN);
            this.results.get(i).setfS(fS);

            this.sE += Math.pow(vE, 2);
            this.sN += Math.pow(vN, 2);
            if (!MathUtils.isIgnorable(resAlt)) {
                diffAlt += this.results.get(i).getWeight() * Math.pow(vA, 2);
            }
            this.meanFS += fS;

            u += Math.pow(this.results.get(i).getPoint().getEast() - centroidYFict, 2);
            v += Math.pow(this.results.get(i).getPoint().getNorth() - centroidXFict, 2);
        }

        this.sE = Math.sqrt((this.sE + this.sN) / ((2 * (
                this.results.size() - numberOfDeactivatedOrientations)) - 4));
        this.sE = this.sE * Math.sqrt(
                (1 / (this.results.size() - numberOfDeactivatedOrientations)) + ((
                        Math.pow(centroidYFict, 2) + Math.pow(centroidXFict, 2)) / (u + v)));
        this.sN = this.sE;

        if (nbAltitudes > 1) {
            this.sA = Math.sqrt(diffAlt / ((nbAltitudes - 1) * totalWeights));
        } else {
            this.sA = MathUtils.IGNORE_DOUBLE;
        }
        this.meanFS /= (this.results.size() - numberOfDeactivatedOrientations);

        this.unknownOrientation = MathUtils.modulo400(meanRotation);

        // if I is not provided, there is no altimetry
        if (MathUtils.isIgnorable(this.i)) {
            this.stationResult.setAltitude(MathUtils.IGNORE_DOUBLE);
        }

        this.postCompute();
    }

    @Override
    protected void postCompute() {
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.station_label)
                + ": " + this.getStationNumber());
        super.postCompute();
    }

    /**
     * Find the minimum rotation from the list of intermediate results.
     *
     * @param results
     * @return
     */
    private double getMinRotation(List<IntermediateResults> results) {
        double minRotation = Double.MAX_VALUE;
        for (IntermediateResults r : results) {
            if (MathUtils.isIgnorable(r.rotation)) {
                continue;
            }
            if (DoubleMath.fuzzyCompare(r.rotation, minRotation, App.getAngleTolerance()) < 0) {
                minRotation = r.rotation;
            }
        }
        return minRotation;
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(FreeStation.STATION_NUMBER, this.stationNumber);
        jo.put(FreeStation.INSTRUMENT, this.i);

        JSONArray measuresArray = new JSONArray();
        for (Measure m : this.measures) {
            measuresArray.put(m.toJSONObject());
        }
        jo.put(FreeStation.MEASURES, measuresArray);

        return jo.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject jo = new JSONObject(jsonInputArgs);
        this.stationNumber = jo.getString(FreeStation.STATION_NUMBER);
        this.i = jo.getDouble(FreeStation.INSTRUMENT);

        for (int i = 0; i < jo.getJSONArray(FreeStation.MEASURES).length(); i++) {
            JSONObject measureObject = (JSONObject) jo.getJSONArray(
                    FreeStation.MEASURES).get(i);

            Point st = SharedResources.getSetOfPoints().find(
                    measureObject.getString(Measure.ORIENTATION_NUMBER));

            Measure m = new Measure(
                    st,
                    measureObject.getDouble(Measure.HORIZ_DIR),
                    measureObject.getDouble(Measure.ZEN_ANGLE),
                    measureObject.getDouble(Measure.DISTANCE),
                    measureObject.getDouble(Measure.S),
                    measureObject.getDouble(Measure.LAT_DEPL),
                    measureObject.getDouble(Measure.LON_DEPL),
                    measureObject.getDouble(Measure.I),
                    measureObject.getDouble(Measure.UNKNOWN_ORIENTATION));
            this.measures.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return FreeStationActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_free_station);
    }

    public final String getStationNumber() {
        return this.stationNumber;
    }

    public final void setStationNumber(String _stationNumber) {
        this.stationNumber = _stationNumber;
    }

    public final ArrayList<Measure> getMeasures() {
        return this.measures;
    }

    public final void setMeasures(ArrayList<Measure> _measures) {
        this.measures = _measures;
    }

    public final Point getStationResult() {
        return this.stationResult;
    }

    public final void setStationResult(Point _stationResult) {
        this.stationResult = _stationResult;
    }

    public final double getUnknownOrientation() {
        return this.unknownOrientation;
    }

    public final void setUnknownOrientation(double _unknownOrientation) {
        this.unknownOrientation = _unknownOrientation;
    }

    public double getScaleFactor() {
        return this.scaleFactor;
    }

    public int getScaleFactorPPM() {
        return MathUtils.scaleToPPM(this.scaleFactor);
    }

    public final double getI() {
        return this.i;
    }

    public final void setI(double i) {
        this.i = i;
    }

    public final double getsE() {
        return this.sE;
    }

    public final double getsN() {
        return this.sN;
    }

    public final double getsA() {
        return this.sA;
    }

    public final double getMeanFS() {
        return this.meanFS;
    }

    public final ArrayList<Result> getResults() {
        return this.results;
    }

    private boolean hasDeactivatedMeasure(ArrayList<Measure> measures) {
        for (Measure m : measures) {
            if (m.isDeactivated()) {
                return true;
            }
        }
        return false;
    }

    public static class Result implements Serializable {
        /**
         * The target point.
         */
        private Point point;

        private boolean deactivated;

        /**
         * Difference between the east coordinate of the original point and the
         * new one.
         */
        private double vE;

        /**
         * Difference between the north coordinate of the original point and the
         * new one.
         */
        private double vN;

        /**
         * Difference between the altitude of the original point and the new
         * one.
         */
        private double vA;

        /**
         * FS.
         */
        private double fS;

        /**
         * Weight.
         */
        private double weight;

        public Result(Point _point, double _vE, double _vN, double _vA,
                      double _weight) {
            this.point = _point;
            this.vE = _vE;
            this.vN = _vN;
            this.vA = _vA;
            this.weight = _weight;
            this.deactivated = false;
        }

        public Result(Point _point) {
            this(_point, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        }

        public Result(Point _point, double _weight) {
            this(_point, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, _weight);
        }

        public final Point getPoint() {
            return this.point;
        }

        public final void setPoint(Point point) {
            this.point = point;
        }

        public final double getvE() {
            return this.vE;
        }

        public final void setvE(double vE) {
            this.vE = vE;
        }

        public final double getvN() {
            return this.vN;
        }

        public final void setvN(double vN) {
            this.vN = vN;
        }

        public final double getvA() {
            return this.vA;
        }

        public final void setvA(double vA) {
            this.vA = vA;
        }

        public final double getfS() {
            return this.fS;
        }

        public final void setfS(double fS) {
            this.fS = fS;
        }

        public final double getWeight() {
            return this.weight;
        }

        public final void setWeight(double weight) {
            this.weight = weight;
        }

        public final boolean isDeactivated() {
            return this.deactivated;
        }

        public final void toggle() {
            this.deactivated = !this.deactivated;
        }
    }

    /**
     * This nested class is just used for holding intermediate results.
     *
     * @author HGdev
     */
    private class IntermediateResults implements Serializable {
        /**
         * Fictive gisement. (this attribute is used from the outside)
         */
        @SuppressWarnings("unused")
        private final double gisFict;

        /**
         * Fictive distance. (this attribute is used from the outside)
         */
        @SuppressWarnings("unused")
        private final double distFict;

        /**
         * Cadastral gisement. (this attribute is used from the outside)
         */
        @SuppressWarnings("unused")
        private final double gisCadast;

        /**
         * Cadastral distance. (this attribute is used from the outside)
         */
        @SuppressWarnings("unused")
        private final double distCadast;

        /**
         * Rotation between fictive and cadastral coordinates. (this attribute
         * is used from the outside)
         */
        private double rotation;

        /**
         * Multiplication constants between fictive and cadastral coordinates.
         * (this attribute is used from the outside)
         */
        @SuppressWarnings("unused")
        private double constant;

        private IntermediateResults(double _gisFict, double _distFict,
                                    double _gisCadast, double _distCadast) {
            this.gisFict = _gisFict;
            this.distFict = _distFict;

            this.gisCadast = _gisCadast;
            this.distCadast = _distCadast;

            this.rotation = MathUtils.IGNORE_DOUBLE;
            this.constant = MathUtils.IGNORE_DOUBLE;
        }
    }
}
