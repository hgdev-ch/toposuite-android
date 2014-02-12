package ch.hgdev.toposuite.calculation.activities.levepolaire;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LevePolaire;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Activity related to the "leve polaire".
 * 
 * @author HGdev
 * 
 */
public class LevePolaireActivity extends TopoSuiteActivity {
    private static final String   STATION_SELECTED_POSITION = "station_selected_position";
    private Spinner               stationSpinner;
    private TextView              iTextView;
    private TextView              stationPointTextView;
    private TextView              unknownOrientTextView;
    private ListView              determinationsListView;
    private int                   stationSelectedPosition;
    private LevePolaire           levePolaire;
    private ArrayAdapter<Measure> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_levepolaire);

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);
        this.unknownOrientTextView = (TextView) this.findViewById(R.id.unknown_orientation);
        this.iTextView = (TextView) this.findViewById(R.id.i);
        this.determinationsListView = (ListView) this.findViewById(R.id.determinations_list);

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LevePolaireActivity.this.stationSelectedPosition = pos;

                Point pt = (Point) LevePolaireActivity.this.stationSpinner
                        .getItemAtPosition(pos);
                if (pt.getNumber() > 0) {
                    LevePolaireActivity.this.stationPointTextView.setText(DisplayUtils
                            .formatPoint(
                                    LevePolaireActivity.this, pt));
                } else {
                    LevePolaireActivity.this.stationPointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });
        ArrayList<Measure> list = new ArrayList<Measure>();

        this.adapter = new ArrayListOfDeterminationsAdapter(this,
                R.layout.determinations_list_item, list);
        this.drawList();
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(a);

        if (this.levePolaire != null) {
            this.stationSpinner.setSelection(
                    a.getPosition(this.levePolaire.getStation()));
        } else {
            if (this.stationSelectedPosition > 0) {
                this.stationSpinner.setSelection(
                        this.stationSelectedPosition);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.levepolaire, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LevePolaireActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.stationSelectedPosition = savedInstanceState.getInt(
                    LevePolaireActivity.STATION_SELECTED_POSITION);
        }
    }

    /**
     * Draw the main table containing all the determinations.
     */
    private void drawList() {
        this.determinationsListView.setAdapter(this.adapter);
    }
}