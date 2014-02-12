package ch.hgdev.toposuite.calculation.activities.cheminortho;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;

public class CheminementOrthoResultsActivity extends TopoSuiteActivity {
    private TextView                  baseTextView;
    private ListView                  resultsListView;

    private ArrayListOfResultsAdapter adapter;

    private CheminementOrthogonal     cheminOrtho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cheminement_ortho_results);

        this.baseTextView = (TextView) this.findViewById(R.id.base);
        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(
                    CheminementOrthoActivity.CHEMINEMENT_ORTHO_POSITION);
            this.cheminOrtho = (CheminementOrthogonal) SharedResources.getCalculationsHistory()
                    .get(position);
            this.cheminOrtho.getResults().clear();
            this.cheminOrtho.compute();

            StringBuilder builder = new StringBuilder();
            builder.append(this.cheminOrtho.getOrthogonalBase().getOrigin());
            builder.append("-");
            builder.append(this.cheminOrtho.getOrthogonalBase().getExtemity());

            this.baseTextView.setText(builder.toString());
            this.drawList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.cheminement_ortho_results, menu);
        return true;
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_ortho_results_list_item,
                this.cheminOrtho.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}
