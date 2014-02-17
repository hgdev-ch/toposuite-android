package ch.hgdev.toposuite.calculation;

import java.util.Date;

import org.json.JSONException;

import ch.hgdev.toposuite.utils.MathUtils;

import com.google.common.base.Preconditions;

public class TriangleSolver extends Calculation {

    private double a;
    private double b;
    private double c;
    private double alpha;
    private double beta;
    private double gamma;

    private double perimeter;
    private double height;
    private double surface;
    private double incircleRadius;
    private double excircleRadius;

    public TriangleSolver(long id, Date lastModification) {
        super(id, null, "Triangle solver", lastModification, true);
    }

    public TriangleSolver(
            double _a, double _b, double _c,
            double _alpha, double _beta, double _gamma,
            boolean hasDAO) throws IllegalArgumentException {
        super(CalculationType.TRIANGLESOLVER, "Triangle solver", hasDAO);

        Preconditions.checkArgument(_a >= 0.0, "Argument was %s but expected nonnegative", _a);
        Preconditions.checkArgument(_b >= 0.0, "Argument was %s but expected nonnegative", _b);
        Preconditions.checkArgument(_c >= 0.0, "Argument was %s but expected nonnegative", _c);
        Preconditions.checkArgument(
                _alpha >= 0.0, "Argument was %s but expected nonnegative", _alpha);
        Preconditions
                .checkArgument(_beta >= 0.0, "Argument was %s but expected nonnegative", _beta);
        Preconditions.checkArgument(
                _gamma >= 0.0, "Argument was %s but expected nonnegative", _gamma);

        this.a = _a;
        this.b = _b;
        this.c = _c;
        this.alpha = MathUtils.modulo400(_alpha);
        this.beta = MathUtils.modulo400(_beta);
        this.gamma = MathUtils.modulo400(_gamma);

        if (!this.checkInputs()) {
            throw new IllegalArgumentException(
                    "TriangleSolver: At least 3 of the arguments should be greater than 0 "
                            + "and the sum of the 3 angles must be less than or equal to 200");
        }

        this.findMissingValues();

        this.perimeter = 0.0;
        this.height = 0.0;
        this.surface = 0.0;
        this.incircleRadius = 0.0;
        this.excircleRadius = 0.0;
    }

    /**
     * Check that at least three arguments are greater than zero and that the
     * sum of all angles is no greater than 200 (remember we use gradian).
     * 
     * @return True if OK, false otherwise.
     */
    private boolean checkInputs() {
        // sum of the angles > 200
        if ((this.alpha + this.beta + this.gamma) > 200.0) {
            return false;
        }
        // three angles given and sum of the angles < 200
        if (MathUtils.isPositive(this.alpha) && MathUtils.isPositive(this.beta)
                && MathUtils.isPositive(this.gamma)
                && ((this.alpha + this.beta + this.gamma) < 200.0)) {
            return false;
        }

        int count = 0;

        if (this.a == 0.0) {
            count++;
        }
        if (this.b == 0.0) {
            count++;
        }
        if (this.c == 0.0) {
            count++;
        }
        if (this.alpha == 0.0) {
            count++;
        }
        if (this.beta == 0.0) {
            count++;
        }
        if (this.gamma == 0.0) {
            count++;
        }

        return count < 4;
    }

