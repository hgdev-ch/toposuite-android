package ch.hgdev.toposuite.calculation.activities.circcurvesolver;

import android.os.Bundle;
import android.view.Menu;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

public class CircularCurvesSolverActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circular_curve_solver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.circular_curve_solver, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
