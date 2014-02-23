package ch.hgdev.toposuite.calculation.activities.linesintersec;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
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
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class LinesIntersectionActivity extends TopoSuiteActivity {

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

    private RadioButton                    modeGisementD1Radio;
    private RadioButton                    modeGisementD2Radio;

    private int                            point1D1SelectedPosition;
    private int                            point2D1SelectedPosition;
    private int                            point1D2SelectedPosition;
    private int                            point2D2SelectedPosition;

    private LinesIntersectionActivity.Mode d1Mode;
    private LinesIntersectionActivity.Mode d2Mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_lines_intersection);

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

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.point1D1Spinner.setAdapter(a);
        this.point2D1Spinner.setAdapter(a);
        this.point1D2Spinner.setAdapter(a);
        this.point2D2Spinner.setAdapter(a);

        /*Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            LinesIntersection ppoal = (LinesIntersection)
                    SharedResources.getCalculationsHistory().get(position);

            this.point1SelectedPosition = a.getPosition(ppoal.getP1());
            this.point2SelectedPosition = a.getPosition(ppoal.getP2());
            this.pointSelectedPosition = a.getPosition(ppoal.getPtToProj());

            this.gisementEditText.setText(DisplayUtils.toString(
                    ppoal.getGisement()));

            this.displacementEditText.setText(DisplayUtils.toString(
                    ppoal.getDisplacement()));

            this.pointNumberEditText.setText(DisplayUtils.toString(
                    ppoal.getNumber()));
        }*/

        this.point1D1Spinner.setSelection(this.point1D1SelectedPosition);
        this.point2D1Spinner.setSelection(this.point2D1SelectedPosition);
        this.point1D2Spinner.setSelection(this.point1D2SelectedPosition);
        this.point2D2Spinner.setSelection(this.point2D2SelectedPosition);

        /*if (this.selectedMode == Mode.GISEMENT) {
            this.modeGisementRadio.callOnClick();
        } else {
            this.modeGisementRadio.callOnClick();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.lines_intersection, menu);
        return super.onCreateOptionsMenu(menu);
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
            break;
        case R.id.is_d2_perpendicular:
            this.distP1D2TexView.setEnabled(checked);
            this.distP1D2EditText.setEnabled(checked);
            break;
        }
    }

    private enum Mode {
        LINE,
        GISEMENT;
    }
}