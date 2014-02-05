package ch.hgdev.toposuite.history;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
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
    
    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        SharedResources.getCalculationsHistory().add(
                new Gisement("foobar", new Point(1,322.3232,323.323,134.564,true),
                        new Point(1,322.3232,323.323,134.564,true)));
        
        for (Calculation c : SharedResources.getCalculationsHistory()) {
            TableRow row = new TableRow(this);
            row.setBaselineAligned(true);
            
            TextView col = new TextView(this);
            col.setText(c.getType());
            col.setPadding(dpToPx(12), dpToPx(16), dpToPx(12), dpToPx(16));
            row.addView(col);
            
            col = new TextView(this);
            col.setText(c.getDescription());
            col.setPadding(dpToPx(12), dpToPx(16), dpToPx(12), dpToPx(16));
            row.addView(col);
            
            col = new TextView(this);
            col.setPadding(dpToPx(12), dpToPx(16), dpToPx(12), dpToPx(16));
            col.setText(DisplayUtils.formatDate(c.getLastModification()));
            row.addView(col);
            
            this.table.addView(row);
        }
    }
}