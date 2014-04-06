package ch.hgdev.toposuite.calculation.activities.axisimpl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

class MeasureDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of MeasureDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface MeasureDialogListener {
        /**
         * Define what to do when the "Add" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogAdd(MeasureDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogEdit(MeasureDialogFragment dialog);

        void onDialogCancel();
    }

    MeasureDialogListener       listener;
    private Point               point;
    private double              horizDir;
    private double              distance;

    private LinearLayout        layout;
    private Spinner             pointSpinner;
    private TextView            pointTextView;
    private EditText            horizDirEditText;
    private EditText            distanceEditText;

    private ArrayAdapter<Point> adapter;

    /**
     * True if the dialog is for edition, false otherwise.
     */
    private final boolean       isEdition;

    /**
     * TODO make something cleaner and only use measure instead of this huge
     * amount of attributes...
     */
    private Measure             measure;

    public MeasureDialogFragment() {
        this.isEdition = false;
    }

    public MeasureDialogFragment(Measure m) {
        this.isEdition = true;
        this.measure = m;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();

        if (this.isEdition) {
            this.setAttributes();
        }

        this.genAddMeasureView();

        int positiveButtonRes = (this.isEdition) ? R.string.edit : R.string.add;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.measure_add))
                .setView(this.layout)
                .setPositiveButton(positiveButtonRes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // overridden below because the dialog dismiss itself
                        // without a call to dialog.dismiss()...
                        // thus, it is impossible to handle error on user input
                        // without closing the dialog otherwise
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MeasureDialogFragment.this.listener.onDialogCancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button addButton = ((AlertDialog) dialog)
                        .getButton(DialogInterface.BUTTON_POSITIVE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MeasureDialogFragment.this.checkDialogInputs()) {
                            MeasureDialogFragment.this.point =
                                    (Point) MeasureDialogFragment.this.pointSpinner
                                            .getSelectedItem();
                            MeasureDialogFragment.this.horizDir = ViewUtils
                                    .readDouble(MeasureDialogFragment.this.horizDirEditText);
                            MeasureDialogFragment.this.distance = ViewUtils
                                    .readDouble(MeasureDialogFragment.this.distanceEditText);

                            if (MeasureDialogFragment.this.isEdition) {
                                MeasureDialogFragment.this.listener
                                        .onDialogEdit(MeasureDialogFragment.this);
                            } else {
                                MeasureDialogFragment.this.listener
                                        .onDialogAdd(MeasureDialogFragment.this);
                            }
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    MeasureDialogFragment.this.getActivity(),
                                    MeasureDialogFragment.this.getActivity().getString(
                                            R.string.error_fill_data));
                        }
                    }
                });
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (MeasureDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MeasureDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.pointTextView = new TextView(this.getActivity());
        this.pointTextView.setText("");

        this.pointSpinner = new Spinner(this.getActivity());
        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Point point = (Point) MeasureDialogFragment.this.pointSpinner
                        .getItemAtPosition(pos);
                if (!point.getNumber().isEmpty()) {
                    MeasureDialogFragment.this.pointTextView.setText(DisplayUtils
                            .formatPoint(MeasureDialogFragment.this.getActivity(), point));
                } else {
                    MeasureDialogFragment.this.pointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        List<Point> points = new ArrayList<Point>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());
        this.adapter = new ArrayAdapter<Point>(
                this.getActivity(), R.layout.spinner_list_item, points);
        this.pointSpinner.setAdapter(this.adapter);

        this.horizDirEditText = new EditText(this.getActivity());
        this.horizDirEditText.setHint(
                this.getActivity().getString(R.string.horiz_direction_3dots)
                        + this.getActivity().getString(R.string.unit_gradian));
        this.horizDirEditText.setInputType(App.getInputTypeCoordinate());

        this.distanceEditText = new EditText(this.getActivity());
        this.distanceEditText.setHint(this.getActivity().getString(
                R.string.distance_3dots)
                + this.getActivity().getString(R.string.unit_meter));
        this.distanceEditText.setInputType(App.getInputTypeCoordinate());

        this.horizDir = 0.0;
        this.distance = 0.0;
    }

    /**
     * Fill views.
     */
    private void setAttributes() {
        this.point = this.measure.getPoint();
        this.horizDir = this.measure.getHorizDir();
        this.distance = this.measure.getDistance();

        this.pointSpinner.setSelection(this.adapter.getPosition(this.point));
        this.pointTextView.setText(DisplayUtils.formatPoint(
                this.getActivity(), this.point));
        this.horizDirEditText.setText(DisplayUtils.toStringForEditText(this.horizDir));
        this.distanceEditText.setText(DisplayUtils.toStringForEditText(this.distance));
    }

    /**
     * Create a view to get information from the user.
     */
    private void genAddMeasureView() {
        this.layout.addView(this.pointSpinner);
        this.layout.addView(this.pointTextView);
        this.layout.addView(this.horizDirEditText);
        this.layout.addView(this.distanceEditText);
    }

    /**
     * Verify that the user has entered all required data.
     * 
     * @return True if every required data has been filled, false otherwise.
     */
    private boolean checkDialogInputs() {
        return ((this.pointSpinner.getSelectedItemPosition() > 0)
                && (this.horizDirEditText.length() > 0)
                && (this.distanceEditText.length() > 0));
    }

    public final double getHorizDir() {
        return this.horizDir;
    }

    public final double getDistance() {
        return this.distance;
    }

    public final Point getPoint() {
        return this.point;
    }

    public final Measure getMeasure() {
        return this.measure;
    }
}