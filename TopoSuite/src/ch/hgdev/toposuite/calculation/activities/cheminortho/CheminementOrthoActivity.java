package ch.hgdev.toposuite.calculation.activities.cheminortho;

import android.os.Bundle;
import android.view.Menu;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

public class CheminementOrthoActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cheminement_ortho);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this.getMenuInflater().inflate(R.menu.cheminement_ortho, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
