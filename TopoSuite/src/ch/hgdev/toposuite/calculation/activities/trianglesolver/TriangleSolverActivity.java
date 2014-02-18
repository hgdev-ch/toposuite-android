package ch.hgdev.toposuite.calculation.activities.trianglesolver;

import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

public class TriangleSolverActivity extends TopoSuiteActivity {
    private double   a;
    private double   b;
    private double   c;
    private double   alpha;
    private double   beta;
    private double   gamma;

    private EditText aEditText;
    private EditText bEditText;
    private EditText cEditText;
    private EditText alphaEditText;
    private EditText betaEditText;
    private EditText gammaEditText;

    private TextView perimeterTextView;
    private TextView heightTextView;
    private TextView surfaceTextView;
    private TextView incircleRadiusTextView;
    private TextView excircleRadiusTextView;

    private TextView perimeterBisTextView;
    private TextView heightBisTextView;
    private TextView surfaceBisTextView;
    private TextView incircleRadiusBisTextView;
    private TextView excircleRadiusBisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_triangle_solver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
