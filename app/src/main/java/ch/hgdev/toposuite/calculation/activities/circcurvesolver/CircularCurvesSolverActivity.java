package ch.hgdev.toposuite.calculation.activities.circcurvesolver;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CircularCurvesSolver;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CircularCurvesSolverActivity extends TopoSuiteActivity {

    private EditText             radiusEditText;
    private EditText             alphaAngleEditText;
    private EditText             chordOFEditText;
    private EditText             tangentEditText;
    private EditText             arrowEditText;

    private TextView             bisectorTextView;
    private TextView             arcTextView;
    private TextView             circumferenceTextView;
    private TextView             chordOMTextView;
    private TextView             betaAngleTextView;
    private TextView             circleSurfaceTextView;
    private TextView             sectorSurfaceTextView;
    private TextView             segmentSurfaceTextView;

    private CircularCurvesSolver ccs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circular_curves_solver);

        this.radiusEditText = (EditText) this.findViewById(R.id.radius);
        this.radiusEditText.setInputType(App.getInputTypeCoordinate());

        this.alphaAngleEditText = (EditText) this.findViewById(R.id.alpha_angle);
        this.alphaAngleEditText.setInputType(App.getInputTypeCoordinate());

        this.chordOFEditText = (EditText) this.findViewById(R.id.chord_of);
        this.chordOFEditText.setInputType(App.getInputTypeCoordinate());

        this.tangentEditText = (EditText) this.findViewById(R.id.tangent);
        this.tangentEditText.setInputType(App.getInputTypeCoordinate());

        this.arrowEditText = (EditText) this.findViewById(R.id.arrow);
        this.arrowEditText.setInputType(App.getInputTypeCoordinate());

        this.bisectorTextView = (TextView) this.findViewById(R.id.bisector);
        this.arcTextView = (TextView) this.findViewById(R.id.arc);
        this.circumferenceTextView = (TextView) this.findViewById(R.id.circumference);
        this.chordOMTextView = (TextView) this.findViewById(R.id.chord_om);
        this.betaAngleTextView = (TextView) this.findViewById(R.id.beta_angle);
        this.circleSurfaceTextView = (TextView) this.findViewById(R.id.circle_surface);
        this.sectorSurfaceTextView = (TextView) this.findViewById(R.id.sector_surface);
        this.segmentSurfaceTextView = (TextView) this.findViewById(R.id.segment_surface);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.ccs = (CircularCurvesSolver) SharedResources.getCalculationsHistory()
                    .get(position);
            this.initEditTexts();
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_circular_curve_solver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.circular_curves_solver, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.clear_button:
            this.clearInputs();
            this.clearResults();
            return true;
        case R.id.run_calculation_button:
            if (this.chickenRun()) {
                this.updateResults();
            } else {
                ViewUtils.showToast(this,
                        this.getText(R.string.error_impossible_calculation));
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check and run the calculation.
     * 
     * @return check/calculation status
     */
    private boolean chickenRun() {
        if (!this.checkInput()) {
            return false;
        }

        double radius, alphaAngle, chordOF, tangent, arrow;
        radius = ViewUtils.readDouble(this.radiusEditText);
        alphaAngle = ViewUtils.readDouble(this.alphaAngleEditText);
        chordOF = ViewUtils.readDouble(this.chordOFEditText);
        tangent = ViewUtils.readDouble(this.tangentEditText);
        arrow = ViewUtils.readDouble(this.arrowEditText);

        if (this.ccs == null) {
            this.ccs = new CircularCurvesSolver(
                    radius,
                    alphaAngle,
                    chordOF,
                    tangent,
                    arrow,
                    true);
        } else {
            this.ccs.setRadius(radius);
            this.ccs.setAlphaAngle(alphaAngle);
            this.ccs.setChordOF(chordOF);
            this.ccs.setTangent(tangent);
            this.ccs.setArrow(arrow);
        }

        this.ccs.compute();

        return true;
    }

    /**
     * Update the results.
     */
    private void updateResults() {
        this.radiusEditText.setText(DisplayUtils.toStringForEditText(this.ccs.getRadius()));
        this.alphaAngleEditText.setText(DisplayUtils.toStringForEditText(this.ccs.getAlphaAngle()));
        this.chordOFEditText.setText(DisplayUtils.toStringForEditText(this.ccs.getChordOF()));
        this.tangentEditText.setText(DisplayUtils.toStringForEditText(this.ccs.getTangent()));
        this.arrowEditText.setText(DisplayUtils.toStringForEditText(this.ccs.getArrow()));

        this.bisectorTextView.setText(DisplayUtils.formatDistance(this.ccs.getBisector()));
        this.arcTextView.setText(DisplayUtils.formatDistance(this.ccs.getArc()));
        this.circumferenceTextView.setText(DisplayUtils.formatDistance(
                this.ccs.getCircumference()));
        this.chordOMTextView.setText(DisplayUtils.formatDistance(this.ccs.getChordOM()));

        this.betaAngleTextView.setText(DisplayUtils.formatAngle(this.ccs.getBetaAngle()));
        this.circleSurfaceTextView.setText(DisplayUtils.formatSurface(
                this.ccs.getCircleSurface()));
        this.sectorSurfaceTextView.setText(DisplayUtils.formatSurface(
                this.ccs.getSectorSurface()));
        this.segmentSurfaceTextView.setText(DisplayUtils.formatSurface(
                this.ccs.getSegmentSurface()));
    }

    /**
     * Check user inputs.
     * 
     * @return true if the user input is valid, false otherwise
     */
    private boolean checkInput() {
        // check if the central angle is 0.0
        if ((this.alphaAngleEditText.length() > 0) && MathUtils.isZero(
                ViewUtils.readDouble(this.alphaAngleEditText))) {
            return false;
        }

        // radius / alpha
        if ((this.radiusEditText.length() > 0) && (this.alphaAngleEditText.length() > 0)) {
            return true;
        }

        // radius / tangent
        if ((this.radiusEditText.length() > 0) && (this.tangentEditText.length() > 0)) {
            return true;
        }

        // radius / arrow
        if ((this.radiusEditText.length() > 0) && (this.arrowEditText.length() > 0)) {
            return true;
        }

        // radius / chord OF
        if ((this.radiusEditText.length() > 0) && (this.chordOFEditText.length() > 0)) {
            return true;
        }

        // chord OF / alpha
        if ((this.chordOFEditText.length() > 0) && (this.alphaAngleEditText.length() > 0)) {
            return true;
        }

        // chord OF / tangent
        if ((this.chordOFEditText.length() > 0) && (this.tangentEditText.length() > 0)) {
            return true;
        }

        // chord OF / arrow
        if ((this.chordOFEditText.length() > 0) && (this.arrowEditText.length() > 0)) {
            return true;
        }

        // tangent / alpha
        if ((this.tangentEditText.length() > 0) && (this.alphaAngleEditText.length() > 0)) {
            return true;
        }

        return false;
    }

    /**
     * Initialize the edit texts.
     */
    private void initEditTexts() {
        if (this.ccs == null) {
            return;
        }

        if (!MathUtils.isIgnorable(this.ccs.getRadius())) {
            this.radiusEditText.setText(String.valueOf(this.ccs.getRadius()));
        }

        if (!MathUtils.isIgnorable(this.ccs.getAlphaAngle())) {
            this.alphaAngleEditText.setText(String.valueOf(this.ccs.getAlphaAngle()));
        }

        if (!MathUtils.isIgnorable(this.ccs.getChordOF())) {
            this.chordOFEditText.setText(String.valueOf(this.ccs.getChordOF()));
        }

        if (!MathUtils.isIgnorable(this.ccs.getTangent())) {
            this.tangentEditText.setText(String.valueOf(this.ccs.getTangent()));
        }

        if (!MathUtils.isIgnorable(this.ccs.getArrow())) {
            this.arrowEditText.setText(String.valueOf(this.ccs.getArrow()));
        }
    }

    /**
     * Clear text edits.
     */
    private void clearInputs() {
        this.radiusEditText.setText("");
        this.alphaAngleEditText.setText("");
        this.chordOFEditText.setText("");
        this.tangentEditText.setText("");
        this.arrowEditText.setText("");
    }

    /**
     * Clear the result text views.
     */
    private void clearResults() {
        String noValue = this.getString(R.string.no_value);
        this.bisectorTextView.setText(noValue);
        this.arcTextView.setText(noValue);
        this.circumferenceTextView.setText(noValue);
        this.chordOMTextView.setText(noValue);
        this.betaAngleTextView.setText(noValue);
        this.circleSurfaceTextView.setText(noValue);
        this.sectorSurfaceTextView.setText(noValue);
        this.segmentSurfaceTextView.setText(noValue);
    }
}
