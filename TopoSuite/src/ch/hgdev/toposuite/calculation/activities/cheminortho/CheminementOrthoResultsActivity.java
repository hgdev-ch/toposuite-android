package ch.hgdev.toposuite.calculation.activities.cheminortho;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.R.layout;
import android.os.Bundle;
import android.view.Menu;

public class CheminementOrthoResultsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cheminement_ortho_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.cheminement_ortho_results, menu);
        return true;
    }

}
