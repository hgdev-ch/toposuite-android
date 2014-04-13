package ch.hgdev.toposuite.test.entry;

import ch.hgdev.toposuite.entry.MainActivity;
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

    public void testLeftSlideMenu() {
        Solo solo = this.getSolo();

        // open left slider
        solo.setNavigationDrawer(Solo.OPENED);

        solo.clickOnText("Home");
        solo.setNavigationDrawer(Solo.OPENED);
        solo.clickOnText("Points Manager");
        solo.setNavigationDrawer(Solo.OPENED);
    }
}