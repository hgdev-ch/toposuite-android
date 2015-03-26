package ch.hgdev.toposuite.calculation.activities.polarsurvey;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Abriss;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.CalculationType;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.PolarSurvey;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Activity related to the polar survey calculation.
 *
 * @author HGdev
 *
 */
public class PolarSurveyActivity extends TopoSuiteActivity implements
        AddDeterminationDialogFragment.AddDeterminationDialogListener,
        EditDeterminationDialogFragment.EditDeterminationDialogListener {
    private static final String   POLAR_SURVEY_ACTIVITY                     = "PolarSurveyActivity: ";

    public static final String    POLAR_SURVEY_POSITION                     = "polar_survey_position";
    public static final String    STATION_NUMBER_LABEL                      = "station_number";
    public static final String    UNKNOWN_ORIENTATION_LABEL                 = "unknown_orientation";
    public static final String    UNKNOWN_ORIENTATION_CALCULATION_ID_LABEL_ = "unknown_orientation_calculation_id";
    public static final String    INSTRUMENT_HEIGHT_LABEL                   = "instrument_height";
    public static final String    DETERMINATIONS_LABEL                      = "determinations";

    public static final String    DETERMINATION_NUMBER                      = "determination_number";
    public static final String    HORIZ_DIR                                 = "horizontal_direction";
    public static final String    DISTANCE                                  = "distance";
    public static final String    ZEN_ANGLE                                 = "zenithal_angle";
    public static final String    S                                         = "s";
    public static final String    LAT_DEPL                                  = "lateral_displacement";
    public static final String    LON_DEPL                                  = "longitudinal displacement";

    private static final String   STATION_SELECTED_POSITION                 = "station_selected_position";
    private Spinner               stationSpinner;
    private int                   stationSelectedPosition;
    private ArrayAdapter<Point>   stationAdapter;
    private TextView              stationPointTextView;
    private EditText              iEditText;
    private EditText              unknownOrientEditText;
    private ListView              determinationsListView;
    private ArrayAdapter<Measure> adapter;
    private CheckBox              z0CheckBox;

    private Point                 station;
    private double                z0;
    private double                instrumentHeight;

    private PolarSurvey           polarSurvey;

    private Point                 z0Station;
    private long                  z0Id;

    /**
     * Position of the calculation in the calculations list. Only used when open
     * from the history.
     */
    private int                   position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_survey);

        this.position = -1;
        this.z0 = MathUtils.IGNORE_DOUBLE;
        this.instrumentHeight = MathUtils.IGNORE_DOUBLE;
        this.z0Id = -1;

        this.stationSpinner = (Spinner) this.findViewById(R.id.station_spinner);
        this.stationPointTextView = (TextView) this.findViewById(R.id.station_point);
        this.unknownOrientEditText = (EditText) this.findViewById(R.id.unknown_orientation);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.determinationsListView = (ListView) this.findViewById(R.id.determinations_list);
        this.z0CheckBox = (CheckBox) this.findViewById(R.id.checkbox_z0);

        this.iEditText.setInputType(App.getInputTypeCoordinate());
        this.unknownOrientEditText.setInputType(App.getInputTypeCoordinate());

        this.stationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PolarSurveyActivity.this.stationSelectedPosition = pos;

                PolarSurveyActivity.this.station = (Point) PolarSurveyActivity.this.stationSpinner
                        .getItemAtPosition(pos);
                if (!PolarSurveyActivity.this.station.getNumber().isEmpty()) {
                    PolarSurveyActivity.this.stationPointTextView.setText(DisplayUtils
                            .formatPoint(PolarSurveyActivity.this, PolarSurveyActivity.this.station));
                } else {
                    PolarSurveyActivity.this.stationPointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        ArrayList<Measure> list = new ArrayList<Measure>();

        // check if we create a new polar survey calculation or if we modify an
        // existing one.
        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.polarSurvey = (PolarSurvey) SharedResources.getCalculationsHistory().get(
                    this.position);
            list = this.polarSurvey.getDeterminations();

            this.z0Id = this.polarSurvey.getZ0CalculationId();
            // the user has retrieved his z0 from last calculation previously
            if (this.z0Id > 0) {
                Calculation c = SharedResources.getCalculationsHistory().find(this.z0Id);
                if ((c != null) && (c.getType() == CalculationType.ABRISS)) {
                    Abriss a = (Abriss) c;
                    a.compute();
                    this.z0 = a.getMean();
                    this.z0Station = a.getStation();
                } else if ((c != null) && (c.getType() == CalculationType.FREESTATION)) {
                    FreeStation fs = (FreeStation) c;
                    fs.compute();
                    this.z0 = fs.getUnknownOrientation();
                    this.z0Station = fs.getStationResult();
                    this.instrumentHeight = fs.getI();
                    this.iEditText.setText(DisplayUtils.toStringForEditText(this.instrumentHeight));
                } else {
                    Logger.log(
                            Logger.ErrLabel.CALCULATION_INVALID_TYPE,
                            PolarSurveyActivity.POLAR_SURVEY_ACTIVITY
                                    + "trying to get Z0 from a calculation that does not compute one");
                }
                this.unknownOrientEditText.setText(DisplayUtils.toStringForEditText(this.z0));
                this.z0CheckBox.setChecked(true);
                this.unknownOrientEditText.setEnabled(false);
            }
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
        points.add(new Point("", 0.0, 0.0, 0.0, false));
        points.addAll(SharedResources.getSetOfPoints());

        this.stationAdapter = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.stationSpinner.setAdapter(this.stationAdapter);

        if (this.polarSurvey != null) {
            this.stationSpinner.setSelection(
                    this.stationAdapter.getPosition(this.polarSurvey.getStation()));

            this.iEditText.setText(DisplayUtils.toStringForEditText(this.instrumentHeight));
            this.unknownOrientEditText.setText(DisplayUtils.toStringForEditText(this.z0));
        } else {
            if (this.stationSelectedPosition > 0) {
                this.stationSpinner.setSelection(
                        this.stationSelectedPosition);
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_polar_survey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.polar_survey, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PolarSurveyActivity.STATION_SELECTED_POSITION,
                this.stationSelectedPosition);

        JSONArray json = new JSONArray();
        for (int i = 0; i < this.adapter.getCount(); i++) {
            json.put(this.adapter.getItem(i).toJSONObject());
        }

        outState.putString(PolarSurveyActivity.DETERMINATIONS_LABEL, json.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.adapter.clear();
            this.stationSelectedPosition = savedInstanceState.getInt(
                    PolarSurveyActivity.STATION_SELECTED_POSITION);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(
                        savedInstanceState.getString(PolarSurveyActivity.DETERMINATIONS_LABEL));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    Measure m = Measure.getMeasureFromJSON(json.toString());
                    this.adapter.add(m);
                }
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR,
                        PolarSurveyActivity.POLAR_SURVEY_ACTIVITY
                                + "error retrieving list of determinations from JSON");
            }
            this.drawList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.add_determination_button:
            this.showAddDeterminationDialog();
            return true;
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                this.showPolarSurveyResultActivity();
            } else {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
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
        inflater.inflate(R.menu.polar_survey_measures_list_context_menu, menu);
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
                this.fetchLastFreeStationOrAbriss();
                if (MathUtils.isIgnorable(this.z0)) {
                    ViewUtils.showToast(this,
                            this.getString(R.string.error_no_suitable_calculation_found));
                } else {
                    this.unknownOrientEditText.setText(DisplayUtils.toStringForEditText(this.z0));
                    this.unknownOrientEditText.setEnabled(false);
                    this.stationSpinner.setSelection(
                            this.stationAdapter.getPosition(this.z0Station));
                    this.stationSpinner.setEnabled(false);
                    if (!MathUtils.isIgnorable(this.instrumentHeight)) {
                        this.iEditText.setText(
                                DisplayUtils.toStringForEditText(this.instrumentHeight));
                        this.iEditText.setEnabled(false);
                    }
                }
            } else {
                this.unknownOrientEditText.setText("");
                this.unknownOrientEditText.setEnabled(true);
                this.stationSpinner.setSelection(0);
                this.stationSpinner.setEnabled(true);
                this.iEditText.setText("");
                this.iEditText.setEnabled(true);
            }
            break;
        }
    }

    /**
     * Display a dialog to allow the user to insert a new determination.
     */
    private void showAddDeterminationDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddDeterminationDialogFragment dialog = new AddDeterminationDialogFragment();
        dialog.show(this.getFragmentManager(), "AddDeterminationDialogFragment");
    }

    /**
     *
     * @param position
     *            Position of the determination measure to edit.
     */
    private void showEditDeterminationDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        EditDeterminationDialogFragment dialog = new EditDeterminationDialogFragment();

        this.position = position;
        Measure d = this.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putString(PolarSurveyActivity.DETERMINATION_NUMBER, d.getMeasureNumber());
        args.putDouble(PolarSurveyActivity.HORIZ_DIR, d.getHorizDir());
        args.putDouble(PolarSurveyActivity.DISTANCE, d.getDistance());
        args.putDouble(PolarSurveyActivity.ZEN_ANGLE, d.getZenAngle());
        args.putDouble(PolarSurveyActivity.S, d.getS());
        args.putDouble(PolarSurveyActivity.LAT_DEPL, d.getLatDepl());
        args.putDouble(PolarSurveyActivity.LON_DEPL, d.getLonDepl());

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
        if ((this.station == null) || (this.station.getNumber().isEmpty())) {
            return false;
        }
        if (this.unknownOrientEditText.length() == 0) {
            return false;
        }
        if (this.adapter.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Perform actions required when the calculation button is clicked.
     */
    private void showPolarSurveyResultActivity() {
        this.instrumentHeight = ViewUtils.readDouble(this.iEditText);
        if (!ViewUtils.isEmpty(this.unknownOrientEditText)) {
            this.z0 = ViewUtils.readDouble(this.unknownOrientEditText);
        } else {
            ViewUtils.showToast(this, this.getText(R.string.error_choose_unknown_orientation));
            return;
        }

        Bundle bundle = new Bundle();

        if (this.polarSurvey != null) {
            bundle.putInt(PolarSurveyActivity.POLAR_SURVEY_POSITION,
                    SharedResources.getCalculationsHistory().indexOf(
                            this.polarSurvey));
        } else {
            bundle.putInt(PolarSurveyActivity.POLAR_SURVEY_POSITION, -1);
            bundle.putString(PolarSurveyActivity.STATION_NUMBER_LABEL, this.station.getNumber());
            bundle.putDouble(PolarSurveyActivity.UNKNOWN_ORIENTATION_LABEL, this.z0);
            bundle.putLong(PolarSurveyActivity.UNKNOWN_ORIENTATION_CALCULATION_ID_LABEL_, this.z0Id);
            bundle.putDouble(PolarSurveyActivity.INSTRUMENT_HEIGHT_LABEL, this.instrumentHeight);

            JSONArray json = new JSONArray();
            for (int j = 0; j < this.adapter.getCount(); j++) {
                json.put(this.adapter.getItem(j).toJSONObject());
            }

            bundle.putString(PolarSurveyActivity.DETERMINATIONS_LABEL, json.toString());
        }

        Intent resultsActivityIntent = new Intent(this, PolarSurveyResultsActivity.class);
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
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                dialog.getDeterminationNo());

        this.position = -1;
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
        this.showAddDeterminationDialog();
    }

    @Override
    public void onDialogCancel(AddDeterminationDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
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
                MathUtils.IGNORE_DOUBLE,
                MathUtils.IGNORE_DOUBLE,
                dialog.getDeterminationNo());

        this.position = -1;
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditDeterminationDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    /**
     * FIXME put this method in a util class of in the super class!
     */
    private void fetchLastFreeStationOrAbriss() {
        for (Calculation c : SharedResources.getCalculationsHistory()) {
            if ((c != null) && (c.getType() == CalculationType.ABRISS)) {
                Abriss a = (Abriss) c;
                a.compute();
                this.z0 = a.getMean();
                this.z0Station = a.getStation();
                this.z0Id = c.getId();
                break;
            }
            if ((c != null) && (c.getType() == CalculationType.FREESTATION)) {
                FreeStation fs = (FreeStation) c;
                fs.compute();
                this.z0 = fs.getUnknownOrientation();
                this.z0Station = fs.getStationResult();
                this.z0Id = c.getId();
                this.instrumentHeight = fs.getI();
                break;
            }
        }
    }
}