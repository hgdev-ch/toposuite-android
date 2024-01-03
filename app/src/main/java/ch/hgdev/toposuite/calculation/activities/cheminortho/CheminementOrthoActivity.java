package ch.hgdev.toposuite.calculation.activities.cheminortho;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

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

    public static final String CHEMINEMENT_ORTHO = "cheminement_ortho";
    public static final String MEASURE_LABEL = "measure";
    public static final String MEASURE_POSITION = "measure_position";

    private static final String ORIGIN_SELECTED_POSITION = "origin_selected_position";
    private static final String EXTREMITY_SELECTED_POSITION = "extremity_selected_position";
    private static final String MEASURES_LIST_LABEL = "measures_list";

    private Spinner originSpinner;
    private Spinner extremitySpinner;

    private TextView originPointTextView;
    private TextView extremityPointTextView;
    private TextView calcDistTextView;

    private ListView measuresListView;
    private FloatingActionButton addButton;

    private int originSelectedPosition;
    private int extremitySelectedPosition;

    private CheminementOrthogonal cheminOrtho;

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
        this.addButton = (FloatingActionButton) this.findViewById(R.id.add_measure_button);

        this.originSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                CheminementOrthoActivity.this.originSelectedPosition = pos;

                Point pt = (Point)
                        CheminementOrthoActivity.this.originSpinner.getItemAtPosition(pos);
                if (!pt.getNumber().isEmpty()) {
                    CheminementOrthoActivity.this.originPointTextView.setText
                            (DisplayUtils.formatPoint(CheminementOrthoActivity.this, pt));
                } else {
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
                } else {
                    CheminementOrthoActivity.this.extremityPointTextView.setText("");
                }
                CheminementOrthoActivity.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // actually nothing
            }
        });

        this.measuresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheminementOrthoActivity.this.showEditMeasureDialog(position);
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  CheminementOrthoActivity.this.showAddMeasureDialog();
                                              }
                                          }
        );

        Bundle bundle = this.getIntent().getExtras();
        if ((bundle != null)) {
            int position = bundle.getInt(HistoryActivity.CALCULATION_POSITION);
            this.cheminOrtho = (CheminementOrthogonal) SharedResources.getCalculationsHistory().get(position);
        } else {
            this.cheminOrtho = new CheminementOrthogonal(true);
        }

        this.adapter = new ArrayListOfMeasuresAdapter(
                this, R.layout.determinations_list_item,
                new ArrayList<>(this.cheminOrtho.getMeasures()));
        this.registerForContextMenu(this.measuresListView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());

        ArrayAdapter<Point> a = new ArrayAdapter<>(this, R.layout.spinner_list_item, points);
        this.originSpinner.setAdapter(a);
        this.extremitySpinner.setAdapter(a);

        if (this.originSelectedPosition > 0) {
            this.originSpinner.setSelection(this.originSelectedPosition);
        } else {
            this.originSpinner.setSelection(a.getPosition(this.cheminOrtho.getOrthogonalBase().getOrigin()));
        }
        if (this.extremitySelectedPosition > 0) {
            this.extremitySpinner.setSelection(this.extremitySelectedPosition);
        } else {
            this.extremitySpinner.setSelection(a.getPosition(this.cheminOrtho.getOrthogonalBase().getExtremity()));
        }

        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_cheminement_ortho);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_run_calculation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CheminementOrthoActivity.ORIGIN_SELECTED_POSITION, this.originSelectedPosition);
        outState.putInt(CheminementOrthoActivity.EXTREMITY_SELECTED_POSITION, this.extremitySelectedPosition);
        outState.putSerializable(CheminementOrthoActivity.MEASURES_LIST_LABEL, this.adapter.getMeasures());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            this.originSelectedPosition = savedInstanceState.getInt(CheminementOrthoActivity.ORIGIN_SELECTED_POSITION);
            this.extremitySelectedPosition = savedInstanceState.getInt(CheminementOrthoActivity.EXTREMITY_SELECTED_POSITION);

            ArrayList<CheminementOrthogonal.Measure> measures =
                    (ArrayList<CheminementOrthogonal.Measure>) savedInstanceState.getSerializable(CheminementOrthoActivity.MEASURES_LIST_LABEL);
            this.adapter.clear();
            this.adapter.addAll(measures);
            this.drawList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.run_calculation_button:
                Point p1 = (Point) this.originSpinner.getSelectedItem();
                Point p2 = (Point) this.extremitySpinner.getSelectedItem();

                if ((p1.getNumber().isEmpty()) || (p2.getNumber().isEmpty()) || (this.adapter.getCount() < 2)) {
                    ViewUtils.showToast(this, this.getString(R.string.error_fill_data));
                    return true;
                }

                this.cheminOrtho.getMeasures().clear();
                this.cheminOrtho.getMeasures().addAll(this.adapter.getMeasures());

                Bundle bundle = new Bundle();
                bundle.putSerializable(CheminementOrthoActivity.CHEMINEMENT_ORTHO, this.cheminOrtho);

                Intent resultsActivityIntent = new Intent(this, CheminementOrthoResultsActivity.class);
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
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete_button:
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
        dialog.show(this.getSupportFragmentManager(), "AddPointDialogFragment");
    }

    private void showEditMeasureDialog(int pos) {
        ViewUtils.lockScreenOrientation(this);

        EditMeasureDialogFragment dialog = new EditMeasureDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(CheminementOrthoActivity.MEASURE_LABEL, this.adapter.getItem(pos));
        bundle.putInt(CheminementOrthoActivity.MEASURE_POSITION, pos);
        dialog.setArguments(bundle);

        dialog.show(this.getSupportFragmentManager(), "EditMeasureDialogFragment");
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
        CheminementOrthogonal.Measure m = this.adapter.getItem(dialog.getMeasurePosition());
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