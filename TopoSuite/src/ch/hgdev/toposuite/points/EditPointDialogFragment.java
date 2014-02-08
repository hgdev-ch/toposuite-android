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
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.utils.DisplayUtils;

import com.google.common.collect.Iterables;

/**
 * Dialog window to allow the user to add a new point to the list of points.
 * 
 * @author HGdev
 * 
 */
public class EditPointDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of EditPointDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface EditPointDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(EditPointDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogEdit(EditPointDialogFragment dialog);
    }

    public final static String POINT_POSITION = "Point position";
    private Bundle             bundle;
    EditPointDialogListener    listener;
    private int                number;
    private double             altitude;
    private double             east;
    private double             north;
    private LinearLayout       layout;
    private EditText           altitudeEditText;
    private EditText           eastEditText;
    private EditText           northEditText;
    private EditText           numberEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genEditPointView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.dialog_add_point).setView(this.layout)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditPointDialogFragment.this.listener
                                .onDialogCancel(EditPointDialogFragment.this);
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
                        if (EditPointDialogFragment.this.checkDialogInputs()) {
                            // altitude is not mandatory
                            if (EditPointDialogFragment.this.altitudeEditText.length() > 0) {
                                EditPointDialogFragment.this.altitude = Double
                                        .parseDouble(EditPointDialogFragment.this.altitudeEditText
                                                .getText().toString());
                            }
                            EditPointDialogFragment.this.number = Integer
                                    .parseInt(EditPointDialogFragment.this.numberEditText.getText()
                                            .toString());
                            EditPointDialogFragment.this.east = Double
                                    .parseDouble(EditPointDialogFragment.this.eastEditText
                                            .getText().toString());
                            EditPointDialogFragment.this.north = Double
                                    .parseDouble(EditPointDialogFragment.this.northEditText
                                            .getText().toString());
                            EditPointDialogFragment.this.listener
                                    .onDialogEdit(EditPointDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            Toast errorToast = Toast.makeText(
                                    EditPointDialogFragment.this.getActivity(),
                                    EditPointDialogFragment.this.getActivity().getString(
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
            this.listener = (EditPointDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditPointDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.bundle = this.getArguments();

        Point point = Iterables.get(SharedResources.getSetOfPoints(),
                this.bundle.getInt(EditPointDialogFragment.POINT_POSITION));
        this.number = point.getNumber();
        this.east = point.getEast();
        this.north = point.getNorth();
        this.altitude = point.getAltitude();

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.numberEditText = new EditText(this.getActivity());
        this.numberEditText.setText(DisplayUtils.toString(this.number));
        this.numberEditText.setEnabled(false);

        this.eastEditText = new EditText(this.getActivity());
        this.eastEditText.setText(DisplayUtils.toString(this.east));
        this.eastEditText.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.northEditText = new EditText(this.getActivity());
        this.northEditText.setText(DisplayUtils.toString(this.north));
        this.northEditText.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.altitudeEditText = new EditText(this.getActivity());
        this.altitudeEditText.setText(DisplayUtils.toString(this.altitude));
        this.altitudeEditText.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    /**
     * Create a view to get updated east, north and altitude value of a point
     * from the user.
     * 
     */
    private void genEditPointView() {
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
        if ((this.eastEditText.length() == 0) || (this.northEditText.length() == 0)) {
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