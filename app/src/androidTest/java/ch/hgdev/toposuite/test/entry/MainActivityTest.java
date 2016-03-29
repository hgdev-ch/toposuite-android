package ch.hgdev.toposuite.test.entry;

import ch.hgdev.toposuite.entry.MainActivity;
import ch.hgdev.toposuite.help.HelpActivity;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.jobs.JobsActivity;
import ch.hgdev.toposuite.points.PointsManagerActivity;
import ch.hgdev.toposuite.settings.SettingsActivity;
import ch.hgdev.toposuite.test.testutils.TestActivity;

import com.robotium.solo.Solo;

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
        solo.clickOnText("TopoSuite");

        solo.clickOnText("Home");
        solo.assertCurrentActivity("Home activity", MainActivity.class);
        solo.clickOnText("TopoSuite");

        solo.clickOnText("Points Manager");
        solo.assertCurrentActivity("Points Manager activity", PointsManagerActivity.class);
        solo.clickOnText("Points Manager");

        solo.clickOnText("History");
        solo.assertCurrentActivity("History activity", HistoryActivity.class);
        solo.clickOnText("History");

        solo.clickOnText("Jobs");
        solo.assertCurrentActivity("Jobs activity", JobsActivity.class);
        solo.clickOnText("Jobs");

        solo.clickOnText("Settings");
        solo.assertCurrentActivity("Settings activity", SettingsActivity.class);
        solo.clickOnText("Settings");

        solo.clickOnText("Help");
        solo.assertCurrentActivity("Help activity", HelpActivity.class);
        solo.clickOnText("Help");
    }
}