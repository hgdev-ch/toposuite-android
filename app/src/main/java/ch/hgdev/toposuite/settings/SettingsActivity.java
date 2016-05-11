package ch.hgdev.toposuite.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

/**
 * Activity that provides access to the application settings as well as
 * information about the application.
 *
 * @author HGdev
 */
public class SettingsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        // Display the settings fragment as the main content.
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.layout, new SettingsFragment())
                .commit();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_settings);
    }

    /**
     * Main settings fragment that is shown when accessing the settings
     * activity. It provides links to about and general settings fragment.
     *
     * @author HGdev
     */
    public static class SettingsFragment extends PreferenceFragmentCompat
            implements OnSharedPreferenceChangeListener {
        private static final String DIALOG_FRAGMENT_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";
        private static final String KEY_ABOUT = "screen_about_toposuite";
        public static final String KEY_PREF_CSV_SEPARATOR = "csv_separator";
        public static final String KEY_PREF_NEGATIVE_COORDINATES = "switch_negative_coordinates";
        public static final String KEY_PREF_COORDINATES_DECIMAL_PRECISION = "coordinates_decimal_precision";
        public static final String KEY_PREF_COORDINATES_DISPLAY_PRECISION = "coordinates_display_precision";
        public static final String KEY_PREF_ANGLES_DISPLAY_PRECISION = "angles_display_precision";
        public static final String KEY_PREF_DISTANCES_DISPLAY_PRECISION = "distances_display_precision";
        public static final String KEY_PREF_AVERAGES_DISPLAY_PRECISION = "averages_display_precision";
        public static final String KEY_PREF_GAPS_DISPLAY_PRECISION = "gaps_display_precision";
        public static final String KEY_PREF_SURFACES_DISPLAY_PRECISION = "surfaces_display_precision";

        @Override
        public void onCreatePreferences(Bundle bundle, String key) {
            this.addPreferencesFromResource(R.xml.preferences);
            Preference pref = this.findPreference(KEY_ABOUT);
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SettingsFragment.this.startAboutActivity();
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            this.getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            this.getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            if (preference instanceof NumberPickerDialogPreference) {
                if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
                    return;
                }
                DialogFragment fragment = NumberPickerPreferenceDialogFragment.newInstance(preference);
                fragment.setTargetFragment(this, 0);
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals(SettingsFragment.KEY_PREF_CSV_SEPARATOR)) {
                App.setCSVSeparator(prefs.getString(SettingsFragment.KEY_PREF_CSV_SEPARATOR, ","));
            }
            if (key.equals(SettingsFragment.KEY_PREF_NEGATIVE_COORDINATES)) {
                App.toggleNegativeCoordinates();
            }
            if (key.equals(SettingsFragment.KEY_PREF_COORDINATES_DECIMAL_PRECISION)) {
                App.setCoordinateDecimalRounding(
                        prefs.getInt(SettingsFragment.KEY_PREF_COORDINATES_DECIMAL_PRECISION, 3));
            }
            if (key.equals(SettingsFragment.KEY_PREF_COORDINATES_DISPLAY_PRECISION)) {
                App.setDecimalPrecisionForCoordinate(prefs.getInt(
                        SettingsFragment.KEY_PREF_COORDINATES_DISPLAY_PRECISION, 3));
            }
            if (key.equals(SettingsFragment.KEY_PREF_ANGLES_DISPLAY_PRECISION)) {
                App.setDecimalPrecisionForAngle(prefs.getInt(
                        SettingsFragment.KEY_PREF_ANGLES_DISPLAY_PRECISION, 4));
            }
            if (key.equals(SettingsFragment.KEY_PREF_DISTANCES_DISPLAY_PRECISION)) {
                App.setDecimalPrecisionForDistance(prefs.getInt(
                        SettingsFragment.KEY_PREF_DISTANCES_DISPLAY_PRECISION, 3));
            }
            if (key.equals(SettingsFragment.KEY_PREF_AVERAGES_DISPLAY_PRECISION)) {
                App.setDecimalPrecisionForAverage(prefs.getInt(
                        SettingsFragment.KEY_PREF_AVERAGES_DISPLAY_PRECISION, 3));
            }
            if (key.equals(SettingsFragment.KEY_PREF_GAPS_DISPLAY_PRECISION)) {
                App.setDecimalPrecisionForGap(prefs.getInt(
                        SettingsFragment.KEY_PREF_GAPS_DISPLAY_PRECISION, 1));
            }
            if (key.equals(SettingsFragment.KEY_PREF_SURFACES_DISPLAY_PRECISION)) {
                App.setDecimalPrecisionForSurface(prefs.getInt(
                        SettingsFragment.KEY_PREF_SURFACES_DISPLAY_PRECISION, 4));
            }
        }

        /**
         * Start the {@link AboutActivity}.
         */
        private void startAboutActivity() {
            Intent aboutActivityIntent = new Intent(this.getActivity(), AboutActivity.class);
            this.getActivity().startActivity(aboutActivityIntent);
        }
    }
}
