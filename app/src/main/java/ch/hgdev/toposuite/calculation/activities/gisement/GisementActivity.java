package ch.hgdev.toposuite.calculation.activities.gisement;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Activity providing an interface for calculating the Gisement / Distance.
 * 
 * @author HGdev
 */
public class GisementActivity extends TopoSuiteActivity {
    private static final String ORIGIN_SELECTED_POSITION      = "origin_selected_position";
    private static final String ORIENTATION_SELECTED_POSITION = "orientation_selected_position";

    private Spinner             originSpinner;
    private Spinner             orientationSpinner;

    private TextView            originPoint;
    private TextView            orientationPoint;

    private TextView            gisementValue;
    private TextView            distValue;
    private TextView            altitudeValue;
    private TextView            slopeValue;

    private Gisement            gisement;

    private int                 originSelectedPosition;
    private int                 orientationSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_gisement);

        this.originSpinner = (Spinner) this.findViewById(R.id.origin_spinner);
        this.orientationSpinner = (Spinner) this.findViewById(R.id.orientation_spinner);

        this.originPoint = (TextView) this.findViewById(R.id.origin_point);
        this.orientationPoint = (TextView) this.findViewById(R.id.orientation_point);

        this.gisementValue = (TextView) this.findViewById(R.id.gisement_value);
        this.distValue = (TextView) this.findViewById(R.id.distance_value);
        this.altitudeValue = (TextView) this.findViewById(R.id.altitude_value);
        this.slopeValue = (TextView) this.findViewById(R.id.slope_value);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                GisementActivity.this.originSelectedPosition = pos;

                Point pt = (Point) GisementActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    GisementActivity.this.originPoint.setText(DisplayUtils.formatPoint(
                            GisementActivity.this, pt));
                } else {
                    GisementActivity.this.originPoint.setText("");
                }
                GisementActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.orientationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                GisementActivity.this.orientationSelectedPosition = pos;

                Point pt = (Point) GisementActivity.this.orientationSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    GisementActivity.this.orientationPoint.setText(DisplayUtils.formatPoint(
                            GisementActivity.this, pt));
                } else {
                    GisementActivity.this.orientationPoint.setText("");
                }
                GisementActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        // check if we create a new gisement calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.gisement = (Gisement) SharedResources.getCalculationsHistory().get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(
                "", MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.orientationSpinner.setAdapter(a);

        if (this.gisement != null) {
            this.originSpinner.setSelection(
                    a.getPosition(this.gisement.getOrigin()));
            this.orientationSpinner.setSelection(
                    a.getPosition(this.gisement.getOrientation()));
        } else {
            if (this.originSelectedPosition > 0) {
                this.originSpinner.setSelection(
                        this.originSelectedPosition);
            }

            if (this.orientationSelectedPosition > 0) {
                this.orientationSpinner.setSelection(
                        this.orientationSelectedPosition);
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_gisement);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(GisementActivity.ORIGIN_SELECTED_POSITION,
                this.originSelectedPosition);
        outState.putInt(GisementActivity.ORIENTATION_SELECTED_POSITION,
                this.orientationSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.originSelectedPosition = savedInstanceState.getInt(
                    GisementActivity.ORIGIN_SELECTED_POSITION);
            this.orientationSelectedPosition = savedInstanceState.getInt(
                    GisementActivity.ORIENTATION_SELECTED_POSITION);
        }
    }

    /**
     * itemSelected is triggered when the selected item of one of the spinners
     * is changed.
     */
    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.orientationSpinner.getSelectedItem();

        if ((p1.getNumber().isEmpty()) || (p2.getNumber().isEmpty())) {
            this.resetResults();
        } else if (p1.getNumber().equals(p2.getNumber())) {
            this.resetResults();
            ViewUtils.showToast(this, this.getString(R.string.error_same_points));
        } else {
            if (this.gisement == null) {
                this.gisement = new Gisement(p1, p2);
            } else {
                this.gisement.setOrigin(p1);
                this.gisement.setOrientation(p2);
            }

            this.gisementValue
                    .setText(DisplayUtils.formatAngle(this.gisement.getGisement()));
            this.distValue.setText(DisplayUtils.formatDistance(this.gisement.getHorizDist()));
            this.altitudeValue
                    .setText(DisplayUtils.formatCoordinate(this.gisement.getAltitude()));
            this.slopeValue.setText(DisplayUtils.formatAngle(this.gisement.getSlope()));
        }
    }

    /**
     * Reset the TextViews that contains the results.
     */
    private void resetResults() {
        this.gisementValue.setText(this.getString(R.string.no_value));
        this.distValue.setText(this.getString(R.string.no_value));
        this.altitudeValue.setText(this.getString(R.string.no_value));
        this.slopeValue.setText(this.getString(R.string.no_value));
    }
}