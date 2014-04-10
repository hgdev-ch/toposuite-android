package ch.hgdev.toposuite.calculation.activities.freestation;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class FreeStationActivity extends TopoSuiteActivity implements
        MeasureDialogFragment.MeasureDialogListener {

    /** Position of this free station calculation in the calculation history. */
    public static final String    FREE_STATION_POSITION = "free_station_position";

    private EditText              stationEditText;
    private EditText              iEditText;
    private ListView              measuresListView;

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
        this.measuresListView = (ListView) this.findViewById(
                R.id.determinations_list);

        this.iEditText.setInputType(App.getInputTypeCoordinate());

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            this.position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.freeStation = (FreeStation) SharedResources.getCalculationsHistory().get(
                    this.position);

            if (!this.freeStation.getStationNumber().isEmpty()) {
                this.stationEditText.setText(String.valueOf(
                        this.freeStation.getStationNumber()));
            }

            if (MathUtils.isPositive(this.freeStation.getI())) {
                this.iEditText.setText(String.valueOf(this.freeStation.getI()));
            }
        }

        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.freeStation == null) {
            this.freeStation = new FreeStation(true);
        }

        this.adapter = new ArrayListOfMeasuresAdapter(this,
                R.layout.determinations_list_item, this.freeStation.getMeasures());
        this.drawList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(FreeStationActivity.FREE_STATION_POSITION,
                SharedResources.getCalculationsHistory().indexOf(
                        this.freeStation));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.freeStation = (FreeStation) SharedResources.getCalculationsHistory().get(
                    savedInstanceState.getInt(
                            FreeStationActivity.FREE_STATION_POSITION));
        }
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.leve_ortho_measures_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.edit_measure:
            this.showEditMeasureDialog(info.position);
            return true;
        case R.id.delete_measure:
            this.adapter.remove(this.adapter.getItem(info.position));
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.add_determination_button:
            this.showAddMeasureDialog();
            return true;
        case R.id.run_calculation_button:
            if (this.checkInputs()) {
                // update I and station number
                if (this.iEditText.length() > 0) {
                    this.freeStation.setI(
                            ViewUtils.readDouble(this.iEditText));
                } else {
                    this.freeStation.setI(MathUtils.IGNORE_DOUBLE);
                }
                this.freeStation.setStationNumber(
                        this.stationEditText.getText().toString());

                this.startFreeStationResultsActivity();
            } else {
                ViewUtils.showToast(
                        this, this.getString(R.string.error_fill_data));
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Display a dialog to allow the user to insert a new measure.
     */
    private void showAddMeasureDialog() {
        ViewUtils.lockScreenOrientation(this);

        boolean isSMandatory = ((this.iEditText.length() == 0) ||
                MathUtils.isZero(ViewUtils.readDouble(this.iEditText))) ? false : true;
        MeasureDialogFragment dialog = new MeasureDialogFragment(isSMandatory);
        dialog.show(this.getFragmentManager(), "MeasureDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit an existing measure.
     */
    private void showEditMeasureDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        boolean isSMandatory = ((this.iEditText.length() == 0) ||
                MathUtils.isZero(ViewUtils.readDouble(this.iEditText))) ? false : true;

        Measure m = this.freeStation.getMeasures().get(position);
        MeasureDialogFragment dialog = new MeasureDialogFragment(m, isSMandatory);
        dialog.show(this.getFragmentManager(), "MeasureDialogFragment");
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
        this.measuresListView.setAdapter(this.adapter);
    }

    /**
     * Check user inputs.
     */
    private boolean checkInputs() {
        return ((this.freeStation != null)
                && (this.stationEditText.length() > 0)
                && (this.freeStation.getMeasures().size() >= 3));
    }

    @Override
    public void onDialogAdd(MeasureDialogFragment dialog) {
        Measure m = new Measure(
                dialog.getPoint(),
                dialog.getHorizDir(),
                dialog.getZenAngle(),
                dialog.getDistance(),
                dialog.getS(),
                dialog.getLatDepl(),
                dialog.getLonDepl());
        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();
        this.showAddMeasureDialog();
    }

    @Override
    public void onDialogEdit(MeasureDialogFragment dialog) {
        int position = this.freeStation.getMeasures().indexOf(dialog.getMeasure());

        Measure m = this.freeStation.getMeasures().get(position);
        m.setPoint(dialog.getPoint());
        m.setHorizDir(dialog.getHorizDir());
        m.setZenAngle(dialog.getZenAngle());
        m.setDistance(dialog.getDistance());
        m.setS(dialog.getS());
        m.setLatDepl(dialog.getLatDepl());
        m.setLonDepl(dialog.getLonDepl());

        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel() {
        ViewUtils.unlockScreenOrientation(this);
    }
}