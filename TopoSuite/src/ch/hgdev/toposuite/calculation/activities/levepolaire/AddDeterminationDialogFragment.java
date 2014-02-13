package ch.hgdev.toposuite.calculation.activities.levepolaire;

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
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;

public class AddDeterminationDialogFragment extends DialogFragment {
    /**
     * The activity that creates an instance of AddDeterminationDialogFragment
     * must implement this interface in order to receive event callbacks. Each
     * method passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface AddDeterminationDialogListener {
        /**
         * Define what to do when the "Cancel" button is clicked
         * 
         * @param dialog
         *            Dialog with NO useful information to fetch from.
         */
        void onDialogCancel(AddDeterminationDialogFragment dialog);

        /**
         * Define what to do when the "Add" button is clicked.
         * 
         * @param dialog
         *            Dialog to fetch information from.
         */
        void onDialogAdd(AddDeterminationDialogFragment dialog);
    }

    AddDeterminationDialogListener listener;
    private int                    determinationNo;
    private double                 horizDir;
    private double                 distance;
    private double                 zenAngle;
    private double                 s;
    private double                 latDepl;
    private double                 lonDepl;

    private LinearLayout           layout;
    private EditText               determinationNoEditText;
    private EditText               horizDirEditText;
    private EditText               distanceEditText;
    private EditText               zenAngleEditText;
    private EditText               sEditText;
    private EditText               latDeplEditText;
    private EditText               lonDeplEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        this.genAddDeterminationView();
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
                            // TODO check that if S is set, I is set too and
                            // pop-up an error
                            if (AddDeterminationDialogFragment.this.zenAngleEditText.length() > 0) {
                                AddDeterminationDialogFragment.this.zenAngle = Double
                                        .parseDouble(AddDeterminationDialogFragment.this.zenAngleEditText
                                                .getText().toString());
                            }
                            if (AddDeterminationDialogFragment.this.sEditText.length() > 0) {
                                AddDeterminationDialogFragment.this.s = Double
                                        .parseDouble(AddDeterminationDialogFragment.this.sEditText
                                                .getText().toString());
                            }
                            if (AddDeterminationDialogFragment.this.latDeplEditText.length() > 0) {
                                AddDeterminationDialogFragment.this.latDepl = Double
                                        .parseDouble(AddDeterminationDialogFragment.this.latDeplEditText
                                                .getText().toString());
                            }
                            if (AddDeterminationDialogFragment.this.lonDeplEditText.length() > 0) {
                                AddDeterminationDialogFragment.this.lonDepl = Double
                                        .parseDouble(AddDeterminationDialogFragment.this.lonDeplEditText
                                                .getText().toString());
                            }

                            AddDeterminationDialogFragment.this.determinationNo = Integer.parseInt(
                                    AddDeterminationDialogFragment.this.determinationNoEditText
                                            .getText().toString());
                            AddDeterminationDialogFragment.this.horizDir = Double
                                    .parseDouble(AddDeterminationDialogFragment.this.horizDirEditText
                                            .getText().toString());
                            AddDeterminationDialogFragment.this.distance = Double
                                    .parseDouble(AddDeterminationDialogFragment.this.distanceEditText
                                            .getText().toString());
                            AddDeterminationDialogFragment.this.listener
                                    .onDialogAdd(AddDeterminationDialogFragment.this);
                            dialog.dismiss();
                        } else {
                            Toast errorToast = Toast.makeText(
                                    AddDeterminationDialogFragment.this.getActivity(),
                                    AddDeterminationDialogFragment.this.getActivity().getString(
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
        this.determinationNoEditText.setHint(
                this.getActivity().getString(R.string.determination_sight_3dots));
        this.determinationNoEditText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        this.horizDirEditText = new EditText(this.getActivity());
        this.horizDirEditText.setHint(
                this.getActivity().getString(R.string.horiz_direction_3dots)
                        + this.getActivity().getString(R.string.unit_gradian));
        this.horizDirEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.distanceEditText = new EditText(this.getActivity());
        this.distanceEditText.setHint(this.getActivity().getString(
                R.string.distance_3dots)
                + this.getActivity().getString(R.string.unit_meter));
        this.distanceEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.zenAngleEditText = new EditText(this.getActivity());
        this.zenAngleEditText.setHint(this.getActivity().getString(
                R.string.zenithal_angle_3dots)
                + this.getActivity().getString(R.string.unit_gradian)
                + this.getActivity().getString(R.string.optional_prths));
        this.zenAngleEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.sEditText = new EditText(this.getActivity());
        this.sEditText.setHint(this.getActivity().getString(
                R.string.prism_height_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.sEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.latDeplEditText = new EditText(this.getActivity());
        this.latDeplEditText.setHint(this.getActivity().getString(
                R.string.lateral_displacement_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.latDeplEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.lonDeplEditText = new EditText(this.getActivity());
        this.lonDeplEditText.setHint(this.getActivity().getString(
                R.string.longitudinal_displacement_3dots)
                + this.getActivity().getString(R.string.unit_meter)
                + this.getActivity().getString(R.string.optional_prths));
        this.lonDeplEditText.setInputType(App.INPUTTYPE_TYPE_NUMBER_COORDINATE);

        this.determinationNo = 0;
        this.horizDir = 0.0;
        this.distance = 0.0;
        this.zenAngle = 100.0;
        this.s = 0.0;
        this.latDepl = 0.0;
        this.lonDepl = 0.0;
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

    public int getDeterminationNo() {
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
