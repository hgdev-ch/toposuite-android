package ch.hgdev.toposuite.calculation.activities.leveortho;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.calculation.LeveOrthogonal;
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

    private String number;
    private double abscissa;
    private double ordinate;
    private int measurePosition;

    private LinearLayout layout;
    private EditText numberEditText;
    private EditText abscissaEditText;
    private EditText ordinateEditText;

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genEditMeasureView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.measure_edit).setView(this.layout)
                .setPositiveButton(R.string.edit, (dialog, id) -> {
                    // overridden below because the dialog dismiss itself
                    // without a call to dialog.dismiss()...
                    // thus, it is impossible to handle error on user input
                    // without closing the dialog otherwise
                }).setNegativeButton(R.string.cancel, (dialog, which) -> EditMeasureDialogFragment.this.listener.onDialogCancel(EditMeasureDialogFragment.this));
        Dialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            Button addButton = ((AlertDialog) dialog1)
                    .getButton(DialogInterface.BUTTON_POSITIVE);
            addButton.setOnClickListener(view -> {
                if (EditMeasureDialogFragment.this.checkDialogInputs()) {
                    EditMeasureDialogFragment.this.number = ViewUtils.readString(EditMeasureDialogFragment.this.numberEditText);
                    EditMeasureDialogFragment.this.abscissa = ViewUtils.readDouble(EditMeasureDialogFragment.this.abscissaEditText);
                    EditMeasureDialogFragment.this.ordinate = ViewUtils.readDouble(EditMeasureDialogFragment.this.ordinateEditText);
                    EditMeasureDialogFragment.this.listener.onDialogEdit(EditMeasureDialogFragment.this);
                    dialog1.dismiss();
                } else {
                    ViewUtils.showToast(
                            EditMeasureDialogFragment.this.getActivity(),
                            EditMeasureDialogFragment.this.getActivity().getString(
                                    R.string.error_fill_data));
                }
            });
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (EditMeasureDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity
                    + " must implement EditMeasureDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        Bundle bundle = this.getArguments();

        this.measurePosition = bundle.getInt(LeveOrthoActivity.MEASURE_POSITION);
        LeveOrthogonal.Measure m = (LeveOrthogonal.Measure) bundle.getSerializable(LeveOrthoActivity.MEASURE_LABEL);
        this.number = m.getNumber();
        this.abscissa = m.getAbscissa();
        this.ordinate = m.getOrdinate();

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.numberEditText = new EditText(this.getActivity());
        this.numberEditText.setText(this.number);

        this.abscissaEditText = new EditText(this.getActivity());
        this.abscissaEditText.setText(DisplayUtils.toStringForEditText(this.abscissa));
        this.abscissaEditText.setInputType(App.getInputTypeCoordinate());

        this.ordinateEditText = new EditText(this.getActivity());
        this.ordinateEditText.setText(DisplayUtils.toStringForEditText(this.ordinate));
        this.ordinateEditText.setInputType(App.getInputTypeCoordinate());
    }

    /**
     * Create a view to get updated abscissa, ordinate and altitude value of a
     * point from the user.
     */
    private void genEditMeasureView() {
        this.layout.addView(this.numberEditText);
        this.layout.addView(this.abscissaEditText);
        this.layout.addView(this.ordinateEditText);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     *
     * @return True if every EditTexts of the dialog have been filled, false
     * otherwise.
     */
    private boolean checkDialogInputs() {
        if ((this.abscissaEditText.length() == 0) || (this.ordinateEditText.length() == 0)) {
            return false;
        }
        return true;
    }

    public String getNumber() {
        return this.number;
    }

    public double getAbscissa() {
        return this.abscissa;
    }

    public double getOrdinate() {
        return this.ordinate;
    }

    public int getMeasurePosition() {
        return this.measurePosition;
    }
}