package ch.hgdev.toposuite.calculation.activities.surface;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Surface;

public class SurfaceActivity extends TopoSuiteActivity {
    private String                                name;
    private String                                description;
    private ArrayAdapter<Surface.PointWithRadius> pointsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_surface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.surface, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
