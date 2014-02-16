package ch.hgdev.toposuite.calculation.activities.leveortho;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
import ch.hgdev.toposuite.calculation.LeveOrthogonal.Measure;
import ch.hgdev.toposuite.points.Point;

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
            this.registerForContextMenu(this.resultsListView);
            this.drawList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.calculation_results_points_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        case R.id.save_points:
            this.savePoints();
            Toast toast = Toast.makeText(this,
                    this.getText(R.string.points_add_success),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.calculations_points_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.save_point:
            if (this.savePoint(info.position)) {
                Toast successToast = Toast.makeText(this,
                        this.getText(R.string.point_add_success),
                        Toast.LENGTH_SHORT);
                successToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                successToast.show();
            } else {
                Toast errorToast = Toast.makeText(this,
                        this.getText(R.string.point_already_exists),
                        Toast.LENGTH_SHORT);
                errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                errorToast.show();
            }
            this.adapter.notifyDataSetChanged();
            return true;
        case R.id.delete_point:
            this.adapter.remove(this.adapter.getItem(info.position));
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Draw the list of results.
     */
    private void drawList() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_ortho_results_list_item,
                this.leveOrtho.getResults());
        this.resultsListView.setAdapter(this.adapter);
    }

    /**
     * Save all points from the list to the database of points. If a point
     * already exists in the database, it is simply skipped.
     */
    private void savePoints() {
        for (int position = 0; position < this.adapter.getCount(); position++) {
            this.savePoint(position);
        }
    }

    /**
     * Save a point to the database of points.
     * 
     * @param position
     *            Position of the point in the list of points.
     * @return True if it was a success, false otherwise.
     */
    private boolean savePoint(int position) {
        Measure m = this.adapter.getItem(position);
        if (SharedResources.getSetOfPoints().find(m.getNumber()) == null) {
            Point point = new Point(
                    m.getNumber(),
                    m.getAbscissa(),
                    m.getOrdinate(),
                    0.0,
                    false);
            SharedResources.getSetOfPoints().add(point);
            return true;
        } else {
            // this point already exists
            return false;
        }
    }
}