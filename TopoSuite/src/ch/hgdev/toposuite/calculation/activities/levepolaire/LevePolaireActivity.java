package ch.hgdev.toposuite.calculation.activities.levepolaire;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.calculation.LevePolaire;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

/**
 * Activity related to the "leve polaire".
 * 
 * @author HGdev
 * 
 */
public class LevePolaireActivity extends TopoSuiteActivity implements
        AddDeterminationDialogFragment.AddDeterminationDialogListener,
        EditDeterminationDialogFragment.EditDeterminationDialogListener {

    public static final String    STATION_NUMBER_LABEL      = "station_number";
    public static final String    DETERMINATIONS_LABEL      = "determinations";

    public static final String    DETERMINATION_NUMBER      = "determination_number";
    public static final String    HORIZ_DIR                 = "horizontal_direction";
    public static final String    DISTANCE                  = "distance";
    public static final String    ZEN_ANGLE                 = "zenithal_angle";
    public static final String    S                         = "s";
    public static final String    LAT_DEPL                  = "lateral_displacement";
    public static final String    LON_DEPL                  = "longitudinal displacement";

    private static final String   STATION_SELECTED_POSITION = "station_selected_position";
    private Spinner               stationSpinner;
    private int                   stationSelectedPosition;
    private ArrayAdapter<Point>   stationAdapter;
    private TextView              stationPointTextView;
    private EditText              iEditText;
    private EditText              unknownOrientEditText;
    private ListView              determinationsListView;
    private ArrayAdapter<Measure> adapter;

    private Point                 station;
    private LevePolaire           levePolaire;

    private double                abrissZ0;
    private Point                 abrissStation;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                   position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_levepolaire);

        this.position = -1;
        this.abrissZ0 = 0.0;

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

        // check if we create a new leve polaire calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.levePolaire = (LevePolaire) SharedResources.getCalculationsHistory().get(
                    this.position);
            list = this.levePolaire.getDeterminations();
        }

        this.adapter = new ArrayListOfDeterminationsAdapter(this,
                R.layout.determinations_list_item, list);
        this.drawList();

        this.registerForContextMenu(this.determinationsListView);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, false));
        points.addAll(SharedResources.getSetOfPoints());

        this.stationAdapter = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(this.stationAdapter);

        for (Calculation c : SharedResources.getCalculationsHistory()) {
            if ((c != null) && (c.getType() != CalculationType.ABRISS)) {
                continue;
            }
            Abriss a = (Abriss) c;
            a.compute();
            this.abrissZ0 = a.getMean();
            this.abrissStation = a.getStation();
            break;
        }

        if (this.levePolaire != null) {
            this.stationSpinner.setSelection(
                    this.stationAdapter.getPosition(this.levePolaire.getStation()));
            Measure m = this.levePolaire.getDeterminations().get(0);

            this.iEditText.setText(DisplayUtils.toString(m.getI()));
            this.unknownOrientEditText.setText(DisplayUtils.toString(m.getUnknownOrientation()));
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

        JSONArray json = new JSONArray();
        for (int i = 0; i < this.adapter.getCount(); i++) {
            json.put(this.adapter.getItem(i).toJSONObject());
        }

        outState.putString(LevePolaireActivity.DETERMINATIONS_LABEL, json.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.adapter.clear();
            this.stationSelectedPosition = savedInstanceState.getInt(
                    LevePolaireActivity.STATION_SELECTED_POSITION);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(
                        savedInstanceState.getString(LevePolaireActivity.DETERMINATIONS_LABEL));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    Measure m = Measure.getMeasureFromJSON(json.toString());
                    this.adapter.add(m);
                }
            } catch (JSONException e) {
                // TODO
            }
            this.drawList();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.leve_polaire_measures_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.edit_measure:
            this.showEditDeterminationDialog(info.position);
            return true;
        case R.id.delete_measure:
            this.adapter.remove(this.adapter.getItem(info.position));
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
        case R.id.checkbox_z0:
            if (checked) {
                if (MathUtils.isZero(this.abrissZ0)) {
                    Toast errorToast = Toast.makeText(this,
                            this.getText(R.string.error_no_abriss_calculation_found),
                            Toast.LENGTH_SHORT);
                    errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    errorToast.show();
                } else {
                    this.unknownOrientEditText.setText(DisplayUtils.toString(this.abrissZ0));
                    this.unknownOrientEditText.setEnabled(false);
                    this.stationSpinner.setSelection(
                            this.stationAdapter.getPosition(this.abrissStation));
                    this.stationSpinner.setEnabled(false);
                }
            } else {
                this.unknownOrientEditText.setText("");
                this.unknownOrientEditText.setEnabled(true);
                this.stationSpinner.setSelection(0);
                this.stationSpinner.setEnabled(true);
            }
            break;
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
     * 
     * @param position
     *            Position of the determination measure to edit.
     */
    private void showEditDeterminationDialog(int position) {
        EditDeterminationDialogFragment dialog = new EditDeterminationDialogFragment();

        this.position = position;
        Measure d = this.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putInt(LevePolaireActivity.DETERMINATION_NUMBER, d.getMeasureNumber());
        args.putDouble(LevePolaireActivity.HORIZ_DIR, d.getHorizDir());
        args.putDouble(LevePolaireActivity.DISTANCE, d.getDistance());
        args.putDouble(LevePolaireActivity.ZEN_ANGLE, d.getZenAngle());
        args.putDouble(LevePolaireActivity.S, d.getS());
        args.putDouble(LevePolaireActivity.LAT_DEPL, d.getLatDepl());
        args.putDouble(LevePolaireActivity.LON_DEPL, d.getLonDepl());

        dialog.setArguments(args);
        dialog.show(this.getFragmentManager(), "EditDeterminationDialogFragment");
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
        if ((this.station == null) || (this.station.getNumber() < 1)) {
            return false;
        }
        if (this.unknownOrientEditText.length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Perform actions required when the calculation button is clicked.
     */
    private void showLevePolaireResultActivity() {
        double i = 0.0;
        double unknownOrient;

        if (this.iEditText.length() > 0) {
            i = Double.parseDouble(this.iEditText.getText().toString());
        }
        if (this.unknownOrientEditText.length() > 0) {
            unknownOrient = Double.parseDouble(this.unknownOrientEditText.getText().toString());
        } else {
            Toast errorToast = Toast.makeText(this,
                    this.getText(R.string.error_choose_unknown_orientation),
                    Toast.LENGTH_SHORT);
            errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            errorToast.show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(LevePolaireActivity.STATION_NUMBER_LABEL, this.station.getNumber());

        JSONArray json = new JSONArray();
        for (int j = 0; j < this.adapter.getCount(); j++) {
            Measure m = this.adapter.getItem(j);
            m.setI(i);
            m.setUnknownOrientation(unknownOrient);
            json.put(m.toJSONObject());
        }

        bundle.putString(LevePolaireActivity.DETERMINATIONS_LABEL, json.toString());

        Intent resultsActivityIntent = new Intent(this, LevePolaireResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    @Override
    public void onDialogAdd(AddDeterminationDialogFragment dialog) {

        Measure m = new Measure(
                null,
                dialog.getHorizDir(),
                dialog.getZenAngle(),
                dialog.getDistance(),
                dialog.getS(),
                dialog.getLatDepl(),
                dialog.getLonDepl(),
                0.0,
                0.0,
                dialog.getDeterminationNo());

        this.position = -1;
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(AddDeterminationDialogFragment dialog) {
        // do nothing actually
    }

    @Override
    public void onDialogEdit(EditDeterminationDialogFragment dialog) {
        this.adapter.remove(this.adapter.getItem(this.position));
        Measure m = new Measure(
                null,
                dialog.getHorizDir(),
                dialog.getZenAngle(),
                dialog.getDistance(),
                dialog.getS(),
                dialog.getLatDepl(),
                dialog.getLonDepl(),
                0.0,
                0.0,
                dialog.getDeterminationNo());

        this.position = -1;
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogCancel(EditDeterminationDialogFragment dialog) {
        // do nothing actually
    }
}