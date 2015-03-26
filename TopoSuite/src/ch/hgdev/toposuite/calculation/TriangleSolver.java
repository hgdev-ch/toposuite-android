package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.trianglesolver.TriangleSolverActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.Pair;

import com.google.common.base.Preconditions;

public class TriangleSolver extends Calculation {

    private static final String  TRIANGLE_SOLVER = "Triangle solver: ";

    private static final String  A               = "a";
    private static final String  B               = "b";
    private static final String  C               = "c";
    private static final String  ALPHA           = "alpha";
    private static final String  BETA            = "beta";
    private static final String  GAMMA           = "gamma";

    private Pair<Double, Double> a;
    private Pair<Double, Double> b;
    private Pair<Double, Double> c;
    private Pair<Double, Double> alpha;
    private Pair<Double, Double> beta;
    private Pair<Double, Double> gamma;

    private Pair<Double, Double> perimeter;
    private Pair<Double, Double> height;
    private Pair<Double, Double> surface;
    private Pair<Double, Double> incircleRadius;
    private Pair<Double, Double> excircleRadius;

    private boolean              twoSolutions;

    public TriangleSolver(long id, Date lastModification) {
        super(id,
                CalculationType.TRIANGLESOLVER,
                App.getContext().getString(R.string.title_activity_triangle_solver),
                lastModification, true);
        this.initAttributes(MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE);
    }

    public TriangleSolver(
            double _a, double _b, double _c,
            double _alpha, double _beta, double _gamma,
            boolean hasDAO) throws IllegalArgumentException {
        super(CalculationType.TRIANGLESOLVER,
                App.getContext().getString(R.string.title_activity_triangle_solver),
                hasDAO);

        this.initAttributes(_a, _b, _c, _alpha, _beta, _gamma);

        if (!this.checkInputs()) {
            throw new IllegalArgumentException(
                    "TriangleSolver: At least 3 of the arguments should be greater than 0 "
                            + "and the sum of the 3 angles must be less than or equal to 200");
        }

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Initialize class attributes.
     *
     * @param _a
     * @param _b
     * @param _c
     * @param _alpha
     * @param _beta
     * @param _gamma
     */
    private void initAttributes(
            double _a, double _b, double _c, double _alpha, double _beta, double _gamma)
            throws IllegalArgumentException {
        this.setA(_a);
        this.setB(_b);
        this.setC(_c);
        this.setAlpha(_alpha);
        this.setBeta(_beta);
        this.setGamma(_gamma);

        this.twoSolutions = false;

        this.perimeter = new Pair<Double, Double>(
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        this.height = new Pair<Double, Double>(
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        this.surface = new Pair<Double, Double>(
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        this.incircleRadius = new Pair<Double, Double>(
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
        this.excircleRadius = new Pair<Double, Double>(
                MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE);
    }

    /**
     * Check that at least three arguments are greater than zero and that the
     * sum of all angles is no greater than 200 (remember we use gradian).
     *
     * @return True if OK, false otherwise.
     */
    private boolean checkInputs() {
        // three angles given and sum of the angles < 200
        if (MathUtils.isPositive(this.alpha.first)
                && MathUtils.isPositive(this.beta.first)
                && MathUtils.isPositive(this.gamma.first)
                && (Math.abs(200 - (this.alpha.first + this.beta.first + this.gamma.first))
                > App.getAngleTolerance())) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER
                            + "three angles are given and their sum does not equal 200 [g] "
                            + "(taking a tolerance into account).");
            return false;
        }
        // at least one side is required
        if (!this.isOnePositive(this.a.first, this.b.first, this.c.first)) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER + "at least one side is required.");
            return false;
        }

        int count = 0;

        if (MathUtils.isPositive(this.a.first)) {
            count++;
        }
        if (MathUtils.isPositive(this.b.first)) {
            count++;
        }
        if (MathUtils.isPositive(this.c.first)) {
            count++;
        }
        if (MathUtils.isPositive(this.alpha.first)) {
            count++;
        }
        if (MathUtils.isPositive(this.beta.first)) {
            count++;
        }
        if (MathUtils.isPositive(this.gamma.first)) {
            count++;
        }

        if (count >= 3) {
            return true;
        } else {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    String.format(
                            "%s%s (a = %s; b = %s; c = %s; alpha = %s; beta = %s; gamma = %s)",
                            TriangleSolver.TRIANGLE_SOLVER,
                            "less than 3 inputs were provided.",
                            DisplayUtils.formatDistance(this.a.first),
                            DisplayUtils.formatDistance(this.b.first),
                            DisplayUtils.formatDistance(this.c.first),
                            DisplayUtils.formatAngle(this.alpha.first),
                            DisplayUtils.formatAngle(this.beta.first),
                            DisplayUtils.formatAngle(this.gamma.first)));
            return false;
        }
    }

