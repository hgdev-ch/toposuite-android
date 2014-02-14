package ch.hgdev.toposuite.calculation.activities.polarimplantation;

import android.os.Bundle;
import android.view.Menu;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

public class PolarImplantationActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_polar_implantation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.polar_implantation, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
