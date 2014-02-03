package ch.hgdev.toposuite.entry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import ch.hgdev.toposuite.R;

/**
 * 
 * @author HGdev
 * 
 */
public class MainActivity extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawerListLeftMenu;
    private ListView drawerListRightMenu;

    private ActionBarDrawerToggle drawerToggle;
    private MenuItem rightMenuToggle;

    private CharSequence title;
    private CharSequence drawerLeftTitle;
    private CharSequence drawerRightTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.setContentView(R.layout.activity_main);

	this.title = this.getString(R.string.app_name);
	// TODO replace by R.string.XXX
	this.drawerLeftTitle = "Gestion des points";
	this.drawerRightTitle = "Calculs";

	this.drawerLayout = (DrawerLayout) this
		.findViewById(R.id.drawer_layout);

	this.drawerListLeftMenu = (ListView) this
		.findViewById(R.id.left_drawer);
	this.drawerListRightMenu = (ListView) this
		.findViewById(R.id.right_drawer);

	this.getActionBar().setDisplayHomeAsUpEnabled(true);
	this.getActionBar().setHomeButtonEnabled(true);

	this.drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout,
		R.drawable.ic_launcher, R.string.drawer_open,
		R.string.drawer_close) {

	    @Override
	    public void onDrawerClosed(View view) {
		MainActivity.this.getActionBar().setTitle(
			MainActivity.this.title);
		MainActivity.this.invalidateOptionsMenu();
	    }

	    @Override
	    public void onDrawerOpened(View drawerView) {
		MainActivity.this.getActionBar().setTitle(
			MainActivity.this.drawerLeftTitle);
		MainActivity.this.invalidateOptionsMenu();
	    }
	};

	this.drawerLayout.setDrawerListener(this.drawerToggle);
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
}