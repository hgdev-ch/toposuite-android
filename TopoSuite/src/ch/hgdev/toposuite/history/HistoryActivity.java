package ch.hgdev.toposuite.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
    public static final String        CALCULATION_POSITION = "calculation_position";
    private ListView                  list;
    private ArrayAdapter<Calculation> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_history);

        this.list = (ListView) this.findViewById(R.id.history_list);
        this.list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<?> activityClass = SharedResources.getCalculationsHistory().get(position)
                        .getActivityClass();

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.history_table_row_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.delete_calculation:
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
    public void drawList() {
        this.adapter = new ArrayAdapter<Calculation>(
                this, R.layout.history_list_item, SharedResources.getCalculationsHistory());
        this.list.setAdapter(this.adapter);
    }
}