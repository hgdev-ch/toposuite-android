package ch.hgdev.toposuite.calculation.activities.trianglesolver;

import android.os.Bundle;
import android.util.Log;
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
    protected void onResume() {
        super.onResume();
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
        case R.id.clear:
            this.clearInputs();
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

        this.aEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.bEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.cEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.alphaEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.betaEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.gammaEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

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
        this.a = 0.0;
        this.b = 0.0;
        this.c = 0.0;
        this.alpha = 0.0;
        this.beta = 0.0;
        this.gamma = 0.0;
    }

    /**
     * Update angles and sides edit texts if their value is greater than 0.
     */
    private void updateAnglesAndSides() {
        if (this.tS != null) {
            if (MathUtils.isPositive(this.tS.getA())) {
                this.aEditText.setText(DisplayUtils.toString(this.tS.getA()));
            }
            if (MathUtils.isPositive(this.tS.getB())) {
                this.bEditText.setText(DisplayUtils.toString(this.tS.getB()));
            }
            if (MathUtils.isPositive(this.tS.getC())) {
                this.cEditText.setText(DisplayUtils.toString(this.tS.getC()));
            }
            if (MathUtils.isPositive(this.tS.getAlpha())) {
                this.alphaEditText.setText(DisplayUtils.toString(this.tS.getAlpha()));
            }
            if (MathUtils.isPositive(this.tS.getBeta())) {
                this.betaEditText.setText(DisplayUtils.toString(this.tS.getBeta()));
            }
            if (MathUtils.isPositive(this.tS.getGamma())) {
                this.gammaEditText.setText(DisplayUtils.toString(this.tS.getGamma()));
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
        if (this.aEditText.length() > 0) {
            this.a = Double.parseDouble(this.aEditText.getText().toString());
        }
        if (this.bEditText.length() > 0) {
            this.b = Double.parseDouble(this.bEditText.getText().toString());
        }
        if (this.cEditText.length() > 0) {
            this.c = Double.parseDouble(this.cEditText.getText().toString());
        }
        if (this.alphaEditText.length() > 0) {
            this.alpha = Double.parseDouble(this.alphaEditText.getText().toString());
        }
        if (this.betaEditText.length() > 0) {
            this.beta = Double.parseDouble(this.betaEditText.getText().toString());
        }
        if (this.gammaEditText.length() > 0) {
            this.gamma = Double.parseDouble(this.gammaEditText.getText().toString());
        }
    }

    /**
     * Clear result views.
     */
    private void clearResults() {
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
        this.perimeterTextView.setText(
                DisplayUtils.toString(this.tS.getPerimeter().first));
        this.perimeterBisTextView.setText(
                DisplayUtils.toString(this.tS.getPerimeter().second));
        this.heightTextView.setText(
                DisplayUtils.toString(this.tS.getHeight().first));
        this.heightBisTextView.setText(
                DisplayUtils.toString(this.tS.getHeight().second));
        this.surfaceTextView.setText(
                DisplayUtils.toString(this.tS.getSurface().first));
        this.surfaceBisTextView.setText(
                DisplayUtils.toString(this.tS.getSurface().second));
        this.incircleRadiusTextView.setText(
                DisplayUtils.toString(this.tS.getIncircleRadius().first));
        this.incircleRadiusBisTextView.setText(
                DisplayUtils.toString(this.tS.getIncircleRadius().second));
        this.excircleRadiusTextView.setText(
                DisplayUtils.toString(this.tS.getExcircleRadius().first));
        this.excircleRadiusBisTextView.setText(
                DisplayUtils.toString(this.tS.getExcircleRadius().second));

        this.updateAnglesAndSides();
    }

    /**
     * Run the calculations if required parameters were filled.
     */
    private void chickenRun() {

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
            this.runCalculations();
        } else {
            this.clearResults();
        }
    }

    private void runCalculations() {
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
            this.updateResults();
        } catch (IllegalArgumentException e) {
            this.clearResults();
            Log.e(Logger.TOPOSUITE_INPUT_ERROR, "Some data input to the solver were not valid");
        }
    }
}
