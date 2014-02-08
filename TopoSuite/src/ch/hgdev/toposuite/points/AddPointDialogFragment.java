package ch.hgdev.toposuite.points;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import ch.hgdev.toposuite.R;

/**
 * Dialog window to allow the user to add a new point to the list of points.
 * 
 * @author HGdev
 * 
 */
public class AddPointDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddPointDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface AddPointDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddPointDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogAdd(AddPointDialogFragment dialog);
    }

    AddPointDialogListener listener;
    private int            number;
    private double         altitude;
    private double         east;
    private double         north;
    private LinearLayout   layout;
    private EditText       altitudeEditText;
    private EditText       eastEditText;
    private EditText       northEditText;
    private EditText       numberEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddPointView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.dialog_add_point).setView(this.layout)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddPointDialogFragment.this.listener
                                .onDialogCancel(AddPointDialogFragment.this);
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
                        if (AddPointDialogFragment.this.checkDialogInputs()) {
                            // altitude is not mandatory
                            if (AddPointDialogFragment.this.altitudeEditText.length() > 0) {
                                AddPointDialogFragment.this.altitude = Double
                                        .parseDouble(AddPointDialogFragment.this.altitudeEditText
                                                .getText().toString());
                            }
                            AddPointDialogFragment.this.number = Integer
                                    .parseInt(AddPointDialogFragment.this.numberEditText.getText()
                                            .toString());
                            AddPointDialogFragment.this.east = Double
                                    .parseDouble(AddPointDialogFragment.this.eastEditText.getText()
                                            .toString());
                            AddPointDialogFragment.this.north = Double
                                    .parseDouble(AddPointDialogFragment.this.northEditText
                                            .getText().toString());
                            AddPointDialogFragment.this.listener
                                    .onDialogAdd(AddPointDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            Toast errorToast = Toast.makeText(
                                    AddPointDialogFragment.this.getActivity(),
                                    AddPointDialogFragment.this.getActivity().getString(
                                            R.string.error_fill_data),
                                    Toast.LENGTH_SHORT);
                            errorToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            errorToast.show();
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
            this.listener = (AddPointDialogListener) activity;
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
        this.numberEditText.setHint(this.getActivity().getString(R.string.point_number_3dots));
        this.numberEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        this.eastEditText = new EditText(this.getActivity());
        this.eastEditText.setHint(this.getActivity().getString(R.string.east_3dots));
        this.eastEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.northEditText = new EditText(this.getActivity());
        this.northEditText.setHint(this.getActivity().getString(R.string.north_3dots));

        this.northEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.altitudeEditText = new EditText(this.getActivity());
        this.altitudeEditText.setHint(this.getActivity().getString(R.string.altitude_3dots)
                + this.getActivity().getString(R.string.optional_prths));
        this.altitudeEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.number = 0;
        this.east = 0.0;
        this.north = 0.0;
        this.altitude = 0.0;
    }

    /**
     * Create a view to get number, east, north and altitude of a point from the
     * user.
     * 
     */
    private void genAddPointView() {
        this.layout.addView(this.numberEditText);
        this.layout.addView(this.eastEditText);
        this.layout.addView(this.northEditText);
        this.layout.addView(this.altitudeEditText);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     * 
     * @return True if every EditTexts of the dialog have been filled, false
     *         otherwise.
     */
    private boolean checkDialogInputs() {
        if ((this.numberEditText.length() == 0) || (this.eastEditText.length() == 0)
                || (this.northEditText.length() == 0)) {
            return false;
        }
        return true;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getEast() {
        return this.east;
    }

    public double getNorth() {
        return this.north;
    }

    public int getNumber() {
        return this.number;
    }
}