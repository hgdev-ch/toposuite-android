package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.circcurvesolver.CircularCurvesSolverActivity;
import ch.hgdev.toposuite.utils.MathUtils;

public class CircularCurvesSolver extends Calculation {
    public static final String RADIUS      = "radius";
    public static final String ALPHA_ANGLE = "alpha_angle";
    public static final String CHORD_OF    = "chord_of";
    public static final String TANGENT     = "tangent";
    public static final String ARROW       = "arrow";

    private double             radius;
    /** central angle */
    private double             alphaAngle;
    private double             chordOF;
    private double             tangent;
    private double             arrow;

    private double             bisector;
    private double             arc;
    private double             circumference;
    private double             chordOM;
    /** Vertex angle */
    private double             betaAngle;
    private double             circleSurface;
    private double             sectorSurface;
    private double             segmentSurface;

    public CircularCurvesSolver(double _radius, double _alphaAngle,
            double _chordOF, double _tangent, double _arrow, boolean hasDAO) {

        super(
                CalculationType.CIRCCURVESOLVER,
                App.getContext().getString(R.string.title_activity_circular_curve_solver),
                hasDAO);

        this.radius = _radius;
        this.alphaAngle = _alphaAngle;
        this.chordOF = _chordOF;
        this.tangent = _tangent;
        this.arrow = _arrow;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public CircularCurvesSolver(long id, Date lastModification) {
        super(
                id,
                CalculationType.CIRCCURVESOLVER,
                App.getContext().getString(R.string.title_activity_circular_curve_solver),
                lastModification,
                true);
    }

    public CircularCurvesSolver(boolean hasDAO) {
        this(MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE, hasDAO);
    }

    @Override
    public void compute() {
        if (!MathUtils.isZero(this.radius) && !MathUtils.isZero(this.chordOF)) {
            // radius / chord OF

            this.alphaAngle = Math.asin(this.chordOF / (2 * this.radius));
            this.betaAngle = MathUtils.radToGrad(Math.PI - (2 * this.alphaAngle));
            this.arrow = this.radius - (this.radius * Math.cos(this.alphaAngle));
            this.tangent = this.radius * Math.tan(this.alphaAngle);
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.radius)
                && !MathUtils.isZero(this.alphaAngle)) {
            // radius / central angle

            this.alphaAngle /= 2;

            this.alphaAngle = MathUtils.gradToRad(this.alphaAngle);
            this.betaAngle = MathUtils.radToGrad(
                    Math.PI - (2 * this.alphaAngle));
            this.arrow = this.radius - (this.radius * Math.cos(
                    this.alphaAngle));
            this.tangent = this.radius * Math.tan(
                    this.alphaAngle);
            this.chordOF = 2 * this.radius * Math.sin(
                    this.alphaAngle);
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.radius)
                && !MathUtils.isZero(this.tangent)) {
            // radius / tangent

            this.alphaAngle = Math.atan(this.tangent / this.radius);
            this.arrow = this.radius - (this.radius * Math.cos(this.alphaAngle));
            this.chordOF = 2 * this.radius * Math.sin(this.alphaAngle);
            this.betaAngle = MathUtils.radToGrad(Math.PI - (2 * this.alphaAngle));
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.radius)
                && !MathUtils.isZero(this.arrow)) {
            // radius / arrow

            this.chordOF = 2 * Math.sqrt(
                    this.arrow * ((2 * this.radius) - this.arrow));
            this.alphaAngle = Math.asin(this.chordOF / (2 * this.radius));
            this.betaAngle = MathUtils.radToGrad(Math.PI - (2 * this.alphaAngle));
            this.tangent = this.radius * Math.tan(this.alphaAngle);
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.chordOF)
                && !MathUtils.isZero(this.alphaAngle)) {
            // chord OF / alpha (central angle)

            this.alphaAngle /= 2;

            this.alphaAngle = MathUtils.gradToRad(this.alphaAngle);
            this.radius = (0.5 * this.chordOF) / Math.sin(this.alphaAngle);
            this.betaAngle = MathUtils.radToGrad(Math.PI - (2 * this.alphaAngle));
            this.arrow = this.radius - (this.radius * Math.cos(this.alphaAngle));
            this.tangent = this.radius * Math.tan(this.alphaAngle);
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.chordOF)
                && !MathUtils.isZero(this.tangent)) {
            // chord OF / tangent

            this.betaAngle = Math.acos(1 - (Math.pow(this.chordOF, 2) / (2 * Math.pow(
                    this.tangent, 2))));
            this.alphaAngle = (Math.PI / 2) - (this.betaAngle / 2);
            this.radius = (0.5 * this.chordOF) / Math.sin(this.alphaAngle);
            this.arrow = this.radius - (this.radius * Math.cos(this.alphaAngle));
            this.betaAngle = MathUtils.radToGrad(this.betaAngle);
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.chordOF)
                && !MathUtils.isZero(this.arrow)) {
            // chord OF / arrow

            this.alphaAngle = 2 * Math.atan(this.arrow / (0.5 * this.chordOF));
            this.betaAngle = MathUtils.radToGrad(Math.PI - (2 * this.alphaAngle));
            this.radius = (0.5 * this.chordOF) / Math.sin(this.alphaAngle);
            this.tangent = this.radius * Math.tan(this.alphaAngle);
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else if (!MathUtils.isZero(this.tangent)
                && !MathUtils.isZero(this.alphaAngle)) {
            // tangent / alpha (central angle)

            this.alphaAngle /= 2;

            this.alphaAngle = MathUtils.gradToRad(this.alphaAngle);
            this.betaAngle = MathUtils.radToGrad(Math.PI - (2 * this.alphaAngle));
            this.radius = this.tangent / Math.tan(this.alphaAngle);
            this.chordOF = 2 * this.radius * Math.sin(this.alphaAngle);
            this.arrow = this.radius - (this.radius * Math.cos(this.alphaAngle));
            this.alphaAngle = MathUtils.radToGrad(this.alphaAngle);

        } else {
            // calculation is impossible
            return;
        }

        this.bisector = (this.radius / Math.cos(
                MathUtils.gradToRad(this.alphaAngle))) - this.radius;
        this.arc = this.radius * 2 * MathUtils.gradToRad(this.alphaAngle);
        this.circleSurface = Math.PI * this.radius * this.radius;
        this.circumference = 2 * Math.PI * this.radius;
        this.chordOM = 2 * this.radius * Math.sin(MathUtils.gradToRad(this.alphaAngle) / 2);
        this.sectorSurface = (this.radius * this.radius * MathUtils.gradToRad(this.alphaAngle));
        this.segmentSurface = this.sectorSurface - ((this.radius * this.radius * Math.sin(
                MathUtils.gradToRad(2 * this.alphaAngle))) / 2);

        this.alphaAngle *= 2;

        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(CircularCurvesSolver.RADIUS, this.radius);
        jo.put(CircularCurvesSolver.ALPHA_ANGLE, this.alphaAngle);
        jo.put(CircularCurvesSolver.CHORD_OF, this.chordOF);
        jo.put(CircularCurvesSolver.TANGENT, this.tangent);
        jo.put(CircularCurvesSolver.ARROW, this.arrow);

        return jo.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject jo = new JSONObject(jsonInputArgs);
        this.radius = jo.getDouble(CircularCurvesSolver.RADIUS);
        this.alphaAngle = jo.getDouble(CircularCurvesSolver.ALPHA_ANGLE);
        this.chordOF = jo.getDouble(CircularCurvesSolver.CHORD_OF);
        this.tangent = jo.getDouble(CircularCurvesSolver.TANGENT);
        this.arrow = jo.getDouble(CircularCurvesSolver.ARROW);
    }

    @Override
    public Class<?> getActivityClass() {
        return CircularCurvesSolverActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_circular_curve_solver);
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double _radius) {
        this.radius = _radius;
    }

    public double getAlphaAngle() {
        return this.alphaAngle;
    }

    public void setAlphaAngle(double _alphaAngle) {
        this.alphaAngle = _alphaAngle;
    }

    public double getChordOF() {
        return this.chordOF;
    }

    public void setChordOF(double _chordOF) {
        this.chordOF = _chordOF;
    }

    public double getTangent() {
        return this.tangent;
    }

    public void setTangent(double _tangent) {
        this.tangent = _tangent;
    }

    public double getArrow() {
        return this.arrow;
    }

    public void setArrow(double _arrow) {
        this.arrow = _arrow;
    }

    public double getBisector() {
        return this.bisector;
    }

    public double getArc() {
        return this.arc;
    }

    public double getCircumference() {
        return this.circumference;
    }

    public double getChordOM() {
        return this.chordOM;
    }

    public double getBetaAngle() {
        return this.betaAngle;
    }

    public double getCircleSurface() {
        return this.circleSurface;
    }

    public double getSectorSurface() {
        return this.sectorSurface;
    }

    public double getSegmentSurface() {
        return this.segmentSurface;
    }
}