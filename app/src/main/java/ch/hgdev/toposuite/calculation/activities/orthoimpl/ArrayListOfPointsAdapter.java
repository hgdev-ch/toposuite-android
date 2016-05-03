package ch.hgdev.toposuite.calculation.activities.orthoimpl;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ch.hgdev.toposuite.points.Point;

public class ArrayListOfPointsAdapter extends ArrayAdapter<Point> {

    public ArrayListOfPointsAdapter(Context context, int textViewResourceId, ArrayList<Point> points) {
        super(context, textViewResourceId, points);
    }

    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < this.getCount(); i++) {
            points.add(this.getItem(i));
        }
        return points;
    }
}