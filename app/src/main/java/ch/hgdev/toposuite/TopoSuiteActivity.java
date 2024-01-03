package ch.hgdev.toposuite;

import android.content.Intent;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.calculation.activities.abriss.AbrissActivity;
import ch.hgdev.toposuite.calculation.activities.axisimpl.AxisImplantationActivity;
import ch.hgdev.toposuite.calculation.activities.cheminortho.CheminementOrthoActivity;
import ch.hgdev.toposuite.calculation.activities.circcurvesolver.CircularCurvesSolverActivity;
import ch.hgdev.toposuite.calculation.activities.circle.CircleActivity;
import ch.hgdev.toposuite.calculation.activities.circlesintersection.CirclesIntersectionActivity;
import ch.hgdev.toposuite.calculation.activities.circularsegmentation.CircularSegmentationActivity;
import ch.hgdev.toposuite.calculation.activities.freestation.FreeStationActivity;
import ch.hgdev.toposuite.calculation.activities.gisement.GisementActivity;
import ch.hgdev.toposuite.calculation.activities.leveortho.LeveOrthoActivity;
import ch.hgdev.toposuite.calculation.activities.limdispl.LimitDisplacementActivity;
import ch.hgdev.toposuite.calculation.activities.linecircleintersection.LineCircleIntersectionActivity;
import ch.hgdev.toposuite.calculation.activities.linesintersec.LinesIntersectionActivity;
import ch.hgdev.toposuite.calculation.activities.orthoimpl.OrthogonalImplantationActivity;
import ch.hgdev.toposuite.calculation.activities.pointproj.PointProjectionActivity;
import ch.hgdev.toposuite.calculation.activities.polarimplantation.PolarImplantationActivity;
import ch.hgdev.toposuite.calculation.activities.polarsurvey.PolarSurveyActivity;
import ch.hgdev.toposuite.calculation.activities.surface.SurfaceActivity;
import ch.hgdev.toposuite.calculation.activities.trianglesolver.TriangleSolverActivity;
import ch.hgdev.toposuite.entry.MainActivity;
import ch.hgdev.toposuite.help.HelpActivity;
import ch.hgdev.toposuite.history.HistoryActivity;
import ch.hgdev.toposuite.jobs.JobsActivity;
import ch.hgdev.toposuite.points.PointsManagerActivity;
import ch.hgdev.toposuite.settings.SettingsActivity;

/**
 * TopoSuiteActivity is the base class for all activities created in TopoSuite.
 * It automatically provides an action bar with left and right sliding menus.
 *
 * @author HGdev
 */
public abstract class TopoSuiteActivity extends AppCompatActivity {
    /**
     * The drawer layout that contains the left/right sliding menus and the
     * activity layout.
     */
    private DrawerLayout drawerLayout;

    /**
     * The left items list that contains the app main menus.
     */
    private ListView drawerListLeftMenu;

    /**
     * The right items list that contains the list of available calculations.
     */
    private ExpandableListView drawerListRightMenu;

