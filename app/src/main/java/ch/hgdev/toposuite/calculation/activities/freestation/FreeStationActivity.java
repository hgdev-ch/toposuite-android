package ch.hgdev.toposuite.calculation.activities.freestation;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class FreeStationActivity extends TopoSuiteActivity implements MeasureDialogFragment.MeasureDialogListener {

    public static final String FREE_STATION = "free_station_position";

    private static final String MEASURES_LIST_LABEL = "measures_list";

    private EditText stationEditText;
    private EditText iEditText;
    private ListView measuresListView;
    private FloatingActionButton addButton;

    private ArrayListOfMeasuresAdapter adapter;
    private FreeStation freeStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_free_station);

        this.stationEditText = (EditText) this.findViewById(R.id.station_edit_text);
        this.iEditText = (EditText) this.findViewById(R.id.i);
        this.measuresListView = (ListView) this.findViewById(R.id.determinations_list);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_determination_button);

        this.iEditText.setInputType(App.getInputTypeCoordinate());

        this.measuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FreeStationActivity.this.showEditMeasureDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  FreeStationActivity.this.showAddMeasureDialog();
                                              }
                                          }
        );

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.freeStation = (FreeStation) SharedResources.getCalculationsHistory().get(position);

            if (!this.freeStation.getStationNumber().isEmpty()) {
                this.stationEditText.setText(String.valueOf(this.freeStation.getStationNumber()));
            }

            if (MathUtils.isPositive(this.freeStation.getI())) {
                this.iEditText.setText(String.valueOf(this.freeStation.getI()));
            }
        } else {
            this.freeStation = new FreeStation(true);
        }

        this.adapter = new ArrayListOfMeasuresAdapter(
                this, R.layout.determinations_list_item,
                new ArrayList<>(this.freeStation.getMeasures()));
        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.drawList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FreeStationActivity.MEASURES_LIST_LABEL, this.adapter.getMeasures());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            ArrayList<Measure> measures = (ArrayList<Measure>) savedInstanceState.getSerializable(FreeStationActivity.MEASURES_LIST_LABEL);
            this.adapter.clear();
            this.adapter.addAll(measures);
            this.drawList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
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
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_button:
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
            case R.id.run_calculation_button:
                if (this.checkInputs()) {
                    // update I and station number
                    this.freeStation.setI(ViewUtils.readDouble(this.iEditText));
                    this.freeStation.setStationNumber(ViewUtils.readString(this.stationEditText));

                    this.freeStation.getMeasures().clear();
                    this.freeStation.getMeasures().addAll(this.adapter.getMeasures());

                    this.startFreeStationResultsActivity();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
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

        boolean isSMandatory = !((this.iEditText.length() == 0) || MathUtils.isIgnorable(ViewUtils.readDouble(this.iEditText)));
        MeasureDialogFragment dialog = MeasureDialogFragment.newInstance(isSMandatory);
        dialog.show(this.getSupportFragmentManager(), "MeasureDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit an existing measure.
     */
    private void showEditMeasureDialog(int position) {
        ViewUtils.lockScreenOrientation(this);

        boolean isSMandatory = !((this.iEditText.length() == 0) || MathUtils.isIgnorable(ViewUtils.readDouble(this.iEditText)));

        Measure m = this.adapter.getItem(position);
        MeasureDialogFragment dialog = MeasureDialogFragment.newInstance(m, isSMandatory);
        dialog.show(this.getSupportFragmentManager(), "MeasureDialogFragment");
    }

    /**
     * Start the free station results activity. This action in only performed
     * when the user run the calculation.
     */
    private void startFreeStationResultsActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FreeStationActivity.FREE_STATION, this.freeStation);

        Intent resultsActivityIntent = new Intent(this, FreeStationResultsActivity.class);
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
        return (this.stationEditText.length() > 0) && (this.adapter.getCount() >= 3);
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
        int position = this.adapter.getMeasures().indexOf(dialog.getMeasure());

        Measure m = this.adapter.getMeasures().get(position);
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