package ch.hgdev.toposuite;

import android.os.Bundle;
import android.view.Menu;

public class PointProjectionActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_point_projection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(R.menu.point_projection, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
