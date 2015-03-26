package ch.hgdev.toposuite.points;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.export.ExportDialog;
import ch.hgdev.toposuite.export.ImportDialog;
import ch.hgdev.toposuite.export.SupportedFileTypes;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

import com.google.common.io.LineReader;

/**
 * Activity to manage points, such as adding, removing or modifying them.
 *
 * @author HGdev
 *
 */
public class PointsManagerActivity extends TopoSuiteActivity implements
        AddPointDialogFragment.AddPointDialogListener,
        EditPointDialogFragment.EditPointDialogListener,
        SearchPointDialogFragment.SearchPointDialogListener,
        ExportDialog.ExportDialogListener,
        ImportDialog.ImportDialogListener {

    private int                      selectedPointId;
    private ListView                 pointsListView;
    private ArrayListOfPointsAdapter adapter;
    private ShareActionProvider      shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_points_manager);

        this.pointsListView = (ListView) this.findViewById(R.id.apm_list_of_points);
        this.registerForContextMenu(this.pointsListView);

        // detect if another app is sending data to this activity
        Uri dataUri = this.getIntent().getData();
        if (dataUri != null) {
            String mime = this.getIntent().getType();

            // minor hack to handle LTOP format
            if (mime.equals("application/octet-stream") || mime.equals("text/plain")
                    || mime.isEmpty()) {
                // We need to check if the file is a LTOP file or not.
                // This verification can only be achieved by reading the
                // first line of the file.
                try {
                    ContentResolver cr = this.getContentResolver();
                    InputStreamReader in = new InputStreamReader(
                            cr.openInputStream(dataUri));
                    LineReader lr = new LineReader(in);
                    String firstLine = lr.readLine();

                    if (firstLine == null) {
                        ViewUtils.showToast(this, this.getString(
                                R.string.error_unsupported_format));
                        return;
                    } else if ((firstLine.length() >= 4)
                            && firstLine.substring(0, 4).equals("$$PK")) {
                        // fix the MIME type
                        mime = "text/ltop";
                    } else {
                        // small hack for handling PTP files because there is no
                        // proper way to detect them
                        mime = "text/ptp";
                    }
                } catch (FileNotFoundException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                    ViewUtils.showToast(this, e.getMessage());
                } catch (IOException e) {
                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                    ViewUtils.showToast(this, e.getMessage());
                }
            }

            this.importFromExternalFile(dataUri, mime);
        }
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
            this.showExportDialog();
            return true;
        case R.id.import_points_button:
            this.showImportDialog();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.points_manager, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        this.shareActionProvider = (ShareActionProvider) item.getActionProvider();
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
        case R.id.edit_point:
            this.showEditPointDialog((int) info.id);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Call to update the share intent
     *
     * @param shareIntent
     *            The share intent.
     */
    private void setShareIntent(Intent shareIntent) {
        if (this.shareActionProvider != null) {
            this.shareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void showExportDialog() {
        ExportDialog dialog = new ExportDialog();
        dialog.show(this.getFragmentManager(), "ExportDialogFragments");
    }

    private void showImportDialog() {
        ImportDialog dialog = new ImportDialog();
        dialog.show(this.getFragmentManager(), "ImportDialogFragment");
    }

    /**
     * Display a dialog to allow the user to insert a new point.
     */
    private void showAddPointDialog() {
        AddPointDialogFragment dialog = new AddPointDialogFragment();
        dialog.show(this.getFragmentManager(), "AddPointDialogFragment");
    }

    private void showSearchPointDialog() {
        SearchPointDialogFragment dialog = new SearchPointDialogFragment();
        dialog.show(this.getFragmentManager(), "SearchPointDialogFragment");
    }

    /**
     * Display a dialog to allow the user to edit a point.
     *
     * @param id
     *            Id of the point to be edited.
     */
    private void showEditPointDialog(int id) {
        EditPointDialogFragment dialog = new EditPointDialogFragment();
        Bundle args = new Bundle();
        this.selectedPointId = id;
        args.putInt(EditPointDialogFragment.POINT_POSITION, id);
        dialog.setArguments(args);

        dialog.show(this.getFragmentManager(), "EditPointDialogFragment");
    }

    /**
     * Create a point based on the input and add it to the table of points and
     * the set of points.
     *
     * @param number
     *            Point's number attribute.
     * @param east
     *            Point's east attribute.
     * @param north
     *            Point's north attribute.
     * @param altitude
     *            Point's altitude attribute.
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
                                SharedResources.getSetOfPoints().clear();
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

    /**
     * TODO
     */
    private final void importFromExternalFile(final Uri dataUri, final String mime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_label)
                .setMessage(R.string.warning_import_file_without_warning_label)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.import_label,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentResolver cr = PointsManagerActivity.this
                                        .getContentResolver();
                                String ext = mime.substring(mime.lastIndexOf("/") + 1);

                                // ugly hack to support ES File Explorer and
                                // Samsung's file explorer that set the MIME
                                // type of a CSV file to
                                // "text/comma-separated-values" instead of
                                // "text/csv"
                                if (ext.equalsIgnoreCase("comma-separated-values")) {
                                    ext = "csv";
                                }

                                // make sure the file format is supported
                                if (!SupportedFileTypes.isSupported(ext)) {
                                    ViewUtils.showToast(PointsManagerActivity.this,
                                            PointsManagerActivity.this.getString(
                                                    R.string.error_unsupported_format));
                                    return;
                                }

                                try {
                                    // clear existing points and calculations
                                    SharedResources.getCalculationsHistory().clear();
                                    SharedResources.getSetOfPoints().clear();

                                    InputStream inputStream = cr.openInputStream(dataUri);
                                    List<Pair<Integer, String>> errors =
                                            PointsImporter.importFromFile(inputStream, ext);

                                    PointsManagerActivity.this.getIntent().setData(null);
                                    PointsManagerActivity.this.drawList();

                                    if (!errors.isEmpty()) {
                                        dialog.dismiss();
                                        PointsManagerActivity.this.onImportDialogError(
                                                PointsImporter.formatErrors(ext, errors));
                                    }

                                } catch (FileNotFoundException e) {
                                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                                    ViewUtils.showToast(PointsManagerActivity.this, e.getMessage());
                                } catch (IOException e) {
                                    Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
                                    ViewUtils.showToast(PointsManagerActivity.this, e.getMessage());
                                }
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
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
        builder.create().show();
    }
}