package ch.hgdev.toposuite.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.preference.DialogPreference;
import android.util.AttributeSet;

import ch.hgdev.toposuite.R;

/**
 * Number picker for preferences. Thanks to Luke Horvat
 * (https://gist.github.com/lukehorvat/4398028)
 *
 * Modified for compatibility with Android support library
 * (see also NumberPickerPreferenceDialogFragment).
 *
 * @author HGdev
 */
public class NumberPickerDialogPreference extends DialogPreference {

    private static final int DEFAULT_MIN_VALUE = 0;
    private static final int DEFAULT_MAX_VALUE = 100;
    private static final int DEFAULT_VALUE = 0;

    private int mMinValue;
    private int mMaxValue;
    private int mValue;

    public NumberPickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get attributes specified in XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.NumberPickerDialogPreference, 0, 0);
        try {
            this.setMinValue(a.getInteger(R.styleable.NumberPickerDialogPreference_min,
                    NumberPickerDialogPreference.DEFAULT_MIN_VALUE));
            this.setMaxValue(a.getInteger(R.styleable.NumberPickerDialogPreference_android_max,
                    NumberPickerDialogPreference.DEFAULT_MAX_VALUE));
        } finally {
            a.recycle();
        }

        // set layout
        this.setDialogLayoutResource(R.layout.preference_number_picker_dialog);
        this.setPositiveButtonText(android.R.string.ok);
        this.setNegativeButtonText(android.R.string.cancel);
        this.setDialogIcon(null);
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        this.setValue(restore ? this.getPersistedInt(NumberPickerDialogPreference.DEFAULT_VALUE)
                : (Integer) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, NumberPickerDialogPreference.DEFAULT_VALUE);
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
        this.setValue(Math.max(this.mValue, this.mMinValue));
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
        this.setValue(Math.min(this.mValue, this.mMaxValue));
    }

    public int getValue() {
        return this.mValue;
    }

    public void setValue(int value) {
        value = Math.max(Math.min(value, this.mMaxValue), this.mMinValue);

        if (value != this.mValue) {
            this.mValue = value;
            this.persistInt(value);
            this.notifyChanged();
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        // save the instance state so that it will survive screen orientation
        // changes and other events that may temporarily destroy it
        final Parcelable superState = super.onSaveInstanceState();

        // set the state's value with the class member that holds current
        // setting value
        final SavedState myState = new SavedState(superState);
        myState.minValue = this.getMinValue();
        myState.maxValue = this.getMaxValue();
        myState.value = this.getValue();

        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // check whether we saved the state in onSaveInstanceState()
        if ((state == null) || !state.getClass().equals(SavedState.class)) {
            // didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // restore the state
        SavedState myState = (SavedState) state;
        this.setMinValue(myState.minValue);
        this.setMaxValue(myState.maxValue);
        this.setValue(myState.value);

        super.onRestoreInstanceState(myState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {
        int minValue;
        int maxValue;
        int value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);

            this.minValue = source.readInt();
            this.maxValue = source.readInt();
            this.value = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeInt(this.minValue);
            dest.writeInt(this.maxValue);
            dest.writeInt(this.value);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
