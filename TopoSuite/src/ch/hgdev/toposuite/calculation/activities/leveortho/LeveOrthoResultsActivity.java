package ch.hgdev.toposuite.calculation.activities.leveortho;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;

public class LeveOrthoResultsActivity extends TopoSuiteActivity {

    private TextView                  baseTextView;
    private ListView                  resultsListView;

    private ArrayListOfResultsAdapter adapter;

    private LeveOrthogonal            leveOrtho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_ortho_results);

        this.baseTextView = (TextView) this.findViewById(R.id.base);
        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(LeveOrthoActivity.LEVE_ORTHO_POSITION);
            this.leveOrtho = (LeveOrthogonal) SharedResources.getCalculationsHistory().get(
                    position);
            this.leveOrtho.getResults().clear();
            this.leveOrtho.compute();

            StringBuilder builder = new StringBuilder();
            builder.append(this.leveOrtho.getOrthogonalBase().getOrigin());
            builder.append("-");
            builder.append(this.leveOrtho.getOrthogonalBase().getExtemity());

            this.baseTextView.setText(builder.toString());
            this.drawList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this.getMenuInflater().inflate(R.menu.leve_ortho_results, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_ortho_results_list_item,
                this.leveOrtho.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}