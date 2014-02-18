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
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.R.id;
import ch.hgdev.toposuite.R.layout;
import ch.hgdev.toposuite.R.menu;
import ch.hgdev.toposuite.R.string;
import ch.hgdev.toposuite.calculation.Circle;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

public class CircleActivity extends TopoSuiteActivity {
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
                if (pt.getNumber() > 0) {
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
                if (pt.getNumber() > 0) {
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
                if (pt.getNumber() > 0) {
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
            if (this.circle.getPointNumber() != 0) {
                this.pointNumberEditText.setText(String.valueOf(
                        this.circle.getPointNumber()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.pointASpinner.setAdapter(a);
        this.pointBSpinner.setAdapter(a);
        this.pointCSpinner.setAdapter(a);

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

        int num = 0;
        if (this.pointNumberEditText.length() > 0) {
            num = Integer.valueOf(this.pointNumberEditText.getText().toString());
        }

        outState.putInt(CircleActivity.POINT_NUMBER, num);
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

            int num = savedInstanceState.getInt(CircleActivity.POINT_NUMBER);
            if (num != 0) {
                this.pointNumberEditText.setText(String.valueOf(num));
            }
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

                Toast.makeText(this, R.string.error_fill_data, Toast.LENGTH_LONG).show();
                return true;
            }

            int num = Integer.valueOf(
                    this.pointNumberEditText.getText().toString());
            if (num == 0) {
                Toast.makeText(this, R.string.error_fill_data, Toast.LENGTH_LONG).show();
                return true;
            }

            this.circle.setPointNumber(num);
            this.circle.compute();

            if (SharedResources.getSetOfPoints().find(this.circle.getPointNumber()) == null) {
                SharedResources.getSetOfPoints().add(this.circle.getCenter());
                this.circle.getCenter().registerDAO(PointsDataSource.getInstance());

                Toast.makeText(this, R.string.point_add_success, Toast.LENGTH_LONG)
                        .show();
            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void itemSelected() {
        if ((this.pointASelectedPosition != 0)
                && (this.pointBSelectedPosition != 0)
                && (this.pointCSelectedPosition != 0)) {

            int num = 0;
            if (this.pointNumberEditText.length() > 0) {
                num = Integer.parseInt(
                        this.pointNumberEditText.getText().toString());
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
                        DisplayUtils.toString(
                                this.circle.getRadius()));
            }
        }
    }
}
