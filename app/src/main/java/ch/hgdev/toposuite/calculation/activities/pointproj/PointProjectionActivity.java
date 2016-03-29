package ch.hgdev.toposuite.calculation.activities.pointproj;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine;
import ch.hgdev.toposuite.calculation.PointProjectionOnALine.Mode;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class PointProjectionActivity extends TopoSuiteActivity {
    public static final String          POINT_PROJ_POSITION       = "point_proj_position";
    public static final String          POINT_1_SELECTED_POSITION = "point_1_selected_position";
    public static final String          POINT_2_SELECTED_POSITION = "point_2_selected_position";
    public static final String          POINT_SELECTED_POSITION   = "point_selected_position";
    public static final String          DISPLACEMENT              = "displacement";
    public static final String          POINT_NUMBER              = "point_number";
    public static final String          GISEMENT                  = "gisement";
    public static final String          IS_MODE_LINE              = "is_mode_line";

    private Spinner                     point1Spinner;
    private Spinner                     point2Spinner;
    private Spinner                     pointSpinner;

    private TextView                    point1TextView;
    private TextView                    point2TextView;
    private TextView                    pointTextView;

    private EditText                    gisementEditText;
    private EditText                    displacementEditText;
    private EditText                    pointNumberEditText;

    private LinearLayout                point2SpinnerLayout;
    private LinearLayout                point2Layout;
    private LinearLayout                gisementLayout;

    private RadioButton                 modeGisementRadio;

    private int                         point1SelectedPosition;
    private int                         point2SelectedPosition;
    private int                         pointSelectedPosition;
    private PointProjectionOnALine.Mode selectedMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_point_projection);

        this.point1SelectedPosition = 0;
        this.point2SelectedPosition = 0;
        this.pointSelectedPosition = 0;

        if (this.selectedMode == null) {
            this.selectedMode = PointProjectionOnALine.Mode.LINE;
        }

        this.point1TextView = (TextView) this.findViewById(R.id.point_1);
        this.point2TextView = (TextView) this.findViewById(R.id.point_2);
        this.pointTextView = (TextView) this.findViewById(R.id.point);

        this.gisementEditText = (EditText) this.findViewById(R.id.gisement);
        this.gisementEditText.setInputType(App.getInputTypeCoordinate());

        this.displacementEditText = (EditText) this.findViewById(R.id.displacement);
        this.displacementEditText.setInputType(App.getInputTypeCoordinate());

        this.pointNumberEditText = (EditText) this.findViewById(R.id.point_number);

        this.point2SpinnerLayout = (LinearLayout) this.findViewById(R.id.point2_spinner_layout);
        this.point2Layout = (LinearLayout) this.findViewById(R.id.point2_layout);
        this.gisementLayout = (LinearLayout) this.findViewById(R.id.gisement_layout);

        this.modeGisementRadio = (RadioButton) this.findViewById(R.id.mode_gisement);

        this.point1Spinner = (Spinner) this.findViewById(R.id.point_1_spinner);
        this.point2Spinner = (Spinner) this.findViewById(R.id.point_2_spinner);
        this.pointSpinner = (Spinner) this.findViewById(R.id.point_spinner);

        this.point1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PointProjectionActivity.this.point1SelectedPosition = pos;

                Point pt = (Point) PointProjectionActivity.this.point1Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    PointProjectionActivity.this.point1TextView.setText(DisplayUtils.formatPoint(
                            PointProjectionActivity.this, pt));
                } else {
                    PointProjectionActivity.this.point1TextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.point2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PointProjectionActivity.this.point2SelectedPosition = pos;

                Point pt = (Point) PointProjectionActivity.this.point2Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    PointProjectionActivity.this.point2TextView.setText(DisplayUtils.formatPoint(
                            PointProjectionActivity.this, pt));
                } else {
                    PointProjectionActivity.this.point2TextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PointProjectionActivity.this.pointSelectedPosition = pos;

                Point pt = (Point) PointProjectionActivity.this.point1Spinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    PointProjectionActivity.this.pointTextView.setText(DisplayUtils.formatPoint(
                            PointProjectionActivity.this, pt));
                } else {
                    PointProjectionActivity.this.pointTextView.setText("");
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
        points.add(new Point(
                "", MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.point1Spinner.setAdapter(a);
        this.point2Spinner.setAdapter(a);
        this.pointSpinner.setAdapter(a);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            PointProjectionOnALine ppoal = (PointProjectionOnALine)
                    SharedResources.getCalculationsHistory().get(position);

            // this.point1SelectedPosition = a.getPosition(ppoal.getP1());
            // this.point2SelectedPosition = a.getPosition(ppoal.getP2());
            this.pointSelectedPosition = a.getPosition(ppoal.getPtToProj());

            // TODO find a more "elegant" solution
            for (int i = 1; i < a.getCount(); i++) {
                if ((ppoal.getP1() != null) && a.getItem(i).getNumber().equals(
                        ppoal.getP1().getNumber())) {
                    this.point1SelectedPosition = i;
                    continue;
                }

                if ((ppoal.getP2() != null) && a.getItem(i).getNumber().equals(
                        ppoal.getP2().getNumber())) {
                    this.point2SelectedPosition = i;
                    continue;
                }
            }

            this.gisementEditText.setText(DisplayUtils.toStringForEditText(
                    ppoal.getGisement()));

            this.displacementEditText.setText(DisplayUtils.toStringForEditText(
                    ppoal.getDisplacement()));

            this.pointNumberEditText.setText(ppoal.getNumber());
        }

        this.point1Spinner.setSelection(this.point1SelectedPosition);
        this.point2Spinner.setSelection(this.point2SelectedPosition);
        this.pointSpinner.setSelection(this.pointSelectedPosition);

        this.modeGisementRadio.callOnClick();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_point_projection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.point_projection, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PointProjectionActivity.POINT_1_SELECTED_POSITION,
                this.point1SelectedPosition);
        outState.putInt(PointProjectionActivity.POINT_2_SELECTED_POSITION,
                this.point2SelectedPosition);
        outState.putInt(PointProjectionActivity.POINT_SELECTED_POSITION,
                this.pointSelectedPosition);
        outState.putString(PointProjectionActivity.GISEMENT,
                this.gisementEditText.getText().toString());
        outState.putString(PointProjectionActivity.DISPLACEMENT,
                this.displacementEditText.getText().toString());
        outState.putString(PointProjectionActivity.POINT_NUMBER,
                this.pointNumberEditText.getText().toString());
        outState.putBoolean(PointProjectionActivity.IS_MODE_LINE,
                this.selectedMode == Mode.LINE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.point1SelectedPosition = savedInstanceState.getInt(
                    PointProjectionActivity.POINT_1_SELECTED_POSITION);
            this.point2SelectedPosition = savedInstanceState.getInt(
                    PointProjectionActivity.POINT_2_SELECTED_POSITION);
            this.pointSelectedPosition = savedInstanceState.getInt(
                    PointProjectionActivity.POINT_SELECTED_POSITION);
            this.gisementEditText.setText(savedInstanceState.getString(
                    PointProjectionActivity.GISEMENT));
            this.displacementEditText.setText(savedInstanceState.getString(
                    PointProjectionActivity.DISPLACEMENT));
            this.pointNumberEditText.setText(savedInstanceState.getString(
                    PointProjectionActivity.POINT_NUMBER));
            this.selectedMode = savedInstanceState.getBoolean(
                    PointProjectionActivity.IS_MODE_LINE) ? Mode.LINE : Mode.GISEMENT;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.run_calculation_button:
            if ((this.point1SelectedPosition == 0)
                    || (this.pointSelectedPosition == 0)
                    || (this.pointNumberEditText.length() == 0)
                    || ((this.selectedMode == Mode.LINE) && (this.point2SelectedPosition == 0))
                    || ((this.selectedMode == Mode.GISEMENT) &&
                    (this.gisementEditText.length() == 0))) {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                return true;
            }

            PointProjectionOnALine ppoal;

            Point p1 = (Point) this.point1Spinner.getItemAtPosition(
                    this.point1SelectedPosition);

            double displ = ViewUtils.readDouble(this.displacementEditText);
            String ptNumber = this.pointNumberEditText.getText().toString();

            Point p = (Point) this.pointSpinner.getItemAtPosition(
                    this.pointSelectedPosition);

            if (this.selectedMode == Mode.GISEMENT) {
                double gisement = ViewUtils.readDouble(this.gisementEditText);
                ppoal = new PointProjectionOnALine(ptNumber, p1, gisement, p, displ, true);
            } else {
                Point p2 = (Point) this.point2Spinner.getItemAtPosition(
                        this.point2SelectedPosition);
                ppoal = new PointProjectionOnALine(ptNumber, p1, p2, p, displ, true);
            }

            int position = SharedResources.getCalculationsHistory().indexOf(ppoal);

            Bundle bundle = new Bundle();
            bundle.putInt(PointProjectionActivity.POINT_SELECTED_POSITION, position);

            Intent resultsActivityIntent = new Intent(
                    this, PointProjectionResultActivity.class);
            resultsActivityIntent.putExtras(bundle);
            this.startActivity(resultsActivityIntent);

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
        case R.id.mode_gisement:
            if (checked) {
                this.point2SpinnerLayout.setVisibility(View.GONE);
                this.point2Layout.setVisibility(View.GONE);
                this.gisementLayout.setVisibility(View.VISIBLE);
                this.selectedMode = PointProjectionOnALine.Mode.GISEMENT;
            }
            break;
        case R.id.mode_line:
            if (checked) {
                this.point2SpinnerLayout.setVisibility(View.VISIBLE);
                this.point2Layout.setVisibility(View.VISIBLE);
                this.gisementLayout.setVisibility(View.GONE);
                this.selectedMode = PointProjectionOnALine.Mode.LINE;
            }
            break;
        default:
            Logger.log(Logger.ErrLabel.INPUT_ERROR, "Unknown mode selected");
        }
    }
}
