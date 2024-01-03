package ch.hgdev.toposuite.settings;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import ch.hgdev.toposuite.R;

/**
 * Number picker for preferences fragment.
 *
 * @author HGdev
 */
public class NumberPickerPreferenceDialogFragment extends PreferenceDialogFragmentCompat {
    private static final String STATE_PICKER_VALUE = "number_picker_value";

    private boolean restoredState;
    private int restoredValue;
    private NumberPicker mNumberPicker;

    public static NumberPickerPreferenceDialogFragment newInstance(Preference preference) {
        NumberPickerPreferenceDialogFragment fragment = new NumberPickerPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, preference.getKey());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoredState = true;
            restoredValue = savedInstanceState.getInt(STATE_PICKER_VALUE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PICKER_VALUE, this.mNumberPicker.getValue());
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        NumberPickerDialogPreference preference = (NumberPickerDialogPreference) getPreference();

        this.mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        this.mNumberPicker.setMinValue(preference.getMinValue());
        this.mNumberPicker.setMaxValue(preference.getMaxValue());
        this.mNumberPicker.setValue(restoredState ? restoredValue : preference.getValue());

        // prevent keyboard from showing up
        this.mNumberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        // when the user selects "OK", persist the new value
        if (positiveResult) {
            int numberPickerValue = this.mNumberPicker.getValue();
            NumberPickerDialogPreference preference = (NumberPickerDialogPreference) getPreference();
            preference.setValue(numberPickerValue);
        }
    }

}
