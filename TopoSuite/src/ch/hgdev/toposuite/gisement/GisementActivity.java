package ch.hgdev.toposuite.gisement;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Activity providing an interface for calculating the Gisement / Distance.
 * 
 * @author HGdev
 */
public class GisementActivity extends TopoSuiteActivity {
    private Spinner  originSpinner;
    private Spinner  orientationSpinner;

    private TextView originPoint;
    private TextView orientationPoint;

    private TextView gisementValue;
    private TextView distValue;
    private TextView altitudeValue;
    private TextView slopeValue;

    private Gisement gisement;

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
                Point pt = (Point) GisementActivity.this.originSpinner.getItemAtPosition(pos);
                if (pt.getNumber() > 0) {
                    GisementActivity.this.originPoint.setText(GisementActivity.this.formatPoint(pt));
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
                Point pt = (Point) GisementActivity.this.orientationSpinner.getItemAtPosition(pos);
                if (pt.getNumber() > 0) {
                    GisementActivity.this.orientationPoint.setText(GisementActivity.this
                            .formatPoint(pt));
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
        points.add(new Point(0, 0.0, 0.0, 0.0, true));
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
        }
    }

    /**
     * itemSelected is triggered when the selected item of one of the spinners
     * is changed.
     */
    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.orientationSpinner.getSelectedItem();

        if ((p1.getNumber() == 0) || (p2.getNumber() == 0)) {
            this.resetResults();
        } else if (p1.getNumber() == p2.getNumber()) {
            this.resetResults();
            Toast.makeText(this, R.string.error_same_points, Toast.LENGTH_LONG).show();
        } else {
            if (this.gisement == null) {
                this.gisement = new Gisement(p1, p2);
            } else {
                this.gisement.setOrigin(p1);
                this.gisement.setOrientation(p2);
            }

            this.gisementValue.setText(DisplayUtils.toString(this.gisement.getGisement()));
            this.distValue.setText(DisplayUtils.toString(this.gisement.getHorizDist()));
            this.altitudeValue.setText(DisplayUtils.toString(this.gisement.getAltitude()));
            this.slopeValue.setText(DisplayUtils.toString(this.gisement.getSlope()));
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

    /**
     * Format a point in order to display it in a TextView.
     * 
     * @param pt
     *            a Point
     * @return formatted Point
     */
    private String formatPoint(Point pt) {
        return String.format("%s: %s, %s: %s, %s: %s", this.getString(R.string.east),
                DisplayUtils.toString(pt.getEast()), this.getString(R.string.north),
                DisplayUtils.toString(pt.getNorth()), this.getString(R.string.altitude),
                DisplayUtils.toString(pt.getAltitude()));
    }
}