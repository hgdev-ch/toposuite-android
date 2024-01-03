package ch.hgdev.toposuite.points;

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
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Dialog window to allow the user to add a new point to the list of points.
 *
 * @author HGdev
 */
public class AddPointDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddPointDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface AddPointDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddPointDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogAdd(AddPointDialogFragment dialog);
    }

    private AddPointDialogListener listener;
    private String number;
    private double altitude;
    private double east;
    private double north;
    private LinearLayout layout;
    private EditText altitudeEditText;
    private EditText eastEditText;
    private EditText northEditText;
    private EditText numberEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddPointView();
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
                            AddPointDialogFragment.this.altitude = ViewUtils.readDouble(AddPointDialogFragment.this.altitudeEditText);
                            AddPointDialogFragment.this.number = ViewUtils.readString(AddPointDialogFragment.this.numberEditText);
                            AddPointDialogFragment.this.east = ViewUtils.readDouble(AddPointDialogFragment.this.eastEditText);
                            AddPointDialogFragment.this.north = ViewUtils.readDouble(AddPointDialogFragment.this.northEditText);
                            AddPointDialogFragment.this.listener.onDialogAdd(AddPointDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    AddPointDialogFragment.this.getActivity(),
                                    AddPointDialogFragment.this.getActivity().getString(
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
        this.numberEditText.setSingleLine();
        this.numberEditText.setHint(this.getActivity().getString(R.string.point_number_3dots));

        this.eastEditText = new EditText(this.getActivity());
        this.eastEditText.setHint(this.getActivity().getString(R.string.east_3dots)
                + this.getActivity().getString(R.string.unit_meter));
        this.eastEditText.setInputType(App.getInputTypeCoordinate());

        this.northEditText = new EditText(this.getActivity());
        this.northEditText.setHint(this.getActivity().getString(R.string.north_3dots)
                + this.getActivity().getString(R.string.unit_meter));

        this.northEditText.setInputType(App.getInputTypeCoordinate());

        this.altitudeEditText = new EditText(this.getActivity());
        this.altitudeEditText.setHint(this.getActivity().getString(R.string.altitude_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.altitudeEditText.setInputType(App.getInputTypeCoordinate());

        this.number = "";
        this.east = MathUtils.IGNORE_DOUBLE;
        this.north = MathUtils.IGNORE_DOUBLE;
        this.altitude = MathUtils.IGNORE_DOUBLE;
    }

    /**
     * Create a view to get number, east, north and altitude of a point from the
     * user.
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
     * otherwise.
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

    public String getNumber() {
        return this.number;
    }
}