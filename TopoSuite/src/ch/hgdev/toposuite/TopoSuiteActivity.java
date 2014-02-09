package ch.hgdev.toposuite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.hgdev.toposuite.calculation.activities.Abriss.AbrissActivity;
import ch.hgdev.toposuite.calculation.activities.gisement.GisementActivity;
import ch.hgdev.toposuite.entry.MainActivity;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.PointsManagerActivity;

/**
 * TopoSuiteActivity is the base class for all activities created in TopoSuite.
 * It automatically provides an action bar with left and right sliding menus.
 * 
 * @author HGdev
 */
public class TopoSuiteActivity extends FragmentActivity {
    /**
     * The drawer layout that contains the left/right sliding menus and the
     * activity layout.
     */
    private DrawerLayout          drawerLayout;

    /**
     * The left items list that contains the app main menus.
     */
    private ListView              drawerListLeftMenu;

    /**
     * The right items list that contains the list of available calculations.
     */
    private ListView              drawerListRightMenu;

    /**
     * The action bar drawer toggle.
     */
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.global_layout);

        // set the titles that will appear in the action bar
        this.getActionBar().setTitle(this.getString(R.string.app_name));

        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

        // set the content of the left sliding menu
        this.drawerListLeftMenu = (ListView) this.findViewById(R.id.left_drawer);
        this.drawerListLeftMenu.setAdapter(new ArrayAdapter<ActivityItem>(this,
                R.layout.drawer_list_item,
                new ActivityItem[] {
                        new ActivityItem(this.getString(R.string.home), MainActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_points_manager),
                                PointsManagerActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_history),
                                HistoryActivity.class) }));

        // set the content of the right sliding menu
        this.drawerListRightMenu = (ListView) this.findViewById(R.id.right_drawer);
        this.drawerListRightMenu.setAdapter(new ArrayAdapter<ActivityItem>(this,
                R.layout.drawer_list_item,
                new ActivityItem[] {
                        new ActivityItem(this.getString(R.string.title_activity_gisement),
                                GisementActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_abriss),
                                AbrissActivity.class)
                }));

        this.drawerListLeftMenu.setOnItemClickListener(new DrawerItemClickListener(
                this.drawerListLeftMenu));
        this.drawerListRightMenu.setOnItemClickListener(new DrawerItemClickListener(
                this.drawerListRightMenu));

        this.drawerListRightMenu = (ListView) this.findViewById(R.id.right_drawer);

        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setHomeButtonEnabled(true);

        // the drawerToggle handles the actions when a sliding menu is opened or
        // closed
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout,
                R.drawable.ic_launcher,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                TopoSuiteActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                TopoSuiteActivity.this.invalidateOptionsMenu();
            }
        };

        this.drawerLayout.setDrawerListener(this.drawerToggle);

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewStub stub = (ViewStub) this.findViewById(R.id.global_activity_include);
        stub.setLayoutResource(layoutResID);
        stub.inflate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
        case R.id.toggle_right_menu:
            if (this.drawerLayout.isDrawerVisible(Gravity.LEFT)) {
                this.drawerLayout.closeDrawer(Gravity.LEFT);
            }

            if (this.drawerLayout.isDrawerVisible(Gravity.RIGHT)) {
                this.drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                this.drawerLayout.openDrawer(Gravity.RIGHT);
            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts a new activity.
     * 
     * @param activityClass
     *            Activity class
     */
    public void startActivity(Class<?> activityClass) {
        Intent newActivityIntent = new Intent(this, activityClass);
        this.startActivity(newActivityIntent);
    }

    /**
     * Click listener for a drawer items list.
     * 
     * @author HGdev
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        private final ListView list;

        /**
         * Constructs a new DrawerItemClickListener.
         * 
         * @param list_
         *            the items list
         */
        public DrawerItemClickListener(ListView _list) {
            this.list = _list;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivityItem item = (ActivityItem) this.list.getItemAtPosition(position);
            TopoSuiteActivity.this.startActivity(item.getActivityClass());
        }
    }

    /**
     * ActivityItem holds a pair of activity's title/class.
     * 
     * @author HGdev
     */
    private class ActivityItem {
        /**
         * The title that will appear in the left or right sliding menu.
         */
        private final String   title;

        /**
         * The activity class to start on item click.
         */
        private final Class<?> activityClass;

        /**
         * Constructs a new ActivityItem.
         * 
         * @param _title
         *            Activity title
         * @param activityClass
         *            Activity class
         */
        public ActivityItem(String _title, Class<?> activityClass) {
            this.title = _title;
            this.activityClass = activityClass;
        }

        @Override
        public String toString() {
            return this.title;
        }

        /**
         * Getter for activityClass.
         * 
         * @return the activity class
         */
        public Class<?> getActivityClass() {
            return this.activityClass;
        }
    }
}