    /**
     * Attempt to find missing values; ie: find values of the angles if a, b and
     * c are greater than 0, etc.
     */
    private void findMissingValues() {
        // three sides given
        if (this.areAllPositive(this.a, this.b, this.c)) {
            this.alpha = this.determineAngleHavingThreeSides(this.a, this.b, this.c);
            this.beta = this.determineAngleHavingThreeSides(this.b, this.a, this.c);
            this.gamma = this.determineAngleHavingThreeSides(this.c, this.a, this.b);
            return;
        }
        // a, b and gamma given
        if (this.areAllPositive(this.a, this.b, this.gamma)) {
            this.c = this.determineSideHavingTwoSidesAndOneAngle(this.a, this.b, this.gamma);
            this.alpha = this.determineAngleHavingThreeSides(this.a, this.b, this.c);
            this.beta = this.determineAngleHavingThreeSides(this.b, this.a, this.c);
            return;
        }
        // b, c and alpha given
        if (this.areAllPositive(this.b, this.c, this.alpha)) {
            this.a = this.determineSideHavingTwoSidesAndOneAngle(this.b, this.c, this.alpha);
            this.beta = this.determineAngleHavingThreeSides(this.b, this.a, this.c);
            this.gamma = this.determineAngleHavingThreeSides(this.c, this.a, this.b);
            return;
        }
        // a, c and beta given
        if (this.areAllPositive(this.a, this.c, this.beta)) {
            this.b = this.determineSideHavingTwoSidesAndOneAngle(this.a, this.c, this.beta);
            this.alpha = this.determineAngleHavingThreeSides(this.a, this.b, this.c);
            this.gamma = this.determineAngleHavingThreeSides(this.c, this.a, this.b);
            return;
        }
        // a, b and alpha given (2 solutions case)
        if (this.areAllPositive(this.a, this.b, this.alpha)) {
            // TODO
        }
        // a, b and beta given (2 solutions case)
        if (this.areAllPositive(this.a, this.b, this.beta)) {
            // TODO
        }
        // b, c and beta given (2 solutions case)
        if (this.areAllPositive(this.b, this.c, this.beta)) {
            // TODO
        }
        // b, c and gamma given (2 solutions case)
        if (this.areAllPositive(this.b, this.c, this.gamma)) {
            // TODO
        }
        // a, c and alpha given (2 solutions case)
        if (this.areAllPositive(this.a, this.c, this.alpha)) {
            // TODO
        }
        // a, c and gamma given (2 solutions case)
        if (this.areAllPositive(this.a, this.c, this.gamma)) {
            // TODO
        }
        // a, beta and gamma given
        if (this.areAllPositive(this.a, this.beta, this.gamma)) {
            this.alpha = this.determineAngleHavingTheTwoOthers(this.beta, this.gamma);
            this.b = this.determineSideHavingOneSideAndTwoAngles(this.a, this.beta, this.alpha);
            this.c = this.determineSideHavingOneSideAndTwoAngles(this.a, this.gamma, this.alpha);
        }
        // b, alpha and gamma given
        if (this.areAllPositive(this.b, this.alpha, this.gamma)) {
            this.beta = this.determineAngleHavingTheTwoOthers(this.alpha, this.gamma);
            this.a = this.determineSideHavingOneSideAndTwoAngles(this.b, this.alpha, this.beta);
            this.c = this.determineSideHavingOneSideAndTwoAngles(this.b, this.gamma, this.beta);
        }
        // c, alpha and beta given
        if (this.areAllPositive(this.c, this.alpha, this.beta)) {
            this.gamma = this.determineAngleHavingTheTwoOthers(this.alpha, this.beta);
            this.a = this.determineSideHavingOneSideAndTwoAngles(this.c, this.alpha, this.gamma);
            this.c = this.determineSideHavingOneSideAndTwoAngles(this.c, this.beta, this.gamma);
        }
        // a, alpha and beta given
        if (this.areAllPositive(this.a, this.alpha, this.beta)) {
            this.gamma = this.determineAngleHavingTheTwoOthers(this.alpha, this.beta);
            this.b = this.determineSideHavingOneSideAndTwoAngles(this.a, this.beta, this.alpha);
            this.c = this.determineSideHavingOneSideAndTwoAngles(this.a, this.gamma, this.alpha);
        }
        // a, alpha and gamma given
        if (this.areAllPositive(this.a, this.alpha, this.gamma)) {
            this.beta = this.determineAngleHavingTheTwoOthers(this.alpha, this.gamma);
            this.b = this.determineSideHavingOneSideAndTwoAngles(this.a, this.beta, this.alpha);
            this.c = this.determineSideHavingOneSideAndTwoAngles(this.a, this.gamma, this.alpha);
        }
        // b, alpha and beta given
        if (this.areAllPositive(this.b, this.alpha, this.beta)) {
            // TODO
        }
        // b, gamma and beta given
        if (this.areAllPositive(this.b, this.gamma, this.beta)) {
            // TODO
        }
        // c, gamma and alpha given
        if (this.areAllPositive(this.c, this.gamma, this.alpha)) {
            // TODO
        }
        // c, gamma and beta given
        if (this.areAllPositive(this.c, this.gamma, this.beta)) {
            // TODO
        }

    }

    /**
     * Compute perimeter, height, surface, incircle radius and excircle radius.
     */
    public void compute() {
        this.computePerimeter();
        this.computeHeight();
        this.computeIncircleRadius();
        this.computeExcircleRadius();
        this.computeSurface();
    }

    /**
     * Compute the triangle perimeter.
     */
    private void computePerimeter() {
        this.perimeter = this.a + this.b + this.c;
    }

    /**
     * Compute the triangle height.
     */
    private void computeHeight() {
        this.height = Math.sin(MathUtils.gradToRad(this.beta)) * this.c;
    }

    /**
     * Compute the triangle surface. Warning: requires excircleRadius to be
     * computed before.
     */
    private void computeSurface() {
        this.surface = (this.a * this.b * this.c) / (4 * this.excircleRadius);
    }

    /**
     * Compute the triangle's incircle radius. Warning: requires the perimeter
     * to be computed before.
     */
    private void computeIncircleRadius() {
        this.incircleRadius = Math.sqrt((((this.perimeter / 2) - this.a)
                * ((this.perimeter / 2) - this.b) * ((this.perimeter / 2) - this.c))
                / (this.perimeter / 2));
    }

    /**
     * Compute the triangle's excircle radius.
     */
    private void computeExcircleRadius() {
        this.excircleRadius = (this.a / Math.sin(MathUtils.gradToRad(this.alpha))) / 2;
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
        // TODO implement
        return null;
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        // TODO implement

    }

    @Override
    public Class<?> getActivityClass() {
        // TODO implement
        return null;
    }

    public double getPerimeter() {
        return this.perimeter;
    }

    public double getHeight() {
        return this.height;
    }

    public double getSurface() {
        return this.surface;
    }

    public double getIncircleRadius() {
        return this.incircleRadius;
    }

    public double getExcircleRadius() {
        return this.excircleRadius;
    }
}
