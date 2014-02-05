package ch.hgdev.toposuite.history;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.Calculation;
import ch.hgdev.toposuite.calculation.Gisement;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

public class HistoryActivity extends TopoSuiteActivity {
    private TableLayout table;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        this.table = (TableLayout) findViewById(R.id.history_table);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_table_row_context_menu, menu);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        SharedResources.getCalculationsHistory().add(
                new Gisement("foobar", new Point(1,322.3232,323.323,134.564,true),
                        new Point(1,322.3232,323.323,134.564,true)));
        
        int pad = DisplayUtils.dpToPx(this, 16);
        
        for (Calculation c : SharedResources.getCalculationsHistory()) {
            TableRow row = new TableRow(this);
            row.setBaselineAligned(true);
            
            TextView col = new TextView(this);
            col.setText(c.getType());
            col.setPadding(pad, pad, pad, pad);
            row.addView(col);
            
            col = new TextView(this);
            col.setText(c.getDescription());
            col.setPadding(pad, pad, pad, pad);
            row.addView(col);
            
            col = new TextView(this);
            col.setPadding(pad, pad, pad, pad);
            col.setGravity(Gravity.CENTER);
            col.setText(DisplayUtils.formatDate(c.getLastModification()));
            row.addView(col);
            
            this.table.addView(row);
            this.registerForContextMenu(row);
        }
    }
}