package ch.hgdev.toposuite.calculation.activities.orthoimpl;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.OrthogonalImplantation;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class EditMeasureDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of EditMeasureDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface EditMeasureDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(EditMeasureDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogEdit(EditMeasureDialogFragment dialog);
    }

    private Bundle                    bundle;
    private EditMeasureDialogListener listener;

    private Point                     point;
    private int                       measurePosition;

    private LinearLayout              layout;
    private Spinner                   pointSpinner;
    private TextView                  pointTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genEditMeasureView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.measure_edit).setView(this.layout)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // overridden below because the dialog dismiss itself
                        // without a call to dialog.dismiss()...
                        // thus, it is impossible to handle error on user input
                        // without closing the dialog otherwise
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditMeasureDialogFragment.this.listener
                                .onDialogCancel(EditMeasureDialogFragment.this);
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
                    public void onClick(View view) {
                        if (EditMeasureDialogFragment.this.checkDialogInputs()) {
                            EditMeasureDialogFragment.this.point =
                                    (Point) EditMeasureDialogFragment.this.pointSpinner
                                            .getSelectedItem();
                            EditMeasureDialogFragment.this.listener
                                    .onDialogEdit(EditMeasureDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    EditMeasureDialogFragment.this.getActivity(),
                                    EditMeasureDialogFragment.this.getActivity().getString(
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
            this.listener = (EditMeasureDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditMeasureDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.bundle = this.getArguments();

        this.pointTextView = new TextView(this.getActivity());
        this.pointTextView.setText("");

        this.pointSpinner = new Spinner(this.getActivity());
        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Point point = (Point) EditMeasureDialogFragment.this.pointSpinner
                        .getItemAtPosition(pos);
                if (!point.getNumber().isEmpty()) {
                    EditMeasureDialogFragment.this.pointTextView.setText(DisplayUtils
                            .formatPoint(EditMeasureDialogFragment.this.getActivity(), point));
                } else {
                    EditMeasureDialogFragment.this.pointTextView.setText("");
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
        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this.getActivity(), R.layout.spinner_list_item, points);
        this.pointSpinner.setAdapter(a);

        int leveOrthoPos = this.bundle.getInt(
                OrthogonalImplantationActivity.ORTHO_IMPL_POSITION);
        this.measurePosition = this.bundle.getInt(
                OrthogonalImplantationActivity.MEASURE_POSITION);
        OrthogonalImplantation lo = (OrthogonalImplantation) SharedResources
                .getCalculationsHistory().get(
                        leveOrthoPos);

        this.point = lo.getMeasures().get(this.measurePosition);

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * Create a view to get updated abscissa, ordinate and altitude value of a
     * point from the user.
     * 
     */
    private void genEditMeasureView() {
        this.layout.addView(this.pointSpinner);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     * 
     * @return True if every EditTexts of the dialog have been filled, false
     *         otherwise.
     */
    private boolean checkDialogInputs() {
        if (this.pointSpinner.getSelectedItemPosition() == 0) {
            return false;
        }

        return true;
    }

    public Point getPoint() {
        return this.point;
    }

    public int getMeasurePosition() {
        return this.measurePosition;
    }
}
