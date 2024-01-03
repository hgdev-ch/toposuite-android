package ch.hgdev.toposuite.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Calculation;

/**
 * Activity for visualizing the calculations history.
 *
 * @author HGdev
 */
public class HistoryActivity extends TopoSuiteActivity {
    public static final String CALCULATION_POSITION = "calculation_position";
    private ListView list;
    private ArrayAdapter<Calculation> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_history);

        this.list = (ListView) this.findViewById(R.id.history_list);
        this.list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<?> activityClass = SharedResources.getCalculationsHistory().get(position).getActivityClass();

                Bundle bundle = new Bundle();
                bundle.putInt(HistoryActivity.CALCULATION_POSITION, position);

                Intent newActivityIntent = new Intent(HistoryActivity.this, activityClass);
                newActivityIntent.putExtras(bundle);

                HistoryActivity.this.startActivity(newActivityIntent);
            }
        });

        this.registerForContextMenu(this.list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.delete_button:
                this.clearHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_button:
                Calculation calc = SharedResources.getCalculationsHistory().get((int) info.id);
                this.adapter.remove(calc);
                this.adapter.notifyDataSetChanged();
                SharedResources.getCalculationsHistory().remove(info.id);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Draw the list.
     */
    private void drawList() {
        this.adapter = new ArrayAdapter<>(this, R.layout.history_list_item, SharedResources.getCalculationsHistory());
        this.list.setAdapter(this.adapter);
    }

    private void clearHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_all_history)
                .setMessage(R.string.loose_history)
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.delete_all,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedResources.getCalculationsHistory().clear();
                                HistoryActivity.this.adapter.clear();
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