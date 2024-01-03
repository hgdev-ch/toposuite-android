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
import ch.hgdev.toposuite.calculation.activities.orthoimpl.OrthogonalImplantationActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class OrthogonalImplantation extends Calculation {
    public static final String POINT_NUMBER = "point_number";

    private static final String ORTHOGONAL_BASE = "orthogonal_base";
    private static final String MEASURES = "measures";
    private static final String DUMMY_POINT_NUMBER = "42";

    private OrthogonalBase orthogonalBase;
    private ArrayList<Point> measures;
    private ArrayList<OrthogonalImplantation.Result> results;

    public OrthogonalImplantation(@NonNull OrthogonalBase base, boolean hasDAO) {
        super(CalculationType.ORTHOIMPL,
                App.getContext().getString(R.string.title_activity_orthogonal_implantation),
                hasDAO);

        this.orthogonalBase = base;
        this.measures = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public OrthogonalImplantation(boolean hasDAO) {
        this(new OrthogonalBase(), true);
    }

    public OrthogonalImplantation(long id, Date lastModification) {
        super(id,
                CalculationType.ORTHOIMPL,
                App.getContext().getString(R.string.title_activity_orthogonal_implantation),
                lastModification,
                true);

        this.orthogonalBase = new OrthogonalBase();
        this.measures = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    @Override
    public void compute() throws CalculationException {
        if (this.measures.size() == 0) {
            throw new CalculationException("no measure provided");
        }
        if ((this.orthogonalBase == null)
                || (this.orthogonalBase.getOrigin() == null)
                || (this.orthogonalBase.getExtremity() == null)) {
            throw new CalculationException("orthogonal base is missing origin, extremity or both points");
        }

        this.results.clear();

        for (Point p : this.measures) {
            PointProjectionOnALine ppoal = new PointProjectionOnALine(
                    OrthogonalImplantation.DUMMY_POINT_NUMBER,
                    this.orthogonalBase.getOrigin(),
                    this.orthogonalBase.getExtremity(),
                    p,
                    0.0,
                    false);
            ppoal.compute();

            Point projPt = ppoal.getProjPt();
            double abscissa = MathUtils.euclideanDistance(this.orthogonalBase.getOrigin(),
                    projPt);
            double ordinate = MathUtils.euclideanDistance(p, projPt);

            double angle = MathUtils.angle3Pts(this.orthogonalBase.getExtremity(),
                    this.orthogonalBase.getOrigin(), p);

            abscissa = ((angle > 100) && (angle < 300)) ? -abscissa : abscissa;
            ordinate = (angle > 200) ? -ordinate : ordinate;

            this.results.add(new Result(p, abscissa, ordinate));
        }

        this.postCompute();
    }

    @Override
    protected void postCompute() {
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.origin_label) + ": "
                + this.orthogonalBase.getOrigin().toString()
                + " / " + App.getContext().getString(R.string.extremity_label) + ": "
                + this.orthogonalBase.getExtremity().toString());
        super.postCompute();
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (this.orthogonalBase != null) {
            json.put(OrthogonalImplantation.ORTHOGONAL_BASE,
                    this.orthogonalBase.toJSONObject());
        }

        if (this.measures.size() > 0) {
            JSONArray measuresArray = new JSONArray();
            for (Point p : this.measures) {
                if (p != null) {
                    measuresArray.put(p.getNumber());
                }
            }

            json.put(OrthogonalImplantation.MEASURES, measuresArray);
        }

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);

        OrthogonalBase ob = OrthogonalBase.getOrthogonalBaseFromJSON(
                json.get(OrthogonalImplantation.ORTHOGONAL_BASE).toString());
        this.orthogonalBase = ob;

        JSONArray measuresArray = json.getJSONArray(OrthogonalImplantation.MEASURES);

        for (int i = 0; i < measuresArray.length(); i++) {
            String number = (String) measuresArray.get(i);
            Point p = SharedResources.getSetOfPoints().find(number);
            this.measures.add(p);
        }
    }

    @Override
    public Class<?> getActivityClass() {
        return OrthogonalImplantationActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_orthogonal_implantation);
    }

    public OrthogonalBase getOrthogonalBase() {
        return this.orthogonalBase;
    }

    public void setOrthogonalBase(OrthogonalBase orthogonalBase) {
        this.orthogonalBase = orthogonalBase;
    }

    public ArrayList<Point> getMeasures() {
        return this.measures;
    }

    public void setMeasures(ArrayList<Point> _measures) {
        this.measures = _measures;
    }

    public ArrayList<OrthogonalImplantation.Result> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<OrthogonalImplantation.Result> _results) {
        this.results = _results;
    }

    public static class Result implements Serializable {
        private Point point;
        private double abscissa;
        private double ordinate;

        public Result(Point _point, double _abscissa, double _ordinate) {
            this.point = _point;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
        }

        public Point getPoint() {
            return this.point;
        }

        public void setPoint(Point _point) {
            this.point = _point;
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