package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;

// TODO implement
public class AxisImplantation extends Calculation {
    private OrthogonalBase      orthogonalBase;

    private final List<Measure> measures;

    public AxisImplantation(long id, Date lastModification) {
        super(id,
                CalculationType.AXISIMPLANTATION,
                "Axis implantation",
                lastModification,
                true);
        this.measures = new ArrayList<Measure>();
    }

    public AxisImplantation(Point station, double unknownOrientation,
            Point origin, Point extremity, boolean hasDAO) {
        super(CalculationType.AXISIMPLANTATION,
                "Axis implantation",
                hasDAO);
        this.orthogonalBase = new OrthogonalBase(origin, extremity);
        this.measures = new ArrayList<Measure>();

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    @Override
    public void compute() {
        // TODO Implement
    }

    @Override
    public String exportToJSON() throws JSONException {
        // TODO Implement
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO Implement

    }

    @Override
    public Class<?> getActivityClass() {
        // TODO Implement
        return null;
    }

    public class Result {
        private int    number;
        private double east;
        private double north;
        private double abscissa;
        private double ordinate;

        public Result(int _number, double _east, double _north, double _abscissa,
                double _ordinate) {
            this.number = _number;
            this.east = _east;
            this.north = _north;
            this.abscissa = _abscissa;
            this.ordinate = _ordinate;
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