    /**
     * Attempt to find missing values; ie: find values of the angles if a, b and
     * c are greater than 0, etc.
     */
    private void findMissingValues() {
        // three sides given
        if (this.areAllPositive(this.a.first, this.b.first, this.c.first)) {
            this.alpha.first = this.determineAngleHavingThreeSides(
                    this.a.first, this.b.first, this.c.first);
            this.beta.first = this.determineAngleHavingThreeSides(
                    this.b.first, this.a.first, this.c.first);
            this.gamma.first = this.determineAngleHavingThreeSides(
                    this.c.first, this.a.first, this.b.first);
            return;
        }
        // a, b and gamma given
        if (this.areAllPositive(this.a.first, this.b.first, this.gamma.first)) {
            this.c.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.first, this.b.first, this.gamma.first);
            this.alpha.first = this.determineAngleHavingThreeSides(
                    this.a.first, this.b.first, this.c.first);
            this.beta.first = this.determineAngleHavingThreeSides(
                    this.b.first, this.a.first, this.c.first);
            return;
        }
        // b, c and alpha given
        if (this.areAllPositive(this.b.first, this.c.first, this.alpha.first)) {
            this.a.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.b.first, this.c.first, this.alpha.first);
            this.beta.first = this.determineAngleHavingThreeSides(
                    this.b.first, this.a.first, this.c.first);
            this.gamma.first = this.determineAngleHavingThreeSides(
                    this.c.first, this.a.first, this.b.first);
            return;
        }
        // a, c and beta given
        if (this.areAllPositive(this.a.first, this.c.first, this.beta.first)) {
            this.b.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.first, this.c.first, this.beta.first);
            this.alpha.first = this.determineAngleHavingThreeSides(
                    this.a.first, this.b.first, this.c.first);
            this.gamma.first = this.determineAngleHavingThreeSides(
                    this.c.first, this.a.first, this.b.first);
            return;
        }
        // a, b and alpha given (2 solutions case)
        if (this.areAllPositive(this.a.first, this.b.first, this.alpha.first)) {
            this.twoSolutions = true;
            this.a.second = this.a.first;
            this.b.second = this.b.first;
            this.alpha.second = this.alpha.first;

            this.beta.first = this.determineAngleHavingTwoSidesAndOneAngle(
                    this.a.first, this.b.first, this.alpha.first);
            this.beta.second = 200.0 - this.beta.first;
            this.gamma.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.beta.first);
            this.gamma.second = this.determineAngleHavingTheTwoOthers(
                    this.alpha.second, this.beta.second);
            this.c.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.first, this.b.first, this.gamma.first);
            this.c.second = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.second, this.b.second, this.gamma.second);

            this.checkSecondSolution();
            return;
        }
        // a, b and beta given (2 solutions case)
        if (this.areAllPositive(this.a.first, this.b.first, this.beta.first)) {
            this.twoSolutions = true;
            this.a.second = this.a.first;
            this.b.second = this.b.first;
            this.beta.second = this.beta.first;

            this.alpha.second = this.determineAngleHavingTwoSidesAndOneAngle(
                    this.b.first, this.a.first, this.beta.first);
            this.alpha.first = 200.0 - this.alpha.second;
            this.gamma.first = this.determineAngleHavingTheTwoOthers(
                    this.beta.first, this.alpha.first);
            this.gamma.second = this.determineAngleHavingTheTwoOthers(
                    this.beta.second, this.alpha.second);
            this.c.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.first, this.b.first, this.gamma.first);
            this.c.second = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.second, this.b.second, this.gamma.second);

            this.checkSecondSolution();
            return;
        }
        // b, c and beta given (2 solutions case)
        if (this.areAllPositive(this.b.first, this.c.first, this.beta.first)) {
            this.twoSolutions = true;
            this.b.second = this.b.first;
            this.c.second = this.c.first;
            this.beta.second = this.beta.first;

            this.gamma.first = this.determineAngleHavingTwoSidesAndOneAngle(
                    this.b.first, this.c.first, this.beta.first);
            this.gamma.second = 200.0 - this.gamma.first;
            this.alpha.first = this.determineAngleHavingTheTwoOthers(
                    this.beta.first, this.gamma.first);
            this.alpha.second = this.determineAngleHavingTheTwoOthers(
                    this.beta.second, this.gamma.second);
            this.a.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.b.first, this.c.first, this.alpha.first);
            this.a.second = this.determineSideHavingTwoSidesAndOneAngle(
                    this.b.second, this.c.second, this.alpha.second);

            this.checkSecondSolution();
            return;
        }
        // b, c and gamma given (2 solutions case)
        if (this.areAllPositive(this.b.first, this.c.first, this.gamma.first)) {
            this.twoSolutions = true;
            this.b.second = this.b.first;
            this.c.second = this.c.first;
            this.gamma.second = this.gamma.first;

            this.beta.first = this.determineAngleHavingTwoSidesAndOneAngle(
                    this.c.first, this.b.first, this.gamma.first);
            this.beta.second = 200.0 - this.beta.first;
            this.alpha.first = this.determineAngleHavingTheTwoOthers(
                    this.beta.first, this.gamma.first);
            this.alpha.second = this.determineAngleHavingTheTwoOthers(
                    this.beta.second, this.gamma.second);
            this.a.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.b.first, this.c.first, this.alpha.first);
            this.a.second = this.determineSideHavingTwoSidesAndOneAngle(
                    this.b.second, this.c.second, this.alpha.second);

            this.checkSecondSolution();
            return;
        }
        // a, c and alpha given (2 solutions case)
        if (this.areAllPositive(this.a.first, this.c.first, this.alpha.first)) {
            this.twoSolutions = true;
            this.a.second = this.a.first;
            this.c.second = this.c.first;
            this.alpha.second = this.alpha.first;

            this.gamma.first = this.determineAngleHavingTwoSidesAndOneAngle(
                    this.a.first, this.c.first, this.alpha.first);
            this.gamma.second = 200.0 - this.gamma.first;
            this.beta.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.gamma.first);
            this.beta.second = this.determineAngleHavingTheTwoOthers(
                    this.alpha.second, this.gamma.second);
            this.b.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.first, this.c.first, this.beta.first);
            this.b.second = this.determineSideHavingTwoSidesAndOneAngle(
                    this.a.second, this.c.second, this.beta.second);

            this.checkSecondSolution();
            return;
        }
        // a, c and gamma given (2 solutions case)
        if (this.areAllPositive(this.a.first, this.c.first, this.gamma.first)) {
            this.twoSolutions = true;
            this.a.second = this.a.first;
            this.c.second = this.c.first;
            this.gamma.second = this.gamma.first;

            this.alpha.second = this.determineAngleHavingTwoSidesAndOneAngle(
                    this.c.first, this.a.first, this.gamma.first);
            this.alpha.first = 200.0 - this.alpha.second;
            this.beta.first = this.determineAngleHavingTheTwoOthers(
                    this.gamma.first, this.alpha.first);
            this.beta.second = this.determineAngleHavingTheTwoOthers(
                    this.gamma.second, this.alpha.second);
            this.b.first = this.determineSideHavingTwoSidesAndOneAngle(
                    this.c.first, this.a.first, this.beta.first);
            this.b.second = this.determineSideHavingTwoSidesAndOneAngle(
                    this.c.second, this.a.second, this.beta.second);

            this.checkSecondSolution();
            return;
        }
        // a, beta and gamma given
        if (this.areAllPositive(this.a.first, this.beta.first, this.gamma.first)) {
            this.alpha.first = this.determineAngleHavingTheTwoOthers(
                    this.beta.first, this.gamma.first);
            this.b.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.a.first, this.beta.first, this.alpha.first);
            this.c.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.a.first, this.gamma.first, this.alpha.first);
            return;
        }
        // b, alpha and gamma given
        if (this.areAllPositive(this.b.first, this.alpha.first, this.gamma.first)) {
            this.beta.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.gamma.first);
            this.a.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.b.first, this.alpha.first, this.beta.first);
            this.c.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.b.first, this.gamma.first, this.beta.first);
            return;
        }
        // c, alpha and beta given
        if (this.areAllPositive(this.c.first, this.alpha.first, this.beta.first)) {
            this.gamma.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.beta.first);
            this.a.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.c.first, this.alpha.first, this.gamma.first);
            this.b.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.c.first, this.beta.first, this.gamma.first);
            return;
        }
        // a, alpha and beta given
        if (this.areAllPositive(this.a.first, this.alpha.first, this.beta.first)) {
            this.gamma.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.beta.first);
            this.b.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.a.first, this.beta.first, this.alpha.first);
            this.c.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.a.first, this.gamma.first, this.alpha.first);
            return;
        }
        // a, alpha and gamma given
        if (this.areAllPositive(this.a.first, this.alpha.first, this.gamma.first)) {
            this.beta.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.gamma.first);
            this.b.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.a.first, this.beta.first, this.alpha.first);
            this.c.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.a.first, this.gamma.first, this.alpha.first);
            return;
        }
        // b, alpha and beta given
        if (this.areAllPositive(this.b.first, this.alpha.first, this.beta.first)) {
            this.gamma.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.beta.first);
            this.a.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.b.first, this.alpha.first, this.beta.first);
            this.c.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.b.first, this.gamma.first, this.beta.first);
            return;
        }
        // b, gamma and beta given
        if (this.areAllPositive(this.b.first, this.gamma.first, this.beta.first)) {
            this.alpha.first = this.determineAngleHavingTheTwoOthers(
                    this.beta.first, this.gamma.first);
            this.a.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.b.first, this.alpha.first, this.beta.first);
            this.c.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.b.first, this.gamma.first, this.beta.first);
            return;
        }
        // c, gamma and alpha given
        if (this.areAllPositive(this.c.first, this.gamma.first, this.alpha.first)) {
            this.beta.first = this.determineAngleHavingTheTwoOthers(
                    this.alpha.first, this.gamma.first);
            this.a.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.c.first, this.alpha.first, this.gamma.first);
            this.b.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.c.first, this.beta.first, this.gamma.first);
            return;
        }
        // c, gamma and beta given
        if (this.areAllPositive(this.c.first, this.gamma.first, this.beta.first)) {
            this.alpha.first = this.determineAngleHavingTheTwoOthers(
                    this.beta.first, this.gamma.first);
            this.a.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.c.first, this.alpha.first, this.gamma.first);
            this.b.first = this.determineSideHavingOneSideAndTwoAngles(
                    this.c.first, this.beta.first, this.gamma.first);
            return;
        }
    }

    /**
     * Having every angles and sides, make sure there is no inconsistency.
     *
     * @return True if no inconsistency has been found, false otherwise.
     */
    private boolean checkFoundValues() {
        if (Math.abs(200 - (this.alpha.first + this.beta.first + this.gamma.first)) > App
                .getAngleTolerance()) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER
                            + "the sum of the found angles does not meet the tolerance.");
            return false;
        }
        if (!MathUtils.isPositive(this.a.first)) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER + "the value of 'a' is not positive.");
            return false;
        }
        if (!MathUtils.isPositive(this.b.first)) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER + "the value of 'b' is not positive.");
            return false;
        }
        if (!MathUtils.isPositive(this.c.first)) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER + "the value of 'c' is not positive.");
            return false;
        }
        return true;
    }

    /**
     * There are cases were the second solution is not valid. This method checks
     * for such cases and set the resulting angles and sides to
     * MathUtils.IGNORE_DOUBLE if the second solution is not valid.
     */
    private void checkSecondSolution() {
        if (this.b.first > (this.c.first * Math.sin(MathUtils.gradToRad(this.beta.first)))) {
            if (this.areAllPositive(this.a.second, this.b.second, this.c.second) &&
                    this.areAllPositive(this.alpha.second, this.beta.second, this.gamma.second)) {
                return;
            }
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    TriangleSolver.TRIANGLE_SOLVER
                            + "at least one of the second solution angle or side is not positive.");
        }
        Logger.log(
                Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                TriangleSolver.TRIANGLE_SOLVER
                        + "the condition for the second solution (b > c * sin(beta) is not respected.");
        this.a.second = MathUtils.IGNORE_DOUBLE;
        this.b.second = MathUtils.IGNORE_DOUBLE;
        this.c.second = MathUtils.IGNORE_DOUBLE;
        this.alpha.second = MathUtils.IGNORE_DOUBLE;
        this.beta.second = MathUtils.IGNORE_DOUBLE;
        this.gamma.second = MathUtils.IGNORE_DOUBLE;
    }

    /**
     * Compute perimeter, height, surface, incircle radius and excircle radius.
     */
    @Override
    public void compute() throws IllegalArgumentException {
        if (!this.checkInputs()) {
            throw new IllegalArgumentException(
                    "TriangleSolver: At least 3 of the arguments should be greater than 0 "
                            + "and the sum of the 3 angles must be less than or equal to 200");
        }

        this.initAttributes(this.a.first, this.b.first, this.c.first,
                this.alpha.first, this.beta.first, this.gamma.first);
        this.findMissingValues();
        if (!this.checkFoundValues()) {
            this.initAttributes(
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE,
                    MathUtils.IGNORE_DOUBLE);
            return;
        }

        this.perimeter.first = this.computePerimeter(this.a.first, this.b.first, this.c.first);
        this.height.first = this.computeHeight(this.beta.first, this.c.first);
        this.incircleRadius.first = this.computeIncircleRadius(
                this.perimeter.first, this.a.first, this.b.first, this.c.first);
        this.excircleRadius.first = this.computeExcircleRadius(this.a.first, this.alpha.first);
        this.surface.first = this.computeSurface(
                this.a.first, this.b.first, this.c.first, this.excircleRadius.first);

        if (this.twoSolutions) {
            // if not all values are positive, then the calculation is
            // impossible
            if (!(this.areAllPositive(this.a.second, this.b.second, this.c.second) && this
                    .areAllPositive(this.alpha.second, this.beta.second, this.gamma.second))) {
                Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                        TriangleSolver.TRIANGLE_SOLVER
                                + "not all values of sides and angles are positives which makes "
                                + "the second solution calculation impossible.");
                return;
            }

            this.perimeter.second = this.computePerimeter(
                    this.a.second, this.b.second, this.c.second);
            this.height.second = this.computeHeight(this.beta.second, this.c.second);
            this.incircleRadius.second = this.computeIncircleRadius(
                    this.perimeter.second, this.a.second, this.b.second, this.c.second);
            this.excircleRadius.second = this.computeExcircleRadius(
                    this.a.second, this.alpha.second);
            this.surface.second = this.computeSurface(
                    this.a.second, this.b.second, this.c.second, this.excircleRadius.second);
        }

        this.twoSolutions = false;

        // update the calculation last modification date
        this.updateLastModification();
        this.notifyUpdate(this);
    }

    /**
     * Compute the triangle perimeter.
     */
    private double computePerimeter(double a, double b, double c) {
        return a + b + c;
    }

    /**
     * Compute the triangle height.
     *
     * @return
     */
    private double computeHeight(double beta, double c) {
        return Math.sin(MathUtils.gradToRad(beta)) * c;
    }

    /**
     * Compute the triangle surface. Warning: requires excircleRadius to be
     * computed before.
     */
    private double computeSurface(double a, double b, double c, double excircleRadius) {
        return (a * b * c) / (4 * excircleRadius);
    }

    /**
     * Compute the triangle's incircle radius. Warning: requires the perimeter
     * to be computed before.
     */
    private double computeIncircleRadius(double perimeter, double a, double b, double c) {
        return Math.sqrt((((perimeter / 2) - a) * ((perimeter / 2) - b) * ((perimeter / 2) - c))
                / (perimeter / 2));
    }

    /**
     * Compute the triangle's excircle radius.
     */
    private double computeExcircleRadius(double a, double alpha) {
        return (a / Math.sin(MathUtils.gradToRad(alpha))) / 2;
    }

    /**
     * Return true if all provided parameters are positive.
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    private boolean areAllPositive(double a, double b, double c) {
        return MathUtils.isPositive(a) && MathUtils.isPositive(b)
                && MathUtils.isPositive(c);
    }

    /**
     * Return true if at least one parameter is positive.
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    private boolean isOnePositive(double a, double b, double c) {
        return MathUtils.isPositive(a) || MathUtils.isPositive(b)
                || MathUtils.isPositive(c);
    }

    /**
     * Determine an angle when having three sides.
     *
     * @param a
     * @param b
     * @param c
     * @return The angle.
     */
    private double determineAngleHavingThreeSides(double a, double b, double c) {
        return MathUtils.radToGrad(Math.acos(
                ((Math.pow(b, 2) + Math.pow(c, 2)) - Math.pow(a, 2))
                        / (2 * b * c)));
    }

    private double determineAngleHavingTwoSidesAndOneAngle(double a, double b, double alpha) {
        return MathUtils.radToGrad(Math.asin((b * Math.sin(MathUtils.gradToRad(alpha))) / a));
    }

    /**
     * Determine alpha angle when having beta and gamma.
     */
    private double determineAngleHavingTheTwoOthers(double beta, double gamma) {
        return 200.0 - gamma - beta;
    }

    /**
     * Determine c when having a, b and gamma.
     *
     * @param a
     * @param b
     * @param gamma
     * @return
     */
    private double determineSideHavingTwoSidesAndOneAngle(double a, double b, double gamma) {
        return Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2))
                - (2 * a * b * Math.cos(MathUtils.gradToRad(gamma))));
    }

    private double determineSideHavingOneSideAndTwoAngles(double a, double beta, double alpha) {
        return (a * Math.sin(MathUtils.gradToRad(beta))) / Math.sin(MathUtils.gradToRad(alpha));
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(TriangleSolver.A, this.a.first);
        json.put(TriangleSolver.B, this.b.first);
        json.put(TriangleSolver.C, this.c.first);
        json.put(TriangleSolver.ALPHA, this.alpha.first);
        json.put(TriangleSolver.BETA, this.beta.first);
        json.put(TriangleSolver.GAMMA, this.gamma.first);

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        this.a.first = json.getDouble(TriangleSolver.A);
        this.b.first = json.getDouble(TriangleSolver.B);
        this.c.first = json.getDouble(TriangleSolver.C);
        this.alpha.first = json.getDouble(TriangleSolver.ALPHA);
        this.beta.first = json.getDouble(TriangleSolver.BETA);
        this.gamma.first = json.getDouble(TriangleSolver.GAMMA);
    }

    @Override
    public Class<?> getActivityClass() {
        return TriangleSolverActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_triangle_solver);
    }

    public Pair<Double, Double> getPerimeter() {
        return this.perimeter;
    }

    public Pair<Double, Double> getHeight() {
        return this.height;
    }

    public Pair<Double, Double> getSurface() {
        return this.surface;
    }

    public Pair<Double, Double> getIncircleRadius() {
        return this.incircleRadius;
    }

    public Pair<Double, Double> getExcircleRadius() {
        return this.excircleRadius;
    }

    public double getA() {
        return this.a.first;
    }

    public double getB() {
        return this.b.first;
    }

    public double getC() {
        return this.c.first;
    }

    public double getAlpha() {
        return this.alpha.first;
    }

    public double getBeta() {
        return this.beta.first;
    }

    public double getGamma() {
        return this.gamma.first;
    }

    public double getABis() {
        return this.a.second;
    }

    public double getBBis() {
        return this.b.second;
    }

    public double getCBis() {
        return this.c.second;
    }

    public double getAlphaBis() {
        return this.alpha.second;
    }

    public double getBetaBis() {
        return this.beta.second;
    }

    public double getGammaBis() {
        return this.gamma.second;
    }

    public void setA(double _a) throws IllegalArgumentException {
        Preconditions.checkArgument(_a >= 0.0, "Argument was %s but expected nonnegative", _a);
        if (this.a == null) {
            this.a = new Pair<Double, Double>(_a, MathUtils.IGNORE_DOUBLE);
        } else {
            this.a.first = _a;
            this.a.second = MathUtils.IGNORE_DOUBLE;
        }
    }

    public void setB(double _b) throws IllegalArgumentException {
        Preconditions.checkArgument(_b >= 0.0, "Argument was %s but expected nonnegative", _b);
        if (this.b == null) {
            this.b = new Pair<Double, Double>(_b, MathUtils.IGNORE_DOUBLE);
        } else {
            this.b.first = _b;
            this.b.second = MathUtils.IGNORE_DOUBLE;
        }
    }

    public void setC(double _c) throws IllegalArgumentException {
        Preconditions.checkArgument(_c >= 0.0, "Argument was %s but expected nonnegative", _c);
        if (this.c == null) {
            this.c = new Pair<Double, Double>(_c, MathUtils.IGNORE_DOUBLE);
        } else {
            this.c.first = _c;
            this.c.second = MathUtils.IGNORE_DOUBLE;
        }
    }

    public void setAlpha(double _alpha) throws IllegalArgumentException {
        Preconditions.checkArgument(
                _alpha >= 0.0, "Argument was %s but expected nonnegative", _alpha);
        if (this.alpha == null) {
            this.alpha = new Pair<Double, Double>(MathUtils.modulo400(_alpha),
                    MathUtils.IGNORE_DOUBLE);
        } else {
            this.alpha.first = _alpha;
            this.alpha.second = MathUtils.IGNORE_DOUBLE;
        }
    }

    public void setBeta(double _beta) throws IllegalArgumentException {
        Preconditions.checkArgument(
                _beta >= 0.0, "Argument was %s but expected nonnegative", _beta);
        if (this.beta == null) {
            this.beta = new Pair<Double, Double>(MathUtils.modulo400(_beta),
                    MathUtils.IGNORE_DOUBLE);
        } else {
            this.beta.first = _beta;
            this.beta.second = MathUtils.IGNORE_DOUBLE;
        }
    }

    public void setGamma(double _gamma) throws IllegalArgumentException {
        Preconditions.checkArgument(
                _gamma >= 0.0, "Argument was %s but expected nonnegative", _gamma);
        if (this.gamma == null) {
            this.gamma = new Pair<Double, Double>(MathUtils.modulo400(_gamma),
                    MathUtils.IGNORE_DOUBLE);
        } else {
            this.gamma.first = _gamma;
            this.gamma.second = MathUtils.IGNORE_DOUBLE;
        }
    }

}
