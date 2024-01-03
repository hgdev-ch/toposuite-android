package ch.hgdev.toposuite.calculation.activities.leveortho;

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
import ch.hgdev.toposuite.utils.ViewUtils;

public class AddMeasureDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddPointDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface AddMeasureDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddMeasureDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogAdd(AddMeasureDialogFragment dialog);
    }

    private AddMeasureDialogListener listener;

    private String number;
    private double abscissa;
    private double ordinate;

    private LinearLayout layout;

    private EditText numberEditText;
    private EditText abscissaEditText;
    private EditText ordinateEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddMeasureView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.measure_add).setView(this.layout)
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
                AddMeasureDialogFragment.this.listener
                        .onDialogCancel(AddMeasureDialogFragment.this);
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
                        if (AddMeasureDialogFragment.this.checkDialogInputs()) {
                            AddMeasureDialogFragment.this.number = ViewUtils.readString(AddMeasureDialogFragment.this.numberEditText);
                            AddMeasureDialogFragment.this.abscissa = ViewUtils.readDouble(AddMeasureDialogFragment.this.abscissaEditText);
                            AddMeasureDialogFragment.this.ordinate = ViewUtils.readDouble(AddMeasureDialogFragment.this.ordinateEditText);
                            AddMeasureDialogFragment.this.listener.onDialogAdd(AddMeasureDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    AddMeasureDialogFragment.this.getActivity(),
                                    AddMeasureDialogFragment.this.getActivity().getString(
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
            this.listener = (AddMeasureDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddPointDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.numberEditText = new EditText(this.getActivity());
        this.numberEditText.setSingleLine();
        this.numberEditText.setHint(this.getActivity().getString(R.string.point_number_3dots));

        this.abscissaEditText = new EditText(this.getActivity());
        this.abscissaEditText.setHint(this.getActivity().getString(R.string.abscissa_3dots)
                + this.getActivity().getString(R.string.unit_meter));
        this.abscissaEditText.setInputType(App.getInputTypeCoordinate());

        this.ordinateEditText = new EditText(this.getActivity());
        this.ordinateEditText.setHint(this.getActivity().getString(R.string.ordinate_3dots)
                + this.getActivity().getString(R.string.unit_meter));

        this.ordinateEditText.setInputType(App.getInputTypeCoordinate());

        this.number = "";
        this.abscissa = 0.0;
        this.ordinate = 0.0;
    }

    /**
     * Create a view to get number, abscissa, ordinate and altitude of a point
     * from the user.
     */
    private void genAddMeasureView() {
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
        if ((this.numberEditText.length() == 0) || (this.abscissaEditText.length() == 0)
                || (this.ordinateEditText.length() == 0)) {
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
}