    /**
     * The action bar drawer toggle.
     */
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.global_layout);

        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

        // set the content of the left sliding menu
        this.drawerListLeftMenu = (ListView) this.findViewById(R.id.left_drawer);
        this.drawerListLeftMenu.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item,
                new ActivityItem[]{
                        new ActivityItem(this.getString(R.string.home), MainActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_points_manager),
                                PointsManagerActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_history),
                                HistoryActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_jobs),
                                JobsActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_settings),
                                SettingsActivity.class),
                        new ActivityItem(this.getString(R.string.title_activity_help),
                                HelpActivity.class)}));

        // set the content of the right sliding menu
        this.drawerListRightMenu = (ExpandableListView) this.findViewById(R.id.right_drawer);
        this.createRightMenuItems();

        this.drawerListLeftMenu.setOnItemClickListener(new DrawerItemClickListener(
                this.drawerListLeftMenu));

        // the drawerToggle handles the actions when a sliding menu is opened or
        // closed
        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout,
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

        this.drawerLayout.addDrawerListener(this.drawerToggle);

        // set the titles that will appear in the action bar
        this.getSupportActionBar().setTitle(this.getActivityTitle());
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewStub stub = (ViewStub) this.findViewById(R.id.global_activity_include);
        stub.setLayoutResource(layoutResID);
        stub.inflate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            if (this.drawerLayout.isDrawerVisible(Gravity.RIGHT)) {
                this.drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            return true;
        }

        switch (item.getItemId()) {
            case R.id.toggle_right_menu_button:
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
    private void createRightMenuItems() {
        SparseArray<CalculationGroup> groups = new SparseArray<>();

        CalculationGroup polarCalculation = new CalculationGroup(
                this.getString(R.string.group_polar_calculation));
        polarCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_abriss),
                AbrissActivity.class));
        polarCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_free_station),
                FreeStationActivity.class));
        polarCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_polar_survey),
                PolarSurveyActivity.class));
        polarCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_polar_implantation),
                PolarImplantationActivity.class));
        polarCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_axis_implantation),
                AxisImplantationActivity.class));
        groups.append(0, polarCalculation);

        CalculationGroup orthoCalculation = new CalculationGroup(
                this.getString(R.string.group_orthogonal_calculation));
        orthoCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_leve_ortho),
                LeveOrthoActivity.class));
        orthoCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_cheminement_ortho),
                CheminementOrthoActivity.class));
        orthoCalculation.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_orthogonal_implantation),
                OrthogonalImplantationActivity.class));
        groups.append(1, orthoCalculation);

        CalculationGroup intersections = new CalculationGroup(
                this.getString(R.string.group_intersections));
        intersections.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_lines_intersection),
                LinesIntersectionActivity.class));
        intersections.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_circles_intersection),
                CirclesIntersectionActivity.class));
        intersections.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_line_circle_intersection),
                LineCircleIntersectionActivity.class));
        groups.append(2, intersections);

        CalculationGroup surfaces = new CalculationGroup(
                this.getString(R.string.group_surfaces));
        surfaces.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_surface),
                SurfaceActivity.class));
        surfaces.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_limit_displacement),
                LimitDisplacementActivity.class));
        groups.append(3, surfaces);

        CalculationGroup various = new CalculationGroup(
                this.getString(R.string.group_various));
        various.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_gisement),
                GisementActivity.class));
        various.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_circle),
                CircleActivity.class));
        various.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_point_projection),
                PointProjectionActivity.class));
        various.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_circular_segmentation),
                CircularSegmentationActivity.class));
        groups.append(4, various);

        CalculationGroup mathematics = new CalculationGroup(
                this.getString(R.string.group_mathematics));
        mathematics.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_triangle_solver),
                TriangleSolverActivity.class));
        mathematics.getChildren().add(new ActivityItem(
                this.getString(R.string.title_activity_circular_curve_solver),
                CircularCurvesSolverActivity.class));
        groups.append(5, mathematics);

        ExpandableRightMenuAdapter a = new ExpandableRightMenuAdapter(this,
                this.drawerListRightMenu, groups);
        this.drawerListRightMenu.setAdapter(a);
    }

    /**
     * Starts a new activity.
     *
     * @param activityClass Activity class
     */
    private void startActivity(Class<?> activityClass) {
        Intent newActivityIntent = new Intent(this, activityClass);
        this.startActivity(newActivityIntent);
    }

    /**
     * The subclass must return the activity title with this method.
     *
     * @return Activity title.
     */
    protected abstract String getActivityTitle();

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
         * @param _list the items list
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
        private final String title;

        /**
         * The activity class to start on item click.
         */
        private final Class<?> activityClass;

        /**
         * Constructs a new ActivityItem.
         *
         * @param _title        Activity title
         * @param activityClass Activity class
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
        private final String groupName;
        private final List<ActivityItem> children;

        public CalculationGroup(String _groupName) {
            this.groupName = _groupName;
            this.children = new ArrayList<>();
        }

        public String getGroupName() {
            return this.groupName;
        }

        public List<ActivityItem> getChildren() {
            return this.children;
        }
    }
}