package ch.hgdev.toposuite.calculation.activities.trianglesolver;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.TriangleSolver;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class TriangleSolverActivity extends TopoSuiteActivity {
    private double         a;
    private double         b;
    private double         c;
    private double         alpha;
    private double         beta;
    private double         gamma;

    private EditText       aEditText;
    private EditText       bEditText;
    private EditText       cEditText;
    private EditText       alphaEditText;
    private EditText       betaEditText;
    private EditText       gammaEditText;

    private TextView       aBisTextView;
    private TextView       bBisTextView;
    private TextView       cBisTextView;
    private TextView       alphaBisTextView;
    private TextView       betaBisTextView;
    private TextView       gammaBisTextView;

    private TextView       perimeterTextView;
    private TextView       heightTextView;
    private TextView       surfaceTextView;
    private TextView       incircleRadiusTextView;
    private TextView       excircleRadiusTextView;

    private TextView       perimeterBisTextView;
    private TextView       heightBisTextView;
    private TextView       surfaceBisTextView;
    private TextView       incircleRadiusBisTextView;
    private TextView       excircleRadiusBisTextView;

    private TriangleSolver tS;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int            position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_triangle_solver);

        this.position = -1;

        this.initViews();
        this.initAttributes();

        // check if we create a new calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.tS = (TriangleSolver) SharedResources.getCalculationsHistory()
                    .get(this.position);
            if (this.tS != null) {
                this.updateAnglesAndSides();
                this.a = this.tS.getA();
                this.b = this.tS.getB();
                this.c = this.tS.getC();
                this.alpha = this.tS.getAlpha();
                this.beta = this.tS.getBeta();
                this.gamma = this.tS.getGamma();
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_triangle_solver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.triangle_solver_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.clear_button:
            this.clearInputs();
            return true;
        case R.id.run_calculation_button:
            if (!this.chickenRun() || !this.areAllSidesAndAnglesPositives()) {
                ViewUtils.showToast(this,
                        this.getString(R.string.error_impossible_calculation));
            } else {
                this.updateResults();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Match views to activity view attributes and set some additional
     * properties.
     */
    private void initViews() {
        this.aEditText = (EditText) this.findViewById(R.id.a);
        this.bEditText = (EditText) this.findViewById(R.id.b);
        this.cEditText = (EditText) this.findViewById(R.id.c);
        this.alphaEditText = (EditText) this.findViewById(R.id.alpha);
        this.betaEditText = (EditText) this.findViewById(R.id.beta);
        this.gammaEditText = (EditText) this.findViewById(R.id.gamma);

        this.aEditText.setInputType(App.getInputTypeCoordinate());
        this.bEditText.setInputType(App.getInputTypeCoordinate());
        this.cEditText.setInputType(App.getInputTypeCoordinate());
        this.alphaEditText.setInputType(App.getInputTypeCoordinate());
        this.betaEditText.setInputType(App.getInputTypeCoordinate());
        this.gammaEditText.setInputType(App.getInputTypeCoordinate());

        this.aEditText.setHint(
                this.getString(R.string.letter_a_unit));
        this.bEditText.setHint(
                this.getString(R.string.letter_b_unit));
        this.cEditText.setHint(
                this.getString(R.string.letter_c_unit));
        this.alphaEditText.setHint(
                this.getString(R.string.letter_alpha_unit));
        this.betaEditText.setHint(
                this.getString(R.string.letter_beta_unit));
        this.gammaEditText.setHint(
                this.getString(R.string.letter_gamma_unit));

        this.aBisTextView = (TextView) this.findViewById(R.id.a_bis);
        this.bBisTextView = (TextView) this.findViewById(R.id.b_bis);
        this.cBisTextView = (TextView) this.findViewById(R.id.c_bis);
        this.alphaBisTextView = (TextView) this.findViewById(R.id.alpha_bis);
        this.betaBisTextView = (TextView) this.findViewById(R.id.beta_bis);
        this.gammaBisTextView = (TextView) this.findViewById(R.id.gamma_bis);

        this.perimeterTextView = (TextView) this.findViewById(R.id.perimeter);
        this.heightTextView = (TextView) this.findViewById(R.id.height);
        this.surfaceTextView = (TextView) this.findViewById(R.id.surface);
        this.incircleRadiusTextView = (TextView) this.findViewById(R.id.incircle);
        this.excircleRadiusTextView = (TextView) this.findViewById(R.id.excircle);

        this.perimeterBisTextView = (TextView) this.findViewById(R.id.perimeter_bis);
        this.heightBisTextView = (TextView) this.findViewById(R.id.height_bis);
        this.surfaceBisTextView = (TextView) this.findViewById(R.id.surface_bis);
        this.incircleRadiusBisTextView = (TextView) this.findViewById(R.id.incircle_bis);
        this.excircleRadiusBisTextView = (TextView) this.findViewById(R.id.excircle_bis);
    }

    /**
     * Initialize class attributes
     */
    private void initAttributes() {
        this.a = MathUtils.IGNORE_DOUBLE;
        this.b = MathUtils.IGNORE_DOUBLE;
        this.c = MathUtils.IGNORE_DOUBLE;
        this.alpha = MathUtils.IGNORE_DOUBLE;
        this.beta = MathUtils.IGNORE_DOUBLE;
        this.gamma = MathUtils.IGNORE_DOUBLE;
    }

    /**
     * Update angles and sides edit texts if their value is greater than 0.
     */
    private void updateAnglesAndSides() {
        if (this.tS != null) {
            if (MathUtils.isPositive(this.tS.getA())) {
                this.aEditText.setText(DisplayUtils.toStringForEditText(this.tS.getA()));
            }
            if (MathUtils.isPositive(this.tS.getB())) {
                this.bEditText.setText(DisplayUtils.toStringForEditText(this.tS.getB()));
            }
            if (MathUtils.isPositive(this.tS.getC())) {
                this.cEditText.setText(DisplayUtils.toStringForEditText(this.tS.getC()));
            }
            if (MathUtils.isPositive(this.tS.getAlpha())) {
                this.alphaEditText.setText(DisplayUtils.toStringForEditText(this.tS.getAlpha()));
            }
            if (MathUtils.isPositive(this.tS.getBeta())) {
                this.betaEditText.setText(DisplayUtils.toStringForEditText(this.tS.getBeta()));
            }
            if (MathUtils.isPositive(this.tS.getGamma())) {
                this.gammaEditText.setText(DisplayUtils.toStringForEditText(this.tS.getGamma()));
            }
        }
    }

    /**
     * Clear input EditTexts and results views.
     */
    private void clearInputs() {
        this.aEditText.setText("");
        this.bEditText.setText("");
        this.cEditText.setText("");
        this.alphaEditText.setText("");
        this.betaEditText.setText("");
        this.gammaEditText.setText("");

        this.clearResults();
    }

    /**
     * Get input from edit texts.
     */
    private void getInputs() {
        this.a = ViewUtils.readDouble(this.aEditText);
        this.b = ViewUtils.readDouble(this.bEditText);
        this.c = ViewUtils.readDouble(this.cEditText);
        this.alpha = ViewUtils.readDouble(this.alphaEditText);
        this.beta = ViewUtils.readDouble(this.betaEditText);
        this.gamma = ViewUtils.readDouble(this.gammaEditText);
    }

    /**
     * Clear result views.
     */
    private void clearResults() {
        this.aBisTextView.setText("");
        this.bBisTextView.setText("");
        this.cBisTextView.setText("");
        this.alphaBisTextView.setText("");
        this.betaBisTextView.setText("");
        this.gammaBisTextView.setText("");

        this.perimeterTextView.setText("");
        this.perimeterBisTextView.setText("");
        this.heightTextView.setText("");
        this.heightBisTextView.setText("");
        this.surfaceTextView.setText("");
        this.surfaceBisTextView.setText("");
        this.incircleRadiusTextView.setText("");
        this.incircleRadiusBisTextView.setText("");
        this.excircleRadiusTextView.setText("");
        this.excircleRadiusBisTextView.setText("");
    }

    /**
     * Update result views with results from the calculations.
     *
     * @param t
     */
    private void updateResults() {
        this.aBisTextView.setText(DisplayUtils.formatDistance(this.tS.getABis()));
        this.bBisTextView.setText(DisplayUtils.formatDistance(this.tS.getBBis()));
        this.cBisTextView.setText(DisplayUtils.formatDistance(this.tS.getCBis()));
        this.alphaBisTextView.setText(DisplayUtils.formatDistance(this.tS.getAlphaBis()));
        this.betaBisTextView.setText(DisplayUtils.formatDistance(this.tS.getBetaBis()));
        this.gammaBisTextView.setText(DisplayUtils.formatDistance(this.tS.getGammaBis()));

        this.perimeterTextView.setText(
                DisplayUtils.formatDistance(this.tS.getPerimeter().first));
        this.perimeterBisTextView.setText(
                DisplayUtils.formatDistance(this.tS.getPerimeter().second));
        this.heightTextView.setText(
                DisplayUtils.formatDistance(this.tS.getHeight().first));
        this.heightBisTextView.setText(
                DisplayUtils.formatDistance(this.tS.getHeight().second));
        this.surfaceTextView.setText(
                DisplayUtils.formatSurface(this.tS.getSurface().first));
        this.surfaceBisTextView.setText(
                DisplayUtils.formatSurface(this.tS.getSurface().second));
        this.incircleRadiusTextView.setText(
                DisplayUtils.formatDistance(this.tS.getIncircleRadius().first));
        this.incircleRadiusBisTextView.setText(
                DisplayUtils.formatDistance(this.tS.getIncircleRadius().second));
        this.excircleRadiusTextView.setText(
                DisplayUtils.formatDistance(this.tS.getExcircleRadius().first));
        this.excircleRadiusBisTextView.setText(
                DisplayUtils.formatDistance(this.tS.getExcircleRadius().second));

        this.updateAnglesAndSides();
    }

    /**
     * Run the calculations if required parameters were filled.
     *
     * @return True if the calculation can be run, false otherwise.
     */
    private boolean chickenRun() {

        // If the information score is equal or greater than 1, then it is safe
        // to proceed to calculation.
        int informationScore = 0;

        if (this.aEditText.length() > 0) {
            informationScore += 4;
        }
        if (this.bEditText.length() > 0) {
            informationScore += 4;
        }
        if (this.cEditText.length() > 0) {
            informationScore += 4;
        }
        if (this.alphaEditText.length() > 0) {
            informationScore += 3;
        }
        if (this.betaEditText.length() > 0) {
            informationScore += 3;
        }
        if (this.gammaEditText.length() > 0) {
            informationScore += 3;
        }

        if (informationScore >= 10) {
            return this.runCalculations();
        } else {
            this.clearResults();
            return false;
        }
    }

    /**
     * Run the calculations.
     *
     * @return True if successful, false otherwise.
     */
    private boolean runCalculations() {
        this.initAttributes();
        this.getInputs();
        try {
            if (this.tS == null) {
                this.tS = new TriangleSolver(
                        this.a, this.b, this.c, this.alpha, this.beta, this.gamma, true);
            } else {
                this.tS.setA(this.a);
                this.tS.setB(this.b);
                this.tS.setC(this.c);
                this.tS.setAlpha(this.alpha);
                this.tS.setBeta(this.beta);
                this.tS.setGamma(this.gamma);
            }
            this.tS.compute();
        } catch (IllegalArgumentException e) {
            this.clearResults();
            Logger.log(Logger.ErrLabel.INPUT_ERROR, "Some data input to the solver were not valid");
            return false;
        }
        return true;
    }

    /**
     * Checks that all sides and angles are positive values.
     *
     * @return True if all sides and angles are positive, false otherwise.
     */
    private boolean areAllSidesAndAnglesPositives() {
        return (MathUtils.isPositive(this.tS.getA())
                && MathUtils.isPositive(this.tS.getB())
                && MathUtils.isPositive(this.tS.getC())
                && MathUtils.isPositive(this.tS.getAlpha())
                && MathUtils.isPositive(this.tS.getBeta())
                && MathUtils.isPositive(this.tS.getGamma()));
    }
}
