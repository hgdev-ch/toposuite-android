package ch.hgdev.toposuite.jobs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.List;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.TopoSuiteActivity;
import ch.hgdev.toposuite.dao.CalculationsDataSource;
import ch.hgdev.toposuite.dao.PointsDataSource;
import ch.hgdev.toposuite.utils.AppUtils;
import ch.hgdev.toposuite.utils.DisplayUtils;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.ViewUtils;

public class JobsActivity extends TopoSuiteActivity implements
        RenameCurrentJobFragment.RenameCurrentJobListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ListView jobsListView;
    private TextView jobNameTextView;
    private ArrayListOfJobsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_jobs);

        this.jobsListView = (ListView) this.findViewById(R.id.apm_list_of_jobs);
        this.registerForContextMenu(this.jobsListView);

        this.jobNameTextView = (TextView) this.findViewById(R.id.current_job);
        this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.drawList();
    }

    @Override
    protected String getActivityTitle() {
        return this.getString(R.string.title_activity_jobs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.jobs, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.jobs_list_row_context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.save_job:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.saveJob();
                } else {
                    AppUtils.requestPermission(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE,
                            String.format(this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                }
                return true;
            case R.id.clear_job:
                this.clearJob();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = (int) info.id;
        switch (item.getItemId()) {
            case R.id.import_job:
                if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    JobsActivity.this.importJob(position);
                } else {
                    AppUtils.requestPermission(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                            String.format(JobsActivity.this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                    if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                        JobsActivity.this.importJob(position);
                    }
                }
                return true;
            case R.id.delete_job:
                if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    JobsActivity.this.deleteJob(position);
                } else {
                    AppUtils.requestPermission(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                            String.format(JobsActivity.this.getString(R.string.need_storage_access), AppUtils.getAppName()));
                    if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                        JobsActivity.this.deleteJob(position);
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        switch (AppUtils.Permission.valueOf(requestCode)) {
            case READ_EXTERNAL_STORAGE:
                if (!AppUtils.isPermissionGranted(this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_import));
                }
                break;
            case WRITE_EXTERNAL_STORAGE:
                if (AppUtils.isPermissionGranted(this, AppUtils.Permission.WRITE_EXTERNAL_STORAGE)) {
                    this.saveJob();
                } else {
                    ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_export));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Draw the main table containing all the points.
     */
    private void drawList() {
        if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
            this.adapter = new ArrayListOfJobsAdapter(this, R.layout.jobs_list_item, Job.getJobsList());
            this.jobsListView.setAdapter(this.adapter);
        } else {
            AppUtils.requestPermission(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE,
                    String.format(JobsActivity.this.getString(R.string.need_storage_access), AppUtils.getAppName()));
            if (AppUtils.isPermissionGranted(JobsActivity.this, AppUtils.Permission.READ_EXTERNAL_STORAGE)) {
                this.adapter = new ArrayListOfJobsAdapter(this, R.layout.jobs_list_item, Job.getJobsList());
                this.jobsListView.setAdapter(this.adapter);
            }
        }
    }

    private void importJob(int pos) {
        Job job = this.adapter.getItem(pos);
        File f = job.getTpst();
        try {
            List<String> lines = Files.readLines(f, Charset.defaultCharset());
            // remove previous points and calculations from the SQLite DB
            PointsDataSource.getInstance().truncate();
            CalculationsDataSource.getInstance().truncate();

            // clean in-memory residues
            SharedResources.getSetOfPoints().clear();
            SharedResources.getCalculationsHistory().clear();

            String json = Joiner.on('\n').join(lines);
            Job.loadJobFromJSON(json);
        } catch (IOException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
            ViewUtils.showToast(this, e.getMessage());
            return;
        } catch (JSONException | ParseException e) {
            Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            ViewUtils.showToast(this, e.getMessage());
            return;
        }

        Job.setCurrentJobName(Files.getNameWithoutExtension(f.getName()));
        this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
        this.drawList();
        ViewUtils.showToast(this, this.getString(R.string.success_import_job_dialog));
    }

    private void deleteJob(int pos) {
        Job job = this.adapter.getItem(pos);
        File f = job.getTpst();
        if (f.delete()) {
            ViewUtils.showToast(this, this.getString(R.string.deletion_success));
            this.drawList();
        } else {
            ViewUtils.showToast(this, this.getString(R.string.deletion_failure));
        }
    }

    private void saveJob() {
        String currentJob = Job.getCurrentJobName();
        if (currentJob == null) {
            this.renameJob();
            return;
        }

        String filename = currentJob.concat("." + Job.EXTENSION);
        File f = new File(this.getFilesDir(), filename);
        if (f.isFile()) {
            ViewUtils.showToast(this, this.getString(R.string.error_file_already_exists));
            return;
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(App.publicDataDirectory, filename));
            outputStream.write(Job.getCurrentJobAsJson().getBytes());
            outputStream.close();
            ViewUtils.showToast(this, this.getString(R.string.success_export_job_dialog));
        } catch (IOException | JSONException e) {
            Logger.log(Logger.ErrLabel.IO_ERROR, e.getMessage());
            ViewUtils.showToast(this, this.getString(R.string.error_impossible_to_export));
        } finally {
            this.drawList();
        }
    }

    private void clearJob() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_job)
                .setMessage(R.string.loose_job)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // remove previous points and calculations from the SQLite DB
                                PointsDataSource.getInstance().truncate();
                                CalculationsDataSource.getInstance().truncate();

                                // clean in-memory residues
                                SharedResources.getSetOfPoints().clear();
                                SharedResources.getCalculationsHistory().clear();

                                // update current view
                                Job.setCurrentJobName(null);
                                JobsActivity.this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
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

    private void renameJob() {
        RenameCurrentJobFragment dialog = new RenameCurrentJobFragment();
        dialog.show(this.getSupportFragmentManager(), "RenameCurrentJobFragment");
    }

    @Override
    public void onRenameCurrentJobSuccess(String message) {
        this.jobNameTextView.setText(DisplayUtils.format(Job.getCurrentJobName()));
        ViewUtils.showToast(this, message);
        this.saveJob();
    }

    @Override
    public void onRenameCurrentJobError(String message) {
        ViewUtils.showToast(this, message);
    }
}
