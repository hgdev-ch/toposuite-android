package ch.hgdev.toposuite.calculation.activities.polarsurvey;

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
import android.widget.ScrollView;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class AddDeterminationDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddDeterminationDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     *
     * @author HGdev
     */
    public interface AddDeterminationDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         *
         * @param dialog Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddDeterminationDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         *
         * @param dialog Dialog to fetch information from.
         */
        void onDialogAdd(AddDeterminationDialogFragment dialog);
    }

    private AddDeterminationDialogListener listener;
    private String determinationNo;
    private double horizDir;
    private double distance;
    private double zenAngle;
    private double s;
    private double latDepl;
    private double lonDepl;

    private LinearLayout layout;
    private ScrollView scrollView;
    private EditText determinationNoEditText;
    private EditText horizDirEditText;
    private EditText distanceEditText;
    private EditText zenAngleEditText;
    private EditText sEditText;
    private EditText latDeplEditText;
    private EditText lonDeplEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddDeterminationView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(this.getActivity().getString(R.string.measure_add))
                .setView(this.scrollView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
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
                        AddDeterminationDialogFragment.this.listener
                                .onDialogCancel(AddDeterminationDialogFragment.this);
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
                        if (AddDeterminationDialogFragment.this.checkDialogInputs()) {
                            AddDeterminationDialogFragment.this.zenAngle = ViewUtils.readDouble(AddDeterminationDialogFragment.this.zenAngleEditText);
                            AddDeterminationDialogFragment.this.s = ViewUtils.readDouble(AddDeterminationDialogFragment.this.sEditText);
                            AddDeterminationDialogFragment.this.latDepl = ViewUtils.readDouble(AddDeterminationDialogFragment.this.latDeplEditText);
                            AddDeterminationDialogFragment.this.lonDepl = ViewUtils.readDouble(AddDeterminationDialogFragment.this.lonDeplEditText);
                            AddDeterminationDialogFragment.this.determinationNo = ViewUtils.readString(AddDeterminationDialogFragment.this.determinationNoEditText);
                            AddDeterminationDialogFragment.this.horizDir = ViewUtils.readDouble(AddDeterminationDialogFragment.this.horizDirEditText);
                            AddDeterminationDialogFragment.this.distance = ViewUtils.readDouble(AddDeterminationDialogFragment.this.distanceEditText);
                            AddDeterminationDialogFragment.this.listener.onDialogAdd(AddDeterminationDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            ViewUtils.showToast(
                                    AddDeterminationDialogFragment.this.getActivity(),
                                    AddDeterminationDialogFragment.this.getActivity().getString(
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
            this.listener = (AddDeterminationDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddDeterminationDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        this.layout = new LinearLayout(this.getActivity());
        this.layout.setOrientation(LinearLayout.VERTICAL);

        this.determinationNoEditText = new EditText(this.getActivity());
        this.determinationNoEditText.setSingleLine();
        this.determinationNoEditText.setHint(
                this.getActivity().getString(R.string.determination_sight_3dots));

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

        this.zenAngleEditText = new EditText(this.getActivity());
        this.zenAngleEditText.setHint(this.getActivity().getString(
                R.string.zenithal_angle_3dots)
                + this.getActivity().getString(R.string.unit_gradian)
                + this.getActivity().getString(R.string.optional_prths));
        this.zenAngleEditText.setInputType(App.getInputTypeCoordinate());

        this.sEditText = new EditText(this.getActivity());
        this.sEditText.setHint(this.getActivity().getString(
                R.string.prism_height_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.sEditText.setInputType(App.getInputTypeCoordinate());

        this.latDeplEditText = new EditText(this.getActivity());
        this.latDeplEditText.setHint(this.getActivity().getString(
                R.string.lateral_displacement_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.latDeplEditText.setInputType(App.getInputTypeCoordinate());

        this.lonDeplEditText = new EditText(this.getActivity());
        this.lonDeplEditText.setHint(this.getActivity().getString(
                R.string.longitudinal_displacement_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.lonDeplEditText.setInputType(App.getInputTypeCoordinate());

        this.determinationNo = "";
        this.horizDir = MathUtils.IGNORE_DOUBLE;
        this.distance = MathUtils.IGNORE_DOUBLE;
        this.zenAngle = MathUtils.IGNORE_DOUBLE;
        this.s = MathUtils.IGNORE_DOUBLE;
        this.latDepl = MathUtils.IGNORE_DOUBLE;
        this.lonDepl = MathUtils.IGNORE_DOUBLE;
    }

    /**
     * Create a view to get information from the user.
     */
    private void genAddDeterminationView() {
        this.layout.addView(this.determinationNoEditText);
        this.layout.addView(this.horizDirEditText);
        this.layout.addView(this.distanceEditText);
        this.layout.addView(this.zenAngleEditText);
        this.layout.addView(this.sEditText);
        this.layout.addView(this.latDeplEditText);
        this.layout.addView(this.lonDeplEditText);

        this.scrollView = new ScrollView(this.getActivity());
        this.scrollView.addView(this.layout);
    }

    /**
     * Verify that the user has entered all required data.
     *
     * @return True if every required data has been filled, false otherwise.
     */
    private boolean checkDialogInputs() {
        if ((this.determinationNoEditText.length() == 0) || (this.horizDirEditText.length() == 0)
                || (this.distanceEditText.length() == 0)) {
            return false;
        }
        return true;
    }

    public String getDeterminationNo() {
        return this.determinationNo;
    }

    public double getHorizDir() {
        return this.horizDir;
    }

    public double getDistance() {
        return this.distance;
    }

    public double getZenAngle() {
        return this.zenAngle;
    }

    public double getS() {
        return this.s;
    }

    public double getLatDepl() {
        return this.latDepl;
    }

    public double getLonDepl() {
        return this.lonDepl;
    }
}
