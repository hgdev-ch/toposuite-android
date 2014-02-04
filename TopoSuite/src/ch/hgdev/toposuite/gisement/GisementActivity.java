package ch.hgdev.toposuite.gisement;

import java.util.TreeSet;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

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
                Point pt = (Point) originSpinner.getItemAtPosition(pos);
                if (pt.getNumber() != -1) {
                    originPoint.setText(formatPoint(pt));
                } else {
                    originPoint.setText("");
                }
                itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.orientationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Point pt = (Point) orientationSpinner.getItemAtPosition(pos);
                if (pt.getNumber() != -1) {
                    orientationPoint.setText(formatPoint(pt));
                } else {
                    orientationPoint.setText("");
                }
                itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.gisement = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // TODO remove this and use the points added by the PointsManager activity
        /*this.pointsList = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return (lhs.getNumber() < rhs.getNumber()) ? 1 : -1;
            }
        });
        
        this.pointsList.add(new Point(2, 683.064, 194.975, 0.0, true));
        this.pointsList.add(new Point(1, 600.245, 200.729, 0.0, true));*/
        
        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, new Point[]{
                        new Point(-1, 0.0, 0.0, 0.0, true),
                        new Point(1, 683.064, 194.975, 0.0, true),
                        new Point(2, 600.245, 200.729, 0.0, true)
                });
        this.originSpinner.setAdapter(a);
        this.orientationSpinner.setAdapter(a);
    }
    
    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.orientationSpinner.getSelectedItem();
        
        if (p1.getNumber() == -1 || p2.getNumber() == -1) {
            this.gisementValue.setText(getString(R.string.no_value));
            this.distValue.setText(getString(R.string.no_value));
            this.altitudeValue.setText(getString(R.string.no_value));
            this.slopeValue.setText(getString(R.string.no_value));
        } else if (p1.getNumber() == p2.getNumber()) {
            Toast.makeText(this, R.string.error_same_points, Toast.LENGTH_LONG).show();
            this.orientationSpinner.setSelection(0);
            this.orientationPoint.setText("");
        } else {
            if (this.gisement == null) {
                this.gisement = new Gisement(p1, p2);
            } else {
                this.gisement.setOrigin(p1);
                this.gisement.setOrientation(p2);
            }
            
            this.gisementValue.setText(
                    DisplayUtils.toString(this.gisement.getGisement()));
            this.distValue.setText(
                    DisplayUtils.toString(this.gisement.getHorizDist()));
            this.altitudeValue.setText(
                    DisplayUtils.toString(this.gisement.getAltitude()));
            this.slopeValue.setText(
                    DisplayUtils.toString(this.gisement.getSlope()));
        }
    }
    
    private String formatPoint(Point pt) {
        return String.format(
                "%s: %s, %s: %s, %s: %s",
                getString(R.string.east),
                DisplayUtils.toString(pt.getEast()),
                getString(R.string.north),
                DisplayUtils.toString(pt.getNorth()),
                getString(R.string.altitude),
                DisplayUtils.toString(pt.getAltitude()));
    }
}