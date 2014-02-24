package ch.hgdev.toposuite.calculation.activities.linesintersec;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.InputType;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LinesIntersection;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

public class LinesIntersectionActivity extends TopoSuiteActivity {
    private static final String            LINES_INTERSEC_POSITION = "lines_intersec_position";
    private static final String            P1D1_SELECTED_POSITION  = "p1d1_selected_position";
    private static final String            P2D1_SELECTED_POSITION  = "p2d1_selected_position";
    private static final String            P1D2_SELECTED_POSITION  = "p1d2_selected_position";
    private static final String            P2D2_SELECTED_POSITION  = "p2d2_selected_position";

    private Spinner                        point1D1Spinner;
    private Spinner                        point2D1Spinner;
    private Spinner                        point1D2Spinner;
    private Spinner                        point2D2Spinner;

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
        this.gisementD1EditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.gisementD2EditText = (EditText) this.findViewById(R.id.gisement_d2);
        this.gisementD2EditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.displacementD1EditText = (EditText) this.findViewById(R.id.displacement_d1);
        this.displacementD1EditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.displacementD2EditText = (EditText) this.findViewById(R.id.displacement_d2);
        this.displacementD2EditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.pointNumberEditText = (EditText) this.findViewById(R.id.point_number);
        this.pointNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        this.distP1D1EditText = (EditText) this.findViewById(R.id.dist_p1_d1);
        this.distP1D1EditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.distP1D2EditText = (EditText) this.findViewById(R.id.dist_p1_d2);
        this.distP1D2EditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

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
                if (pt.getNumber() > 0) {
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
                if (pt.getNumber() > 0) {
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
                if (pt.getNumber() > 0) {
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
                if (pt.getNumber() > 0) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, true));
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

            this.point1D1SelectedPosition = this.adapter.getPosition(
                    this.lineIntersec.getP1D1());
            this.point2D1SelectedPosition = this.adapter.getPosition(
                    this.lineIntersec.getP2D1());
            this.point1D2SelectedPosition = this.adapter.getPosition(
                    this.lineIntersec.getP1D2());
            this.point2D2SelectedPosition = this.adapter.getPosition(
                    this.lineIntersec.getP2D2());

            if (!MathUtils.isZero(this.lineIntersec.getGisementD1())) {
                this.gisementD1EditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getGisementD1()));
            }

            if (!MathUtils.isZero(this.lineIntersec.getGisementD2())) {
                this.gisementD2EditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getGisementD2()));
            }

            if (!MathUtils.isZero(this.lineIntersec.getDisplacementD1())) {
                this.displacementD1EditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getDisplacementD1()));
            }

            if (!MathUtils.isZero(this.lineIntersec.getDisplacementD2())) {
                this.displacementD2EditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getDisplacementD2()));
            }

            if (!MathUtils.isZero(this.lineIntersec.getDistanceP1D1())) {
                this.distP1D1EditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getDistanceP1D1()));
                this.perpendicularD1CheckBox.setChecked(true);
            }

            if (!MathUtils.isZero(this.lineIntersec.getDistanceP1D2())) {
                this.distP1D2EditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getDistanceP1D2()));
                this.perpendicularD2CheckBox.setChecked(true);
            }

            if (this.lineIntersec.getPointNumber() != 0) {
                this.pointNumberEditText.setText(DisplayUtils.toString(
                        this.lineIntersec.getPointNumber()));
            }
        }

        this.point1D1Spinner.setSelection(this.point1D1SelectedPosition);
        this.point2D1Spinner.setSelection(this.point2D1SelectedPosition);
        this.point1D2Spinner.setSelection(this.point1D2SelectedPosition);
        this.point2D2Spinner.setSelection(this.point2D2SelectedPosition);

        if (this.d1Mode == Mode.GISEMENT) {
            this.modeGisementD1Radio.callOnClick();
        } else {
            this.modeGisementD1Radio.callOnClick();
        }

        if (this.d2Mode == Mode.GISEMENT) {
            this.modeGisementD2Radio.callOnClick();
        } else {
            this.modeGisementD2Radio.callOnClick();
        }
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
                this.lineIntersec.compute();
                this.displayResult();
            }

            this.point1D1SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P1D1_SELECTED_POSITION);
            this.point2D1SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P2D1_SELECTED_POSITION);
            this.point1D2SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P1D2_SELECTED_POSITION);
            this.point2D2SelectedPosition = savedInstanceState.getInt(
                    LinesIntersectionActivity.P2D2_SELECTED_POSITION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.run_calculation_button:
            if ((this.point1D1SelectedPosition == 0)
                    || (this.point1D2SelectedPosition == 0)
                    || ((this.d1Mode == Mode.GISEMENT) && (this.gisementD1EditText.length() == 0))
                    || ((this.d1Mode == Mode.LINE) && (this.point2D1SelectedPosition == 0))
                    || ((this.d2Mode == Mode.GISEMENT) && (this.gisementD2EditText.length() == 0))
                    || ((this.d2Mode == Mode.LINE) && (this.point2D2SelectedPosition == 0))
                    || (this.isD1Perpendicular && (this.distP1D1EditText.length() == 0))
                    || (this.isD2Perpendicular && (this.distP1D2EditText.length() == 0))) {
                Toast.makeText(this, R.string.error_fill_data, Toast.LENGTH_LONG).show();
                return true;
            }

            Point p1D1 = this.adapter.getItem(this.point1D1SelectedPosition);
            Point p2D1 = null;
            double gisementD1 = 0.0;
            if (this.d1Mode == Mode.GISEMENT) {
                gisementD1 = Double.parseDouble(
                        this.gisementD1EditText.getText().toString());
            } else {
                p2D1 = this.adapter.getItem(this.point2D1SelectedPosition);
            }
            double displacementD1 = 0.0;
            if (this.displacementD1EditText.length() > 0) {
                displacementD1 = Double.parseDouble(
                        this.displacementD1EditText.getText().toString());
            }
            double distP1D1 = 0.0;
            if ((this.distP1D1EditText.length() > 0) && this.isD1Perpendicular) {
                distP1D1 = Double.parseDouble(
                        this.distP1D1EditText.getText().toString());
            }

            Point p1D2 = this.adapter.getItem(this.point1D2SelectedPosition);
            Point p2D2 = null;
            double gisementD2 = 0.0;
            if (this.d2Mode == Mode.GISEMENT) {
                gisementD2 = Double.parseDouble(
                        this.gisementD2EditText.getText().toString());
            } else {
                p2D2 = this.adapter.getItem(this.point2D2SelectedPosition);
            }
            double displacementD2 = 0.0;
            if (this.displacementD2EditText.length() > 0) {
                displacementD2 = Double.parseDouble(
                        this.displacementD2EditText.getText().toString());
            }
            double distP1D2 = 0.0;
            if ((this.distP1D2EditText.length() > 0) && this.isD2Perpendicular) {
                distP1D2 = Double.parseDouble(
                        this.distP1D2EditText.getText().toString());
            }

            int pointNumber = 0;
            if (this.pointNumberEditText.length() > 0) {
                pointNumber = Integer.parseInt(
                        this.pointNumberEditText.getText().toString());
            }

            if (this.lineIntersec == null) {
                this.lineIntersec = new LinesIntersection(p1D1, p2D1, displacementD1, gisementD1,
                        distP1D1, p1D2, p2D2, displacementD2, gisementD2, distP1D2, pointNumber,
                        true);
            } else {
                this.lineIntersec.setP1D1(p1D1);
                this.lineIntersec.setP1D1(p2D1);
                this.lineIntersec.setDisplacementD1(displacementD1);
                this.lineIntersec.setGisementD1(gisementD1);
                this.lineIntersec.setDistanceP1D1(distP1D1);
                this.lineIntersec.setP1D2(p1D2);
                this.lineIntersec.setP2D2(p2D2);
                this.lineIntersec.setDisplacementD2(displacementD2);
                this.lineIntersec.setDistanceP1D2(distP1D2);
                this.lineIntersec.setPointNumber(pointNumber);
            }

            this.lineIntersec.compute();
            this.displayResult();

            return true;
        case R.id.save_point:
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
                break;
            }
        case R.id.mode_d2_gisement:
            if (checked) {
                this.point2D2SpinnerLayout.setVisibility(View.GONE);
                if (this.point2D1Layout != null) {
                    this.point2D2Layout.setVisibility(View.GONE);
                }
                this.gisementD2Layout.setVisibility(View.VISIBLE);
                this.d1Mode = LinesIntersectionActivity.Mode.GISEMENT;
                break;
            }
        case R.id.mode_d2_line:
            if (checked) {
                this.point2D2SpinnerLayout.setVisibility(View.VISIBLE);
                if (this.point2D1Layout != null) {
                    this.point2D2Layout.setVisibility(View.VISIBLE);
                }
                this.gisementD2Layout.setVisibility(View.GONE);
                this.d2Mode = LinesIntersectionActivity.Mode.LINE;
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
            break;
        case R.id.is_d2_perpendicular:
            this.distP1D2TexView.setEnabled(checked);
            this.distP1D2EditText.setEnabled(checked);
            this.isD2Perpendicular = checked;
            break;
        }
    }

    private void displayResult() {
        this.intersectionPointTextView.setText(DisplayUtils.formatPoint(
                this, this.lineIntersec.getIntersectionPoint()));
        this.resultLayout.setVisibility(View.VISIBLE);
    }

    private enum Mode {
        LINE,
        GISEMENT;
    }
}