package ch.hgdev.toposuite.calculation.activities.linecircleintersection;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import ch.hgdev.toposuite.R;

public class LineCircleIntersectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_line_circle_intersection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.line_circle_intersection, menu);
        return true;
    }

}
