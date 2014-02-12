package ch.hgdev.toposuite;

import android.os.Bundle;
import android.view.Menu;

public class LevePolaireResultsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_polaire_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.leve_polaire_results, menu);
        return true;
    }
}