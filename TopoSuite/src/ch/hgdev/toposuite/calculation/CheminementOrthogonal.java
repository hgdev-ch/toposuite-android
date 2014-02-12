package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class CheminementOrthogonal extends Calculation {
    private static final String                      CALCULATION_NAME = "Cheminement orthogonal";

    private OrthogonalBase                           orthogonalBase;
    private ArrayList<CheminementOrthogonal.Measure> measures;
    private ArrayList<CheminementOrthogonal.Result>  results;

    private double                                   fE;
    private double                                   fN;
    private double                                   fs;

    public CheminementOrthogonal(Point origin, Point extremity, boolean hasDAO) {
        super(CalculationType.CHEMINORTHO, CheminementOrthogonal.CALCULATION_NAME, hasDAO);

        this.orthogonalBase = new OrthogonalBase(origin, extremity);
        this.measures = new ArrayList<CheminementOrthogonal.Measure>();
        this.results = new ArrayList<CheminementOrthogonal.Result>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public CheminementOrthogonal(long id, Date lastModification) {
        super(id, CalculationType.CHEMINORTHO, CheminementOrthogonal.CALCULATION_NAME,
                lastModification, true);
    }

    public void compute() {
        if (this.measures.size() < 1) {
            return;
        }

        // current iteration
        int iter = 1;

        // because the first gisement is +100, by definition
        double gis = -100.0;

        // current east/north coordinate
        double currEast = this.orthogonalBase.getOrigin().getEast();
        double currNorth = this.orthogonalBase.getOrigin().getNorth();

        ArrayList<CheminementOrthogonal.Result> tmpResults =
                new ArrayList<CheminementOrthogonal.Result>();

        double dist = 0.0;

        for (CheminementOrthogonal.Measure m : this.measures) {
            dist = (iter == 1) ? Math.abs(m.getDistance()) : m.getDistance();

            if (MathUtils.isPositive(dist) || MathUtils.isZero(dist)) {
                gis += 100;
            } else {
                gis -= 100;
            }

            CheminementOrthogonal.Result res = new CheminementOrthogonal.Result(
                    m.getNumber(),
                    MathUtils.pointLanceEast(currEast, gis, Math.abs(dist)),
                    MathUtils.pointLanceNorth(currNorth, gis, Math.abs(dist)));
            tmpResults.add(res);

            currEast = res.getEast();
            currNorth = res.getNorth();
            iter++;
        }

        // diff
        double eastExt = currEast;
        double northExt = currNorth;
        double deltaCalcEast = this.orthogonalBase.getExtemity().getEast() -
                this.orthogonalBase.getOrigin().getEast();
        double deltaCalcNorth = this.orthogonalBase.getExtemity().getNorth() -
                this.orthogonalBase.getOrigin().getNorth();

        // rotation of the temporary calculation

        // gisement of the temportary base
        Gisement g = new Gisement(this.orthogonalBase.getOrigin(),
                new Point(42, eastExt, northExt, 0.0, false, false), false);
        g.compute();
        gis = g.getGisement();

        // gisement of the calculated base
        g = new Gisement(this.orthogonalBase.getOrigin(),
                this.orthogonalBase.getExtemity(), false);
        double calcGis = g.getGisement();

        // rotation to apply
        double rot = calcGis - gis;

        currEast = this.orthogonalBase.getOrigin().getEast();
        currNorth = this.orthogonalBase.getOrigin().getNorth();

        // correction / adjustment
        gis += rot;
        dist = MathUtils.euclideanDistance(
                this.orthogonalBase.getOrigin(),
                new Point(3232, eastExt, northExt, 0.0, false, false));

        double deltaMeasEast = MathUtils.pointLanceEast(currEast, gis, dist) - currEast;
        double deltaMeasNorth = MathUtils.pointLanceNorth(currNorth, gis, dist) - currNorth;

        this.fE = deltaCalcEast - deltaMeasEast;
        this.fN = deltaCalcNorth - deltaMeasNorth;
        this.fs = MathUtils.pythagoras(this.fE, this.fN);

        double scale = this.orthogonalBase.getScaleFactor();
        if (MathUtils.isZero(scale)) {
            // automatic scale factor determination
            scale = MathUtils.euclideanDistance(this.orthogonalBase.getOrigin(),
                    this.orthogonalBase.getExtemity()) / dist;
        }

        // calculation of the final coordinates
        for (CheminementOrthogonal.Result r : tmpResults) {
            Point p1 = new Point(42, currEast, currNorth, 0.0, false, false);
            Point p2 = new Point(24, r.getEast(), r.getNorth(), 0.0, false, false);

            g = new Gisement(p1, p2, false);
            g.compute();
            gis = g.getGisement() + rot;
            dist = MathUtils.euclideanDistance(p1, p2) * scale;

            CheminementOrthogonal.Result newR = new CheminementOrthogonal.Result(
                    r.getNumber(),
                    MathUtils.pointLanceEast(currEast, gis, dist),
                    MathUtils.pointLanceNorth(currNorth, gis, dist));

            Point p = SharedResources.getSetOfPoints().find(r.getNumber());
            if (p != null) {
                newR.setvE(p.getEast() - newR.getEast());
                newR.setvN(p.getNorth() - newR.getNorth());
            }

            this.results.add(newR);
        }

        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO Auto-generated method stub

    }

    @Override
    public Class<?> getActivityClass() {
        // TODO Auto-generated method stub
        return null;
    }

    public OrthogonalBase getOrthogonalBase() {
        return this.orthogonalBase;
    }

    public void setOrthogonalBase(OrthogonalBase orthogonalBase) {
        this.orthogonalBase = orthogonalBase;
    }

    public ArrayList<CheminementOrthogonal.Measure> getMeasures() {
        return this.measures;
    }

    public void setMeasures(ArrayList<CheminementOrthogonal.Measure> measures) {
        this.measures = measures;
    }

    public ArrayList<CheminementOrthogonal.Result> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<CheminementOrthogonal.Result> results) {
        this.results = results;
    }

    public double getfE() {
        return this.fE;
    }

    public void setfE(double fE) {
        this.fE = fE;
    }

    public double getfN() {
        return this.fN;
    }

    public void setfN(double fN) {
        this.fN = fN;
    }

    public double getFs() {
        return this.fs;
    }

    public void setFs(double fs) {
        this.fs = fs;
    }

    /**
     * Result holds the resulting points of the cheminement orthogonal.
     * 
     * @author HGdev
     */
    public static class Result {
        private int    number;
        private double east;
        private double north;
        private double vE;
        private double vN;

        public Result(int _number, double _east, double _north, double _vE, double _vN) {
            this.number = _number;
            this.east = _east;
            this.north = _north;
            this.vE = _vE;
            this.vN = _vN;
        }

        public Result(int _number, double _east, double _north) {
            this(_number, _east, _north, 0.0, 0.0);
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

        public double getvE() {
            return this.vE;
        }

        public void setvE(double vE) {
            this.vE = vE;
        }

        public double getvN() {
            return this.vN;
        }

        public void setvN(double vN) {
            this.vN = vN;
        }
    }

    /**
     * Measure holds an input measure of the cheminement orthogoal.
     * 
     * @author HGdev
     */
    public static class Measure {
        private int    number;
        private double distance;

        public Measure(int _number, double _distance) {
            this.number = _number;
            this.distance = _distance;
        }

        public int getNumber() {
            return this.number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public double getDistance() {
            return this.distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}