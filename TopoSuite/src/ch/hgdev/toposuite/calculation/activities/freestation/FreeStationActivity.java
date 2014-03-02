package ch.hgdev.toposuite.calculation.activities.freestation;

import android.os.Bundle;
import android.view.Menu;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

public class FreeStationActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_free_station);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(R.menu.free_station, menu);
        return true;
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_free_station);
    }
}
