package ch.hgdev.toposuite.calculation.activities.circle;

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
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Circle;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CircleActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {
    private static final String POINT_A      = "point_a";
    private static final String POINT_B      = "point_b";
    private static final String POINT_C      = "point_c";
    private static final String POINT_NUMBER = "point_number";

    private Spinner             pointASpinner;
    private Spinner             pointBSpinner;
    private Spinner             pointCSpinner;

    private TextView            pointATextView;
    private TextView            pointBTextView;
    private TextView            pointCTextView;
    private TextView            circleCenterTextView;
    private TextView            circleRadiusTextView;

    private EditText            pointNumberEditText;

    private int                 pointASelectedPosition;
    private int                 pointBSelectedPosition;
    private int                 pointCSelectedPosition;

    private Circle              circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circle);

        this.pointASelectedPosition = 0;
        this.pointBSelectedPosition = 0;
        this.pointCSelectedPosition = 0;

        this.pointASpinner = (Spinner) this.findViewById(
                R.id.point_1_spinner);
        this.pointBSpinner = (Spinner) this.findViewById(
                R.id.point_2_spinner);
        this.pointCSpinner = (Spinner) this.findViewById(
                R.id.point_3_spinner);

        this.pointATextView = (TextView) this.findViewById(R.id.point_1);
        this.pointBTextView = (TextView) this.findViewById(R.id.point_2);
        this.pointCTextView = (TextView) this.findViewById(R.id.point_3);

        this.circleCenterTextView = (TextView) this.findViewById(
                R.id.circle_center);
        this.circleRadiusTextView = (TextView) this.findViewById(
                R.id.circle_radius);

        this.pointNumberEditText = (EditText) this.findViewById(
                R.id.point_number);

        this.pointASpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CircleActivity.this.pointASelectedPosition = pos;

                Point pt = (Point)
                        CircleActivity.this.pointASpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    CircleActivity.this.pointATextView.setText
                            (DisplayUtils.formatPoint(CircleActivity.this, pt));
                }
                else {
                    CircleActivity.this.pointATextView.setText("");
                }
                CircleActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.pointBSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CircleActivity.this.pointBSelectedPosition = pos;

                Point pt = (Point)
                        CircleActivity.this.pointBSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    CircleActivity.this.pointBTextView.setText
                            (DisplayUtils.formatPoint(CircleActivity.this, pt));
                }
                else {
                    CircleActivity.this.pointBTextView.setText("");
                }
                CircleActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.pointCSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CircleActivity.this.pointCSelectedPosition = pos;

                Point pt = (Point)
                        CircleActivity.this.pointCSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    CircleActivity.this.pointCTextView.setText
                            (DisplayUtils.formatPoint(CircleActivity.this, pt));
                }
                else {
                    CircleActivity.this.pointCTextView.setText("");
                }
                CircleActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.circle = (Circle) SharedResources.getCalculationsHistory()
                    .get(position);
            if (!this.circle.getPointNumber().isEmpty()) {
                this.pointNumberEditText.setText(String.valueOf(
                        this.circle.getPointNumber()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayAdapter<Point> a = this.initSpinners();

        if (this.circle != null) {
            this.pointASpinner.setSelection(
                    a.getPosition(this.circle.getPointA()));
            this.pointBSpinner.setSelection(
                    a.getPosition(this.circle.getPointB()));
            this.pointCSpinner.setSelection(
                    a.getPosition(this.circle.getPointC()));
        } else {
            if (this.pointASelectedPosition > 0) {
                this.pointASpinner.setSelection(
                        this.pointASelectedPosition);
            }

            if (this.pointBSelectedPosition > 0) {
                this.pointBSpinner.setSelection(
                        this.pointBSelectedPosition);
            }

            if (this.pointCSelectedPosition > 0) {
                this.pointCSpinner.setSelection(
                        this.pointCSelectedPosition);
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_circle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.calculation_results_points_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CircleActivity.POINT_A, this.pointASelectedPosition);
        outState.putInt(CircleActivity.POINT_B, this.pointBSelectedPosition);
        outState.putInt(CircleActivity.POINT_C, this.pointCSelectedPosition);

        String num = this.pointNumberEditText.getText().toString();

        outState.putString(CircleActivity.POINT_NUMBER, num);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (this.circle == null) {
                this.pointASelectedPosition = savedInstanceState.getInt(
                        CircleActivity.POINT_A);
                this.pointBSelectedPosition = savedInstanceState.getInt(
                        CircleActivity.POINT_B);
                this.pointCSelectedPosition = savedInstanceState.getInt(
                        CircleActivity.POINT_C);
            }

            this.pointNumberEditText.setText(
                    savedInstanceState.getString(CircleActivity.POINT_NUMBER));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.save_points:
            if ((this.pointASelectedPosition == 0)
                    || (this.pointBSelectedPosition == 0)
                    || (this.pointCSelectedPosition == 0)
                    || (this.pointNumberEditText.length() == 0)
                    || (this.circle == null)) {

                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                return true;
            }

            String num = this.pointNumberEditText.getText().toString();
            if (num.isEmpty()) {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                return true;
            }

            this.circle.setPointNumber(num);
            this.circle.compute();

            if (SharedResources.getSetOfPoints().find(this.circle.getPointNumber()) == null) {
                SharedResources.getSetOfPoints().add(this.circle.getCenter());
                this.circle.getCenter().registerDAO(PointsDataSource.getInstance());

                ViewUtils.showToast(this, this.getString(R.string.point_add_success));
            } else {
                this.showMergePointsDialog();
            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private final ArrayAdapter<Point> initSpinners() {
        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.pointASpinner.setAdapter(a);
        this.pointBSpinner.setAdapter(a);
        this.pointCSpinner.setAdapter(a);

        return a;
    }

    private final void showMergePointsDialog() {
        MergePointsDialog dialog = new MergePointsDialog();

        Bundle args = new Bundle();
        if (this.pointNumberEditText.length() > 0) {
            args.putString(
                    MergePointsDialog.POINT_NUMBER,
                    this.pointNumberEditText.getText().toString());
        } else {
            args.putString(MergePointsDialog.POINT_NUMBER, "");
        }

        args.putDouble(MergePointsDialog.NEW_EAST,
                this.circle.getCenter().getEast());
        args.putDouble(MergePointsDialog.NEW_NORTH,
                this.circle.getCenter().getNorth());
        args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                this.circle.getCenter().getAltitude());

        dialog.setArguments(args);
        dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");
    }

    private final void itemSelected() {
        if ((this.pointASelectedPosition != 0)
                && (this.pointBSelectedPosition != 0)
                && (this.pointCSelectedPosition != 0)) {

            String num = "";
            if (this.pointNumberEditText.length() > 0) {
                num = this.pointNumberEditText.getText().toString();
            }

            Point a = (Point) this.pointASpinner.getSelectedItem();
            Point b = (Point) this.pointBSpinner.getSelectedItem();
            Point c = (Point) this.pointCSpinner.getSelectedItem();

            if (this.circle == null) {
                this.circle = new Circle(a, b, c, num, true);
            } else {
                this.circle.setPointA(a);
                this.circle.setPointB(b);
                this.circle.setPointC(c);
            }

            this.circle.compute();

            if (this.circle.getCenter() != null) {
                this.circleCenterTextView.setText(
                        DisplayUtils.format2DPoint(this,
                                this.circle.getCenter()));
            }

            if (MathUtils.isPositive(this.circle.getRadius())) {
                this.circleRadiusTextView.setText(
                        DisplayUtils.formatDistance(
                                this.circle.getRadius()));
            }
        }
    }

    @Override
    public void onMergePointsDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
        this.initSpinners();
        this.pointASpinner.setSelection(this.pointASelectedPosition);
        this.pointBSpinner.setSelection(this.pointBSelectedPosition);
        this.pointCSpinner.setSelection(this.pointCSelectedPosition);
    }

    @Override
    public void onMergePointsDialogError(String message) {
        ViewUtils.showToast(this, message);
    }
}
