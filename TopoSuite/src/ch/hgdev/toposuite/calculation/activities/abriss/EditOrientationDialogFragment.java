package ch.hgdev.toposuite.calculation.activities.abriss;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Dialog window to allow the user to edit an orientation for the abriss
 * calculation.
 * 
 * @author HGdev
 * 
 */
public class EditOrientationDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of EditOrientationDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface EditOrientationDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(EditOrientationDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogEdit(EditOrientationDialogFragment dialog);
    }

    public static final String    ORIENTATION_NUMBER   = "Orientation number";
    public static final String    HORIZONTAL_DIRECTION = "Horizontal direction";
    public static final String    HORIZONTAL_DISTANCE  = "Horizontal distance";
    public static final String    ZENITHAL_ANGLE       = "Zenithal angle";
    public static final String    ORIENTATION_POSITION = "Orientation position";
    private Bundle                bundle;
    EditOrientationDialogListener listener;
    private Spinner               orientationSpinner;
    private TextView              orientationView;
    private Point                 orientation;
    private double                horizontalDirection;
    private double                horizontalDistance;
    private double                zenithalAngle;
    private LinearLayout          layout;
    private EditText              horizontalDirectionEditText;
    private EditText              horizontalDistanceEditText;
    private EditText              zenithalAngleEditText;

    /**
     * The position of the current orientation in the ArrayList adapter. This is
     * used for retrieving the orientation in the adapter after its
     * modification.
     */
    private int                   orientationPosition;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddOrientationView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.measure_edit))
                .setView(this.layout)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
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
                        EditOrientationDialogFragment.this.listener
                                .onDialogCancel(EditOrientationDialogFragment.this);
                    }
                });
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button editButton = ((AlertDialog) dialog)
                        .getButton(DialogInterface.BUTTON_POSITIVE);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (EditOrientationDialogFragment.this.checkDialogInputs()) {
                            if (EditOrientationDialogFragment.this.horizontalDistanceEditText
                                    .length() > 0) {
                                EditOrientationDialogFragment.this.horizontalDistance = ViewUtils
                                        .readDouble(EditOrientationDialogFragment.this.horizontalDistanceEditText);
                            }
                            if (EditOrientationDialogFragment.this.zenithalAngleEditText.length() > 0) {
                                EditOrientationDialogFragment.this.zenithalAngle = ViewUtils
                                        .readDouble(EditOrientationDialogFragment.this.zenithalAngleEditText);
                            }
                            EditOrientationDialogFragment.this.horizontalDirection = ViewUtils
                                    .readDouble(EditOrientationDialogFragment.this.horizontalDirectionEditText);
                            EditOrientationDialogFragment.this.listener
                                    .onDialogEdit(EditOrientationDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    EditOrientationDialogFragment.this.getActivity(),
                                    EditOrientationDialogFragment.this.getActivity().getString(
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
            this.listener = (EditOrientationDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditOrientationDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.bundle = this.getArguments();

        this.orientationPosition = this.bundle
                .getInt(EditOrientationDialogFragment.ORIENTATION_POSITION);

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.orientationView = new TextView(this.getActivity());

        this.orientationSpinner = new Spinner(this.getActivity());
        List<Point> points = new ArrayList<Point>();
        points.addAll(SharedResources.getSetOfPoints());
        ArrayAdapter<Point> a = new ArrayAdapter<Point>(
                this.getActivity(), R.layout.spinner_list_item, points);

        this.orientationSpinner.setAdapter(a);
        this.orientationSpinner.setSelection(a.getPosition(SharedResources.getSetOfPoints().find(
                this.bundle.getString(EditOrientationDialogFragment.ORIENTATION_NUMBER))));

        this.orientationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Point point = (Point) EditOrientationDialogFragment.this.orientationSpinner
                        .getItemAtPosition(pos);
                if (!point.getNumber().isEmpty()) {
                    EditOrientationDialogFragment.this.orientationView.setText(DisplayUtils
                            .formatPoint(
                                    EditOrientationDialogFragment.this.getActivity(), point));
                } else {
                    EditOrientationDialogFragment.this.orientationView.setText("");
                }
                EditOrientationDialogFragment.this.itemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        this.orientation = (Point) this.orientationSpinner.getSelectedItem();

        this.itemSelected();
        this.horizontalDirection = this.bundle
                .getDouble(EditOrientationDialogFragment.HORIZONTAL_DIRECTION);
        this.horizontalDistance = this.bundle
                .getDouble(EditOrientationDialogFragment.HORIZONTAL_DISTANCE);
        this.zenithalAngle = this.bundle.getDouble(EditOrientationDialogFragment.ZENITHAL_ANGLE);

        this.horizontalDirectionEditText = new EditText(this.getActivity());
        this.horizontalDirectionEditText.setText(DisplayUtils
                .toStringForEditText(this.horizontalDirection));
        this.horizontalDirectionEditText.setInputType(App.getInputTypeCoordinate());

        this.horizontalDistanceEditText = new EditText(this.getActivity());
        this.horizontalDistanceEditText.setText(DisplayUtils
                .toStringForEditText(this.horizontalDistance));
        this.horizontalDistanceEditText.setInputType(App.getInputTypeCoordinate());

        this.zenithalAngleEditText = new EditText(this.getActivity());
        this.zenithalAngleEditText.setText(DisplayUtils.toStringForEditText(this.zenithalAngle));
        this.zenithalAngleEditText.setInputType(App.getInputTypeCoordinate());
    }

    /**
     * itemSelected is triggered when the selected item of one of the spinners
     * is changed.
     */
    private void itemSelected() {
        this.orientation = (Point) this.orientationSpinner.getSelectedItem();
        if (this.orientation != null) {
            this.orientationView.setText(
                    DisplayUtils.formatPoint(this.getActivity(), this.orientation));
        }
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
        if ((this.horizontalDirectionEditText.length() == 0)
                || (this.orientation.getNumber().isEmpty())) {
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

    public int getOrientationPosition() {
        return this.orientationPosition;
    }
}