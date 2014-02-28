package ch.hgdev.toposuite.calculation.activities.circlesintersection;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

public class CirclesIntersectionActivity extends TopoSuiteActivity {

    private Spinner             centerOneSpinner;
    private int                 centerOneSelectedPosition;
    private Point               centerOnePoint;
    private TextView            centerOneTextView;
    private EditText            radiusOneEditText;
    private Spinner             byPointOneSpinner;
    private int                 byPointOneSelectedPosition;
    private Point               byPointOne;
    private TextView            byPointOneTextView;

    private Spinner             centerTwoSpinner;
    private int                 centerTwoSelectedPosition;
    private Point               centerTwoPoint;
    private TextView            centerTwoTextView;
    private EditText            radiusTwoEditText;
    private Spinner             byPointTwoSpinner;
    private int                 byPointTwoSelectedPosition;
    private Point               byPointTwo;
    private TextView            byPointTwoTextView;

    private ArrayAdapter<Point> adapter;

    private TextView            intersectionOneTextView;
    private TextView            intersectionTwoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circles_intersection);

        this.mapViews();
        this.initViews();
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, false));
        points.addAll(SharedResources.getSetOfPoints());

        this.adapter = new ArrayAdapter<Point>(this, R.layout.spinner_list_item, points);
        this.centerOneSpinner.setAdapter(this.adapter);
        this.centerTwoSpinner.setAdapter(this.adapter);
        this.byPointOneSpinner.setAdapter(this.adapter);
        this.byPointTwoSpinner.setAdapter(this.adapter);

    }

    /**
     * Map views to their respective attributes.
     */
    private void mapViews() {
        this.centerOneSpinner = (Spinner) this.findViewById(R.id.center_one_spinner);
        this.centerOneTextView = (TextView) this.findViewById(R.id.center_one_textview);
        this.radiusOneEditText = (EditText) this.findViewById(R.id.radius_one);
        this.byPointOneSpinner = (Spinner) this.findViewById(R.id.by_point_one_spinner);
        this.byPointOneTextView = (TextView) this.findViewById(R.id.by_point_one_textview);

        this.centerTwoSpinner = (Spinner) this.findViewById(R.id.center_two_spinner);
        this.centerTwoTextView = (TextView) this.findViewById(R.id.center_two_textview);
        this.radiusTwoEditText = (EditText) this.findViewById(R.id.radius_two);
        this.byPointTwoSpinner = (Spinner) this.findViewById(R.id.by_point_two_spinner);
        this.byPointTwoTextView = (TextView) this.findViewById(R.id.by_point_two_textview);

        this.intersectionOneTextView = (TextView) this.findViewById(R.id.intersection_one);
        this.intersectionTwoTextView = (TextView) this.findViewById(R.id.intersection_two);
    }

    /**
     * Init views.
     */
    private void initViews() {
        this.centerOneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.centerOneSelectedPosition = pos;

                CirclesIntersectionActivity.this.centerOnePoint = (Point) CirclesIntersectionActivity.this.centerOneSpinner
                        .getItemAtPosition(pos);
                if (CirclesIntersectionActivity.this.centerOnePoint.getNumber() > 0) {
                    CirclesIntersectionActivity.this.centerOneTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.centerOnePoint));
                } else {
                    CirclesIntersectionActivity.this.centerOneTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusOne();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.byPointOneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.byPointOneSelectedPosition = pos;

                CirclesIntersectionActivity.this.byPointOne = (Point) CirclesIntersectionActivity.this.byPointOneSpinner
                        .getItemAtPosition(pos);
                if (CirclesIntersectionActivity.this.byPointOne.getNumber() > 0) {
                    CirclesIntersectionActivity.this.byPointOneTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.byPointOne));
                } else {
                    CirclesIntersectionActivity.this.byPointOneTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusOne();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.centerTwoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.centerTwoSelectedPosition = pos;

                CirclesIntersectionActivity.this.centerTwoPoint = (Point) CirclesIntersectionActivity.this.centerTwoSpinner
                        .getItemAtPosition(pos);
                if (CirclesIntersectionActivity.this.centerTwoPoint.getNumber() > 0) {
                    CirclesIntersectionActivity.this.centerTwoTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.centerTwoPoint));
                } else {
                    CirclesIntersectionActivity.this.centerTwoTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusTwo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.byPointTwoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CirclesIntersectionActivity.this.byPointTwoSelectedPosition = pos;

                CirclesIntersectionActivity.this.byPointTwo = (Point) CirclesIntersectionActivity.this.byPointTwoSpinner
                        .getItemAtPosition(pos);
                if (CirclesIntersectionActivity.this.byPointTwo.getNumber() > 0) {
                    CirclesIntersectionActivity.this.byPointTwoTextView.setText(DisplayUtils
                            .formatPoint(CirclesIntersectionActivity.this,
                                    CirclesIntersectionActivity.this.byPointTwo));
                } else {
                    CirclesIntersectionActivity.this.byPointTwoTextView.setText("");
                }
                CirclesIntersectionActivity.this.fillRadiusTwo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });
    }

    /**
     * Fill radius for the first circle with the distance between the center of
     * the first circle and the selected point from the spinner.
     */
    private void fillRadiusOne() {
        if ((this.centerOneSelectedPosition > 0) && (this.byPointOneSelectedPosition > 0)) {
            this.radiusOneEditText.setText(DisplayUtils.toString(
                    MathUtils.euclideanDistance(this.centerOnePoint, this.byPointOne)));
            this.radiusOneEditText.setEnabled(false);
        } else {
            this.radiusOneEditText.setEnabled(true);
        }
    }

    /**
     * Fill radius for the second circle with the distance between the center of
     * the second circle and the selected point from the spinner.
     */
    private void fillRadiusTwo() {
        if ((this.centerTwoSelectedPosition > 0) && (this.byPointTwoSelectedPosition > 0)) {
            this.radiusTwoEditText.setText(DisplayUtils.toString(
                    MathUtils.euclideanDistance(this.centerTwoPoint, this.byPointTwo)));
            this.radiusTwoEditText.setEnabled(false);
        } else {
            this.radiusTwoEditText.setEnabled(true);
        }
    }

}
