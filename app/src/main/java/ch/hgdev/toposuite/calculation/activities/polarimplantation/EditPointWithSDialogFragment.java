package ch.hgdev.toposuite.calculation.activities.polarimplantation;

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

public class EditPointWithSDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of EditPointWithSDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface EditPointWithSDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(EditPointWithSDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogEdit(EditPointWithSDialogFragment dialog);
    }

    private EditPointWithSDialogListener listener;
    private LinearLayout layout;
    private Point point;
    private Spinner pointSpinner;
    private TextView pointTextView;
    private double s;
    private EditText sEditText;
    private int position;

    @Override
    public @NonNull Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddMeasureView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.dialog_edit_point).setView(this.layout)
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
                EditPointWithSDialogFragment.this.listener
                        .onDialogCancel(EditPointWithSDialogFragment.this);
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
                        if (EditPointWithSDialogFragment.this.checkDialogInputs()) {
                            EditPointWithSDialogFragment.this.s = ViewUtils.readDouble(EditPointWithSDialogFragment.this.sEditText);
                            EditPointWithSDialogFragment.this.point = (Point) EditPointWithSDialogFragment.this.pointSpinner.getSelectedItem();
                            EditPointWithSDialogFragment.this.listener.onDialogEdit(EditPointWithSDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    EditPointWithSDialogFragment.this.getActivity(),
                                    EditPointWithSDialogFragment.this.getActivity().getString(
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
            this.listener = (EditPointWithSDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditPointWithSDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        Bundle bundle = this.getArguments();

        this.s = bundle.getDouble(PolarImplantationActivity.S);
        this.position = bundle.getInt(PolarImplantationActivity.POINT_WITH_S_POSITION);

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.pointTextView = new TextView(this.getActivity());
        this.pointTextView.setText("");

        this.sEditText = new EditText(this.getActivity());
        this.sEditText.setHint(
                this.getActivity().getString(R.string.prism_height_3dots)
                        + this.getActivity().getString(R.string.unit_meter)
                        + this.getActivity().getString(R.string.optional_prths));
        this.sEditText.setText(DisplayUtils.toStringForEditText(this.s));
        this.sEditText.setInputType(App.getInputTypeCoordinate());

        this.pointSpinner = new Spinner(this.getActivity());
        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                EditPointWithSDialogFragment.this.point = (Point) EditPointWithSDialogFragment.this.pointSpinner.getItemAtPosition(pos);
                if (!EditPointWithSDialogFragment.this.point.getNumber().isEmpty()) {
                    EditPointWithSDialogFragment.this.pointTextView.setText(DisplayUtils
                            .formatPoint(EditPointWithSDialogFragment.this.getActivity(),
                                    EditPointWithSDialogFragment.this.point));
                } else {
                    EditPointWithSDialogFragment.this.pointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());
        ArrayAdapter<Point> a = new ArrayAdapter<>(
                this.getActivity(), R.layout.spinner_list_item, points);
        this.pointSpinner.setAdapter(a);

        String pointNumber = bundle.getString(PolarImplantationActivity.POINT_WITH_S_NUMBER_LABEL);
        this.pointSpinner.setSelection(a.getPosition(
                SharedResources.getSetOfPoints().find(pointNumber)));
    }

    /**
     * Create a view to get number, abscissa, ordinate and altitude of a point
     * from the user.
     */
    private void genAddMeasureView() {
        this.layout.addView(this.pointSpinner);
        this.layout.addView(this.pointTextView);
        this.layout.addView(this.sEditText);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     *
     * @return True if every EditTexts of the dialog have been filled, false
     * otherwise.
     */
    private boolean checkDialogInputs() {
        return this.pointSpinner.getSelectedItemPosition() > 0;
    }

    public Point getPoint() {
        return this.point;
    }

    public double getS() {
        return this.s;
    }

    public int getPosition() {
        return this.position;
    }
}
