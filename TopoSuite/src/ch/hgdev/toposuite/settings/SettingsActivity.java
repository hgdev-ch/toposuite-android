package ch.hgdev.toposuite.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
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

        // Display the settings fragment as the main content.
        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment(this))
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
    public static class SettingsFragment extends PreferenceFragment {
        private Activity activity;

        public SettingsFragment(Activity _activity) {
            super();
            this.activity = _activity;
        }

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

        /**
         * Start the {@link AboutActivity}.
         */
        private void startAboutActivity() {
            // TODO choose between Activity or Fragment

            // uncomment for starting the AboutActivity
            /*Intent aboutActivityIntent = new Intent(
                    this.activity, AboutActivity.class);
            this.activity.startActivity(aboutActivityIntent);*/

            // uncomment for replacing the current fragment by the AboutFragment
            /*this.activity.getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new AboutFragment())
                    .commit();*/
        }
    }
}
