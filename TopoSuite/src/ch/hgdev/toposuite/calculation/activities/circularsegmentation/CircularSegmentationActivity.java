package ch.hgdev.toposuite.calculation.activities.circularsegmentation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
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
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

public class CircularSegmentationActivity extends TopoSuiteActivity {

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
    private RadioButton         arcLengthRadio;

    private Mode                selectedMode;

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
                0, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);

        this.circleCenterSpinner.setAdapter(a);
        this.circleStartSpinner.setAdapter(a);
        this.circleEndSpinner.setAdapter(a);

        // TODO handle bundle
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {

        }

        this.circleCenterSpinner.setSelection(this.circleCenterSelectedPosition);
        this.circleStartSpinner.setSelection(this.circleStartSelectedPosition);
        this.circleEndSpinner.setSelection(this.circleEndSelectedPosition);

        this.arcLengthRadio.callOnClick();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CIRCLE_CENTER_SELECTED_POSITION, this.circleCenterSelectedPosition);
        outState.putInt(CIRCLE_START_SELECTED_POSITION, this.circleStartSelectedPosition);
        outState.putInt(CIRCLE_END_SELECTED_POSITION, this.circleEndSelectedPosition);
        outState.putBoolean(IS_MODE_ARC_LENGTH, this.selectedMode == Mode.ARCLENGTH);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.circleCenterSelectedPosition = savedInstanceState.getInt(
                    CIRCLE_CENTER_SELECTED_POSITION);
            this.circleStartSelectedPosition = savedInstanceState.getInt(
                    CIRCLE_START_SELECTED_POSITION);
            this.circleEndSelectedPosition = savedInstanceState.getInt(
                    CIRCLE_END_SELECTED_POSITION);
            this.selectedMode = savedInstanceState.getBoolean(
                    IS_MODE_ARC_LENGTH) ? Mode.ARCLENGTH : Mode.SEGMENT;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.circular_segmentation, menu);
        return super.onCreateOptionsMenu(menu);
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
            Log.e(Logger.TOPOSUITE_INPUT_ERROR, "Unknown mode selected");
        }
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
        this.arcLengthRadio = (RadioButton) this.findViewById(R.id.mode_arc_length);
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
                if (p.getNumber() > 0) {
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
                if (p.getNumber() > 0) {
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
                if (p.getNumber() > 0) {
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
    }

    private enum Mode {
        SEGMENT,
        ARCLENGTH
    }
}
