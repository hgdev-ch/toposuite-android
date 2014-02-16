package ch.hgdev.toposuite.calculation.activities.levepolaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.LevePolaire;
import ch.hgdev.toposuite.calculation.LevePolaire.Result;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;

public class LevePolaireResultsActivity extends TopoSuiteActivity {

    private ListView                  resultsListView;

    private LevePolaire               levePolaire;
    private ArrayListOfResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_leve_polaire_results);

        this.resultsListView = (ListView) this.findViewById(R.id.results_list);

        Bundle bundle = this.getIntent().getExtras();
        Point station = SharedResources.getSetOfPoints().find(
                bundle.getInt(LevePolaireActivity.STATION_NUMBER_LABEL));

        if (this.levePolaire == null) {
            this.levePolaire = new LevePolaire(station, true);
        }

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(bundle.getString(LevePolaireActivity.DETERMINATIONS_LABEL));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                Measure m = Measure.getMeasureFromJSON(json.toString());
                this.levePolaire.getDeterminations().add(m);
            }
        } catch (JSONException e) {
            // TODO
        }

        this.registerForContextMenu(this.resultsListView);

        this.levePolaire.compute();

        this.displayResults();
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
            this.saveAllPoints();
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
     * Display the list of resulting points.
     */
    private void displayResults() {
        this.adapter = new ArrayListOfResultsAdapter(this, R.layout.leve_polaire_results_list_item,
                this.levePolaire.getResults());
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
        Result r = this.adapter.getItem(position);
        if (SharedResources.getSetOfPoints().find(r.getDeterminationNumber()) == null) {
            Point point = new Point(
                    r.getDeterminationNumber(),
                    r.getEast(),
                    r.getNorth(),
                    r.getAltitude(),
                    false);
            SharedResources.getSetOfPoints().add(point);
            return true;
        } else {
            // this point already exists
            return false;
        }
    }

    /**
     * Pop up a confirmation dialog and save all points on approval.
     */
    private void saveAllPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_points)
                .setMessage(R.string.save_all_points)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.save_all,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LevePolaireResultsActivity.this.savePoints();
                                LevePolaireResultsActivity.this.adapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }
}