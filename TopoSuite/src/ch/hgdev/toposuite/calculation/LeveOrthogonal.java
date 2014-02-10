package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class LeveOrthogonal extends Calculation {
    private static final String               CALCULATION_NAME = "Levé Orthogonal";

    private OrthogonalBase                    orthogonalBase;

    private ArrayList<LeveOrthogonal.Measure> measures;

    private ArrayList<LeveOrthogonal.Measure> results;

    public LeveOrthogonal(Point origin, Point extremity, double measuredDistance, boolean hasDAO) {
        super(CalculationType.LEVEORTHO, LeveOrthogonal.CALCULATION_NAME, hasDAO);

        this.orthogonalBase = new OrthogonalBase(origin, extremity, measuredDistance);
        this.measures = new ArrayList<LeveOrthogonal.Measure>();
        this.results = new ArrayList<LeveOrthogonal.Measure>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public void computer() {
        if (this.measures.size() < 1) {
            return;
        }

        Gisement g = new Gisement(this.orthogonalBase.getOrigin(),
                this.orthogonalBase.getExtemity(), false);
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

            this.results.add(newM);
        }
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

    public static class Measure {
        private int    number;
        private double abscissa;
        private double ordinate;

        public Measure(int _number, double _abscissa, double _ordinate) {
            this.number = _number;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
        }

        public int getNumber() {
            return this.number;
        }

        public void setNumber(int number) {
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
    }
}