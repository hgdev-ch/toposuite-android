package ch.hgdev.toposuite.jobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.utils.DisplayUtils;

/**
 * Adapter to correctly format a list of jobs to display.
 *
 * @author HGdev
 */
public class ArrayListOfJobFilesAdapter extends ArrayAdapter<File> {
    private final ArrayList<File> files;
    private final Context context;

    public ArrayListOfJobFilesAdapter(Context context, int textViewResourceId, ArrayList<File> files) {
        super(context, textViewResourceId, files);
        this.files = files;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.jobs_list_item, null);
        }

        File f = this.files.get(position);
        if (f != null) {
            TextView nameTextView = (TextView) view.findViewById(R.id.job_filename);
            TextView lastModificationTextView = (TextView) view.findViewById(R.id.job_file_last_modification);

            if (nameTextView != null) {
                nameTextView.setText(f.getName());
            }

            if (lastModificationTextView != null) {
                lastModificationTextView.setText(DisplayUtils.formatDate(f.lastModified()));
            }
        }

        return view;
    }
}