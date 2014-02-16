package ch.hgdev.toposuite;

import android.os.Bundle;
import android.view.Menu;

public class CircleActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_circle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(R.menu.circle, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
