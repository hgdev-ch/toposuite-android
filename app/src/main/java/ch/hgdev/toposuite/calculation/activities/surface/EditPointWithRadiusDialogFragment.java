package ch.hgdev.toposuite.calculation.activities.surface;

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
import ch.hgdev.toposuite.calculation.Surface;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class EditPointWithRadiusDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of
     * EditPointWithRadiusDialogFragment must implement this interface in order
     * to receive event callbacks. Each method passes the DialogFragment in case
     * the host needs to query it.
     *
     * @author HGdev
     */
    public interface EditPointWithRadiusDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(EditPointWithRadiusDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogEdit(EditPointWithRadiusDialogFragment dialog);
    }

    private EditPointWithRadiusDialogListener listener;
    private LinearLayout layout;
    private Point point;
    private Spinner pointSpinner;
    private TextView pointTextView;
    private double radius;
    private EditText radiusEditText;

    private Spinner positionSpinner;
    private String positionAfter;
    private int position;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddMeasureView();
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
                EditPointWithRadiusDialogFragment.this.listener
                        .onDialogCancel(EditPointWithRadiusDialogFragment.this);
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
                        if (EditPointWithRadiusDialogFragment.this.checkDialogInputs()) {
                            if (!ViewUtils.isEmpty(
                                    EditPointWithRadiusDialogFragment.this.radiusEditText)) {
                                EditPointWithRadiusDialogFragment.this.radius =
                                        ViewUtils.readDouble(EditPointWithRadiusDialogFragment.this
                                                .radiusEditText);
                            }
                            EditPointWithRadiusDialogFragment.this.point =
                                    (Point) EditPointWithRadiusDialogFragment.this.pointSpinner
                                            .getSelectedItem();
                            EditPointWithRadiusDialogFragment.this.listener
                                    .onDialogEdit(EditPointWithRadiusDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    EditPointWithRadiusDialogFragment.this.getActivity(),
                                    EditPointWithRadiusDialogFragment.this.getActivity().getString(
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
            this.listener = (EditPointWithRadiusDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditPointWithRadiusDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        Bundle bundle = this.getArguments();

        this.positionAfter = "";

        this.radius = bundle.getDouble(SurfaceActivity.RADIUS_LABEL);
        this.position = bundle.getInt(SurfaceActivity.POINT_POSITION_LABEL);

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.pointTextView = new TextView(this.getActivity());
        this.pointTextView.setText("");

        this.radiusEditText = new EditText(this.getActivity());
        this.radiusEditText.setHint(
                this.getActivity().getString(R.string.radius_3dots)
                        + this.getActivity().getString(R.string.unit_meter)
                        + this.getActivity().getString(R.string.optional_prths));
        this.radiusEditText.setText(DisplayUtils.toStringForEditText(this.radius));
        this.radiusEditText.setInputType(App.getInputTypeCoordinate());

        this.pointSpinner = new Spinner(this.getActivity());
        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                EditPointWithRadiusDialogFragment.this.point = (Point) EditPointWithRadiusDialogFragment.this.pointSpinner.getItemAtPosition(pos);
                if (!EditPointWithRadiusDialogFragment.this.point.getNumber().isEmpty()) {
                    EditPointWithRadiusDialogFragment.this.pointTextView.setText(
                            DisplayUtils.formatPoint(
                                    EditPointWithRadiusDialogFragment.this.getActivity(),
                                    EditPointWithRadiusDialogFragment.this.point));
                } else {
                    EditPointWithRadiusDialogFragment.this.pointTextView.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        List<Point> points = new ArrayList<>();
        points.add(new Point(false));
        points.addAll(SharedResources.getSetOfPoints());
        ArrayAdapter<Point> a = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_list_item, points);
        this.pointSpinner.setAdapter(a);

        String pointNumber = bundle.getString(SurfaceActivity.POINT_WITH_RADIUS_NUMBER_LABEL);
        this.pointSpinner.setSelection(a.getPosition(SharedResources.getSetOfPoints().find(pointNumber)));

        this.positionSpinner = new Spinner(this.getActivity());
        final ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.spinner_list_item,
                new ArrayList<String>());
        this.positionSpinner.setAdapter(positionAdapter);
        positionAdapter.add(this.getActivity().getString(R.string.displace_after));

        ArrayList<Surface.PointWithRadius> pointsWithRadius = (ArrayList<Surface.PointWithRadius>) bundle.getSerializable(
                SurfaceActivity.POINT_WITH_RADIUS_LABEL);
        for (Surface.PointWithRadius pt : pointsWithRadius) {
            if (!pt.getNumber().equals(pointNumber)) {
                positionAdapter.add(pt.toString());
            }
        }
        this.positionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    return;
                }
                EditPointWithRadiusDialogFragment.this.positionAfter = positionAdapter.getItem(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
            }
        });
    }

    /**
     * Create a view to get number, abscissa, ordinate and altitude of a point
     * from the user.
     */
    private void genAddMeasureView() {
        this.layout.addView(this.positionSpinner);
        this.layout.addView(this.pointSpinner);
        this.layout.addView(this.pointTextView);
        this.layout.addView(this.radiusEditText);
    }

    /**
     * Verify that the user has entered all required data. Note that the
     * altitude is not required and should be set to 0 if no data was inserted.
     *
     * @return True if every EditTexts of the dialog have been filled, false
     * otherwise.
     */
    private boolean checkDialogInputs() {
        return this.pointSpinner.getSelectedItemPosition() > 0;
    }

    public final Point getPoint() {
        return this.point;
    }

    public final double getRadius() {
        return this.radius;
    }

    public final String getPositionAfter() {
        return this.positionAfter;
    }

    public int getPosition() {
        return position;
    }
}
