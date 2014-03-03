package ch.hgdev.toposuite.calculation.activities.freestation;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.calculation.activities.levepolaire.ArrayListOfDeterminationsAdapter;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.MathUtils;

public class FreeStationActivity extends TopoSuiteActivity implements
        AddMeasureDialogFragment.AddMeasureDialogListener {

    /** Position of this free station calculation in the calculation history. */
    public static final String    FREE_STATION_POSITION = "free_station_position";

    private EditText              stationEditText;
    private EditText              iEditText;
    private ListView              determinationsListView;

    private ArrayAdapter<Measure> adapter;
    private FreeStation           freeStation;

    private int                   position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_free_station);

        this.position = 0;

        this.stationEditText = (EditText) this.findViewById(
                R.id.station_edit_text);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.determinationsListView = (ListView) this.findViewById(
                R.id.determinations_list);

        this.iEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);
        this.stationEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.freeStation = (FreeStation) SharedResources.getCalculationsHistory().get(
                    this.position);

            if (this.freeStation.getStationNumber() != 0) {
                this.stationEditText.setText(String.valueOf(
                        this.freeStation.getStationNumber()));
            }

            if (MathUtils.isPositive(this.freeStation.getI())) {
                this.iEditText.setText(String.valueOf(this.freeStation.getI()));
            }
        } else {
            this.freeStation = new FreeStation(true);
        }

        this.adapter = new ArrayListOfDeterminationsAdapter(this,
                R.layout.determinations_list_item, this.freeStation.getMeasures());
        this.drawList();

        this.registerForContextMenu(this.determinationsListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.free_station, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_free_station);
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
                // update I and station number
                this.freeStation.setI(
                        Double.parseDouble(
                                this.iEditText.getText().toString()));
                this.freeStation.setStationNumber(
                        Integer.parseInt(
                                this.stationEditText.getText().toString()));

                this.startFreeStationResultsActivity();
            } else {
                Toast errorToast = Toast.makeText(
                        this, this.getText(R.string.error_fill_data),
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
        AddMeasureDialogFragment dialog = new AddMeasureDialogFragment();
        dialog.show(this.getFragmentManager(), "AddDeterminationDialogFragment");
    }

    /**
     * Start the free station results activity. This action in only performed
     * when the user run the calculation.
     */
    private void startFreeStationResultsActivity() {
        Bundle bundle = new Bundle();

        // At this point we are sure that the free station calculation
        // has been instantiated.
        bundle.putInt(
                FreeStationActivity.FREE_STATION_POSITION,
                SharedResources.getCalculationsHistory().indexOf(
                        this.freeStation));

        Intent resultsActivityIntent = new Intent(
                this, FreeStationResultsActivity.class);
        resultsActivityIntent.putExtras(bundle);
        this.startActivity(resultsActivityIntent);
    }

    /**
     * Draw the main table containing all the determinations.
     */
    private void drawList() {
        this.determinationsListView.setAdapter(this.adapter);
    }

    /**
     * Check user inputs.
     */
    private boolean checkInputs() {
        return ((this.freeStation != null) && (this.iEditText.length() > 0)
                && (this.stationEditText.length() > 0)
                && (this.freeStation.getMeasures().size() >= 3));
    }

    @Override
    public void onDialogAdd(AddMeasureDialogFragment dialog) {
        Measure m = new Measure(
                dialog.getPoint(),
                dialog.getHorizDir(),
                dialog.getZenAngle(),
                dialog.getDistance(),
                dialog.getS(),
                dialog.getLatDepl(),
                dialog.getLonDepl(),
                0.0,
                0.0,
                dialog.getPoint().getNumber()); // small trick to use the determinations_list_item layout. 
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
    }
}