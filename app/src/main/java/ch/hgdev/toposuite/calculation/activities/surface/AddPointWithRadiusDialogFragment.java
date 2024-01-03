package ch.hgdev.toposuite.calculation.activities.surface;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class AddPointWithRadiusDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddPointWithRadiusDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface AddPointWithRadiusDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddPointWithRadiusDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogAdd(AddPointWithRadiusDialogFragment dialog);
    }

    private AddPointWithRadiusDialogListener listener;
    private LinearLayout             layout;
    private Point                    point;
    private Spinner                  pointSpinner;
    private TextView                 pointTextView;
    private double                   radius;
    private EditText                 radiusEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddMeasureView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.dialog_add_point).setView(this.layout)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
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
                        AddPointWithRadiusDialogFragment.this.listener
                                .onDialogCancel(AddPointWithRadiusDialogFragment.this);
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
                        if (AddPointWithRadiusDialogFragment.this.checkDialogInputs()) {
                            if (!ViewUtils.isEmpty(
                                    AddPointWithRadiusDialogFragment.this.radiusEditText)) {
                                AddPointWithRadiusDialogFragment.this.radius = ViewUtils
                                        .readDouble(AddPointWithRadiusDialogFragment.this.radiusEditText);
                            }
                            AddPointWithRadiusDialogFragment.this.point =
                                    (Point) AddPointWithRadiusDialogFragment.this.pointSpinner
                                            .getSelectedItem();
                            AddPointWithRadiusDialogFragment.this.listener
                                    .onDialogAdd(AddPointWithRadiusDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    AddPointWithRadiusDialogFragment.this.getActivity(),
                                    AddPointWithRadiusDialogFragment.this.getActivity().getString(
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
            this.listener = (AddPointWithRadiusDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddPointWithRadiusDialogListener");
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

        this.radiusEditText = new EditText(this.getActivity());
        this.radiusEditText.setHint(
                this.getActivity().getString(R.string.radius_3dots)
                        + this.getActivity().getString(R.string.unit_meter)
                        + this.getActivity().getString(R.string.optional_prths));
        this.radiusEditText.setInputType(App.getInputTypeCoordinate());

        this.pointSpinner = new Spinner(this.getActivity());
        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                AddPointWithRadiusDialogFragment.this.point = (Point) AddPointWithRadiusDialogFragment
                        .this.pointSpinner.getItemAtPosition(pos);
                if (!AddPointWithRadiusDialogFragment.this.point.getNumber().isEmpty()) {
                    AddPointWithRadiusDialogFragment.this.pointTextView.setText(DisplayUtils
                            .formatPoint(AddPointWithRadiusDialogFragment.this.getActivity(),
                                    AddPointWithRadiusDialogFragment.this.point));
                } else {
                    AddPointWithRadiusDialogFragment.this.pointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        List<Point> points = new ArrayList<>();
        points.add(new Point("", 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());
        ArrayAdapter<Point> a = new ArrayAdapter<>(
                this.getActivity(), R.layout.spinner_list_item, points);
        this.pointSpinner.setAdapter(a);

        this.radius = 0.0;
    }

    /**
     * Create a view to get number, abscissa, ordinate and altitude of a point
     * from the user.
     * 
     */
    private void genAddMeasureView() {
        this.layout.addView(this.pointSpinner);
        this.layout.addView(this.pointTextView);
        this.layout.addView(this.radiusEditText);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     * 
     * @return True if every EditTexts of the dialog have been filled, false
     *         otherwise.
     */
    private boolean checkDialogInputs() {
        if (this.pointSpinner.getSelectedItemPosition() < 1) {
            return false;
        }
        return true;
    }

    public Point getPoint() {
        return this.point;
    }

    public double getRadius() {
        return this.radius;
    }
}
