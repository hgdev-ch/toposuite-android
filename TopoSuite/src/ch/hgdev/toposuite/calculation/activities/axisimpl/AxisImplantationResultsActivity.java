package ch.hgdev.toposuite.calculation.activities.axisimpl;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.R.layout;
import ch.hgdev.toposuite.R.menu;
import ch.hgdev.toposuite.R.string;
import android.os.Bundle;
import android.view.Menu;

public class AxisImplantationResultsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_axis_implantation_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.axis_implantation_results, menu);
        return true;
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_axis_implantation_results);
    }
}