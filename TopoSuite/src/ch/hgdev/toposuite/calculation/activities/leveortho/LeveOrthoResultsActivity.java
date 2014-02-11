package ch.hgdev.toposuite.calculation.activities.leveortho;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.R.layout;
import android.os.Bundle;
import android.view.Menu;

public class LeveOrthoResultsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_ortho_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this.getMenuInflater().inflate(R.menu.leve_ortho_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
