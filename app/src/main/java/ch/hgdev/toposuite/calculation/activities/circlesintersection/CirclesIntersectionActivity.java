package ch.hgdev.toposuite.calculation.activities.circlesintersection;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.CirclesIntersection;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CirclesIntersectionActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {

    private static final String CENTER_ONE_SELECTED_POSITION = "center_one_selected_position";
    private static final String BY_ONE_SELECTED_POSITION     = "by_one_selected_position";
    private static final String CENTER_TWO_SELECTED_POSITION = "center_two_selected_position";
    private static final String BY_TWO_SELECTED_POSITION     = "by_two_selected_position";

    private Spinner             centerOneSpinner;
    private int                 centerOneSelectedPosition;
    private Point               centerOnePoint;
    private TextView            centerOneTextView;
    private double              radiusOne;
    private EditText            radiusOneEditText;
    private Spinner             byPointOneSpinner;
    private int                 byPointOneSelectedPosition;
    private Point               byPointOne;
    private TextView            byPointOneTextView;

    private Spinner             centerTwoSpinner;
    private int                 centerTwoSelectedPosition;
    private Point               centerTwoPoint;
    private TextView            centerTwoTextView;
    private double              radiusTwo;
    private EditText            radiusTwoEditText;
    private Spinner             byPointTwoSpinner;
    private int                 byPointTwoSelectedPosition;
    private Point               byPointTwo;
    private TextView            byPointTwoTextView;

    private ArrayAdapter<Point> adapter;

    private CirclesIntersection circlesIntersection;

    private TextView            intersectionOneTextView;
    private EditText            intersectionOneEditText;
    private Point               intersectionOne;

    private TextView            intersectionTwoTextView;
    private EditText            intersectionTwoEditText;
    private Point               intersectionTwo;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                 position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circles_intersection);

        this.mapViews();
        this.initViews();

        this.position = -1;

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, false, false));
        points.addAll(SharedResources.getSetOfPoints());

        this.adapter = new ArrayAdapter<Point>(this, R.layout.spinner_list_item, points);
        this.centerOneSpinner.setAdapter(this.adapter);
        this.centerTwoSpinner.setAdapter(this.adapter);
        this.byPointOneSpinner.setAdapter(this.adapter);
        this.byPointTwoSpinner.setAdapter(this.adapter);

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.circlesIntersection = (CirclesIntersection) SharedResources
                    .getCalculationsHistory().get(this.position);
            this.centerOneSelectedPosition = this.adapter.getPosition(
                    this.circlesIntersection.getCenterFirst());
            this.centerTwoSelectedPosition = this.adapter.getPosition(
                    this.circlesIntersection.getCenterSecond());
            this.radiusOneEditText.setText(
                    DisplayUtils.toStringForEditText(this.circlesIntersection.getRadiusFirst()));
            this.radiusTwoEditText.setText(
                    DisplayUtils.toStringForEditText(this.circlesIntersection.getRadiusSecond()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.centerOneSpinner.setSelection(this.centerOneSelectedPosition);
        this.centerTwoSpinner.setSelection(this.centerTwoSelectedPosition);
        this.byPointOneSpinner.setSelection(this.byPointOneSelectedPosition);
        this.byPointTwoSpinner.setSelection(this.byPointTwoSelectedPosition);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_circles_intersection);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CirclesIntersectionActivity.CENTER_ONE_SELECTED_POSITION,
                this.centerOneSelectedPosition);
        outState.putInt(CirclesIntersectionActivity.CENTER_TWO_SELECTED_POSITION,
                this.centerTwoSelectedPosition);
        outState.putInt(CirclesIntersectionActivity.BY_ONE_SELECTED_POSITION,
                this.byPointOneSelectedPosition);
        outState.putInt(CirclesIntersectionActivity.BY_TWO_SELECTED_POSITION,
                this.byPointTwoSelectedPosition);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.centerOneSelectedPosition = savedInstanceState.getInt(
                    CirclesIntersectionActivity.CENTER_ONE_SELECTED_POSITION);
            this.centerTwoSelectedPosition = savedInstanceState.getInt(
                    CirclesIntersectionActivity.CENTER_TWO_SELECTED_POSITION);
            this.byPointOneSelectedPosition = savedInstanceState.getInt(
                    CirclesIntersectionActivity.BY_ONE_SELECTED_POSITION);
            this.byPointTwoSelectedPosition = savedInstanceState.getInt(
                    CirclesIntersectionActivity.BY_TWO_SELECTED_POSITION);
            this.centerOnePoint = this.adapter.getItem(this.centerOneSelectedPosition);
            this.centerTwoPoint = this.adapter.getItem(this.centerTwoSelectedPosition);
            this.byPointOne = this.adapter.getItem(this.byPointOneSelectedPosition);
            this.byPointTwo = this.adapter.getItem(this.byPointTwoSelectedPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.circles_intersection, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                if (!MathUtils.isPositive(ViewUtils.readDouble(this.radiusOneEditText))
                        || !MathUtils.isPositive(ViewUtils.readDouble(this.radiusTwoEditText))) {
                    ViewUtils.showToast(this,
                            this.getString(R.string.error_radius_must_be_positive));
                } else {
                    this.runCalculations();
                    this.updateResults();
                }
            } else {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
            }
            return true;
        case R.id.save_points:
            if ((this.intersectionOne == null) || (this.intersectionTwo == null)) {
                ViewUtils.showToast(this, this.getString(R.string.error_no_points_to_save));
                return true;
            }

            if ((this.intersectionOneEditText.length() < 1)
                    && (this.intersectionTwoEditText.length() < 1)) {
                ViewUtils.showToast(this, this.getString(R.string.error_no_points_saved));
                return true;
            }

            // save first point
            if (this.intersectionOneEditText.length() > 0) {
                this.intersectionOne.setNumber(this.intersectionOneEditText.getText().toString());

                if (MathUtils.isZero(this.intersectionOne.getEast())
                        && MathUtils.isZero(this.intersectionOne.getNorth())) {
                    ViewUtils.showToast(this, this.getString(R.string.error_no_points_to_save));
                } else if (SharedResources.getSetOfPoints().find(
                        this.intersectionOne.getNumber()) == null) {
                    SharedResources.getSetOfPoints().add(this.intersectionOne);
                    this.intersectionOne.registerDAO(PointsDataSource.getInstance());

                    ViewUtils.showToast(this, this.getString(R.string.point_add_success));
                } else {
                    // this point already exists
                    MergePointsDialog dialog = new MergePointsDialog();

                    Bundle args = new Bundle();
                    args.putString(
                            MergePointsDialog.POINT_NUMBER,
                            this.intersectionOne.getNumber());

                    args.putDouble(MergePointsDialog.NEW_EAST,
                            this.intersectionOne.getEast());
                    args.putDouble(MergePointsDialog.NEW_NORTH,
                            this.intersectionOne.getNorth());
                    args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                            this.intersectionOne.getAltitude());

                    dialog.setArguments(args);
                    dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");
                }
            } else {
                ViewUtils.showToast(this, this.getString(R.string.point_one_not_saved));
            }

            // save second point
            if (this.intersectionTwoEditText.length() > 0) {
                this.intersectionTwo.setNumber(this.intersectionTwoEditText.getText().toString());

                if (MathUtils.isZero(this.intersectionTwo.getEast())
                        && MathUtils.isZero(this.intersectionTwo.getNorth())) {
                    ViewUtils.showToast(this, this.getString(R.string.error_no_points_to_save));
                } else if (SharedResources.getSetOfPoints().find(
                        this.intersectionTwo.getNumber()) == null) {
                    SharedResources.getSetOfPoints().add(this.intersectionTwo);
                    this.intersectionTwo.registerDAO(PointsDataSource.getInstance());

                    ViewUtils.showToast(this, this.getString(R.string.point_add_success));
                } else {
                    // this point already exists
                    MergePointsDialog dialog = new MergePointsDialog();

                    Bundle args = new Bundle();
                    args.putString(
                            MergePointsDialog.POINT_NUMBER,
                            this.intersectionTwo.getNumber());

                    args.putDouble(MergePointsDialog.NEW_EAST,
                            this.intersectionTwo.getEast());
                    args.putDouble(MergePointsDialog.NEW_NORTH,
                            this.intersectionTwo.getNorth());
                    args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                            this.intersectionTwo.getAltitude());

                    dialog.setArguments(args);
                    dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");
                }
            } else {
                ViewUtils.showToast(this, this.getString(R.string.point_two_not_saved));
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Update the results view.
     */
    private void updateResults() {
        this.intersectionOneTextView.setText(
                DisplayUtils.formatPoint(this, this.intersectionOne));
        this.intersectionTwoTextView.setText(
                DisplayUtils.formatPoint(this, this.intersectionTwo));
    }

    /**
     * Map views to their respective attributes.
     */
    private void mapViews() {
        this.centerOneSpinner = (Spinner) this.findViewById(R.id.center_one_spinner);
        this.centerOneTextView = (TextView) this.findViewById(R.id.center_one_textview);
        this.radiusOneEditText = (EditText) this.findViewById(R.id.radius_one);
        this.byPointOneSpinner = (Spinner) this.findViewById(R.id.by_point_one_spinner);
        this.byPointOneTextView = (TextView) this.findViewById(R.id.by_point_one_textview);

        this.centerTwoSpinner = (Spinner) this.findViewById(R.id.center_two_spinner);
        this.centerTwoTextView = (TextView) this.findViewById(R.id.center_two_textview);
        this.radiusTwoEditText = (EditText) this.findViewById(R.id.radius_two);
        this.byPointTwoSpinner = (Spinner) this.findViewById(R.id.by_point_two_spinner);
        this.byPointTwoTextView = (TextView) this.findViewById(R.id.by_point_two_textview);

        this.intersectionOneTextView = (TextView) this.findViewById(R.id.intersection_one);
        this.intersectionTwoTextView = (TextView) this.findViewById(R.id.intersection_two);

        this.intersectionOneEditText = (EditText) this.findViewById(R.id.intersection_one_number);
        this.intersectionTwoEditText = (EditText) this.findViewById(R.id.intersection_two_number);
    }

    /**
     * Init views.
     */
    private void initViews() {
        this.centerOneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.centerOneSelectedPosition = pos;

                CirclesIntersectionActivity.this.centerOnePoint = (Point) CirclesIntersectionActivity.this.centerOneSpinner
                        .getItemAtPosition(pos);
                if (!CirclesIntersectionActivity.this.centerOnePoint.getNumber().isEmpty()) {
                    CirclesIntersectionActivity.this.centerOneTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.centerOnePoint));
                } else {
                    CirclesIntersectionActivity.this.centerOneTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusOne();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.byPointOneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.byPointOneSelectedPosition = pos;

                CirclesIntersectionActivity.this.byPointOne = (Point) CirclesIntersectionActivity.this.byPointOneSpinner
                        .getItemAtPosition(pos);
                if (!CirclesIntersectionActivity.this.byPointOne.getNumber().isEmpty()) {
                    CirclesIntersectionActivity.this.byPointOneTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.byPointOne));
                } else {
                    CirclesIntersectionActivity.this.byPointOneTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusOne();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.centerTwoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.centerTwoSelectedPosition = pos;

                CirclesIntersectionActivity.this.centerTwoPoint = (Point) CirclesIntersectionActivity.this.centerTwoSpinner
                        .getItemAtPosition(pos);
                if (!CirclesIntersectionActivity.this.centerTwoPoint.getNumber().isEmpty()) {
                    CirclesIntersectionActivity.this.centerTwoTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.centerTwoPoint));
                } else {
                    CirclesIntersectionActivity.this.centerTwoTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusTwo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.byPointTwoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.byPointTwoSelectedPosition = pos;

                CirclesIntersectionActivity.this.byPointTwo = (Point) CirclesIntersectionActivity.this.byPointTwoSpinner
                        .getItemAtPosition(pos);
                if (!CirclesIntersectionActivity.this.byPointTwo.getNumber().isEmpty()) {
                    CirclesIntersectionActivity.this.byPointTwoTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.byPointTwo));
                } else {
                    CirclesIntersectionActivity.this.byPointTwoTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusTwo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.radiusOneEditText.setInputType(App.getInputTypeCoordinate());
        this.radiusTwoEditText.setInputType(App.getInputTypeCoordinate());
    }

    /**
     * Fill radius for the first circle with the distance between the center of
     * the first circle and the selected point from the spinner.
     */
    private void fillRadiusOne() {
        if ((this.centerOneSelectedPosition > 0) && (this.byPointOneSelectedPosition > 0)) {
            this.radiusOneEditText.setText(DisplayUtils.toStringForEditText(
                    MathUtils.euclideanDistance(this.centerOnePoint, this.byPointOne)));
            this.radiusOneEditText.setEnabled(false);
        } else {
            this.radiusOneEditText.setEnabled(true);
        }
    }

    /**
     * Fill radius for the second circle with the distance between the center of
     * the second circle and the selected point from the spinner.
     */
    private void fillRadiusTwo() {
        if ((this.centerTwoSelectedPosition > 0) && (this.byPointTwoSelectedPosition > 0)) {
            this.radiusTwoEditText.setText(DisplayUtils.toStringForEditText(
                    MathUtils.euclideanDistance(this.centerTwoPoint, this.byPointTwo)));
            this.radiusTwoEditText.setEnabled(false);
        } else {
            this.radiusTwoEditText.setEnabled(true);
        }
    }

    /**
     * Check that inputs are OK so the calculation can be run safely.
     * 
     * @return True if OK, false otherwise.
     */
    private boolean checkInputs() {
        if ((this.centerOneSelectedPosition < 1) || (this.centerTwoSelectedPosition < 1)) {
            return false;
        }
        // the two points must be different
        if (this.centerOneSelectedPosition == this.centerTwoSelectedPosition) {
            return false;
        }
        if ((this.radiusOneEditText.length() < 1) || (this.radiusTwoEditText.length() < 1)) {
            return false;
        }
        return true;
    }

    /**
     * Do the actual calculation and update the results.
     */
    private void runCalculations() {
        if (this.circlesIntersection == null) {
            this.circlesIntersection = new CirclesIntersection();
        }

        this.centerOnePoint = (Point) this.centerOneSpinner
                .getItemAtPosition(this.centerOneSelectedPosition);
        this.radiusOne = ViewUtils.readDouble(this.radiusOneEditText);
        this.centerTwoPoint = (Point) this.centerTwoSpinner
                .getItemAtPosition(this.centerTwoSelectedPosition);
        this.radiusTwo = ViewUtils.readDouble(this.radiusTwoEditText);

        this.circlesIntersection.setCenterFirst(this.centerOnePoint);
        this.circlesIntersection.setRadiusFirst(this.radiusOne);
        this.circlesIntersection.setCenterSecond(this.centerTwoPoint);
        this.circlesIntersection.setRadiusSecond(this.radiusTwo);

        try {
            this.circlesIntersection.compute();
        } catch (CalculationException e) {
            ViewUtils.showToast(this, e.getMessage());
        }

        this.intersectionOne = this.circlesIntersection.getFirstIntersection();
        this.intersectionTwo = this.circlesIntersection.getSecondIntersection();
    }

    @Override
    public void onMergePointsDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onMergePointsDialogError(String message) {
        ViewUtils.showToast(this, message);
    }
}
