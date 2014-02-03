package ch.hgdev.toposuite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.hgdev.toposuite.entry.MainActivity;
import ch.hgdev.toposuite.points.PointsManagerActivity;

/**
 * 
 * @author HGdev
 */
public class TopoSuiteActivity extends Activity {
    private DrawerLayout          drawerLayout;
    private ListView              drawerListLeftMenu;
    private ListView              drawerListRightMenu;

    private ActionBarDrawerToggle drawerToggle;
    private MenuItem              rightMenuToggle;

    private CharSequence          title;
    private CharSequence          drawerLeftTitle;
    private CharSequence          drawerRightTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.global_layout);

        this.title = this.getString(R.string.app_name);
        // TODO replace by R.string.XXX
        this.drawerLeftTitle = "Gestion des points";
        this.drawerRightTitle = "Calculs";

        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);

        this.drawerListLeftMenu = (ListView) this.findViewById(R.id.left_drawer);
        this.drawerListLeftMenu.setAdapter(new ArrayAdapter<ActivityItem>(this, R.layout.drawer_list_item,
                new ActivityItem[] { new ActivityItem("Home", MainActivity.class),
                        new ActivityItem("Points management", PointsManagerActivity.class) }));
        this.drawerListLeftMenu.setOnItemClickListener(new DrawerItemClickListener(this.drawerListLeftMenu));

        this.drawerListRightMenu = (ListView) this.findViewById(R.id.right_drawer);

        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.getActionBar().setHomeButtonEnabled(true);

        this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.drawable.ic_launcher,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                TopoSuiteActivity.this.getActionBar().setTitle(TopoSuiteActivity.this.title);
                TopoSuiteActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                TopoSuiteActivity.this.getActionBar().setTitle(TopoSuiteActivity.this.drawerLeftTitle);
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

    public void startActivity(Class<?> activityClass) {
        Intent newActivityIntent = new Intent(this, activityClass);
        this.startActivity(newActivityIntent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        private ListView list;

        public DrawerItemClickListener(ListView list_) {
            this.list = list_;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivityItem item = (ActivityItem) this.list.getItemAtPosition(position);
            TopoSuiteActivity.this.startActivity(item.getActivityClass());
        }
    }

    private class ActivityItem {
        private String   title;
        private Class<?> activityClass;

        public ActivityItem(String title_, Class<?> activityClass) {
            this.title = title_;
            this.activityClass = activityClass;
        }

        @Override
        public String toString() {
            return this.title;
        }

        public Class<?> getActivityClass() {
            return this.activityClass;
        }
    }
}