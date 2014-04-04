package ch.hgdev.toposuite.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.TopoSuiteActivity;

/**
 * Activity that provides access to the application settings as well as
 * information about the application.
 * 
 * @author HGdev
 * 
 */
public class SettingsActivity extends TopoSuiteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        // Display the settings fragment as the main content.
        this.getFragmentManager().beginTransaction()
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
     * 
     */
    public static class SettingsFragment extends PreferenceFragment
            implements OnSharedPreferenceChangeListener {
        public static final String KEY_PREF_NEGATIVE_COORDINATES          = "switch_negative_coordinates";
        public static final String KEY_PREF_COORDINATES_DECIMAL_PRECISION = "coordinates_decimal_precision";
        public static final String KEY_PREF_COORDINATES_DISPLAY_PRECISION = "coordinates_display_precision";
        public static final String KEY_PREF_ANGLES_DISPLAY_PRECISION      = "angles_display_precision";
        public static final String KEY_PREF_DISTANCES_DISPLAY_PRECISION   = "distances_display_precision";
        public static final String KEY_PREF_AVERAGES_DISPLAY_PRECISION    = "averages_display_precision";
        public static final String KEY_PREF_GAPS_DISPLAY_PRECISION        = "gaps_display_precision";
        public static final String KEY_PREF_SURFACES_DISPLAY_PRECISION    = "surfaces_display_precision";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.addPreferencesFromResource(R.xml.preferences);

            Preference aboutPref = this.findPreference("screen_about_toposuite");
            aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
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
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
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
            Intent aboutActivityIntent = new Intent(
                    this.getActivity(), AboutActivity.class);
            this.getActivity().startActivity(aboutActivityIntent);
        }
    }
}
