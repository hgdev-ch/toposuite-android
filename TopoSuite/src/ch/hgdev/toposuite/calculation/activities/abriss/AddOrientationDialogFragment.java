package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
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
    private double               zenithalAngle;
    private LinearLayout         layout;
    private EditText             horizontalDirectionEditText;
    private EditText             horizontalDistanceEditText;
    private EditText             zenithalAngleEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddOrientationView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.orientation_add))
                .setView(this.layout)
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
            public void onShow(final DialogInterface dialog) {
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
                            if (AddOrientationDialogFragment.this.zenithalAngleEditText.length() > 0) {
                                AddOrientationDialogFragment.this.zenithalAngle = Double
                                        .parseDouble(AddOrientationDialogFragment.this.zenithalAngleEditText
                                                .getText().toString());
                            }
                            AddOrientationDialogFragment.this.horizontalDirection = Double
                                    .parseDouble(AddOrientationDialogFragment.this.horizontalDirectionEditText
                                            .getText().toString());
                            AddOrientationDialogFragment.this.listener
                                    .onDialogAdd(AddOrientationDialogFragment.this);
                            dialog.dismiss();
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
        List<Point> points = new ArrayList<Point>();
        points.add(new Point(0, 0.0, 0.0, 0.0, true));
        points.addAll(SharedResources.getSetOfPoints());
        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this.getActivity(), R.layout.spinner_list_item, points);
        this.orientationSpinner.setAdapter(a);

        this.horizontalDirectionEditText = new EditText(this.getActivity());
        this.horizontalDirectionEditText.setHint(this.getActivity().getString(
                R.string.horiz_direction));
        this.horizontalDirectionEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.horizontalDistanceEditText = new EditText(this.getActivity());
        this.horizontalDistanceEditText.setHint(this.getActivity().getString(
                R.string.horiz_distance) + " "
                + this.getActivity().getString(R.string.optional_prths));
        this.horizontalDistanceEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.zenithalAngleEditText = new EditText(this.getActivity());
        this.zenithalAngleEditText.setHint(this.getActivity().getString(R.string.zenithal_angle)
                + " " + this.getActivity().getString(R.string.optional_prths));
        this.zenithalAngleEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.horizontalDirection = 0.0;
        this.horizontalDistance = 0.0;
        this.zenithalAngle = 0.0;
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
        this.layout.addView(this.zenithalAngleEditText);
    }

    /**
     * Verify that the user has entered all required data.
     * 
     * @return True if every required data has been filled, false otherwise.
     */
    private boolean checkDialogInputs() {
        if ((this.horizontalDirectionEditText.length() == 0) || (this.orientation.getNumber() < 1)) {
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

    public double getZenithalAngle() {
        return this.zenithalAngle;
    }
}