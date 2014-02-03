package ch.hgdev.toposuite.entry;

import android.os.Bundle;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

/**
 * 
 * @author HGdev
 * 
 */
public class MainActivity extends TopoSuiteActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
    }
}