package ch.hgdev.toposuite.calculation.activities.axisimpl;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.AxisImplantation;
import ch.hgdev.toposuite.calculation.activities.orthoimpl.OrthogonalImplantationActivity;

public class AxisImplantationResultsActivity extends TopoSuiteActivity {

    private TextView                  axisImplantationTextView;
    private ListView                  resultsListView;

    private AxisImplantation          axisImpl;

    private ArrayListOfResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_axis_implantation_results);

        this.axisImplantationTextView = (TextView) this.findViewById(
                R.id.axis_implantation);
        this.resultsListView = (ListView) this.findViewById(
                R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(OrthogonalImplantationActivity.ORTHO_IMPL_POSITION);
            this.axisImpl = (AxisImplantation) SharedResources.getCalculationsHistory().
                    get(position);
            this.axisImpl.compute();

            StringBuilder builder = new StringBuilder();
            builder.append(this.axisImpl.getOrthogonalBase().getOrigin());
            builder.append("-");
            builder.append(this.axisImpl.getOrthogonalBase().getExtremity());

            this.axisImplantationTextView.setText(builder.toString());
            this.drawList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.axis_implantation_results, menu);
        return true;
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_axis_implantation_results);
    }

    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this,
                R.layout.orth_impl_results_list_item,
                this.axisImpl.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }
}