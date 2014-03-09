package ch.hgdev.toposuite.calculation.activities.freestation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.calculation.activities.MergePointsDialog;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;

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
                    DisplayUtils.toString(this.freeStation.getUnknownOrientation()));

            this.drawList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.free_station_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.save_points:
            this.savePoint(this.freeStation.getStationResult());
            return true;
        default:
            return super.onOptionsItemSelected(item);
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
            Toast.makeText(this, R.string.point_add_success, Toast.LENGTH_LONG).show();
            return true;
        } else {
            // this point already exists
            MergePointsDialog dialog = new MergePointsDialog();

            Bundle args = new Bundle();
            args.putInt(
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMergePointsDialogError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
