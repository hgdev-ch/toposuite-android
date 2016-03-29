package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.leveortho.LeveOrthoActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

public class LeveOrthogonal extends Calculation {
    public static final String                      ORTHOGONAL_BASE = "orthogonal_base";
    public static final String                      MEASURES        = "measures";

    private OrthogonalBase                          orthogonalBase;

    private final ArrayList<LeveOrthogonal.Measure> measures;

    private final ArrayList<LeveOrthogonal.Measure> results;

    public LeveOrthogonal(Point origin, Point extremity, double measuredDistance, boolean hasDAO) {
        super(CalculationType.LEVEORTHO,
                App.getContext().getString(R.string.title_activity_leve_ortho),
                hasDAO);

        this.orthogonalBase = new OrthogonalBase(origin, extremity, measuredDistance, 1.0);
        this.measures = new ArrayList<LeveOrthogonal.Measure>();
        this.results = new ArrayList<LeveOrthogonal.Measure>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public LeveOrthogonal(Point origin, Point extremity, boolean hasDAO) {
        this(origin, extremity, MathUtils.IGNORE_DOUBLE, hasDAO);
    }

    public LeveOrthogonal(boolean hasDAO) {
        super(CalculationType.LEVEORTHO,
                App.getContext().getString(R.string.title_activity_leve_ortho),
                hasDAO);

        this.orthogonalBase = new OrthogonalBase(1.0);
        this.measures = new ArrayList<LeveOrthogonal.Measure>();
        this.results = new ArrayList<LeveOrthogonal.Measure>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public LeveOrthogonal(long id, Date lastModification) {
        super(id, CalculationType.LEVEORTHO,
                App.getContext().getString(R.string.title_activity_leve_ortho),
                lastModification,
                true);
        this.orthogonalBase = new OrthogonalBase(1.0);
        this.measures = new ArrayList<LeveOrthogonal.Measure>();
        this.results = new ArrayList<LeveOrthogonal.Measure>();
    }

    @Override
    public void compute() {
        if (this.measures.size() < 1) {
            return;
        }

        this.results.clear();

        Gisement g = new Gisement(this.orthogonalBase.getOrigin(),
                this.orthogonalBase.getExtremity(), false);
        g.compute();

        double giseCalc = MathUtils.gradToRad(g.getGisement());
        double k = this.orthogonalBase.getScaleFactor();

        for (LeveOrthogonal.Measure m : this.measures) {
            double east = this.orthogonalBase.getOrigin().getEast() +
                    (k * m.getAbscissa() * Math.sin(giseCalc)) +
                    (k * m.getOrdinate() * Math.sin(giseCalc + (Math.PI / 2)));
            double north = this.orthogonalBase.getOrigin().getNorth() +
                    (k * m.getAbscissa() * Math.cos(giseCalc)) +
                    (k * m.getOrdinate() * Math.cos(giseCalc + (Math.PI / 2)));

            LeveOrthogonal.Measure newM = new LeveOrthogonal.Measure(
                    m.getNumber(), east, north);

            Point p = SharedResources.getSetOfPoints().find(m.getNumber());
            if (p != null) {
                newM.setvE(MathUtils.mToCm(p.getEast() - east));
                newM.setvN(MathUtils.mToCm(p.getNorth() - north));
            }

            this.results.add(newM);
        }

        super.updateLastModification();
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.origin_label) + ": "
                + this.orthogonalBase.getOrigin().toString()
                + " / " + App.getContext().getString(R.string.extremity_label) + ": "
                + this.orthogonalBase.getExtremity().toString());
        super.notifyUpdate(this);
    }

    public OrthogonalBase getOrthogonalBase() {
        return this.orthogonalBase;
    }

    public void setOrthogonalBase(OrthogonalBase orthogonalBase) {
        this.orthogonalBase = orthogonalBase;
    }

    public ArrayList<LeveOrthogonal.Measure> getMeasures() {
        return this.measures;
    }

    public ArrayList<LeveOrthogonal.Measure> getResults() {
        return this.results;
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (this.orthogonalBase != null) {
            json.put(LeveOrthogonal.ORTHOGONAL_BASE, this.orthogonalBase.toJSONObject());
        }

        if (this.measures.size() > 0) {
            JSONArray measuresArray = new JSONArray();
            for (LeveOrthogonal.Measure m : this.measures) {
                measuresArray.put(m.toJSONObject());
            }

            json.put(LeveOrthogonal.MEASURES, measuresArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);

        OrthogonalBase ob = OrthogonalBase.getOrthogonalBaseFromJSON(
                ((JSONObject) json.get(LeveOrthogonal.ORTHOGONAL_BASE)).toString());
        this.orthogonalBase = ob;

        JSONArray measuresArray = json.getJSONArray(LeveOrthogonal.MEASURES);

        for (int i = 0; i < measuresArray.length(); i++) {
            JSONObject jo = (JSONObject) measuresArray.get(i);
            LeveOrthogonal.Measure m = LeveOrthogonal.Measure.getMeasureFromJSON(
                    jo.toString());
            this.measures.add(m);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return LeveOrthoActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_leve_ortho);
    }

    public static class Measure {
        public static final String NUMBER   = "number";
        public static final String ABSCISSA = "abscissa";
        public static final String ORDINATE = "ordinate";

        private String             number;
        private double             abscissa;
        private double             ordinate;

        /**
         * If the points already exists, vE is the difference between the old
         * east value and the new one.
         */
        private double             vE;

        /**
         * If the points already exists, vN is the difference between the old
         * north value and the new one.
         */
        private double             vN;

        public Measure(String _number, double _abscissa, double _ordinate, double _vE, double _vN) {
            this.number = _number;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
            this.vE = _vE;
            this.vN = _vN;
        }

        public Measure(String _number, double _abscissa, double _ordinate) {
            this(_number, _abscissa, _ordinate, 0.0, 0.0);
        }

        public JSONObject toJSONObject() {
            JSONObject jo = new JSONObject();

            try {
                jo.put(Measure.NUMBER, this.number);
                jo.put(Measure.ABSCISSA, this.abscissa);
                jo.put(Measure.ORDINATE, this.ordinate);
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
                double abscissa = jo.getDouble(Measure.ABSCISSA);
                double ordinate = jo.getDouble(Measure.ORDINATE);

                m = new Measure(number, abscissa, ordinate);
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
}