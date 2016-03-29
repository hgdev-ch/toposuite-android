package ch.hgdev.toposuite.calculation.activities.orthoimpl;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;

public class OrthoImplantationResultsActivity extends TopoSuiteActivity {
    private TextView                  baseTextView;
    private ListView                  resultsListView;

    private ArrayListOfResultsAdapter adapter;

    private OrthogonalImplantation    orthImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ortho_implantation_results);

        this.baseTextView = (TextView) this.findViewById(R.id.orthogonal_implantation);
        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(OrthogonalImplantationActivity.ORTHO_IMPL_POSITION);
            this.orthImpl = (OrthogonalImplantation) SharedResources.getCalculationsHistory().
                    get(position);
            this.orthImpl.compute();

            StringBuilder builder = new StringBuilder();
            builder.append(this.orthImpl.getOrthogonalBase().getOrigin());
            builder.append("-");
            builder.append(this.orthImpl.getOrthogonalBase().getExtremity());

            this.baseTextView.setText(builder.toString());
            this.drawList();
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_ortho_implantation_results);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this.getMenuInflater().inflate(R.menu.ortho_implantation_results,
        // menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.orth_impl_results_list_item,
                this.orthImpl.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}
