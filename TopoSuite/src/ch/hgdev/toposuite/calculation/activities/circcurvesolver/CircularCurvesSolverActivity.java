package ch.hgdev.toposuite.calculation.activities.circcurvesolver;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CircularCurvesSolver;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

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
        this.radiusEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.alphaAngleEditText = (EditText) this.findViewById(R.id.alpha_angle);
        this.alphaAngleEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.chordOFEditText = (EditText) this.findViewById(R.id.chord_of);
        this.chordOFEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.tangentEditText = (EditText) this.findViewById(R.id.tangent);
        this.tangentEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.arrowEditText = (EditText) this.findViewById(R.id.arrow);
        this.arrowEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

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
            return true;
        case R.id.run_calculation_button:
            if (this.chickenRun()) {
                this.updateResults();
            } else {
                Toast errorToast = Toast.makeText(this,
                        this.getText(R.string.error_impossible_calculation),
                        Toast.LENGTH_SHORT);
                errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                errorToast.show();
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
        radius = alphaAngle = chordOF = tangent = arrow = 0.0;

        if (this.radiusEditText.length() > 0) {
            radius = Double.parseDouble(
                    this.radiusEditText.getText().toString());
        }

        if (this.alphaAngleEditText.length() > 0) {
            alphaAngle = Double.parseDouble(
                    this.alphaAngleEditText.getText().toString());
        }

        if (this.chordOFEditText.length() > 0) {
            chordOF = Double.parseDouble(
                    this.chordOFEditText.getText().toString());
        }

        if (this.tangentEditText.length() > 0) {
            tangent = Double.parseDouble(
                    this.tangentEditText.getText().toString());
        }

        if (this.arrowEditText.length() > 0) {
            arrow = Double.parseDouble(
                    this.arrowEditText.getText().toString());
        }

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
        this.radiusEditText.setText(DisplayUtils.toString(this.ccs.getRadius()));
        this.alphaAngleEditText.setText(DisplayUtils.toString(this.ccs.getAlphaAngle()));
        this.chordOFEditText.setText(DisplayUtils.toString(this.ccs.getChordOF()));
        this.tangentEditText.setText(DisplayUtils.toString(this.ccs.getTangent()));
        this.arrowEditText.setText(DisplayUtils.toString(this.ccs.getArrow()));

        this.bisectorTextView.setText(DisplayUtils.toString(this.ccs.getBisector()));
        this.arcTextView.setText(DisplayUtils.toString(this.ccs.getArc()));
        this.circumferenceTextView.setText(DisplayUtils.toString(this.ccs.getCircumference()));
        this.chordOMTextView.setText(DisplayUtils.toString(this.ccs.getChordOM()));

        this.betaAngleTextView.setText(DisplayUtils.toString(this.ccs.getBetaAngle()));
        this.circleSurfaceTextView.setText(DisplayUtils.toString(this.ccs.getCircleSurface()));
        this.sectorSurfaceTextView.setText(DisplayUtils.toString(this.ccs.getSectorSurface()));
        this.segmentSurfaceTextView.setText(DisplayUtils.toString(this.ccs.getSegmentSurface()));
    }

    /**
     * Check user inputs.
     * 
     * @return true if the user input is valid, false otherwise
     */
    private boolean checkInput() {
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

        if (!MathUtils.isZero(this.ccs.getRadius())) {
            this.radiusEditText.setText(String.valueOf(this.ccs.getRadius()));
        }

        if (!MathUtils.isZero(this.ccs.getAlphaAngle())) {
            this.alphaAngleEditText.setText(String.valueOf(this.ccs.getAlphaAngle()));
        }

        if (!MathUtils.isZero(this.ccs.getChordOF())) {
            this.chordOFEditText.setText(String.valueOf(this.ccs.getChordOF()));
        }

        if (!MathUtils.isZero(this.ccs.getTangent())) {
            this.tangentEditText.setText(String.valueOf(this.ccs.getTangent()));
        }

        if (!MathUtils.isZero(this.ccs.getArrow())) {
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
}
