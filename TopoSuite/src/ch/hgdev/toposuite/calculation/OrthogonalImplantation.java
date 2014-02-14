package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class OrthogonalImplantation extends Calculation {
    public static final String                       CALCULATION_NAME = "Implant. Ortho.";

    private OrthogonalBase                           orthogonalBase;
    private ArrayList<Point>                         measures;
    private ArrayList<OrthogonalImplantation.Result> results;

    public OrthogonalImplantation(Point origin, Point extremity, boolean hasDAO) {
        super(CalculationType.ORTHOIMPL, OrthogonalImplantation.CALCULATION_NAME, hasDAO);

        this.orthogonalBase = new OrthogonalBase(origin, extremity);
        this.measures = new ArrayList<Point>();
        this.results = new ArrayList<OrthogonalImplantation.Result>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public OrthogonalImplantation(boolean hasDAO) {
        super(CalculationType.ORTHOIMPL, OrthogonalImplantation.CALCULATION_NAME, hasDAO);

        this.orthogonalBase = new OrthogonalBase();
        this.measures = new ArrayList<Point>();
        this.results = new ArrayList<OrthogonalImplantation.Result>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public OrthogonalImplantation(long id, Date lastModification) {
        super(id, CalculationType.ORTHOIMPL,
                OrthogonalImplantation.CALCULATION_NAME, lastModification, true);

        this.orthogonalBase = new OrthogonalBase();
        this.measures = new ArrayList<Point>();
        this.results = new ArrayList<OrthogonalImplantation.Result>();
    }

    public void compute() {
        if (this.measures.size() == 0) {
            return;
        }

        this.results.clear();

        double gisBase = new Gisement(this.orthogonalBase.getOrigin(),
                this.orthogonalBase.getExtemity()).getGisement();

        for (Point p : this.measures) {
            PointProjectionOnALine ppoal = new PointProjectionOnALine(
                    4242,
                    this.orthogonalBase.getOrigin(),
                    this.orthogonalBase.getExtemity(),
                    p,
                    0.0,
                    false);
            ppoal.compute();

            Point projPt = ppoal.getProjPt();
            double abscissa = MathUtils.euclideanDistance(this.orthogonalBase.getOrigin(),
                    projPt);
            double ordinate = MathUtils.euclideanDistance(p, projPt);

            double angle = MathUtils.angle3Pts(this.orthogonalBase.getExtemity(),
                    this.orthogonalBase.getOrigin(), p);

            abscissa = ((angle > 100) && (angle < 300)) ? -abscissa : abscissa;
            ordinate = (angle > 200) ? -ordinate : ordinate;

            this.results.add(new Result(p, abscissa, ordinate));
        }

    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO
        return "";
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO
    }

    @Override
    public Class<?> getActivityClass() {
        return null;
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

    public static class Result {
        private Point  pt;
        private double abscissa;
        private double ordinate;

        public Result(Point _pt, double _abscissa, double _ordinate) {
            this.pt = _pt;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
        }

        public Point getPt() {
            return this.pt;
        }

        public void setPt(Point _pt) {
            this.pt = _pt;
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