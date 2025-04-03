package ch.hgdev.toposuite.points;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.SQLiteTopoSuiteException;
import ch.hgdev.toposuite.jobs.Job;
import ch.hgdev.toposuite.transfer.ExportDialogListener;
import ch.hgdev.toposuite.transfer.ImportDialogListener;
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
        ExportDialogListener,
        ImportDialogListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private int selectedPointId;
    private ListView pointsListView;
    private ArrayListOfPointsAdapter adapter;
    private File tmpPointsFile;

    private boolean shouldShowImportDialog;
    private boolean shouldShowExportDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.pointsListView = (ListView) this.findViewById(R.id.apm_list_of_points);
        this.registerForContextMenu(this.pointsListView);

        FloatingActionButton addButton = (FloatingActionButton) this.findViewById(R.id.add_point_button);
        addButton.setOnClickListener(v -> PointsManagerActivity.this.showAddPointDialog()
        );

        this.shouldShowExportDialog = false;
        this.shouldShowImportDialog = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.selectedPointId = 0;
        this.drawList();
    }

    @Override
    protected void onPostResume() {
        // workaround for this Android issue: https://code.google.com/p/android/issues/detail?id=190966
        // TODO: remove once fixed
        if (this.shouldShowImportDialog) {
            this.showImportDialog();
            this.shouldShowImportDialog = false;
        }
        if (this.shouldShowExportDialog) {
            this.showExportDialog();
            this.shouldShowExportDialog = false;
        }
        super.onPostResume();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_points_manager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_points_button) {
            this.removeAllPoints();
            return true;
        }
        if (id == R.id.export_points_button) {
            this.showExportDialog();
            return true;
        }
        if (id == R.id.import_points_button) {
            this.showImportDialog();
            return true;
        }
        if (id == R.id.share_job_button) {
            this.sharePoints();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_points_manager, menu);

        MenuItem itemSearch = menu.findItem(R.id.search_point_button);
        SearchManager sm = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(itemSearch);
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if ((query != null) && (!query.isEmpty())) {
                    Point point = SharedResources.getSetOfPoints().find(query);
                    if (point != null) {
                        int position = PointsManagerActivity.this.adapter.getPosition(point);
                        PointsManagerActivity.this.showEditPointDialog(position);
                        return true;

                    } else {
                        ViewUtils.showToast(PointsManagerActivity.this, PointsManagerActivity.this.getString(R.string.point_not_found));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if ((query == null) || query.isEmpty()) {
                    PointsManagerActivity.this.drawList();
                    return false;
                }

                PointsManagerActivity.this.adapter.clear();
                ArrayList<Point> points = new ArrayList<>(SharedResources.getSetOfPoints());
                for (Point p : points) {
                    if (p.getNumber().contains(query)) {
                        PointsManagerActivity.this.adapter.add(p);
                    }
                }
                PointsManagerActivity.this.pointsListView.setAdapter(PointsManagerActivity.this.adapter);
                return false;
            }
        });

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
        this.prepareSharingFile();
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogCancel(AddPointDialogFragment dialog) {
        ViewUtils.unlockScreenOrientation(this);
    }

    @Override
    public void onDialogEdit(EditPointDialogFragment dialog) {
        Point point = SharedResources.getSetOfPoints().get(this.selectedPointId);
        point.setEast(dialog.getEast());
        point.setNorth(dialog.getNorth());
        point.setAltitude(dialog.getAltitude());
        this.editPoint(point);
        this.drawList();
        this.prepareSharingFile();
    }

    @Override
    public void onDialogCancel(EditPointDialogFragment dialog) {
        // do nothing
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_list_row_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Point point;
        if (item.getItemId() == R.id.delete_button) {
            point = SharedResources.getSetOfPoints().get((int) info.id);
            this.adapter.remove(point);
            this.adapter.notifyDataSetChanged();
            SharedResources.getSetOfPoints().remove(point);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showExportDialog() {
        PointsExporterDialog dialog = new PointsExporterDialog();
        dialog.show(this.getSupportFragmentManager(), "ExportDialogFragments");
    }

    private void showImportDialog() {
        PointsImporterDialog dialog = new PointsImporterDialog();
        dialog.show(this.getSupportFragmentManager(), "ImportDialogFragment");
    }

    /**
     * Display a dialog to allow the user to insert a new point.
     */
    private void showAddPointDialog() {
        ViewUtils.lockScreenOrientation(this);
        AddPointDialogFragment dialog = new AddPointDialogFragment();
        dialog.show(this.getSupportFragmentManager(), "AddPointDialogFragment");
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
            Point point = new Point(number, east, north, altitude, true);
            SharedResources.getSetOfPoints().add(point);
        }
    }

    /**
     * Replace an existing point with the one provided as input.
     */
    private void editPoint(Point point) {
        Point oldPoint = SharedResources.getSetOfPoints().find(point.getNumber());
        if (oldPoint == null) {
            ViewUtils.showToast(this, this.getString(R.string.error_point_number));
        } else {
            SharedResources.getSetOfPoints().remove(oldPoint);
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
                .setIcon(R.drawable.ic_dialog_warning)
                .setPositiveButton(R.string.delete_all,
                        (dialog, which) -> {
                            try {
                                Job.deleteCurrentJob();
                            } catch (SQLiteTopoSuiteException e) {
                                Logger.log(Logger.ErrLabel.SQL_ERROR, e.getMessage());
                            }
                            PointsManagerActivity.this.drawList();
                        })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    // do nothing
                });
        builder.create().show();
    }

    /**
     * Draw the main table containing all the points.
     */
    private void drawList() {
        ArrayList<Point> points = new ArrayList<>(SharedResources.getSetOfPoints());
        this.adapter = new ArrayListOfPointsAdapter(this, R.layout.points_list_item, points);
        this.pointsListView.setAdapter(this.adapter);
        this.pointsListView.setOnItemClickListener((parent, view, position, id) -> PointsManagerActivity.this.showEditPointDialog(position));
    }

    /**
     * Share points with other applications
     */
    private void sharePoints() {
        if (this.tmpPointsFile != null && this.tmpPointsFile.exists()) {
            try {
                Uri fileUri = FileProvider.getUriForFile(this, this.getPackageName(), this.tmpPointsFile);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/csv");
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Intent chooser = Intent.createChooser(intent, getString(R.string.share_points));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                } else {
                    ViewUtils.showToast(this, getString(R.string.error_no_app_for_sharing));
                }
            } catch (Exception e) {
                Logger.log(Logger.ErrLabel.IO_ERROR, "Failed to share points: " + e.getMessage());
                ViewUtils.showToast(this, getString(R.string.error_failed_to_share));
            }
        } else {
            this.prepareSharingFile();
            this.sharePoints();
        }
    }

    /**
     * Prepare the file for sharing.
     */
    private void prepareSharingFile() {
        try {
            final File tmpPointsPath = new File(this.getCacheDir(), "points");
            if (!tmpPointsPath.exists()) {
                if (!tmpPointsPath.mkdir()) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, "failed to create directory " + tmpPointsPath.getAbsolutePath());
                    return;
                }
            }
            String currentJobName = Job.getCurrentJobName();
            String name = (currentJobName == null) || (currentJobName.isEmpty()) ? "points" : currentJobName;
            this.tmpPointsFile = new File(tmpPointsPath, name + ".csv");
            SharedResources.getSetOfPoints().saveAsCSV(this, this.tmpPointsFile);
        } catch (IOException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
            this.tmpPointsFile = null;
        }
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
        this.prepareSharingFile();
    }

    @Override
    public void onImportDialogError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_import_label)
                .setIcon(R.drawable.ic_dialog_error)
                .setMessage(message)
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> {
                            PointsManagerActivity.this.drawList();
                            PointsManagerActivity.this.prepareSharingFile();
                        });
        builder.create().show();
    }
}
