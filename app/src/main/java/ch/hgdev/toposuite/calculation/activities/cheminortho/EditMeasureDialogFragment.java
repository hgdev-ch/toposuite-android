package ch.hgdev.toposuite.calculation.activities.cheminortho;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.CheminementOrthogonal;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class EditMeasureDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of EditMeasureDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface EditMeasureDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(EditMeasureDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogEdit(EditMeasureDialogFragment dialog);
    }

    private EditMeasureDialogListener listener;

    private int measurePosition;
    private String number;
    private double distance;

    private LinearLayout layout;
    private EditText numberEditText;
    private EditText distanceEditText;

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
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
                EditMeasureDialogFragment.this.listener.onDialogCancel(EditMeasureDialogFragment.this);
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
                            EditMeasureDialogFragment.this.number = ViewUtils.readString(EditMeasureDialogFragment.this.numberEditText);
                            EditMeasureDialogFragment.this.distance = ViewUtils.readDouble(EditMeasureDialogFragment.this.distanceEditText);
                            EditMeasureDialogFragment.this.listener.onDialogEdit(EditMeasureDialogFragment.this);
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
        Bundle bundle = this.getArguments();

        CheminementOrthogonal.Measure m = (CheminementOrthogonal.Measure) bundle.getSerializable(CheminementOrthoActivity.MEASURE_LABEL);
        this.number = m.getNumber();
        this.distance = m.getDistance();
        this.measurePosition = bundle.getInt(CheminementOrthoActivity.MEASURE_POSITION);

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.numberEditText = new EditText(this.getActivity());
        this.numberEditText.setText(this.number);

        this.distanceEditText = new EditText(this.getActivity());
        this.distanceEditText.setText(DisplayUtils.toStringForEditText(this.distance));
        this.distanceEditText.setInputType(App.getInputTypeCoordinate());
    }

    /**
     * Create a view to get updated abscissa, ordinate and altitude value of a
     * point from the user.
     */
    private void genEditMeasureView() {
        this.layout.addView(this.numberEditText);
        this.layout.addView(this.distanceEditText);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     *
     * @return True if every EditTexts of the dialog have been filled, false
     * otherwise.
     */
    private boolean checkDialogInputs() {
        return this.distanceEditText.length() > 0;
    }

    public String getNumber() {
        return this.number;
    }

    public double getDistance() {
        return this.distance;
    }

    public int getMeasurePosition() {
        return measurePosition;
    }
}