package ch.hgdev.toposuite;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import ch.hgdev.toposuite.calculation.activities.abriss.AbrissActivity;
import ch.hgdev.toposuite.calculation.activities.cheminortho.CheminementOrthoActivity;
import ch.hgdev.toposuite.calculation.activities.gisement.GisementActivity;
import ch.hgdev.toposuite.calculation.activities.leveortho.LeveOrthoActivity;
import ch.hgdev.toposuite.calculation.activities.levepolaire.LevePolaireActivity;
import ch.hgdev.toposuite.calculation.activities.orthoimpl.OrthogonalImplantationActivity;
import ch.hgdev.toposuite.calculation.activities.pointproj.PointProjectionActivity;
import ch.hgdev.toposuite.calculation.activities.polarimplantation.PolarImplantationActivity;
import ch.hgdev.toposuite.entry.MainActivity;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.points.PointsManagerActivity;

/**
 * TopoSuiteActivity is the base class for all activities created in TopoSuite.
 * It automatically provides an action bar with left and right sliding menus.
 * 
 * @author HGdev
 */
public abstract class TopoSuiteActivity extends FragmentActivity {
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
    private ExpandableListView    drawerListRightMenu;

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
        this.drawerListRightMenu = (ExpandableListView) this.findViewById(R.id.right_drawer);
        this.createRightMenuItems();

        this.drawerListLeftMenu.setOnItemClickListener(new DrawerItemClickListener(
                this.drawerListLeftMenu));

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
     * Create items in the right menu.
     */
    public final void createRightMenuItems() {
        SparseArray<CalculationGroup> groups = new SparseArray<CalculationGroup>();

        CalculationGroup polarCalculation = new CalculationGroup(
                this.getString(R.string.group_polar_calculation));
        polarCalculation.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_abriss),
                        AbrissActivity.class));
        polarCalculation.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_leve_polaire),
                        LevePolaireActivity.class));
        polarCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_polar_implantation),
                PolarImplantationActivity.class));
        groups.append(0, polarCalculation);

        CalculationGroup orthoCalculation = new CalculationGroup(
                this.getString(R.string.group_orthogonal_calculation));
        orthoCalculation.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_leve_ortho),
                        LeveOrthoActivity.class));
        orthoCalculation.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_cheminement_ortho),
                        CheminementOrthoActivity.class));
        orthoCalculation.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_orthogonal_implantation),
                        OrthogonalImplantationActivity.class));
        groups.append(1, orthoCalculation);

        CalculationGroup intersections = new CalculationGroup(
                this.getString(R.string.group_intersections));
        groups.append(2, intersections);

        CalculationGroup surfaces = new CalculationGroup(
                this.getString(R.string.group_surfaces));
        groups.append(3, surfaces);

        CalculationGroup various = new CalculationGroup(
                this.getString(R.string.group_various));
        various.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_gisement),
                        GisementActivity.class));
        various.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_circle),
                        CircleActivity.class));
        various.getChildren().add(
                new ActivityItem(this.getString(R.string.title_activity_point_projection),
                        PointProjectionActivity.class));
        groups.append(4, various);

        CalculationGroup mathematics = new CalculationGroup(
                this.getString(R.string.group_mathematics));
        groups.append(5, mathematics);

        ExpandableRightMenuAdapter a = new ExpandableRightMenuAdapter(this,
                this.drawerListRightMenu, groups);
        this.drawerListRightMenu.setAdapter(a);
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
    public static class ActivityItem {
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

    public static class CalculationGroup {
        private final String             groupName;
        private final List<ActivityItem> children;

        public CalculationGroup(String _groupName) {
            this.groupName = _groupName;
            this.children = new ArrayList<TopoSuiteActivity.ActivityItem>();
        }

        public String getGroupName() {
            return this.groupName;
        }

        public List<ActivityItem> getChildren() {
            return this.children;
        }
    }
}