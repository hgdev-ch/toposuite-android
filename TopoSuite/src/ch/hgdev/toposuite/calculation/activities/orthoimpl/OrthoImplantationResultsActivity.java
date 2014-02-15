package ch.hgdev.toposuite.calculation.activities.orthoimpl;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.R.layout;
import android.os.Bundle;
import android.view.Menu;

public class OrthoImplantationResultsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ortho_implantation_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(R.menu.ortho_implantation_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
