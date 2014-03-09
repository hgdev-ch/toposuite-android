package ch.hgdev.toposuite.settings;

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
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_settings);
    }
}
