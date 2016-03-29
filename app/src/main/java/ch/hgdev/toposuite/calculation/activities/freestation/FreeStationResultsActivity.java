package ch.hgdev.toposuite.calculation.activities.freestation;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class FreeStationResultsActivity extends TopoSuiteActivity implements
        MergePointsDialog.MergePointsDialogListener {

    private TextView                  freeStationTextView;
    private TextView                  freeStationPointTextView;
    private TextView                  sETextView;
    private TextView                  sNTextView;
    private TextView                  sATextView;
    private TextView                  unknownOrientationTextView;

    private ListView                  resultsListView;
    private ArrayListOfResultsAdapter adapter;
    private FreeStation               freeStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_free_station_results);

        this.freeStationTextView = (TextView) this.findViewById(
                R.id.free_station);
        this.freeStationPointTextView = (TextView) this.findViewById(
                R.id.free_station_point);
        this.sETextView = (TextView) this.findViewById(R.id.se);
        this.sNTextView = (TextView) this.findViewById(R.id.sn);
        this.sATextView = (TextView) this.findViewById(R.id.sa);
        this.unknownOrientationTextView = (TextView) this.findViewById(
                R.id.unknown_orientation);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(FreeStationActivity.FREE_STATION_POSITION);
            this.freeStation = (FreeStation) SharedResources.getCalculationsHistory().get(
                    position);
            this.freeStation.compute();
            this.drawList();
            this.refreshResults();
        }

        this.registerForContextMenu(this.resultsListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.free_station_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.abriss_results_context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.save_points:
            this.savePoint(this.freeStation.getStationResult());
            return true;
        case R.id.run_calculation_button:
            for (int i = 0; i < this.freeStation.getResults().size(); i++) {
                if (this.freeStation.getResults().get(i).isDeactivated()) {
                    this.freeStation.getMeasures().get(i).deactivate();
                } else {
                    this.freeStation.getMeasures().get(i).reactivate();
                }
            }
            this.freeStation.compute();
            this.refreshResults();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.toggle_measure:
            this.freeStation.getResults().get(info.position).toggle();
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_free_station_results);
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.free_station_results_list_item,
                this.freeStation.getResults(), !MathUtils.isIgnorable(this.freeStation.getI()));
        this.resultsListView.setAdapter(this.adapter);
    }

    private boolean savePoint(Point st) {
        if (SharedResources.getSetOfPoints().find(st.getNumber()) == null) {
            SharedResources.getSetOfPoints().add(st);
            ViewUtils.redirectToPointsManagerActivity(this);
            ViewUtils.showToast(this, this.getString(R.string.point_add_success));
            return true;
        } else {
            // this point already exists
            MergePointsDialog dialog = new MergePointsDialog();

            Bundle args = new Bundle();
            args.putString(
                    MergePointsDialog.POINT_NUMBER,
                    st.getNumber());
            args.putDouble(MergePointsDialog.NEW_EAST,
                    st.getEast());
            args.putDouble(MergePointsDialog.NEW_NORTH,
                    st.getNorth());
            args.putDouble(MergePointsDialog.NEW_ALTITUDE,
                    st.getAltitude());

            dialog.setArguments(args);
            dialog.show(this.getFragmentManager(), "MergePointsDialogFragment");

            return false;
        }
    }

    @Override
    public void onMergePointsDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
        ViewUtils.redirectToPointsManagerActivity(this);
    }

    @Override
    public void onMergePointsDialogError(String message) {
        ViewUtils.showToast(this, message);
    }

    private void refreshResults() {
        this.freeStationTextView.setText(String.valueOf(
                this.freeStation.getStationNumber()));

        this.freeStationPointTextView.setText(
                DisplayUtils.formatPoint(
                        this, this.freeStation.getStationResult()));

        this.sETextView.setText(
                DisplayUtils.formatDifferences(this.freeStation.getsE()));
        this.sNTextView.setText(
                DisplayUtils.formatDifferences(this.freeStation.getsN()));

        if (!MathUtils.isIgnorable(this.freeStation.getI())) {
            this.sATextView.setText(
                    DisplayUtils.formatDifferences(this.freeStation.getsA()));
        } else {
            this.sATextView.setText(this.getString(R.string.no_value));
        }

        this.unknownOrientationTextView.setText(
                DisplayUtils.formatAngle(this.freeStation.getUnknownOrientation()));

        this.adapter.notifyDataSetChanged();
    }
}
