package ch.hgdev.toposuite;

import android.os.Bundle;
import android.view.Menu;

public class OrthogonalImplantationActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_orthogonal_implantation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //this.getMenuInflater().inflate(R.menu.orthogonal_implantation, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
