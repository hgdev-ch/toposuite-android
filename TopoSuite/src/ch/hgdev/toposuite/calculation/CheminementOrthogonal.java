package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.cheminortho.CheminementOrthoActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

public class CheminementOrthogonal extends Calculation {
    public static final String                       ORTHOGONAL_BASE      = "orthogonal_base";
    public static final String                       MEASURES             = "measures";

    private static final String                      DUMMY_POINT_NUMBER_1 = "42";
    private static final String                      DUMMY_POINT_NUMBER_2 = "22";
    private static final String                      DUMMY_POINT_NUMBER_3 = "3232";

    private OrthogonalBase                           orthogonalBase;
    private ArrayList<CheminementOrthogonal.Measure> measures;
    private ArrayList<CheminementOrthogonal.Result>  results;

    private double                                   fE;
    private double                                   fN;
    private double                                   fs;
    private double                                   scale;

    public CheminementOrthogonal(Point origin, Point extremity, boolean hasDAO) {
        super(CalculationType.CHEMINORTHO,
                App.getContext().getString(R.string.title_activity_cheminement_ortho),
                hasDAO);

        this.orthogonalBase = new OrthogonalBase(origin, extremity);
        this.measures = new ArrayList<CheminementOrthogonal.Measure>();
        this.results = new ArrayList<CheminementOrthogonal.Result>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public CheminementOrthogonal(long id, Date lastModification) {
        super(id, CalculationType.CHEMINORTHO,
                App.getContext().getString(R.string.title_activity_cheminement_ortho),
                lastModification,
                true);
        this.orthogonalBase = new OrthogonalBase();
        this.measures = new ArrayList<CheminementOrthogonal.Measure>();
        this.results = new ArrayList<CheminementOrthogonal.Result>();
    }

    public CheminementOrthogonal(boolean hasDAO) {
        super(CalculationType.CHEMINORTHO,
                App.getContext().getString(R.string.title_activity_cheminement_ortho),
                hasDAO);

        this.orthogonalBase = new OrthogonalBase();
        this.measures = new ArrayList<CheminementOrthogonal.Measure>();
        this.results = new ArrayList<CheminementOrthogonal.Result>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    @Override
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
        double deltaCalcEast = this.orthogonalBase.getExtremity().getEast() -
                this.orthogonalBase.getOrigin().getEast();
        double deltaCalcNorth = this.orthogonalBase.getExtremity().getNorth() -
                this.orthogonalBase.getOrigin().getNorth();

        // rotation of the temporary calculation

        // gisement of the temporary base
        Gisement g = new Gisement(this.orthogonalBase.getOrigin(),
                new Point("42", eastExt, northExt, 0.0, false, false), false);
        g.compute();
        gis = g.getGisement();

        // gisement of the calculated base
        g = new Gisement(this.orthogonalBase.getOrigin(),
                this.orthogonalBase.getExtremity(), false);
        double calcGis = g.getGisement();

        // rotation to apply
        double rot = calcGis - gis;

        currEast = this.orthogonalBase.getOrigin().getEast();
        currNorth = this.orthogonalBase.getOrigin().getNorth();

        // correction / adjustment
        gis += rot;
        dist = MathUtils.euclideanDistance(
                this.orthogonalBase.getOrigin(),
                new Point(CheminementOrthogonal.DUMMY_POINT_NUMBER_3, eastExt, northExt, 0.0,
                        false, false));

        double deltaMeasEast = MathUtils.pointLanceEast(currEast, gis, dist) - currEast;
        double deltaMeasNorth = MathUtils.pointLanceNorth(currNorth, gis, dist) - currNorth;

        this.fE = deltaCalcEast - deltaMeasEast;
        this.fN = deltaCalcNorth - deltaMeasNorth;
        this.fs = MathUtils.pythagoras(this.fE, this.fN);

        this.scale = this.orthogonalBase.getScaleFactor();
        if (MathUtils.isZero(this.scale)) {
            // automatic scale factor determination
            this.scale = MathUtils.euclideanDistance(this.orthogonalBase.getOrigin(),
                    this.orthogonalBase.getExtremity()) / dist;
        }

        // calculation of the final coordinates
        for (CheminementOrthogonal.Result r : tmpResults) {
            Point p1 = new Point(CheminementOrthogonal.DUMMY_POINT_NUMBER_1, currEast, currNorth,
                    0.0, false, false);
            Point p2 = new Point(CheminementOrthogonal.DUMMY_POINT_NUMBER_2, r.getEast(),
                    r.getNorth(), 0.0, false, false);

            g = new Gisement(p1, p2, false);
            g.compute();
            gis = g.getGisement() + rot;
            dist = MathUtils.euclideanDistance(p1, p2) * this.scale;

            CheminementOrthogonal.Result newR = new CheminementOrthogonal.Result(
                    r.getNumber(),
                    MathUtils.pointLanceEast(currEast, gis, dist),
                    MathUtils.pointLanceNorth(currNorth, gis, dist));

            Point p = SharedResources.getSetOfPoints().find(r.getNumber());
            if (p != null) {
                newR.setvE(MathUtils.mToCm(p.getEast() - newR.getEast()));
                newR.setvN(MathUtils.mToCm(p.getNorth() - newR.getNorth()));
            }

            this.results.add(newR);
        }

        this.updateLastModification();
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.origin_label) + ": "
                + this.orthogonalBase.getOrigin().toString()
                + " / " + App.getContext().getString(R.string.extremity_label) + ": "
                + this.orthogonalBase.getExtremity().toString());
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (this.orthogonalBase != null) {
            json.put(CheminementOrthogonal.ORTHOGONAL_BASE,
                    this.orthogonalBase.toJSONObject());
        }

