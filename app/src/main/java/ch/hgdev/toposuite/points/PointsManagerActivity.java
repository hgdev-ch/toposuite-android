package ch.hgdev.toposuite.points;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.export.ExportDialog;
import ch.hgdev.toposuite.export.ImportDialog;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

/**
 * Activity to manage points, such as adding, removing or modifying them.
 *
 * @author HGdev
 */
public class PointsManagerActivity extends TopoSuiteActivity implements
        AddPointDialogFragment.AddPointDialogListener,
        EditPointDialogFragment.EditPointDialogListener,
        SearchPointDialogFragment.SearchPointDialogListener,
        ExportDialog.ExportDialogListener,
        ImportDialog.ImportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private int selectedPointId;
    private ListView pointsListView;
    private ArrayListOfPointsAdapter adapter;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.pointsListView = (ListView) this.findViewById(R.id.apm_list_of_points);
        this.registerForContextMenu(this.pointsListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.selectedPointId = 0;
        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_points_manager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.add_point_button:
                this.showAddPointDialog();
                return true;
            case R.id.delete_points_button:
                this.removeAllPoints();
                return true;
            case R.id.search_point_button:
                this.showSearchPointDialog();
                return true;
            case R.id.export_points_button:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.showExportDialog();
                } else {
                    AppUtils.requestPermission(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE,
                            String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                }
                return true;
            case R.id.import_points_button:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    this.showImportDialog();
                } else {
                    AppUtils.requestPermission(this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                            String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.points_manager, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        this.shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        this.updateShareIntent();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDialogAdd(AddPointDialogFragment dialog) {
        Point point = SharedResources.getSetOfPoints().find(dialog.getNumber());
        if (point == null) {
            this.addPoint(dialog.getNumber(), dialog.getEast(),
                    dialog.getNorth(), dialog.getAltitude());
            this.drawList();
            ViewUtils.showToast(this, this.getString(R.string.point_add_success));
        } else {
            ViewUtils.showToast(this, this.getString(R.string.point_already_exists));
        }
        this.showAddPointDialog();
        this.updateShareIntent();
    }

    @Override
    public void onDialogCancel(AddPointDialogFragment dialog) {
        // do nothing
    }

    @Override
    public void onDialogEdit(EditPointDialogFragment dialog) {
        Point point = SharedResources.getSetOfPoints().get(this.selectedPointId);
        point.setEast(dialog.getEast());
        point.setNorth(dialog.getNorth());
        point.setAltitude(dialog.getAltitude());
        this.drawList();
        this.updateShareIntent();
    }

    @Override
    public void onDialogCancel(EditPointDialogFragment dialog) {
        // do nothing
    }

    @Override
    public void onDialogSearch(SearchPointDialogFragment dialog) {
        Point point = SharedResources.getSetOfPoints().find(dialog.getPointNumber());
        if (point != null) {
            int position = PointsManagerActivity.this.adapter.getPosition(point);
            this.showEditPointDialog(position);

        } else {
            ViewUtils.showToast(this, this.getString(R.string.point_not_found));
        }
    }

    @Override
    public void onDialogCancel(SearchPointDialogFragment dialog) {
        // do nothing
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.points_list_row_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Point point;
        switch (item.getItemId()) {
            case R.id.delete_point:
                point = SharedResources.getSetOfPoints().get((int) info.id);
                this.adapter.remove(point);
                this.adapter.notifyDataSetChanged();
                SharedResources.getSetOfPoints().remove(point);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Call to update the share intent
     *
     * @param shareIntent The share intent.
     */
    private void setShareIntent(Intent shareIntent) {
        if (this.shareActionProvider != null) {
            this.shareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void showExportDialog() {
        ExportDialog dialog = new ExportDialog();
        dialog.show(this.getSupportFragmentManager(), "ExportDialogFragments");
    }

    private void showImportDialog() {
        ImportDialog dialog = new ImportDialog();
        dialog.show(this.getSupportFragmentManager(), "ImportDialogFragment");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (AppUtils.Permission.valueOf(requestCode)) {
            case READ_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    this.showImportDialog();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_import));
                }
                break;
            case WRITE_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.showExportDialog();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_export));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Display a dialog to allow the user to insert a new point.
     */
    private void showAddPointDialog() {
        AddPointDialogFragment dialog = new AddPointDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "AddPointDialogFragment");
    }

    private void showSearchPointDialog() {
        SearchPointDialogFragment dialog = new SearchPointDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "SearchPointDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit a point.
     *
     * @param id Id of the point to be edited.
     */
    private void showEditPointDialog(int id) {
        EditPointDialogFragment dialog = new EditPointDialogFragment();
        Bundle args = new Bundle();
        this.selectedPointId = id;
        args.putInt(EditPointDialogFragment.POINT_POSITION, id);
        dialog.setArguments(args);

        dialog.show(this.getSupportFragmentManager(), "EditPointDialogFragment");
    }

    /**
     * Create a point based on the input and add it to the table of points and
     * the set of points.
     *
     * @param number   Point's number attribute.
     * @param east     Point's east attribute.
     * @param north    Point's north attribute.
     * @param altitude Point's altitude attribute.
     */
    private void addPoint(String number, double east, double north, double altitude) {
        if (number.isEmpty()) {
            ViewUtils.showToast(this, this.getString(R.string.error_point_number));
        } else {
            // when created by a user and not computed, a point IS a basepoint
            boolean basePoint = true;
            Point point = new Point(number, east, north, altitude, basePoint);
            SharedResources.getSetOfPoints().add(point);
        }
    }

    /**
     * Remove all the points from the list and prompt the user beforehand.
     */
    private void removeAllPoints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_all_points)
                .setMessage(R.string.loose_calculations)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.delete_all,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Job.deleteCurrentJob();
                                PointsManagerActivity.this.drawList();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }

    /**
     * Draw the main table containing all the points.
     */
    private void drawList() {
        ArrayList<Point> points = new ArrayList<Point>(SharedResources.getSetOfPoints());
        this.adapter = new ArrayListOfPointsAdapter(this, R.layout.points_list_item, points);
        this.pointsListView.setAdapter(this.adapter);
        this.pointsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PointsManagerActivity.this.showEditPointDialog(position);
            }
        });
    }

    /**
     * Update the share intent.
     */
    private void updateShareIntent() {
        try {
            SharedResources.getSetOfPoints().saveAsCSV(
                    this, App.tmpDirectoryPath, App.FILENAME_FOR_POINTS_SHARING);
        } catch (IOException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(
                new File(App.tmpDirectoryPath, App.FILENAME_FOR_POINTS_SHARING)));
        sendIntent.setType("text/csv");
        this.setShareIntent(sendIntent);
    }

    @Override
    public void onExportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onExportDialogError(String message) {
        ViewUtils.showToast(this, message);
    }

    @Override
    public void onImportDialogSuccess(String message) {
        ViewUtils.showToast(this, message);
        this.drawList();
        this.updateShareIntent();
    }

    @Override
    public void onImportDialogError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_import_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PointsManagerActivity.this.drawList();
                                PointsManagerActivity.this.updateShareIntent();
                            }
                        });
        builder.create().show();
    }
}