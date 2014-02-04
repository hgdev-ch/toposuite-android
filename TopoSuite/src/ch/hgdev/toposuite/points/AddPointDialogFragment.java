package ch.hgdev.toposuite.points;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import ch.hgdev.toposuite.R;

/**
 * Dialog window to allow the user to add a new point to the list of points.
 * 
 * @author HGdev
 * 
 */
public class AddPointDialogFragment extends DialogFragment {

    /**
     * The activity that creates an instance of AddPointDialogFragment must
     * implement this interface in order to receive event callbacks. Each method
     * passes the DialogFragment in case the host needs to query it.
     * 
     * @author HGdev
     * 
     */
    public interface AddPointDialogListener {
        public void onDialogAdd(DialogFragment dialog);
    }

    AddPointDialogListener listener;
    private int            number;
    private double         altitude;
    private double         east;
    private double         north;
    private EditText       altitudeEditText;
    private EditText       eastEditText;
    private EditText       northEditText;
    private EditText       numberEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.initAttributes();
        View view = this.genAddPointView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.dialog_add_point)
                .setView(view)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddPointDialogFragment.this.number = Integer
                                .parseInt(AddPointDialogFragment.this.numberEditText.getText()
                                        .toString());
                        AddPointDialogFragment.this.altitude = Double
                                .parseDouble(AddPointDialogFragment.this.altitudeEditText.getText().toString());
                        AddPointDialogFragment.this.east = Double
                                .parseDouble(AddPointDialogFragment.this.eastEditText.getText().toString());
                        AddPointDialogFragment.this.north = Double
                                .parseDouble(AddPointDialogFragment.this.northEditText.getText().toString());
                        AddPointDialogFragment.this.listener.onDialogAdd(AddPointDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (AddPointDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddPointDialogListener");
        }
    }

    /**
     * Initializes class attributes.
     */
    private void initAttributes() {
        // TODO use strings.xml
        this.numberEditText = new EditText(this.getActivity());
        this.numberEditText.setHint("point number...");
        this.numberEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        this.eastEditText = new EditText(this.getActivity());
        this.eastEditText.setHint("east...");
        this.eastEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.northEditText = new EditText(this.getActivity());
        this.northEditText.setHint("north...");
        this.northEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.altitudeEditText = new EditText(this.getActivity());
        this.altitudeEditText.setHint("altitude...");
        this.altitudeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        this.number = 0;
        this.east = 0.0;
        this.north = 0.0;
        this.altitude = 0.0;
    }

    /**
     * Create a view to get number, east, north and altitude of a point from the
     * user.
     * 
     * @return A view suitable for the AlertDialog that should get a new point
     *         from the user.
     */
    private View genAddPointView() {
        LinearLayout layout = new LinearLayout(this.getActivity());

        layout.addView(this.numberEditText);
        layout.addView(this.eastEditText);
        layout.addView(this.northEditText);
        layout.addView(this.altitudeEditText);

        return layout;
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