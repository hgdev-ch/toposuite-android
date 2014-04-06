package ch.hgdev.toposuite.calculation.activities.limdispl;

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
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LimitDisplacement;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class LimitDisplacementActivity extends TopoSuiteActivity {
    public final static String  LIMIT_DISPLACEMENT_POSITION = "limit_displacement_position";
    private final static String POINT_A_SELECTED_POSITION   = "point_a_selected_position";
    private final static String POINT_B_SELECTED_POSITION   = "point_b_selected_position";
    private final static String POINT_C_SELECTED_POSITION   = "point_c_selected_position";
    private final static String POINT_D_SELECTED_POSITION   = "point_d_selected_position";

    private TextView            pointATextView;
    private TextView            pointBTextView;
    private TextView            pointCTextView;
    private TextView            pointDTextView;

    private Spinner             pointASpinner;
    private Spinner             pointBSpinner;
    private Spinner             pointCSpinner;
    private Spinner             pointDSpinner;

    private EditText            imposedSurfaceEditText;
    private EditText            pointWestNumberEditText;
    private EditText            pointEastNumberEditText;

    private int                 pointASelectedPosition;
    private int                 pointBSelectedPosition;
    private int                 pointCSelectedPosition;
    private int                 pointDSelectedPosition;

    private LimitDisplacement   limDispl;
    private ArrayAdapter<Point> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_limit_displacement);

        this.pointASelectedPosition = 0;
        this.pointBSelectedPosition = 0;
        this.pointCSelectedPosition = 0;
        this.pointDSelectedPosition = 0;

        this.pointATextView = (TextView) this.findViewById(R.id.point_a);
        this.pointBTextView = (TextView) this.findViewById(R.id.point_b);
        this.pointCTextView = (TextView) this.findViewById(R.id.point_c);
        this.pointDTextView = (TextView) this.findViewById(R.id.point_d);

        this.pointASpinner = (Spinner) this.findViewById(R.id.point_a_spinner);
        this.pointBSpinner = (Spinner) this.findViewById(R.id.point_b_spinner);
        this.pointCSpinner = (Spinner) this.findViewById(R.id.point_c_spinner);
        this.pointDSpinner = (Spinner) this.findViewById(R.id.point_d_spinner);

        this.pointASpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LimitDisplacementActivity.this.pointASelectedPosition = pos;

                Point pt = (Point) LimitDisplacementActivity.this.pointASpinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LimitDisplacementActivity.this.pointATextView.setText(
                            DisplayUtils.formatPoint(LimitDisplacementActivity.this, pt));
                } else {
                    LimitDisplacementActivity.this.pointATextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing
            }
        });

        this.pointBSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LimitDisplacementActivity.this.pointBSelectedPosition = pos;

                Point pt = (Point) LimitDisplacementActivity.this.pointBSpinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LimitDisplacementActivity.this.pointBTextView.setText(
                            DisplayUtils.formatPoint(LimitDisplacementActivity.this, pt));
                } else {
                    LimitDisplacementActivity.this.pointBTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing
            }
        });

        this.pointCSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LimitDisplacementActivity.this.pointCSelectedPosition = pos;

                Point pt = (Point) LimitDisplacementActivity.this.pointCSpinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LimitDisplacementActivity.this.pointCTextView.setText(
                            DisplayUtils.formatPoint(LimitDisplacementActivity.this, pt));
                } else {
                    LimitDisplacementActivity.this.pointCTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing
            }
        });

        this.pointDSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LimitDisplacementActivity.this.pointDSelectedPosition = pos;

                Point pt = (Point) LimitDisplacementActivity.this.pointDSpinner
                        .getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    LimitDisplacementActivity.this.pointDTextView.setText(
                            DisplayUtils.formatPoint(LimitDisplacementActivity.this, pt));
                } else {
                    LimitDisplacementActivity.this.pointDTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing
            }
        });

        this.imposedSurfaceEditText = (EditText) this.findViewById(
                R.id.imposed_surface);
        this.pointWestNumberEditText = (EditText) this.findViewById(R.id.point_number_west);
        this.pointEastNumberEditText = (EditText) this.findViewById(R.id.point_number_east);

        this.imposedSurfaceEditText.setInputType(App.getInputTypeCoordinate());

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.limDispl = (LimitDisplacement) SharedResources.getCalculationsHistory().get(
                    position);

            this.imposedSurfaceEditText.setText(
                    DisplayUtils.toStringForEditText(this.limDispl.getSurface()));
            this.pointWestNumberEditText.setText(
                    String.valueOf(this.limDispl.getPointXNumber()));
            this.pointEastNumberEditText.setText(
                    String.valueOf(this.limDispl.getPointYNumber()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(
                "", MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, true));
        points.addAll(SharedResources.getSetOfPoints());

        this.adapter = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.pointASpinner.setAdapter(this.adapter);
        this.pointBSpinner.setAdapter(this.adapter);
        this.pointCSpinner.setAdapter(this.adapter);
        this.pointDSpinner.setAdapter(this.adapter);

        if (this.limDispl != null) {
            this.pointASelectedPosition = this.adapter.getPosition(
                    this.limDispl.getPointA());
            this.pointBSelectedPosition = this.adapter.getPosition(
                    this.limDispl.getPointB());
            this.pointCSelectedPosition = this.adapter.getPosition(
                    this.limDispl.getPointC());
            this.pointDSelectedPosition = this.adapter.getPosition(
                    this.limDispl.getPointD());
        }

        this.pointASpinner.setSelection(this.pointASelectedPosition);
        this.pointBSpinner.setSelection(this.pointBSelectedPosition);
        this.pointCSpinner.setSelection(this.pointCSelectedPosition);
        this.pointDSpinner.setSelection(this.pointDSelectedPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.limit_displacement, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.run_calculation_button:
            if ((this.pointASelectedPosition == 0)
                    || (this.pointBSelectedPosition == 0)
                    || (this.pointCSelectedPosition == 0)
                    || (this.pointDSelectedPosition == 0)
                    || (this.pointWestNumberEditText.getText().toString().isEmpty())
                    || (this.pointEastNumberEditText.getText().toString().isEmpty())
                    || (ViewUtils.readDouble(
                            this.imposedSurfaceEditText) == MathUtils.IGNORE_DOUBLE)) {
                ViewUtils.showToast(this,
                        this.getText(R.string.error_fill_data));
                return true;
            }

            Point pointA = this.adapter.getItem(this.pointASelectedPosition);
            Point pointB = this.adapter.getItem(this.pointBSelectedPosition);
            Point pointC = this.adapter.getItem(this.pointCSelectedPosition);
            Point pointD = this.adapter.getItem(this.pointDSelectedPosition);
            double surface = ViewUtils.readDouble(this.imposedSurfaceEditText);
            String pointXNumber = this.pointWestNumberEditText.getText().toString();
            String pointYNumber = this.pointEastNumberEditText.getText().toString();

            if (this.limDispl == null) {
                this.limDispl = new LimitDisplacement(
                        pointA, pointB, pointC, pointD, surface,
                        pointXNumber, pointYNumber, true);
            } else {
                this.limDispl.setPointA(pointA);
                this.limDispl.setPointB(pointB);
                this.limDispl.setPointC(pointC);
                this.limDispl.setPointD(pointD);
                this.limDispl.setSurface(surface);
                this.limDispl.setPointXNumber(pointXNumber);
                this.limDispl.setPointYNumber(pointYNumber);
            }

            this.startLimitDisplacementResultsActivity();

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LimitDisplacementActivity.POINT_A_SELECTED_POSITION,
                this.pointASelectedPosition);
        outState.putInt(LimitDisplacementActivity.POINT_B_SELECTED_POSITION,
                this.pointBSelectedPosition);
        outState.putInt(LimitDisplacementActivity.POINT_C_SELECTED_POSITION,
                this.pointCSelectedPosition);
        outState.putInt(LimitDisplacementActivity.POINT_D_SELECTED_POSITION,
                this.pointDSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.pointASelectedPosition = savedInstanceState.getInt(
                    LimitDisplacementActivity.POINT_A_SELECTED_POSITION);
            this.pointBSelectedPosition = savedInstanceState.getInt(
                    LimitDisplacementActivity.POINT_B_SELECTED_POSITION);
            this.pointCSelectedPosition = savedInstanceState.getInt(
                    LimitDisplacementActivity.POINT_C_SELECTED_POSITION);
            this.pointDSelectedPosition = savedInstanceState.getInt(
                    LimitDisplacementActivity.POINT_D_SELECTED_POSITION);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_limit_displacement);
    }

    private void startLimitDisplacementResultsActivity() {
        Bundle bundle = new Bundle();

        bundle.putInt(
                LimitDisplacementActivity.LIMIT_DISPLACEMENT_POSITION,
                SharedResources.getCalculationsHistory().indexOf(
                        this.limDispl));

        Intent resultsActivityIntent = new Intent(
                this, LimitDisplacementResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }
}