        if (this.measures.size() > 0) {
            JSONArray measuresArray = new JSONArray();
            for (CheminementOrthogonal.Measure m : this.measures) {
                measuresArray.put(m.toJSONObject());
            }

            json.put(CheminementOrthogonal.MEASURES, measuresArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);

        OrthogonalBase ob = OrthogonalBase.getOrthogonalBaseFromJSON(
                ((JSONObject) json.get(CheminementOrthogonal.ORTHOGONAL_BASE)).toString());
        this.orthogonalBase = ob;

        JSONArray measuresArray = json.getJSONArray(CheminementOrthogonal.MEASURES);

        for (int i = 0; i < measuresArray.length(); i++) {
            JSONObject jo = (JSONObject) measuresArray.get(i);
            CheminementOrthogonal.Measure m = CheminementOrthogonal.Measure.getMeasureFromJSON(
                    jo.toString());
            this.measures.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return CheminementOrthoActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_cheminement_ortho);
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

    public void setResults(ArrayList<CheminementOrthogonal.Result> _results) {
        this.results = _results;
    }

    public double getfE() {
        return this.fE;
    }

    public void setfE(double _fE) {
        this.fE = _fE;
    }

    public double getfN() {
        return this.fN;
    }

    public void setfN(double _fN) {
        this.fN = _fN;
    }

    public double getFs() {
        return this.fs;
    }

    public void setFs(double _fs) {
        this.fs = _fs;
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double _scale) {
        this.scale = _scale;
    }

    /**
     * Result holds the resulting points of the cheminement orthogonal.
     *
     * @author HGdev
     */
    public static class Result {
        private String number;
        private double east;
        private double north;
        private double vE;
        private double vN;

        public Result(String _number, double _east, double _north, double _vE, double _vN) {
            this.number = _number;
            this.east = _east;
            this.north = _north;
            this.vE = _vE;
            this.vN = _vN;
        }

        public Result(String _number, double _east, double _north) {
            this(_number, _east, _north, 0.0, 0.0);
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
        public static final String NUMBER   = "number";
        public static final String DISTANCE = "distance";

        private String             number;
        private double             distance;

        public Measure(String _number, double _distance) {
            this.number = _number;
            this.distance = _distance;
        }

        public JSONObject toJSONObject() {
            JSONObject jo = new JSONObject();

            try {
                jo.put(Measure.NUMBER, this.number);
                jo.put(Measure.DISTANCE, this.distance);
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            }

            return jo;
        }

        public static Measure getMeasureFromJSON(String json) {
            Measure m = null;

            try {
                JSONObject jo = new JSONObject(json);

                String number = jo.getString(Measure.NUMBER);
                double distance = jo.getDouble(Measure.DISTANCE);

                m = new Measure(number, distance);
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            }

            return m;
        }

        public String getNumber() {
            return this.number;
        }

        public void setNumber(String number) {
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