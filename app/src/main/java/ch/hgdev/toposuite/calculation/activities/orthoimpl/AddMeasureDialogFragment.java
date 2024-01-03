package ch.hgdev.toposuite.calculation.activities.orthoimpl;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.DisplayUtils;
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

    private LinearLayout layout;
    private Point point;
    private Spinner pointSpinner;
    private TextView pointTextView;

    @Override
    public
    @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
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
                AddMeasureDialogFragment.this.listener.onDialogCancel(AddMeasureDialogFragment.this);
            }
        });
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button addButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AddMeasureDialogFragment.this.checkDialogInputs()) {
                            AddMeasureDialogFragment.this.point = (Point) AddMeasureDialogFragment.this.pointSpinner.getSelectedItem();
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
            throw new ClassCastException(activity.toString() + " must implement AddPointDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.pointTextView = new TextView(this.getActivity());
        this.pointTextView.setText("");

        this.pointSpinner = new Spinner(this.getActivity());
        this.pointSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Point point = (Point) AddMeasureDialogFragment.this.pointSpinner.getItemAtPosition(pos);
                if (!point.getNumber().isEmpty()) {
                    AddMeasureDialogFragment.this.pointTextView.setText(
                            DisplayUtils.formatPoint(AddMeasureDialogFragment.this.getActivity(), point));
                } else {
                    AddMeasureDialogFragment.this.pointTextView.setText("");
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
    }

    /**
     * Create a view to get number, abscissa, ordinate and altitude of a point
     * from the user.
     */
    private void genAddMeasureView() {
        this.layout.addView(this.pointSpinner);
        this.layout.addView(this.pointTextView);
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

    public Point getPoint() {
        return this.point;
    }
}