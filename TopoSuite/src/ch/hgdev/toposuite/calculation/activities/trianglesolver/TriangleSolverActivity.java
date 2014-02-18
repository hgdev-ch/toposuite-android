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
        this.initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Match views to activity view attributes.
     */
    private void initView() {
        this.aEditText = (EditText) this.findViewById(R.id.a);
        this.bEditText = (EditText) this.findViewById(R.id.b);
        this.cEditText = (EditText) this.findViewById(R.id.c);
        this.alphaEditText = (EditText) this.findViewById(R.id.alpha);
        this.betaEditText = (EditText) this.findViewById(R.id.beta);
        this.gammaEditText = (EditText) this.findViewById(R.id.gamma);

        this.perimeterTextView = (TextView) this.findViewById(R.id.perimeter);
        this.heightTextView = (TextView) this.findViewById(R.id.height);
        this.surfaceTextView = (TextView) this.findViewById(R.id.surface);
        this.incircleRadiusTextView = (TextView) this.findViewById(R.id.incircle);
        this.excircleRadiusTextView = (TextView) this.findViewById(R.id.excircle);

        this.perimeterBisTextView = (TextView) this.findViewById(R.id.perimeter_bis);
        this.heightBisTextView = (TextView) this.findViewById(R.id.height_bis);
        this.surfaceBisTextView = (TextView) this.findViewById(R.id.surface_bis);
        this.incircleRadiusBisTextView = (TextView) this.findViewById(R.id.incircle_bis);
        this.excircleRadiusBisTextView = (TextView) this.findViewById(R.id.excircle_bis);
    }
}
