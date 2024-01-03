package ch.hgdev.toposuite.calculation.activities.abriss;

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
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Dialog window to allow the user to add a new orientation for the abriss
 * calculation.
 *
 * @author HGdev
 */
public class AddOrientationDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddOrientationDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface AddOrientationDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddOrientationDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogAdd(AddOrientationDialogFragment dialog);
    }

    private AddOrientationDialogListener listener;
    private Spinner orientationSpinner;
    private TextView orientationView;
    private Point orientation;
    private double horizontalDirection;
    private double horizontalDistance;
    private double zenithalAngle;
    private LinearLayout layout;
    private EditText horizontalDirectionEditText;
    private EditText horizontalDistanceEditText;
    private EditText zenithalAngleEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddOrientationView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.measure_add))
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
                AddOrientationDialogFragment.this.listener.onDialogCancel(AddOrientationDialogFragment.this);
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
                            AddOrientationDialogFragment.this.horizontalDistance = ViewUtils.readDouble(AddOrientationDialogFragment.this.horizontalDistanceEditText);
                            AddOrientationDialogFragment.this.zenithalAngle = ViewUtils.readDouble(AddOrientationDialogFragment.this.zenithalAngleEditText);
                            AddOrientationDialogFragment.this.horizontalDirection = ViewUtils.readDouble(AddOrientationDialogFragment.this.horizontalDirectionEditText);
                            AddOrientationDialogFragment.this.listener.onDialogAdd(AddOrientationDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    AddOrientationDialogFragment.this.getActivity(),
                                    AddOrientationDialogFragment.this.getActivity().getString(
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
                Point point = (Point) AddOrientationDialogFragment.this.orientationSpinner.getItemAtPosition(pos);
                if (!point.getNumber().isEmpty()) {
                    AddOrientationDialogFragment.this.orientationView.setText(
                            DisplayUtils.formatPoint(AddOrientationDialogFragment.this.getActivity(), point));
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
        List<Point> points = new ArrayList<>();
        points.add(new Point("", MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, MathUtils.IGNORE_DOUBLE, true));
        points.addAll(SharedResources.getSetOfPoints());
        ArrayAdapter<Point> a = new ArrayAdapter<>(
                this.getActivity(), R.layout.spinner_list_item, points);
        this.orientationSpinner.setAdapter(a);

        this.horizontalDirectionEditText = new EditText(this.getActivity());
        this.horizontalDirectionEditText.setHint(
                this.getActivity().getString(R.string.horiz_direction_3dots)
                        + this.getActivity().getString(R.string.unit_gradian));
        this.horizontalDirectionEditText.setInputType(App.getInputTypeCoordinate());

        this.horizontalDistanceEditText = new EditText(this.getActivity());
        this.horizontalDistanceEditText.setHint(this.getActivity().getString(
                R.string.distance_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.horizontalDistanceEditText.setInputType(App.getInputTypeCoordinate());

        this.zenithalAngleEditText = new EditText(this.getActivity());
        this.zenithalAngleEditText.setHint(this.getActivity().getString(
                R.string.zenithal_angle_3dots)
                + this.getActivity().getString(R.string.unit_gradian)
                + this.getActivity().getString(R.string.optional_prths));
        this.zenithalAngleEditText.setInputType(App.getInputTypeCoordinate());

        this.horizontalDirection = MathUtils.IGNORE_DOUBLE;
        this.horizontalDistance = MathUtils.IGNORE_DOUBLE;
        this.zenithalAngle = MathUtils.IGNORE_DOUBLE;
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
}