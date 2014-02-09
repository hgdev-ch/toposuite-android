package ch.hgdev.toposuite.calculation.activities.abriss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Dialog window to allow the user to add a new orientation for the abriss
 * calculation.
 * 
 * @author HGdev
 * 
 */
public class AddOrientationDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddOrientationDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface AddOrientationDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddOrientationDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogAdd(AddOrientationDialogFragment dialog);
    }

    AddOrientationDialogListener listener;
    private Spinner              orientationSpinner;
    private TextView             orientationView;
    private Point                orientation;
    private double               horizontalDirection;
    private double               horizontalDistance;
    private double               altitude;
    private LinearLayout         layout;
    private EditText             horizontalDirectionEditText;
    private EditText             horizontalDistanceEditText;
    private EditText             altitudeEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddOrientationView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Add Orientation").setView(this.layout)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // overridden below because the dialog dismiss itself
                        // without a call to dialog.dismiss()...
                        // thus, it is impossible to handle error on user input
                        // without closing the dialog otherwise
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddOrientationDialogFragment.this.listener
                                .onDialogCancel(AddOrientationDialogFragment.this);
                    }
                });
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button addButton = ((AlertDialog) dialog)
                        .getButton(DialogInterface.BUTTON_POSITIVE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AddOrientationDialogFragment.this.checkDialogInputs()) {
                            if (AddOrientationDialogFragment.this.horizontalDistanceEditText
                                    .length() > 0) {
                                AddOrientationDialogFragment.this.horizontalDistance = Double
                                        .parseDouble(AddOrientationDialogFragment.this.horizontalDirectionEditText
                                                .getText().toString());
                            }
                            if (AddOrientationDialogFragment.this.altitudeEditText.length() > 0) {
                                AddOrientationDialogFragment.this.altitude = Double
                                        .parseDouble(AddOrientationDialogFragment.this.altitudeEditText
                                                .getText().toString());
                            }
                            AddOrientationDialogFragment.this.horizontalDirection = Double
                                    .parseDouble(AddOrientationDialogFragment.this.horizontalDirectionEditText
                                            .getText().toString());
                        } else {
                            Toast errorToast = Toast.makeText(
                                    AddOrientationDialogFragment.this.getActivity(),
                                    AddOrientationDialogFragment.this.getActivity().getString(
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
            this.listener = (AddOrientationDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddOrientationDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.orientationView = new TextView(this.getActivity());
        this.orientationView.setText("");

        this.orientationSpinner = new Spinner(this.getActivity());
        this.orientationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Point point = (Point) AddOrientationDialogFragment.this.orientationSpinner
                        .getItemAtPosition(pos);
                if (point.getNumber() > 0) {
                    AddOrientationDialogFragment.this.orientationView.setText(DisplayUtils
                            .formatPoint(
                                    AddOrientationDialogFragment.this.getActivity(), point));
                } else {
                    AddOrientationDialogFragment.this.orientationView.setText("");
                }
                AddOrientationDialogFragment.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        this.horizontalDirectionEditText = new EditText(this.getActivity());
        this.horizontalDirectionEditText.setHint("Hz");
        this.horizontalDirectionEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.horizontalDistanceEditText = new EditText(this.getActivity());
        this.horizontalDistanceEditText.setHint("Horizontal distance (optional)");
        this.horizontalDistanceEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.altitudeEditText = new EditText(this.getActivity());
        this.altitudeEditText.setHint("altitude (optional)");
        this.altitudeEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.horizontalDirection = 0.0;
        this.horizontalDistance = 0.0;
        this.altitude = 0.0;
    }

    /**
     * itemSelected is triggered when the selected item of one of the spinners
     * is changed.
     */
    private void itemSelected() {
        this.orientation = (Point) this.orientationSpinner.getSelectedItem();
        this.orientationView
                .setText(DisplayUtils.formatPoint(this.getActivity(), this.orientation));
    }

    /**
     * Create a view to get information from the user.
     */
    private void genAddOrientationView() {
        this.layout.addView(this.orientationSpinner);
        this.layout.addView(this.orientationView);
        this.layout.addView(this.horizontalDirectionEditText);
        this.layout.addView(this.horizontalDistanceEditText);
        this.layout.addView(this.altitudeEditText);
    }

    /**
     * Verify that the user has entered all required data.
     * 
     * @return True if every required data has been filled, false otherwise.
     */
    private boolean checkDialogInputs() {
        if ((this.horizontalDirectionEditText.length() == 0) || (this.orientation == null)) {
            return false;
        }
        return true;
    }

    public Point getOrientation() {
        return this.orientation;
    }

    public double getHorizontalDirection() {
        return this.horizontalDirection;
    }

    public double getHorizontalDistance() {
        return this.horizontalDistance;
    }

    public double getAltitude() {
        return this.altitude;
    }
}