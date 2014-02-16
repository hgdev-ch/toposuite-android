package ch.hgdev.toposuite;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.calculation.Circle;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

public class CircleActivity extends TopoSuiteActivity {

    private Spinner  pointASpinner;
    private Spinner  pointBSpinner;
    private Spinner  pointCSpinner;

    private TextView pointATextView;
    private TextView pointBTextView;
    private TextView pointCTextView;
    private TextView circleCenterTextView;
    private TextView circleRadiusTextView;

    private EditText pointNumberEditText;

    private int      pointASelectedPosition;
    private int      pointBSelectedPosition;
    private int      pointCSelectedPosition;

    private Circle   circle;

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
        //this.getMenuInflater().inflate(R.menu.circle, menu);
        return super.onCreateOptionsMenu(menu);
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
