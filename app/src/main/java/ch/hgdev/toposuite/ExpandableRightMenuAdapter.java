package ch.hgdev.toposuite;

import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import ch.hgdev.toposuite.TopoSuiteActivity.ActivityItem;

public class ExpandableRightMenuAdapter extends BaseExpandableListAdapter {
    private Activity                                              activity;
    private final SparseArray<TopoSuiteActivity.CalculationGroup> groups;
    private LayoutInflater                                        inflater;
    private int                                                   lastExpandedGroupPosition;
    private ExpandableListView                                    listView;

    public ExpandableRightMenuAdapter(Activity _activity, ExpandableListView _listView,
            SparseArray<TopoSuiteActivity.CalculationGroup> _groups) {
        this.activity = _activity;
        this.groups = _groups;
        this.inflater = this.activity.getLayoutInflater();
        this.listView = _listView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.groups.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
        final ActivityItem activityItem = (TopoSuiteActivity.ActivityItem) this.getChild(
                groupPosition, childPosition);
        final String children = activityItem.toString();

        TextView text = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.group_child_item, null);
        }

        text = (TextView) convertView.findViewById(R.id.text_item);
        text.setText(children);

        // add an highlight effect when an item is touched
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_HOVER_ENTER:
                case MotionEvent.ACTION_HOVER_MOVE:
                    v.setBackgroundResource(android.R.color.holo_blue_dark);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_HOVER_EXIT:
                    v.setBackgroundResource(android.R.color.transparent);
                }
                return false;
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivityIntent = new Intent(
                        ExpandableRightMenuAdapter.this.activity,
                        activityItem.getActivityClass());
                ExpandableRightMenuAdapter.this.activity.startActivity(
                        newActivityIntent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.groups.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.group_item, null);
        }

        TopoSuiteActivity.CalculationGroup group = (TopoSuiteActivity.CalculationGroup) this
                .getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.getGroupName());
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        if (groupPosition != this.lastExpandedGroupPosition) {
            this.listView.collapseGroup(this.lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);
        this.lastExpandedGroupPosition = groupPosition;
    }
}
