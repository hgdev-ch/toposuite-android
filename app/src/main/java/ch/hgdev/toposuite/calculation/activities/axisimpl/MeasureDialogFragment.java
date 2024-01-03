package ch.hgdev.toposuite.calculation.activities.axisimpl;

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
import ch.hgdev.toposuite.calculation.Measure;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class MeasureDialogFragment extends DialogFragment {
    private static final String MEASURE = "measure";
    private static final String IS_EDITION = "is_edition";


    /**
     * The activity that creates an instance of MeasureDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface MeasureDialogListener {
        /**
         * Define what to do when the "Add" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogAdd(MeasureDialogFragment dialog);

        /**
         * Define what to do when the "Edit" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogEdit(MeasureDialogFragment dialog);

        void onDialogCancel();
    }

    private MeasureDialogListener listener;
    private String measureNumber;
    private double horizDir;
    private double distance;

    private LinearLayout layout;
    private EditText measureNumberEditText;
    private EditText horizDirEditText;
    private EditText distanceEditText;

    /**
     * True if the dialog is for edition, false otherwise.
     */
    private boolean isEdition;

    private Measure measure;

    public static MeasureDialogFragment newInstance() {
        MeasureDialogFragment mdf = new MeasureDialogFragment();

        Bundle args = new Bundle();
        args.putBoolean(IS_EDITION, false);
        mdf.setArguments(args);

        return mdf;
    }

    public static MeasureDialogFragment newInstance(Measure m) {
        MeasureDialogFragment mdf = new MeasureDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(MEASURE, m);
        args.putBoolean(IS_EDITION, true);
        mdf.setArguments(args);

        return mdf;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();

        if (this.isEdition) {
            this.setAttributes();
        }

        this.genAddMeasureView();

        int positiveButtonRes = (this.isEdition) ? R.string.edit : R.string.add;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.measure_add))
                .setView(this.layout)
                .setPositiveButton(positiveButtonRes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // overridden below because the dialog dismiss itself
                        // without a call to dialog.dismiss()...
                        // thus, it is impossible to handle error on user input
                        // without closing the dialog otherwise
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MeasureDialogFragment.this.listener.onDialogCancel();
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
                    public void onClick(View v) {
                        if (MeasureDialogFragment.this.checkDialogInputs()) {
                            MeasureDialogFragment.this.measureNumber = ViewUtils.readString(MeasureDialogFragment.this.measureNumberEditText);
                            MeasureDialogFragment.this.horizDir = ViewUtils.readDouble(MeasureDialogFragment.this.horizDirEditText);
                            MeasureDialogFragment.this.distance = ViewUtils.readDouble(MeasureDialogFragment.this.distanceEditText);

                            if (MeasureDialogFragment.this.isEdition) {
                                MeasureDialogFragment.this.listener
                                        .onDialogEdit(MeasureDialogFragment.this);
                            } else {
                                MeasureDialogFragment.this.listener
                                        .onDialogAdd(MeasureDialogFragment.this);
                            }
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    MeasureDialogFragment.this.getActivity(),
                                    MeasureDialogFragment.this.getActivity().getString(
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
            this.listener = (MeasureDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MeasureDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.isEdition = getArguments().getBoolean(IS_EDITION);
        this.measure = (Measure) getArguments().getSerializable(MEASURE);

        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.measureNumberEditText = new EditText(this.getActivity());
        this.measureNumberEditText.setSingleLine();
        this.measureNumberEditText.setHint(
                this.getActivity().getString(
                        R.string.determination_sight_label));

        this.horizDirEditText = new EditText(this.getActivity());
        this.horizDirEditText.setHint(
                this.getActivity().getString(R.string.horiz_direction_3dots)
                        + this.getActivity().getString(R.string.unit_gradian));
        this.horizDirEditText.setInputType(App.getInputTypeCoordinate());

        this.distanceEditText = new EditText(this.getActivity());
        this.distanceEditText.setHint(this.getActivity().getString(
                R.string.distance_3dots)
                + this.getActivity().getString(R.string.unit_meter));
        this.distanceEditText.setInputType(App.getInputTypeCoordinate());

        this.horizDir = 0.0;
        this.distance = 0.0;
    }

    /**
     * Fill views.
     */
    private void setAttributes() {
        this.measureNumber = this.measure.getMeasureNumber();
        this.horizDir = this.measure.getHorizDir();
        this.distance = this.measure.getDistance();

        this.measureNumberEditText.setText(this.measureNumber);
        this.horizDirEditText.setText(DisplayUtils.toStringForEditText(this.horizDir));
        this.distanceEditText.setText(DisplayUtils.toStringForEditText(this.distance));
    }

    /**
     * Create a view to get information from the user.
     */
    private void genAddMeasureView() {
        this.layout.addView(this.measureNumberEditText);
        this.layout.addView(this.horizDirEditText);
        this.layout.addView(this.distanceEditText);
    }

    /**
     * Verify that the user has entered all required data.
     *
     * @return True if every required data has been filled, false otherwise.
     */
    private boolean checkDialogInputs() {
        return ((this.measureNumberEditText.length() > 0)
                && (this.horizDirEditText.length() > 0)
                && (this.distanceEditText.length() > 0));
    }

    public final double getHorizDir() {
        return this.horizDir;
    }

    public final double getDistance() {
        return this.distance;
    }

    public final String getMeasureNumber() {
        return this.measureNumber;
    }

    public final Measure getMeasure() {
        return this.measure;
    }
}
