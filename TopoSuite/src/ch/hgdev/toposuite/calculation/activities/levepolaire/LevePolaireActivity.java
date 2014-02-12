package ch.hgdev.toposuite.calculation.activities.levepolaire;

import android.os.Bundle;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

/**
 * Activity related to the "leve polaire".
 * 
 * @author HGdev
 * 
 */
public class LevePolaireActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_levepolaire);
    }
}