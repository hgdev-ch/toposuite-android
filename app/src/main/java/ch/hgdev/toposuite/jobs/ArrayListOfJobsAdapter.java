package ch.hgdev.toposuite.jobs;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Adapter to correctly format a list of jobs to display.
 *
 * @author HGdev
 */
public class ArrayListOfJobsAdapter extends ArrayAdapter<Job> {
    private final ArrayList<Job> jobs;
    private final Context context;

    public ArrayListOfJobsAdapter(@NonNull  Context context, int textViewResourceId, @NonNull ArrayList<Job> jobs) {
        super(context, textViewResourceId, jobs);
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.jobs_list_item, parent, false);
        }

        if (!this.jobs.isEmpty()) {
            Job job = this.jobs.get(position);
            if (job != null) {
                TextView nameTextView = (TextView) view.findViewById(R.id.job_filename);
                TextView lastModificationTextView = (TextView) view.findViewById(R.id.job_file_last_modification);

                if (nameTextView != null) {
                    nameTextView.setText(job.getName());
                }

                if (lastModificationTextView != null) {
                    lastModificationTextView.setText(DisplayUtils.formatDate(job.getLastModified()));
                }
            }
        }

        return view;
    }
}