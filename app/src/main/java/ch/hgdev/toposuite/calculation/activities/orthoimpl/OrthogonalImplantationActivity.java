package ch.hgdev.toposuite.calculation.activities.orthoimpl;

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
import ch.hgdev.toposuite.calculation.OrthogonalBase;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class OrthogonalImplantationActivity extends TopoSuiteActivity
        implements AddMeasureDialogFragment.AddMeasureDialogListener,
        EditMeasureDialogFragment.EditMeasureDialogListener {
    public static final String     ORIGIN_SELECTED_POSITION    = "origin_selected_position";
    public static final String     EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    public static final String     ORTHO_IMPL_POSITION         = "ortho_impl_position";
    public static final String     MEASURE_POSITION            = "measure_position";

    private Spinner                originSpinner;
    private Spinner                extremitySpinner;

    private TextView               originPointTextView;
    private TextView               extremityPointTextView;
    private TextView               calcDistTextView;

    private ListView               measuresListView;

    private int                    originSelectedPosition;
    private int                    extremitySelectedPosition;

    private OrthogonalImplantation orthoImpl;

    private ArrayAdapter<Point>    adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_orthogonal_implantation);

        this.originSpinner = (Spinner) this.findViewById(R.id.origin_spinner);
        this.extremitySpinner = (Spinner) this.findViewById(R.id.extremity_spinner);

        this.originPointTextView = (TextView) this.findViewById(R.id.origin_point);
        this.extremityPointTextView = (TextView) this.findViewById(R.id.extremity_point);
        this.calcDistTextView = (TextView) this.findViewById(R.id.calculated_distance);

        this.measuresListView = (ListView) this.findViewById(R.id.points_list);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                OrthogonalImplantationActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        OrthogonalImplantationActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    OrthogonalImplantationActivity.this.originPointTextView.setText
                            (DisplayUtils.formatPoint(OrthogonalImplantationActivity.this, pt));
                }
                else {
                    OrthogonalImplantationActivity.this.originPointTextView.setText("");
                }
                OrthogonalImplantationActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.extremitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                OrthogonalImplantationActivity.this.extremitySelectedPosition = pos;

                Point pt = (Point)
                        OrthogonalImplantationActivity.this.extremitySpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    OrthogonalImplantationActivity.this.extremityPointTextView.setText
                            (DisplayUtils.formatPoint(OrthogonalImplantationActivity.this, pt));
                }
                else {
                    OrthogonalImplantationActivity.this.extremityPointTextView.setText("");
                }
                OrthogonalImplantationActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.orthoImpl = (OrthogonalImplantation) SharedResources
                    .getCalculationsHistory().get(position);
        }

        this.drawList();

        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.leve_ortho, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.extremitySpinner.setAdapter(a);

        if (this.orthoImpl != null) {
            this.originSpinner.setSelection(
                    a.getPosition(this.orthoImpl.getOrthogonalBase().getOrigin()));
            this.extremitySpinner.setSelection(
                    a.getPosition(this.orthoImpl.getOrthogonalBase().getExtremity()));
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
        return this.getString(R.string.title_activity_ortho_implantation_results);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.orthogonal_implantation_context_menu, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(OrthogonalImplantationActivity.ORIGIN_SELECTED_POSITION,
                this.originSelectedPosition);
        outState.putInt(OrthogonalImplantationActivity.EXTREMITY_SELECTED_POSITION,
                this.extremitySelectedPosition);

        if (this.orthoImpl != null) {
            int index = SharedResources.getCalculationsHistory().indexOf(this.orthoImpl);
            outState.putInt(OrthogonalImplantationActivity.ORTHO_IMPL_POSITION,
                    index);
        } else {
            outState.putInt(OrthogonalImplantationActivity.ORTHO_IMPL_POSITION,
                    -1);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(
                    OrthogonalImplantationActivity.ORTHO_IMPL_POSITION);
            if (index != -1) {
                if (this.adapter != null) {
                    this.adapter.clear();
                }

                this.orthoImpl = (OrthogonalImplantation) SharedResources
                        .getCalculationsHistory().get(index);
                this.drawList();
            } else {
                this.originSelectedPosition = savedInstanceState
                        .getInt(OrthogonalImplantationActivity.ORIGIN_SELECTED_POSITION);
                this.extremitySelectedPosition = savedInstanceState
                        .getInt(OrthogonalImplantationActivity.EXTREMITY_SELECTED_POSITION);
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
            if ((this.originSelectedPosition == 0) || (this.extremitySelectedPosition == 0)
                    || (this.adapter.getCount() == 0)) {
                ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                return true;
            }

            int position = SharedResources.getCalculationsHistory()
                    .indexOf(this.orthoImpl);

            Bundle bundle = new Bundle();
            bundle.putInt(OrthogonalImplantationActivity.ORTHO_IMPL_POSITION,
                    position);

            Intent resultsActivityIntent = new Intent(this,
                    OrthoImplantationResultsActivity.class);
            resultsActivityIntent.putExtras(bundle);
            this.startActivity(resultsActivityIntent);

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void drawList() {
        if (this.orthoImpl != null) {
            this.adapter = new ArrayAdapter<Point>(this, R.layout.history_list_item,
                    this.orthoImpl.getMeasures());
        } else {
            this.adapter = new ArrayAdapter<Point>(this, R.layout.history_list_item);
        }
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
            if (this.orthoImpl == null) {
                this.orthoImpl = new OrthogonalImplantation(p1, p2, true);
            } else {
                if (this.orthoImpl.getOrthogonalBase() != null) {
                    this.orthoImpl.getOrthogonalBase().setOrigin(p1);
                    this.orthoImpl.getOrthogonalBase().setExtremity(p2);
                } else {
                    this.orthoImpl.setOrthogonalBase(new OrthogonalBase(p1, p2));
                }
            }

            this.calcDistTextView.setText(DisplayUtils.formatGap(
                    MathUtils.euclideanDistance(p1, p2)));
        }
    }

    private void resetResults() {
        this.calcDistTextView.setText("");
    }

    private void showAddMeasureDialog() {
        ViewUtils.lockScreenOrientation(this);

        AddMeasureDialogFragment dialog = new AddMeasureDialogFragment();
        dialog.show(this.getFragmentManager(), "MeasureDialogFragment");
    }

    private void showEditMeasureDialog(int pos) {
        ViewUtils.lockScreenOrientation(this);

        EditMeasureDialogFragment dialog = new EditMeasureDialogFragment();

        int orthImplPos = SharedResources.getCalculationsHistory().indexOf(
                this.orthoImpl);

        Bundle bundle = new Bundle();
        bundle.putInt(OrthogonalImplantationActivity.ORTHO_IMPL_POSITION, orthImplPos);
        bundle.putInt(OrthogonalImplantationActivity.MEASURE_POSITION, pos);

        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "EditMeasureDialogFragment");
    }

    @Override
    public void onDialogAdd(AddMeasureDialogFragment dialog) {
        if (this.orthoImpl == null) {
            this.orthoImpl = new OrthogonalImplantation(true);
        }

        this.orthoImpl.getMeasures().add(dialog.getPoint());

        this.adapter.add(dialog.getPoint());
        this.adapter.notifyDataSetChanged();
        this.showAddMeasureDialog();
    }

    @Override
    public void onDialogCancel(AddMeasureDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditMeasureDialogFragment dialog) {
        @SuppressWarnings("unused")
        Point p = this.adapter.getItem(dialog.getMeasurePosition());
        p = dialog.getPoint();
        this.adapter.notifyDataSetChanged();

        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(EditMeasureDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }
}
