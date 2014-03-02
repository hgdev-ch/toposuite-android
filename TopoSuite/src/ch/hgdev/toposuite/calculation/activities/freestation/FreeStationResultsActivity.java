package ch.hgdev.toposuite.calculation.activities.freestation;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.FreeStation;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class FreeStationResultsActivity extends TopoSuiteActivity {

    private TextView    freeStationTextView;
    private TextView    freeStationPointTextView;
    private TextView    sETextView;
    private TextView    sNTextView;
    private TextView    sATextView;
    private TextView    unknownOrientationTextView;

    private FreeStation freeStation;

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

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
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
            this.sATextView.setText(
                    DisplayUtils.formatDifferences(this.freeStation.getsA()));
            this.unknownOrientationTextView.setText(
                    DisplayUtils.toString(this.freeStation.getUnknownOrientation()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(R.menu.free_station_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_free_station_results);
    }
}
