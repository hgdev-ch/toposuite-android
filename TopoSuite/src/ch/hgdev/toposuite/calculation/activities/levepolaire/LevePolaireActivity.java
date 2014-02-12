package ch.hgdev.toposuite.calculation.activities.levepolaire;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Activity related to the "leve polaire".
 * 
 * @author HGdev
 * 
 */
public class LevePolaireActivity extends TopoSuiteActivity implements
        AddDeterminationDialogFragment.AddDeterminationDialogListener {

    public static final String    STATION_NUMBER_LABEL      = "station_number";
    public static final String    DETERMINATIONS_LABEL      = "determinations";

    private static final String   STATION_SELECTED_POSITION = "station_selected_position";
    private Spinner               stationSpinner;
    private EditText              iEditText;
    private TextView              stationPointTextView;
    private EditText              unknownOrientEditText;
    private ListView              determinationsListView;
    private int                   stationSelectedPosition;
    private ArrayAdapter<Measure> adapter;

    private Point                 station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_levepolaire);

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);
        this.unknownOrientEditText = (EditText) this.findViewById(R.id.unknown_orientation);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.determinationsListView = (ListView) this.findViewById(R.id.determinations_list);

        this.iEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.unknownOrientEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                LevePolaireActivity.this.stationSelectedPosition = pos;

                LevePolaireActivity.this.station = (Point) LevePolaireActivity.this.stationSpinner
                        .getItemAtPosition(pos);
                if (LevePolaireActivity.this.station.getNumber() > 0) {
                    LevePolaireActivity.this.stationPointTextView.setText(DisplayUtils
                            .formatPoint(LevePolaireActivity.this, LevePolaireActivity.this.station));
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

        if (this.stationSelectedPosition > 0) {
            this.stationSpinner.setSelection(this.stationSelectedPosition);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.add_determination_button:
            if (this.checkInputs()) {
                this.showAddDeterminationDialog();
            } else {
                Toast errorToast = Toast.makeText(this, this.getText(R.string.error_fill_data),
                        Toast.LENGTH_SHORT);
                errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                errorToast.show();
            }
            return true;
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                this.showLevePolaireResultActivity();
            } else {
                Toast errorToast = Toast.makeText(this, this.getText(R.string.error_fill_data),
                        Toast.LENGTH_SHORT);
                errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                errorToast.show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Display a dialog to allow the user to insert a new determination.
     */
    private void showAddDeterminationDialog() {
        AddDeterminationDialogFragment dialog = new AddDeterminationDialogFragment();
        dialog.show(this.getFragmentManager(), "AddDeterminationDialogFragment");
    }

    /**
     * Draw the main table containing all the determinations.
     */
    private void drawList() {
        this.determinationsListView.setAdapter(this.adapter);
    }

    /**
     * Check that the I field, the unknown orientation field have been filled
     * and that the station has been chosen.
     * 
     * @return True if inputs are OK, false otherwise.
     */
    private boolean checkInputs() {
        if ((this.iEditText.length() == 0) || (this.unknownOrientEditText.length() == 0)
                || (this.station.getNumber() < 1)) {
            return false;
        }
        return true;
    }

    /**
     * Perform actions required when the calculation button is clicked.
     */
    private void showLevePolaireResultActivity() {
        Bundle bundle = new Bundle();
        bundle.putInt(LevePolaireActivity.STATION_NUMBER_LABEL, this.station.getNumber());

        JSONArray json = new JSONArray();
        for (int i = 0; i < this.adapter.getCount(); i++) {
            json.put(this.adapter.getItem(i).toJSONObject());
        }

        bundle.putString(LevePolaireActivity.DETERMINATIONS_LABEL, json.toString());

        Intent resultsActivityIntent = new Intent(this, LevePolaireResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    @Override
    public void onDialogAdd(AddDeterminationDialogFragment dialog) {
        double i = 0.0;
        double unknownOrient;

        if (this.iEditText.length() > 0) {
            i = Double.parseDouble(this.iEditText.getText().toString());
        }
        if (this.unknownOrientEditText.length() == 0) {
            // Pop-up error
            return;
        } else {
            unknownOrient = Double.parseDouble(this.unknownOrientEditText.getText().toString());
        }

        Measure m = new Measure(
                dialog.getDetermination(),
                dialog.getHorizDir(),
                dialog.getZenAngle(),
                dialog.getHorizDist(),
                dialog.getS(),
                dialog.getLatDepl(),
                dialog.getLonDepl(),
                i,
                unknownOrient);

        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(AddDeterminationDialogFragment dialog) {
        // do nothing actually
    }
}