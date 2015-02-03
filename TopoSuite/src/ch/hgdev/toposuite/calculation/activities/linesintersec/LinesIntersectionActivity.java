package ch.hgdev.toposuite.calculation.activities.linesintersec;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.calculation.LinesIntersection;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

@SuppressLint("NewApi")
public class LinesIntersectionActivity extends TopoSuiteActivity implements
MergePointsDialog.MergePointsDialogListener {
    private static final String            LINES_INTERSEC_POSITION = "lines_intersec_position";
    private static final String            P1D1_SELECTED_POSITION  = "p1d1_selected_position";
    private static final String            P2D1_SELECTED_POSITION  = "p2d1_selected_position";
    private static final String            P1D2_SELECTED_POSITION  = "p1d2_selected_position";
    private static final String            P2D2_SELECTED_POSITION  = "p2d2_selected_position";
    private static final String            D1_IS_PERPENDICULAR     = "d1_is_perpendicular";
    private static final String            D2_IS_PERPENDICULAR     = "d2_is_perpendicular";

    private Spinner                        point1D1Spinner;
    private Spinner                        point2D1Spinner;
    private Spinner                        point1D2Spinner;
    private Spinner                        point2D2Spinner;

    private TextView                       displacementD1TextView;
    private TextView                       displacementD2TextView;
    private TextView                       point1D1TextView;
    private TextView                       point2D1TextView;
    private TextView                       point1D2TextView;
    private TextView                       point2D2TextView;
    private TextView                       distP1D1TexView;
    private TextView                       distP1D2TexView;
    private TextView                       intersectionPointTextView;

    private EditText                       gisementD1EditText;
    private EditText                       gisementD2EditText;
    private EditText                       displacementD1EditText;
    private EditText                       displacementD2EditText;
    private EditText                       pointNumberEditText;
    private EditText                       distP1D1EditText;
    private EditText                       distP1D2EditText;

    private LinearLayout                   point2D1SpinnerLayout;
    private LinearLayout                   point2D2SpinnerLayout;
    private LinearLayout                   point2D1Layout;
    private LinearLayout                   point2D2Layout;
    private LinearLayout                   gisementD1Layout;
    private LinearLayout                   gisementD2Layout;
    private LinearLayout                   resultLayout;

    private RadioButton                    modeGisementD1Radio;
    private RadioButton                    modeGisementD2Radio;

    private CheckBox                       perpendicularD1CheckBox;
    private CheckBox                       perpendicularD2CheckBox;

    private ScrollView                     scrollView;

    private ArrayAdapter<Point>            adapter;

    private int                            point1D1SelectedPosition;
    private int                            point2D1SelectedPosition;
    private int                            point1D2SelectedPosition;
    private int                            point2D2SelectedPosition;

    private boolean                        isD1Perpendicular;
    private boolean                        isD2Perpendicular;

    private LinesIntersectionActivity.Mode d1Mode;
    private LinesIntersectionActivity.Mode d2Mode;

    private LinesIntersection              lineIntersec;

    private AnimationDrawable              blinkAnimation;

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_lines_intersection);

        this.isD1Perpendicular = false;
        this.isD2Perpendicular = false;

        this.d1Mode = LinesIntersectionActivity.Mode.LINE;
        this.d2Mode = LinesIntersectionActivity.Mode.LINE;

        this.point1D1SelectedPosition = 0;
        this.point2D1SelectedPosition = 0;

        this.point1D2SelectedPosition = 0;
        this.point2D2SelectedPosition = 0;

        this.scrollView = (ScrollView) this.findViewById(R.id.scroll_view);

        this.point1D1TextView = (TextView) this.findViewById(R.id.point_1_d1);
        this.point2D1TextView = (TextView) this.findViewById(R.id.point_2_d1);
        this.point1D2TextView = (TextView) this.findViewById(R.id.point_1_d2);
        this.point2D2TextView = (TextView) this.findViewById(R.id.point_2_d2);
        this.distP1D1TexView = (TextView) this.findViewById(R.id.dist_p1_d1_label);
        this.distP1D2TexView = (TextView) this.findViewById(R.id.dist_p1_d2_label);
        this.intersectionPointTextView = (TextView) this.findViewById(
                R.id.intersection_point);

        this.perpendicularD1CheckBox = (CheckBox) this.findViewById(
                R.id.is_d1_perpendicular);
        this.perpendicularD2CheckBox = (CheckBox) this.findViewById(
                R.id.is_d2_perpendicular);

        if (this.perpendicularD1CheckBox.isChecked()) {
            this.distP1D1EditText.setEnabled(true);
            this.distP1D1TexView.setEnabled(true);
        }

        if (this.perpendicularD2CheckBox.isChecked()) {
            this.distP1D2EditText.setEnabled(true);
            this.distP1D2TexView.setEnabled(true);
        }

        this.gisementD1EditText = (EditText) this.findViewById(R.id.gisement_d1);
        this.gisementD1EditText.setInputType(App.getInputTypeCoordinate());

        this.gisementD2EditText = (EditText) this.findViewById(R.id.gisement_d2);
        this.gisementD2EditText.setInputType(App.getInputTypeCoordinate());

        this.displacementD1TextView = (TextView) this.findViewById(R.id.displacement_d1_label);
        this.displacementD1EditText = (EditText) this.findViewById(R.id.displacement_d1);
        this.displacementD1EditText.setInputType(App.getInputTypeCoordinate());

        this.displacementD2TextView = (TextView) this.findViewById(R.id.displacement_d2_label);
        this.displacementD2EditText = (EditText) this.findViewById(R.id.displacement_d2);
        this.displacementD2EditText.setInputType(App.getInputTypeCoordinate());

        this.pointNumberEditText = (EditText) this.findViewById(R.id.point_number);

        this.distP1D1EditText = (EditText) this.findViewById(R.id.dist_p1_d1);
        this.distP1D1EditText.setInputType(App.getInputTypeCoordinate());
        this.distP1D1EditText.setText("0.0");

        this.distP1D2EditText = (EditText) this.findViewById(R.id.dist_p1_d2);
        this.distP1D2EditText.setInputType(App.getInputTypeCoordinate());
        this.distP1D2EditText.setText("0.0");

        this.point2D1SpinnerLayout = (LinearLayout) this
                .findViewById(R.id.point2_d1_spinner_layout);
        if (this.findViewById(R.id.point2_d1_layout) != null) {
            // this layout does not exists in landscape mode
            this.point2D1Layout = (LinearLayout) this.findViewById(R.id.point2_d1_layout);
        }
        this.gisementD1Layout = (LinearLayout) this.findViewById(R.id.gisement_d1_layout);

        this.point2D2SpinnerLayout = (LinearLayout) this
                .findViewById(R.id.point2_d2__spinner_layout);
        if (this.findViewById(R.id.point2_d2_layout) != null) {
            // this layout does not exists in landscape mode
            this.point2D2Layout = (LinearLayout) this.findViewById(R.id.point2_d2_layout);
        }
        this.gisementD2Layout = (LinearLayout) this.findViewById(R.id.gisement_d2_layout);
        this.resultLayout = (LinearLayout) this.findViewById(R.id.result_layout);

        this.modeGisementD1Radio = (RadioButton) this.findViewById(R.id.mode_d1_gisement);
        this.modeGisementD2Radio = (RadioButton) this.findViewById(R.id.mode_d2_gisement);

        this.point1D1Spinner = (Spinner) this.findViewById(R.id.point_1_d1_spinner);
        this.point2D1Spinner = (Spinner) this.findViewById(R.id.point_2_d1_spinner);

        this.point1D2Spinner = (Spinner) this.findViewById(R.id.point_1_d2_spinner);
        this.point2D2Spinner = (Spinner) this.findViewById(R.id.point_2_d2_spinner);

        this.point1D1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LinesIntersectionActivity.this.point1D1SelectedPosition = pos;

                Point pt = (Point) LinesIntersectionActivity.this.point1D1Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LinesIntersectionActivity.this.point1D1TextView.setText(DisplayUtils
                            .formatPoint(
                                    LinesIntersectionActivity.this, pt));
                } else {
                    LinesIntersectionActivity.this.point1D1TextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.point2D1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LinesIntersectionActivity.this.point2D1SelectedPosition = pos;

                Point pt = (Point) LinesIntersectionActivity.this.point2D1Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LinesIntersectionActivity.this.point2D1TextView.setText(DisplayUtils
                            .formatPoint(
                                    LinesIntersectionActivity.this, pt));
                } else {
                    LinesIntersectionActivity.this.point2D1TextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.point1D2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LinesIntersectionActivity.this.point1D2SelectedPosition = pos;

                Point pt = (Point) LinesIntersectionActivity.this.point1D2Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LinesIntersectionActivity.this.point1D2TextView.setText(DisplayUtils
                            .formatPoint(
                                    LinesIntersectionActivity.this, pt));
                } else {
                    LinesIntersectionActivity.this.point1D2TextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.point2D2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LinesIntersectionActivity.this.point2D2SelectedPosition = pos;

                Point pt = (Point) LinesIntersectionActivity.this.point2D2Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LinesIntersectionActivity.this.point2D2TextView.setText(DisplayUtils
                            .formatPoint(
                                    LinesIntersectionActivity.this, pt));
                } else {
                    LinesIntersectionActivity.this.point2D2TextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        // setup blink animation
        this.blinkAnimation = new AnimationDrawable();
        this.blinkAnimation.addFrame(this.getResources()
                .getDrawable(android.R.color.holo_blue_light), 900);
        this.blinkAnimation.addFrame(this.getResources()
                .getDrawable(android.R.color.transparent), 900);
        this.blinkAnimation.setOneShot(true);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.resultLayout.setBackgroundDrawable(this.blinkAnimation);
        } else {
            this.resultLayout.setBackground(this.blinkAnimation);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        this.adapter = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.point1D1Spinner.setAdapter(this.adapter);
        this.point2D1Spinner.setAdapter(this.adapter);
        this.point1D2Spinner.setAdapter(this.adapter);
        this.point2D2Spinner.setAdapter(this.adapter);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.lineIntersec = (LinesIntersection)
                    SharedResources.getCalculationsHistory().get(position);

            Point tmpPoint = SharedResources.getSetOfPoints().find(
                    this.lineIntersec.getP1D1().getNumber());
            this.point1D1SelectedPosition = this.adapter.getPosition(tmpPoint);

            if (this.lineIntersec.getP2D1() != null) {
                tmpPoint = SharedResources.getSetOfPoints().find(
                        this.lineIntersec.getP2D1().getNumber());
                this.point2D1SelectedPosition = this.adapter.getPosition(tmpPoint);
            }

            tmpPoint = SharedResources.getSetOfPoints().find(
                    this.lineIntersec.getP1D2().getNumber());
            this.point1D2SelectedPosition = this.adapter.getPosition(tmpPoint);

            if (this.lineIntersec.getP2D2() != null) {
                tmpPoint = SharedResources.getSetOfPoints().find(
                        this.lineIntersec.getP2D2().getNumber());
                this.point2D2SelectedPosition = this.adapter.getPosition(tmpPoint);
            }

            if (!MathUtils.isZero(this.lineIntersec.getGisementD1())) {
                this.gisementD1EditText.setText(DisplayUtils.toStringForEditText(
                        this.lineIntersec.getGisementD1()));

                if (this.point2D1SelectedPosition == 0) {
                    RadioButton rb = (RadioButton) this.findViewById(
                            R.id.mode_d1_gisement);
                    rb.setChecked(true);
                }
            }

            if (!MathUtils.isZero(this.lineIntersec.getGisementD2())) {
                this.gisementD2EditText.setText(DisplayUtils.toStringForEditText(
                        this.lineIntersec.getGisementD2()));

                if (this.point2D2SelectedPosition == 0) {
                    RadioButton rb = (RadioButton) this.findViewById(
                            R.id.mode_d2_gisement);
                    rb.setChecked(true);
                }
            }

            if (!MathUtils.isZero(this.lineIntersec.getDisplacementD1())) {
                this.displacementD1EditText.setText(DisplayUtils.toStringForEditText(
                        this.lineIntersec.getDisplacementD1()));
            }
            this.displacementD1TextView.setEnabled(!this.isD1Perpendicular);
            this.displacementD1EditText.setEnabled(!this.isD1Perpendicular);

            if (!MathUtils.isZero(this.lineIntersec.getDisplacementD2())) {
                this.displacementD2EditText.setText(DisplayUtils.toStringForEditText(
                        this.lineIntersec.getDisplacementD2()));
            }
            this.displacementD2TextView.setEnabled(!this.isD2Perpendicular);
            this.displacementD2EditText.setEnabled(!this.isD2Perpendicular);

            if (!MathUtils.isZero(this.lineIntersec.getDistanceP1D1())) {
                this.distP1D1EditText.setText(DisplayUtils.toStringForEditText(
                        this.lineIntersec.getDistanceP1D1()));
                this.perpendicularD1CheckBox.setChecked(true);
                this.distP1D1EditText.setEnabled(true);
                this.distP1D1TexView.setEnabled(true);
                this.isD1Perpendicular = true;
            }

            if (!MathUtils.isZero(this.lineIntersec.getDistanceP1D2())) {
                this.distP1D2EditText.setText(DisplayUtils.toStringForEditText(
                        this.lineIntersec.getDistanceP1D2()));
                this.perpendicularD2CheckBox.setChecked(true);
                this.distP1D2EditText.setEnabled(true);
                this.distP1D2TexView.setEnabled(true);
                this.isD2Perpendicular = true;
            }

            this.pointNumberEditText.setText(
                    this.lineIntersec.getPointNumber());

        }

        this.point1D1Spinner.setSelection(this.point1D1SelectedPosition);
        this.point2D1Spinner.setSelection(this.point2D1SelectedPosition);
        this.point1D2Spinner.setSelection(this.point1D2SelectedPosition);
        this.point2D2Spinner.setSelection(this.point2D2SelectedPosition);

        this.modeGisementD1Radio.callOnClick();
        this.modeGisementD2Radio.callOnClick();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_lines_intersection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.lines_intersection, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int position = -1;
        if (this.lineIntersec != null) {
            position = SharedResources.getCalculationsHistory().indexOf(
                    this.lineIntersec);
        }

        outState.putInt(LinesIntersectionActivity.LINES_INTERSEC_POSITION,
                position);
        outState.putInt(LinesIntersectionActivity.P1D1_SELECTED_POSITION,
                this.point1D1SelectedPosition);
        outState.putInt(LinesIntersectionActivity.P2D1_SELECTED_POSITION,
                this.point2D1SelectedPosition);
        outState.putInt(LinesIntersectionActivity.P1D2_SELECTED_POSITION,
                this.point1D2SelectedPosition);
        outState.putInt(LinesIntersectionActivity.P2D2_SELECTED_POSITION,
                this.point2D2SelectedPosition);
        outState.putBoolean(LinesIntersectionActivity.D1_IS_PERPENDICULAR,
                this.isD1Perpendicular);
        outState.putBoolean(LinesIntersectionActivity.D2_IS_PERPENDICULAR,
                this.isD2Perpendicular);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(
                    LinesIntersectionActivity.LINES_INTERSEC_POSITION);
            if (position != -1) {
                this.lineIntersec = (LinesIntersection) SharedResources
                        .getCalculationsHistory().get(position);
                this.resultLayout.setVisibility(View.VISIBLE);
            }

            this.point1D1SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P1D1_SELECTED_POSITION);
            this.point2D1SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P2D1_SELECTED_POSITION);
            this.point1D2SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P1D2_SELECTED_POSITION);
            this.point2D2SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P2D2_SELECTED_POSITION);
            this.isD1Perpendicular = savedInstanceState.getBoolean(
                    LinesIntersectionActivity.D1_IS_PERPENDICULAR);
            this.isD2Perpendicular = savedInstanceState.getBoolean(
                    LinesIntersectionActivity.D2_IS_PERPENDICULAR);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.run_calculation_button:
            this.runCalculation();
            return true;
        case R.id.save_point:
            // check if the user has supplied a point number
            if ((this.lineIntersec == null) || (this.pointNumberEditText.length() == 0)) {
                ViewUtils.showToast(this, this.getString(R.string.error_no_points_to_save));
                return true;
            }

            // make sure that the computation has been done before
            this.runCalculation();

            if (MathUtils.isZero(this.lineIntersec.getIntersectionPoint().getEast())
                    || MathUtils.isZero(this.lineIntersec.getIntersectionPoint().getNorth())) {
                ViewUtils.showToast(this, this.getString(R.string.error_no_points_to_save));
                return true;
            }

            if (SharedResources.getSetOfPoints().find(
                    this.lineIntersec.getPointNumber()) == null) {
                SharedResources.getSetOfPoints().add(
                        this.lineIntersec.getIntersectionPoint());
                this.lineIntersec.getIntersectionPoint().registerDAO(
                        PointsDataSource.getInstance());

                ViewUtils.showToast(this, this.getString(R.string.point_add_success));
            } else {
                // this point already exists
                MergePointsDialog dialog = new MergePointsDialog();

                Bundle args = new Bundle();
                args.putString(
                        MergePointsDialog.POINT_NUMBER,
                        this.lineIntersec.getPointNumber());

                args.putDouble(MergePointsDialog.NEW_EAST,
                        this.lineIntersec.getIntersectionPoint().getEast());
                args.putDouble(MergePointsDialog.NEW_NORTH,
                        this.lineIntersec.getIntersectionPoint().getNorth());
                args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                        this.lineIntersec.getIntersectionPoint().getAltitude());

                dialog.setArguments(args);
                dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
        case R.id.mode_d1_gisement:
            if (checked) {
                this.point2D1SpinnerLayout.setVisibility(View.GONE);
                if (this.point2D1Layout != null) {
                    this.point2D1Layout.setVisibility(View.GONE);
                }
                this.gisementD1Layout.setVisibility(View.VISIBLE);
                this.d1Mode = LinesIntersectionActivity.Mode.GISEMENT;

                this.perpendicularD1CheckBox.setEnabled(false);
                this.perpendicularD1CheckBox.setChecked(false);
                this.distP1D1TexView.setEnabled(false);
                this.distP1D1EditText.setEnabled(false);
                break;
            }
        case R.id.mode_d1_line:
            if (checked) {
                this.point2D1SpinnerLayout.setVisibility(View.VISIBLE);
                if (this.point2D1Layout != null) {
                    this.point2D1Layout.setVisibility(View.VISIBLE);
                }
                this.gisementD1Layout.setVisibility(View.GONE);
                this.d1Mode = LinesIntersectionActivity.Mode.LINE;
                this.perpendicularD1CheckBox.setEnabled(true);
                this.distP1D1TexView.setEnabled(true);
                this.distP1D1EditText.setEnabled(true);
                break;
            }
        case R.id.mode_d2_gisement:
            if (checked) {
                this.point2D2SpinnerLayout.setVisibility(View.GONE);
                if (this.point2D2Layout != null) {
                    this.point2D2Layout.setVisibility(View.GONE);
                }
                this.gisementD2Layout.setVisibility(View.VISIBLE);
                this.d2Mode = LinesIntersectionActivity.Mode.GISEMENT;
                this.perpendicularD2CheckBox.setEnabled(false);
                this.perpendicularD2CheckBox.setChecked(false);
                this.distP1D2TexView.setEnabled(false);
                this.distP1D2EditText.setEnabled(false);
                break;
            }
        case R.id.mode_d2_line:
            if (checked) {
                this.point2D2SpinnerLayout.setVisibility(View.VISIBLE);
                if (this.point2D2Layout != null) {
                    this.point2D2Layout.setVisibility(View.VISIBLE);
                }
                this.gisementD2Layout.setVisibility(View.GONE);
                this.d2Mode = LinesIntersectionActivity.Mode.LINE;
                this.perpendicularD2CheckBox.setEnabled(true);
                this.distP1D2TexView.setEnabled(true);
                this.distP1D2EditText.setEnabled(true);
                break;
            }
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
        case R.id.is_d1_perpendicular:
            this.distP1D1TexView.setEnabled(checked);
            this.distP1D1EditText.setEnabled(checked);
            this.isD1Perpendicular = checked;
            this.displacementD1EditText.setEnabled(!checked);
            this.displacementD1TextView.setEnabled(!checked);
            break;
        case R.id.is_d2_perpendicular:
            this.distP1D2TexView.setEnabled(checked);
            this.distP1D2EditText.setEnabled(checked);
            this.isD2Perpendicular = checked;
            this.displacementD2EditText.setEnabled(!checked);
            this.displacementD2TextView.setEnabled(!checked);
            break;
        }
    }

    private void displayResult() {
        this.blinkAnimation.stop();
        this.intersectionPointTextView.setText(DisplayUtils.formatPoint(
                this, this.lineIntersec.getIntersectionPoint()));
        this.resultLayout.setVisibility(View.VISIBLE);
        this.scrollView.scrollTo(0, 0);
        this.blinkAnimation.start();
    }

    private enum Mode {
        LINE,
        GISEMENT;
    }

    @Override
    public void onMergePointsDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onMergePointsDialogError(String message) {
        ViewUtils.showToast(this, message);
    }

    private boolean isComputable() {
        if (this.point1D1SelectedPosition == 0) {
            return false;
        }

        if (this.point1D2SelectedPosition == 0) {
            return false;
        }

        if ((this.d1Mode == Mode.GISEMENT) && (this.gisementD1EditText.length() == 0)) {
            return false;
        }

        if ((this.d1Mode == Mode.LINE) && (this.point2D1SelectedPosition == 0)) {
            return false;
        }

        if ((this.d2Mode == Mode.GISEMENT) && (this.gisementD2EditText.length() == 0)) {
            return false;
        }

        if ((this.d2Mode == Mode.LINE) && (this.point2D2SelectedPosition == 0)) {
            return false;
        }

        if (this.isD1Perpendicular && (this.distP1D1EditText.length() == 0)) {
            return false;
        }

        if (this.isD2Perpendicular && (this.distP1D2EditText.length() == 0)) {
            return false;
        }

        // check that the two "gisements" are different, in other
        double g1 = ViewUtils.readDouble(this.gisementD1EditText);
        double g2 = ViewUtils.readDouble(this.gisementD2EditText);

        if (this.d1Mode != Mode.GISEMENT) {
            g1 = new Gisement(
                    this.adapter.getItem(this.point1D1SelectedPosition),
                    this.adapter.getItem(this.point2D1SelectedPosition)
                    ).getGisement();
        }

        if (this.d2Mode != Mode.GISEMENT) {
            g2 = new Gisement(
                    this.adapter.getItem(this.point1D2SelectedPosition),
                    this.adapter.getItem(this.point2D2SelectedPosition)
                    ).getGisement();
        }

        if ((MathUtils.roundWithTolerance(g1 - g2, 0.001) % 200) == 0) {
            return false;
        }

        if (this.isD1Perpendicular && (this.displacementD1EditText.length() > 0)) {
            return false;
        }

        if (this.isD2Perpendicular && (this.displacementD2EditText.length() > 0)) {
            return false;
        }

        return true;
    }

    private final void runCalculation() {
        if (!this.isComputable()) {
            ViewUtils.showToast(this, this.getString(R.string.error_impossible_calculation));
            return;
        }

        Point p1D1 = this.adapter.getItem(this.point1D1SelectedPosition);

        Point p2D1 = null;
        double gisementD1 = MathUtils.IGNORE_DOUBLE;
        if (this.d1Mode == Mode.GISEMENT) {
            gisementD1 = ViewUtils.readDouble(this.gisementD1EditText);
        } else {
            p2D1 = this.adapter.getItem(this.point2D1SelectedPosition);
        }

        double displacementD1 = MathUtils.IGNORE_DOUBLE;
        if (this.displacementD1EditText.length() > 0) {
            displacementD1 = ViewUtils.readDouble(this.displacementD1EditText);
        }

        double distP1D1 = MathUtils.IGNORE_DOUBLE;
        if ((this.distP1D1EditText.length() > 0) && this.isD1Perpendicular) {
            distP1D1 = ViewUtils.readDouble(this.distP1D1EditText);
        }

        Point p1D2 = this.adapter.getItem(this.point1D2SelectedPosition);

        Point p2D2 = null;
        double gisementD2 = MathUtils.IGNORE_DOUBLE;
        if (this.d2Mode == Mode.GISEMENT) {
            gisementD2 = ViewUtils.readDouble(this.gisementD2EditText);
        } else {
            p2D2 = this.adapter.getItem(this.point2D2SelectedPosition);
        }

        double displacementD2 = MathUtils.IGNORE_DOUBLE;
        if (this.displacementD2EditText.length() > 0) {
            displacementD2 = ViewUtils.readDouble(this.displacementD2EditText);
        }

        double distP1D2 = MathUtils.IGNORE_DOUBLE;
        if ((this.distP1D2EditText.length() > 0) && this.isD2Perpendicular) {
            distP1D2 = ViewUtils.readDouble(this.distP1D2EditText);
        }

        String pointNumber = this.pointNumberEditText.getText().toString();

        if (this.lineIntersec == null) {
            this.lineIntersec = new LinesIntersection(p1D1, p2D1, displacementD1, gisementD1,
                    distP1D1, p1D2, p2D2, displacementD2, gisementD2, distP1D2, pointNumber,
                    true);
        } else {
            this.lineIntersec.setP1D1(p1D1);
            this.lineIntersec.setP2D1(p2D1);
            this.lineIntersec.setGisementD1(gisementD1);
            this.lineIntersec.setDisplacementD1(displacementD1);
            this.lineIntersec.setDistanceP1D1(distP1D1);

            this.lineIntersec.setP1D2(p1D2);
            this.lineIntersec.setP2D2(p2D2);
            this.lineIntersec.setGisementD2(gisementD2);
            this.lineIntersec.setDisplacementD2(displacementD2);
            this.lineIntersec.setDistanceP1D2(distP1D2);

            this.lineIntersec.setPointNumber(pointNumber);
        }

        if ((this.lineIntersec.getP2D2() == null) || (this.lineIntersec.getP2D1() == null)) {
            ViewUtils.showToast(this, this.getString(
                    R.string.error_impossible_calculation));
            return;
        }

        try {
            this.lineIntersec.compute();
        } catch (CalculationException e) {
            ViewUtils.showToast(this, e.getMessage());

            // avoid to display the results
            return;
        }
        this.displayResult();
    }
}