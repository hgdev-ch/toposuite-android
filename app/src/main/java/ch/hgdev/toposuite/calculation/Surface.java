package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.surface.SurfaceActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

public class Surface extends Calculation {
    private static final String                 SURFACE             = "Surface: ";

    private static final String                 POINTS_LIST         = "points_list";
    private static final String                 SURFACE_NAME        = "surface_name";
    private static final String                 SURFACE_DESCRIPTION = "surface_description";

    private String                              surfaceName;
    private String                              surfaceDescription;
    private double                              surface;
    private double                              perimeter;
    private final List<Surface.PointWithRadius> points;

    public Surface(long id, Date lastModification) {
        super(id,
                CalculationType.SURFACE,
                App.getContext().getString(R.string.title_activity_surface),
                lastModification,
                true);
        this.points = new ArrayList<Surface.PointWithRadius>();
    }

    public Surface(String _surfaceName, String _surfaceDescription, boolean hasDAO) {
        super(CalculationType.SURFACE,
                App.getContext().getString(R.string.title_activity_surface),
                hasDAO);
        this.setSurfaceName(_surfaceName);
        this.setSurfaceDescription(_surfaceDescription);
        this.points = new ArrayList<Surface.PointWithRadius>();
        this.surface = 0.0;
        this.perimeter = 0.0;

        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    /**
     * Check input.
     *
     * @return True if the input is OK and the calculation can be run, false
     *         otherwise.
     */
    private boolean checkInput() {
        // we need at least three points to define a surface
        if (this.points.size() < 3) {
            Logger.log(Logger.WarnLabel.CALCULATION_IMPOSSIBLE,
                    Surface.SURFACE + "at least three points must be provided to define a surface.");
            return false;
        }
        return true;
    }

    @Override
    public void compute() {
        if (!this.checkInput()) {
            return;
        }
        this.surface = 0.0;
        this.perimeter = 0.0;

        int j;
        int nbVertex = this.points.size();
        // compute polygon
        for (int i = 0; i < nbVertex; i++) {
            // last vertex is also the first to close the surface
            if (i == (nbVertex - 1)) {
                j = 0;
            } else {
                j = i + 1;
            }

            PointWithRadius p1 = this.points.get(i);
            PointWithRadius p2 = this.points.get(j);

            this.surface += (((p2.getEast() - p1.getEast()) * (p2.getNorth() + p1.getNorth()))) / 2;

            // compute circular segment
            double radius = Math.abs(p1.getRadius());
            if (MathUtils.isPositive(radius)) {
                // compute angle at the center
                double alpha = Math.asin(MathUtils.euclideanDistance(p1, p2) / (2 * radius)) * 2;
                // compute circular segment
                double segment = ((Math.pow(radius, 2)) * (alpha - Math.sin(alpha))) / 2;
                if (MathUtils.isPositive(p1.getRadius())) {
                    this.surface += segment;
                } else {
                    this.surface -= segment;
                }
                this.perimeter += alpha * radius;
            } else {
                this.perimeter += MathUtils.euclideanDistance(p1, p2);
            }
        }
        this.surface = Math.abs(this.surface);

        this.updateLastModification();
        this.setDescription(this.getCalculationName()
                + (this.surfaceName.isEmpty() ? "" : " - " + this.surfaceName));
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.points.size() > 0) {
            JSONArray pointsArray = new JSONArray();
            for (Surface.PointWithRadius p : this.points) {
                pointsArray.put(p.toJSONObject());
            }
            json.put(Surface.POINTS_LIST, pointsArray);
        }
        json.put(Surface.SURFACE_NAME, this.surfaceName);
        json.put(Surface.SURFACE_DESCRIPTION, this.surfaceDescription);
        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);
        JSONArray pointsArray = json.getJSONArray(Surface.POINTS_LIST);

        for (int i = 0; i < pointsArray.length(); i++) {
            JSONObject jo = (JSONObject) pointsArray.get(i);
            Surface.PointWithRadius p = Surface.PointWithRadius.getPointFromJSON(jo.toString());
            this.points.add(p);
        }
        this.surfaceName = json.getString(Surface.SURFACE_NAME);
        this.surfaceDescription = json.getString(Surface.SURFACE_DESCRIPTION);
    }

    @Override
    public Class<?> getActivityClass() {
        return SurfaceActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_surface);
    }

    public String getSurfaceName() {
        return this.surfaceName;
    }

    public void setSurfaceName(String _name) {
        this.surfaceName = _name != null ? _name : "";
    }

    public String getSurfaceDescription() {
        return this.surfaceDescription;
    }

    public void setSurfaceDescription(String _description) {
        this.surfaceDescription = _description != null ? _description : "";
    }

    public double getSurface() {
        return this.surface;
    }

    public double getPerimeter() {
        return this.perimeter;
    }

    public List<Surface.PointWithRadius> getPoints() {
        return this.points;
    }

    public void setPoints(List<Surface.PointWithRadius> _points) {
        // this.points = _points;
        this.points.clear();
        this.points.addAll(_points);
    }

    /**
     * Point with a radius.
     *
     * @author HGdev
     *
     */
    public static class PointWithRadius extends Point {
        private static final String NUMBER        = "number";
        private static final String EAST          = "east";
        private static final String NORTH         = "north";
        private static final String RADIUS        = "radius";
        private static final String VERTEX_NUMBER = "vertex_number";
        /**
         * Radius wrt to the point of origin. Altitude is ignored.
         */
        private double              radius;
        private int                 vertexNumber;

        public PointWithRadius(String number, double east, double north, double _radius,
                int _vertexNumber) {
            super(number, east, north, MathUtils.IGNORE_DOUBLE, false);
            this.radius = _radius;
            this.vertexNumber = _vertexNumber;
        }

        public PointWithRadius(String number, double east, double north, int _vertexNumber) {
            super(number, east, north, MathUtils.IGNORE_DOUBLE, false);
            this.radius = 0.0;
            this.vertexNumber = _vertexNumber;
        }

        public JSONObject toJSONObject() {
            JSONObject json = new JSONObject();
            try {
                json.put(Surface.PointWithRadius.NUMBER, this.getNumber());
                json.put(Surface.PointWithRadius.EAST, this.getEast());
                json.put(Surface.PointWithRadius.NORTH, this.getNorth());
                json.put(Surface.PointWithRadius.RADIUS, this.radius);
                json.put(Surface.PointWithRadius.VERTEX_NUMBER, this.vertexNumber);
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            }
            return json;
        }

        public static PointWithRadius getPointFromJSON(String json) {
            PointWithRadius p = null;
            try {
                JSONObject jo = new JSONObject(json);
                String number = jo.getString(Surface.PointWithRadius.NUMBER);
                double east = jo.getDouble(Surface.PointWithRadius.EAST);
                double north = jo.getDouble(Surface.PointWithRadius.NORTH);
                double radius = jo.getDouble(Surface.PointWithRadius.RADIUS);
                int vertex_number = jo.getInt(Surface.PointWithRadius.VERTEX_NUMBER);
                p = new PointWithRadius(number, east, north, radius, vertex_number);
            } catch (JSONException e) {
                Logger.log(Logger.ErrLabel.PARSE_ERROR, e.getMessage());
            }
            return p;
        }

        public double getRadius() {
            return this.radius;
        }

        public void setRadius(double _radius) {
            this.radius = _radius;
        }

        public int getVertexNumber() {
            return this.vertexNumber;
        }

        public void setVertexNumber(int vertexNumber) {
            this.vertexNumber = vertexNumber;
        }
    }
}
