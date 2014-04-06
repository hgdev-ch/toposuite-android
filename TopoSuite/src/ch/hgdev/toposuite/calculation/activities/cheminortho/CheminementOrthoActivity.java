package ch.hgdev.toposuite.calculation.activities.cheminortho;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.calculation.OrthogonalBase;
import ch.hgdev.toposuite.calculation.activities.cheminortho.AddMeasureDialogFragment.AddMeasureDialogListener;
import ch.hgdev.toposuite.calculation.activities.cheminortho.EditMeasureDialogFragment.EditMeasureDialogListener;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class CheminementOrthoActivity extends TopoSuiteActivity implements
        AddMeasureDialogListener, EditMeasureDialogListener {

    public static final String         ORIGIN_SELECTED_POSITION    = "origine_selected_position";
    public static final String         EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    public static final String         CHEMINEMENT_ORTHO_POSITION  = "leve_ortho_position";
    public static final String         MEASURE_POSITION            = "measure_position";

    private Spinner                    originSpinner;
    private Spinner                    extremitySpinner;

    private TextView                   originPointTextView;
    private TextView                   extremityPointTextView;
    private TextView                   calcDistTextView;

    private ListView                   measuresListView;

    private int                        originSelectedPosition;
    private int                        extremitySelectedPosition;

    private CheminementOrthogonal      cheminOrtho;

    private ArrayListOfMeasuresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cheminement_ortho);

        this.originSelectedPosition = 0;
        this.extremitySelectedPosition = 0;

        this.originSpinner = (Spinner) this.findViewById(R.id.origin_spinner);
        this.extremitySpinner = (Spinner) this.findViewById(R.id.extremity_spinner);

        this.originPointTextView = (TextView) this.findViewById(R.id.origin_point);
        this.extremityPointTextView = (TextView) this.findViewById(R.id.extremity_point);
        this.calcDistTextView = (TextView) this.findViewById(R.id.calculated_distance);
        this.measuresListView = (ListView) this.findViewById(R.id.points_list);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CheminementOrthoActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        CheminementOrthoActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    CheminementOrthoActivity.this.originPointTextView.setText
                            (DisplayUtils.formatPoint(CheminementOrthoActivity.this, pt));
                }
                else {
                    CheminementOrthoActivity.this.originPointTextView.setText("");
                }
                CheminementOrthoActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.extremitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CheminementOrthoActivity.this.extremitySelectedPosition = pos;

                Point pt = (Point)
                        CheminementOrthoActivity.this.extremitySpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    CheminementOrthoActivity.this.extremityPointTextView.setText
                            (DisplayUtils.formatPoint(CheminementOrthoActivity.this, pt));
                }
                else {
                    CheminementOrthoActivity.this.extremityPointTextView.setText("");
                }
                CheminementOrthoActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.cheminOrtho = (CheminementOrthogonal) SharedResources.getCalculationsHistory()
                    .get(position);
        } else {
            this.cheminOrtho = new CheminementOrthogonal(true);
        }

        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.adapter = new ArrayListOfMeasuresAdapter(this,
                R.layout.determinations_list_item, this.cheminOrtho.getMeasures());
        this.drawList();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.extremitySpinner.setAdapter(a);

        if (this.cheminOrtho != null) {
            this.originSpinner.setSelection(
                    a.getPosition(this.cheminOrtho.getOrthogonalBase().getOrigin()));
            this.extremitySpinner.setSelection(
                    a.getPosition(this.cheminOrtho.getOrthogonalBase().getExtremity()));
        } else {
            if (this.originSelectedPosition > 0) {
                this.originSpinner.setSelection(
                        this.originSelectedPosition);
            }

            if (this.extremitySelectedPosition > 0) {
                this.extremitySpinner.setSelection(
                        this.extremitySelectedPosition);
            }
        }
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_cheminement_ortho);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.leve_ortho, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CheminementOrthoActivity.ORIGIN_SELECTED_POSITION,
                this.originSelectedPosition);
        outState.putInt(CheminementOrthoActivity.EXTREMITY_SELECTED_POSITION,
                this.extremitySelectedPosition);

        if (this.cheminOrtho != null) {
            int index = SharedResources.getCalculationsHistory().indexOf(this.cheminOrtho);
            outState.putInt(CheminementOrthoActivity.CHEMINEMENT_ORTHO_POSITION, index);
        } else {
            outState.putInt(CheminementOrthoActivity.CHEMINEMENT_ORTHO_POSITION, -1);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            int index = savedInstanceState
                    .getInt(CheminementOrthoActivity.CHEMINEMENT_ORTHO_POSITION);
            if (index != -1) {
                this.cheminOrtho = (CheminementOrthogonal) SharedResources.getCalculationsHistory()
                        .get(index);
                this.drawList();
            } else {
                this.originSelectedPosition = savedInstanceState
                        .getInt(CheminementOrthoActivity.ORIGIN_SELECTED_POSITION);
                this.extremitySelectedPosition = savedInstanceState
                        .getInt(CheminementOrthoActivity.EXTREMITY_SELECTED_POSITION);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.add_point_button:
            this.showAddMeasureDialog();
            return true;
        case R.id.run_calculation_button:
            Point p1 = (Point) this.originSpinner.getSelectedItem();
            Point p2 = (Point) this.extremitySpinner.getSelectedItem();

            if ((p1.getNumber().isEmpty()) || (p2.getNumber().isEmpty()) ||
                    (this.adapter.getCount() < 2)) {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                return true;
            }

            int position = SharedResources.getCalculationsHistory()
                    .indexOf(this.cheminOrtho);

            Bundle bundle = new Bundle();
            bundle.putInt(CheminementOrthoActivity.CHEMINEMENT_ORTHO_POSITION, position);

            Intent resultsActivityIntent = new Intent(this,
                    CheminementOrthoResultsActivity.class);
            resultsActivityIntent.putExtras(bundle);
            this.startActivity(resultsActivityIntent);

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.leve_ortho_measures_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.edit_measure:
            this.showEditMeasureDialog(info.position);
            return true;
        case R.id.delete_measure:
            this.adapter.remove(this.adapter.getItem(info.position));
            this.adapter.notifyDataSetChanged();
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void drawList() {
        this.measuresListView.setAdapter(this.adapter);
    }

    private void itemSelected() {
        Point p1 = (Point) this.originSpinner.getSelectedItem();
        Point p2 = (Point) this.extremitySpinner.getSelectedItem();

        if ((p1.getNumber().isEmpty()) || (p2.getNumber().isEmpty())) {
            this.resetResults();
        } else if (p1.getNumber().equals(p2.getNumber())) {
            this.resetResults();
            ViewUtils.showToast(this, this.getString(R.string.error_same_points));
        } else {
            if (this.cheminOrtho.getOrthogonalBase() != null) {
                this.cheminOrtho.getOrthogonalBase().setOrigin(p1);
                this.cheminOrtho.getOrthogonalBase().setExtremity(p2);
            } else {
                this.cheminOrtho.setOrthogonalBase(new OrthogonalBase(p1, p2));
            }

            this.calcDistTextView.setText(DisplayUtils.formatDistance(
                    this.cheminOrtho.getOrthogonalBase().getCalculatedDistance()));
        }
    }

    private void resetResults() {
        this.calcDistTextView.setText("");
    }

    private void showAddMeasureDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddMeasureDialogFragment dialog = new AddMeasureDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointDialogFragment");
    }

    private void showEditMeasureDialog(int pos) {
        ViewUtils.lockScreenOrientation(this);

        EditMeasureDialogFragment dialog = new EditMeasureDialogFragment();

        int leveOrthoPos = SharedResources.getCalculationsHistory().indexOf(this.cheminOrtho);

        Bundle bundle = new Bundle();
        bundle.putInt(CheminementOrthoActivity.CHEMINEMENT_ORTHO_POSITION, leveOrthoPos);
        bundle.putInt(CheminementOrthoActivity.MEASURE_POSITION, pos);

        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "EditMeasureDialogFragment");
    }

    @Override
    public void onDialogAdd(AddMeasureDialogFragment dialog) {
        String number = dialog.getNumber();
        double distance = dialog.getDistance();

        CheminementOrthogonal.Measure m = new CheminementOrthogonal.Measure(number, distance);

        this.adapter.add(m);
        this.adapter.notifyDataSetChanged();

        this.showAddMeasureDialog();
    }

    @Override
    public void onDialogCancel(AddMeasureDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditMeasureDialogFragment dialog) {
        CheminementOrthogonal.Measure m = this.cheminOrtho.getMeasures().get(
                dialog.getMeasurePosition());

        m.setNumber(dialog.getNumber());
        m.setDistance(dialog.getDistance());
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditMeasureDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }
}