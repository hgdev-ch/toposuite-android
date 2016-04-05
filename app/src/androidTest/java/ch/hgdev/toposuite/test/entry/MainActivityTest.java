package ch.hgdev.toposuite.test.entry;

import com.robotium.solo.Solo;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.entry.MainActivity;
import ch.hgdev.toposuite.help.HelpActivity;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.jobs.JobsActivity;
import ch.hgdev.toposuite.points.PointsManagerActivity;
import ch.hgdev.toposuite.settings.SettingsActivity;
import ch.hgdev.toposuite.test.testutils.TestActivity;

/**
 * This set of tests click on all the menus accessible from the main activity.
 * 
 * @author HGdev
 * 
 */
public class MainActivityTest extends TestActivity<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Test clicking on every menu item from the left slider menu and check the
     * activity launched.
     */
    public void testLeftSlideMenu() {
        Solo solo = this.getSolo();

        // open left slider
        solo.clickOnImageButton(0);
        solo.clickOnText(solo.getString(R.string.home));
        solo.assertCurrentActivity("Home activity", MainActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText(solo.getString(R.string.title_activity_points_manager));
        solo.assertCurrentActivity("Points Manager activity", PointsManagerActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText(solo.getString(R.string.title_activity_history));
        solo.assertCurrentActivity("History activity", HistoryActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText(solo.getString(R.string.title_activity_jobs));
        solo.assertCurrentActivity("Jobs activity", JobsActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText(solo.getString(R.string.title_activity_settings));
        solo.assertCurrentActivity("Settings activity", SettingsActivity.class);

        solo.clickOnImageButton(0);
        solo.clickOnText(solo.getString(R.string.title_activity_help));
        solo.assertCurrentActivity("Help activity", HelpActivity.class);
    }
}