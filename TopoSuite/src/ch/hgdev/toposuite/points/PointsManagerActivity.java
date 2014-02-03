package ch.hgdev.toposuite.points;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Activity to manage points, such as adding, removing or modifying them.
 * 
 * @author HGdev
 * 
 */
public class PointsManagerActivity extends TopoSuiteActivity {

    private TableLayout mainTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.mainTable = (TableLayout) this.findViewById(R.id.apm_header_table_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.points_manager, menu);
        return true;
    }

    /**
     * Add a point to the main table of points.
     * 
     * @param point
     */
    private void addPointToMainTable(Point point) {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams();

        // wrap the content of a row
        rowParams.height = LayoutParams.WRAP_CONTENT;
        rowParams.width = LayoutParams.WRAP_CONTENT;

        // wrap the content of a cell
        TableRow.LayoutParams cellParams = new TableRow.LayoutParams();
        cellParams.height = LayoutParams.WRAP_CONTENT;
        cellParams.width = LayoutParams.WRAP_CONTENT;
        cellParams.gravity = Gravity.CENTER;

        TextView cell = new TextView(this);

        // point number cell
        cell.setText(point.getNumber());
        row.addView(cell, cellParams);

        // east cell
        cell.setText(DisplayUtils.toString(point.getEast()));
        row.addView(cell, cellParams);

        // north cell
        cell.setText(DisplayUtils.toString(point.getNorth()));
        row.addView(cell, cellParams);

        // altitude cell
        cell.setText(DisplayUtils.toString(point.getAltitude()));
        row.addView(cell, cellParams);

        // base point cell
        cell.setText(point.getBasePoint());
        row.addView(cell, cellParams);

        this.mainTable.addView(row, rowParams);
    }
}
