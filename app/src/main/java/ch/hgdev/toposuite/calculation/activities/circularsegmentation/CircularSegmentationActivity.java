package ch.hgdev.toposuite.calculation.activities.circularsegmentation;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CircularSegmentation;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CircularSegmentationActivity extends TopoSuiteActivity {
    public static final String  CIRCLE_CENTER_POINT_NUMBER      = "circle_center_point_number";
    public static final String  CIRCLE_START_POINT_NUMBER       = "circle_start_point_number";
    public static final String  CIRCLE_END_POINT_NUMBER         = "circle_end_point_number";
    public static final String  NUMBER_OF_SEGMENTS              = "number_of_segments";
    public static final String  ARC_LENGTH                      = "arc_length";
    public static final String  FIRST_RESULT_POINT_NUMBER       = "first_result_point_number";

    private static final String CIRCLE_CENTER_SELECTED_POSITION = "circle_center_selected_position";
    private static final String CIRCLE_START_SELECTED_POSITION  = "circle_start_selected_position";
    private static final String CIRCLE_END_SELECTED_POSITION    = "circle_end_selected_position";
    private static final String IS_MODE_ARC_LENGTH              = "is_mode_arc_length";

    private Spinner             circleCenterSpinner;
    private int                 circleCenterSelectedPosition;
    private TextView            circleCenterTextView;

    private Spinner             circleStartSpinner;
    private int                 circleStartSelectedPosition;
    private TextView            circleStartTextView;

    private Spinner             circleEndSpinner;
    private int                 circleEndSelectedPosition;
    private TextView            circleEndTextView;

    private LinearLayout        segmentLayout;
    private EditText            segmentEditText;

    private LinearLayout        arcLengthLayout;
    private EditText            arcLengthEditText;

    private Mode                selectedMode;

    private EditText            firstPointNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circular_segmentation);

        this.mapViews();
        this.initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(
                "", MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);

        this.circleCenterSpinner.setAdapter(a);
        this.circleStartSpinner.setAdapter(a);
        this.circleEndSpinner.setAdapter(a);

        // check if we create a new circular segmentation calculation or if we
        // modify an existing one.
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            CircularSegmentation circularSegmentation = (CircularSegmentation) SharedResources
                    .getCalculationsHistory().get(position);

            this.circleCenterSelectedPosition = a.getPosition(
                    circularSegmentation.getCircleCenter());
            this.circleStartSelectedPosition = a.getPosition(
                    circularSegmentation.getCircleStartPoint());
            this.circleEndSelectedPosition = a.getPosition(
                    circularSegmentation.getCircleEndPoint());

            double arcLength = circularSegmentation.getArcLength();
            int numberOfSegments = circularSegmentation.getNumberOfSegments();
            this.arcLengthEditText.setText(DisplayUtils.toStringForEditText(arcLength));
            this.segmentEditText.setText(DisplayUtils.toStringForEditText(numberOfSegments));
        }

        this.circleCenterSpinner.setSelection(this.circleCenterSelectedPosition);
        this.circleStartSpinner.setSelection(this.circleStartSelectedPosition);
        this.circleEndSpinner.setSelection(this.circleEndSelectedPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CircularSegmentationActivity.CIRCLE_CENTER_SELECTED_POSITION,
                this.circleCenterSelectedPosition);
        outState.putInt(CircularSegmentationActivity.CIRCLE_START_SELECTED_POSITION,
                this.circleStartSelectedPosition);
        outState.putInt(CircularSegmentationActivity.CIRCLE_END_SELECTED_POSITION,
                this.circleEndSelectedPosition);
        outState.putBoolean(CircularSegmentationActivity.IS_MODE_ARC_LENGTH,
                this.selectedMode == Mode.ARCLENGTH);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.circleCenterSelectedPosition = savedInstanceState.getInt(
                    CircularSegmentationActivity.CIRCLE_CENTER_SELECTED_POSITION);
            this.circleStartSelectedPosition = savedInstanceState.getInt(
                    CircularSegmentationActivity.CIRCLE_START_SELECTED_POSITION);
            this.circleEndSelectedPosition = savedInstanceState.getInt(
                    CircularSegmentationActivity.CIRCLE_END_SELECTED_POSITION);
            this.selectedMode = savedInstanceState.getBoolean(
                    CircularSegmentationActivity.IS_MODE_ARC_LENGTH) ? Mode.ARCLENGTH
                    : Mode.SEGMENT;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.circular_segmentation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                this.showCircularSegmentationResultActivity();
            } else {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_circular_segmentation);
    }

    /**
     * Define the action done when the mode radio button is clicked.
     *
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
        case R.id.mode_segment:
            if (checked) {
                this.arcLengthLayout.setVisibility(View.GONE);
                this.segmentLayout.setVisibility(View.VISIBLE);
                this.selectedMode = Mode.SEGMENT;
            }
            break;
        case R.id.mode_arc_length:
            if (checked) {
                this.arcLengthLayout.setVisibility(View.VISIBLE);
                this.segmentLayout.setVisibility(View.GONE);
                this.selectedMode = Mode.ARCLENGTH;
            }
            break;
        default:
            Logger.log(Logger.ErrLabel.INPUT_ERROR, "Unknown mode selected");
        }
    }

    /**
     * Check that all input have been filled correctly.
     *
     * @return True if inputs are OK, false otherwise.
     */
    private boolean checkInputs() {
        if ((this.circleCenterSelectedPosition < 1) || (this.circleStartSelectedPosition < 1)
                || (this.circleEndSelectedPosition < 1)) {
            return false;
        }

        if (this.selectedMode == Mode.ARCLENGTH) {
            if (this.arcLengthEditText.length() == 0) {
                return false;
            }
        } else if (this.selectedMode == Mode.SEGMENT) {
            if (this.segmentEditText.length() == 0) {
                return false;
            }
        } else {
            // this state should never be reached
            return false;
        }

        if (this.firstPointNumberEditText.length() == 0) {
            return false;
        }

        return true;
    }

    /**
     * Show the results activity.
     */
    private void showCircularSegmentationResultActivity() {
        Bundle bundle = new Bundle();

        Point circleCenter = (Point) this.circleCenterSpinner
                .getItemAtPosition(this.circleCenterSelectedPosition);
        Point circleStart = (Point) this.circleStartSpinner
                .getItemAtPosition(this.circleStartSelectedPosition);
        Point circleEnd = (Point) this.circleEndSpinner
                .getItemAtPosition(this.circleEndSelectedPosition);

        int numberOfSegments = MathUtils.IGNORE_INT;
        double arcLength = MathUtils.IGNORE_DOUBLE;
        if (this.selectedMode == Mode.SEGMENT) {
            numberOfSegments = ViewUtils.readInt(this.segmentEditText);
        } else {
            arcLength = ViewUtils.readDouble(this.arcLengthEditText);
        }

        String firstResultPointNumber = this.firstPointNumberEditText.getText().toString();

        bundle.putString(CircularSegmentationActivity.CIRCLE_CENTER_POINT_NUMBER,
                circleCenter.getNumber());
        bundle.putString(CircularSegmentationActivity.CIRCLE_START_POINT_NUMBER,
                circleStart.getNumber());
        bundle.putString(CircularSegmentationActivity.CIRCLE_END_POINT_NUMBER,
                circleEnd.getNumber());

        bundle.putInt(CircularSegmentationActivity.NUMBER_OF_SEGMENTS, numberOfSegments);
        bundle.putDouble(CircularSegmentationActivity.ARC_LENGTH, arcLength);
        bundle.putString(CircularSegmentationActivity.FIRST_RESULT_POINT_NUMBER,
                firstResultPointNumber);

        Intent resultsActivityIntent = new Intent(this, CircularSegmentationResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);

    }

    /**
     * Map views to their respective attributes.
     */
    private void mapViews() {
        this.circleCenterSpinner = (Spinner) this.findViewById(R.id.point_center_spinner);
        this.circleCenterTextView = (TextView) this.findViewById(R.id.point_center_textview);

        this.circleStartSpinner = (Spinner) this.findViewById(R.id.point_start_spinner);
        this.circleStartTextView = (TextView) this.findViewById(R.id.point_start_textview);

        this.circleEndSpinner = (Spinner) this.findViewById(R.id.point_end_spinner);
        this.circleEndTextView = (TextView) this.findViewById(R.id.point_end_textview);

        this.segmentLayout = (LinearLayout) this.findViewById(R.id.segments_layout);
        this.segmentEditText = (EditText) this.findViewById(R.id.segment);

        this.arcLengthLayout = (LinearLayout) this.findViewById(R.id.arc_length_layout);
        this.arcLengthEditText = (EditText) this.findViewById(R.id.arc_length);

        this.firstPointNumberEditText = (EditText) this.findViewById(R.id.first_point_number);
    }

    /**
     * Init views.
     */
    private void initViews() {
        this.circleCenterSelectedPosition = 0;
        this.circleStartSelectedPosition = 0;
        this.circleEndSelectedPosition = 0;

        if (this.selectedMode == null) {
            this.selectedMode = Mode.SEGMENT;
        }

        this.arcLengthEditText.setInputType(
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.segmentEditText.setInputType(
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        this.circleCenterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CircularSegmentationActivity.this.circleCenterSelectedPosition = pos;
                Point p = (Point) CircularSegmentationActivity.this.circleCenterSpinner
                        .getItemAtPosition(pos);
                if (!p.getNumber().isEmpty()) {
                    CircularSegmentationActivity.this.circleCenterTextView.setText(
                            DisplayUtils.formatPoint(CircularSegmentationActivity.this, p));
                } else {
                    CircularSegmentationActivity.this.circleCenterTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.circleStartSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CircularSegmentationActivity.this.circleStartSelectedPosition = pos;
                Point p = (Point) CircularSegmentationActivity.this.circleStartSpinner
                        .getItemAtPosition(pos);
                if (!p.getNumber().isEmpty()) {
                    CircularSegmentationActivity.this.circleStartTextView.setText(
                            DisplayUtils.formatPoint(CircularSegmentationActivity.this, p));
                } else {
                    CircularSegmentationActivity.this.circleStartTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.circleEndSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CircularSegmentationActivity.this.circleEndSelectedPosition = pos;
                Point p = (Point) CircularSegmentationActivity.this.circleEndSpinner
                        .getItemAtPosition(pos);
                if (!p.getNumber().isEmpty()) {
                    CircularSegmentationActivity.this.circleEndTextView.setText(
                            DisplayUtils.formatPoint(CircularSegmentationActivity.this, p));
                } else {
                    CircularSegmentationActivity.this.circleEndTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.firstPointNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    private enum Mode {
        SEGMENT,
        ARCLENGTH
    }
}