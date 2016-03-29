package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.activities.circularsegmentation.CircularSegmentationActivity;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.Logger;
import ch.hgdev.toposuite.utils.MathUtils;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

/**
 * Arc of circle segmentation calculation.
 *
 * @author HGdev
 *
 */
public class CircularSegmentation extends Calculation {

    public static final String  CIRCLE_CENTER         = "circle_center";
    public static final String  CIRCLE_START_POINT    = "circle_start_point";
    public static final String  CIRCLE_END_POINT      = "circle_end_point";
    public static final String  ARC_LENGTH            = "arc_length";
    public static final String  NUMBER_OF_SEGMENTS    = "number_of_segments";

    private static final String CIRCULAR_SEGMENTATION = "Circular segmentation: ";

    /**
     * Center of the circle.
     */
    private Point               circleCenter;
    private Point               circleStartPoint;
    private Point               circleEndPoint;
    /**
     * Radius of the circle.
     */
    private double              circleRadius;
    /**
     * Number of segments in which to partition the circle. This is used of the
     * length of an arc is not set.
     */
    private int                 numberOfSegments;
    /**
     * Length of an arc. This is used if the number of segments is not set.
     */
    private double              arcLength;

    /**
     * Resulting points.
     */
    private List<Point>         points;

    public CircularSegmentation(long id, Date lastModification) {
        super(id,
                CalculationType.CIRCULARSEGMENTATION,
                App.getContext().getString(R.string.title_activity_circular_segmentation),
                lastModification,
                true);
    }

    public CircularSegmentation(boolean hasDAO) {
        super(CalculationType.CIRCULARSEGMENTATION,
                App.getContext().getString(R.string.title_activity_circular_segmentation),
                hasDAO);
        this.initAttributes();
        if (hasDAO) {
            SharedResources.getCalculationsHistory().add(0, this);
        }
    }

    public CircularSegmentation() {
        this(true);
    }

    /**
     * Initialize class attributes to some default values.
     */
    public void initAttributes() {
        this.circleCenter = new Point(false);
        this.circleStartPoint = new Point(false);
        this.circleEndPoint = new Point(false);

        this.numberOfSegments = MathUtils.IGNORE_INT;
        this.arcLength = MathUtils.IGNORE_DOUBLE;
        this.circleRadius = MathUtils.IGNORE_DOUBLE;

        this.points = new ArrayList<Point>();
    }

    /**
     * Perform some checks on given values and set class attributes.
     *
     * @param center
     *            Center of the circle.
     * @param start
     *            Start point on the border of the circle.
     * @param end
     *            End point on the border of the circle.
     * @param numberOfSegments
     *            Number of segments in which to segment. This value must be set
     *            to MathUtils.IGNORE_INT if arcLength is specified.
     * @param arcLength
     *            Length of an arc. This value must be set to
     *            MathUtils.IGNORE_DOUBLE if numberOfSegments is specified.
     * @throws IllegalArgumentException
     *             Raised when some given arguments are not consistent.
     */
    public void initAttributes(Point center, Point start, Point end,
            int numberOfSegments, double arcLength) throws IllegalArgumentException {
        Preconditions.checkNotNull(center);
        Preconditions.checkNotNull(start);
        Preconditions.checkNotNull(end);

        if ((MathUtils.isIgnorable(numberOfSegments) && MathUtils.isIgnorable(arcLength))
                || (!MathUtils.isIgnorable(numberOfSegments) && !MathUtils.isIgnorable(arcLength))) {
            String msg = CircularSegmentation.CIRCULAR_SEGMENTATION
                    + "either the length of an arc or the number of segments "
                    + "must be provided but not none or both.";
            Logger.log(Logger.ErrLabel.INPUT_ERROR, msg);
            throw new IllegalArgumentException(msg);
        }

        if ((numberOfSegments < 2) && MathUtils.isIgnorable(arcLength)) {
            String msg = CircularSegmentation.CIRCULAR_SEGMENTATION
                    + "at least two segments must be chosen for a segmentation.";
            Logger.log(Logger.ErrLabel.INPUT_ERROR, msg);
            throw new IllegalArgumentException(msg);
        }

        if (!(MathUtils.isPositive(arcLength)) && MathUtils.isIgnorable(numberOfSegments)) {
            String msg = CircularSegmentation.CIRCULAR_SEGMENTATION
                    + "the arc length must be positive.";
            Logger.log(Logger.ErrLabel.INPUT_ERROR, msg);
            throw new IllegalArgumentException(msg);
        }

        if (start.equals(end) || start.equals(center) || end.equals(center)) {
            String msg = CircularSegmentation.CIRCULAR_SEGMENTATION
                    + "all points must be different from each another.";
            Logger.log(Logger.ErrLabel.INPUT_ERROR, msg);
            throw new IllegalArgumentException(msg);
        }

        double radiusStart = MathUtils.euclideanDistance(start, center);
        double radiusEnd = MathUtils.euclideanDistance(end, center);
        if (!(DoubleMath.fuzzyEquals(radiusStart, radiusEnd, App.getCoordinateTolerance()))) {
            String msg = String.format(CircularSegmentation.CIRCULAR_SEGMENTATION
                    + "the two points must be at the same distance from the center each."
                    + "Radius according to the starting point is %f.\n"
                    + "Radius according to the ending point is %f.\n",
                    radiusStart, radiusEnd);
            Logger.log(Logger.ErrLabel.INPUT_ERROR, msg);
            throw new IllegalArgumentException(msg);
        }

        this.circleCenter = center;
        this.circleStartPoint = start;
        this.circleEndPoint = end;
        // Taking the mean of the radius from the two points should be a little
        // more accurate than taking arbitrary the radius from one or the other.
        // Remember that the radius have been checked to be equal within a
        // tolerance.
        this.circleRadius = DoubleMath.mean(radiusStart, radiusEnd);

        this.numberOfSegments = numberOfSegments;
        this.arcLength = arcLength;

        this.points = new ArrayList<Point>();
    }

    @Override
    public void compute() throws CalculationException {
        double angle = MathUtils.angle3Pts(
                this.circleStartPoint, this.circleCenter, this.circleEndPoint);

        int numberOfSegmentsForCalc = this.numberOfSegments;

        if (!MathUtils.isIgnorable(this.numberOfSegments)) {
            angle /= numberOfSegmentsForCalc;
            // it is not necessary to compute the last point
            numberOfSegmentsForCalc--;
        } else if (!MathUtils.isIgnorable(this.arcLength)) {
            double alpha = MathUtils.radToGrad(this.arcLength / this.circleRadius);
            numberOfSegmentsForCalc = (int) Math.floor(angle / alpha);

            // update the number of segments attributes
            this.numberOfSegments = numberOfSegmentsForCalc;

            angle = alpha;
        } else {
            String msg = CircularSegmentation.CIRCULAR_SEGMENTATION
                    + "either the number of segments or the length of an arc must be ignorable.";
            Logger.log(Logger.ErrLabel.INPUT_ERROR, msg);
            throw new CalculationException(msg);
        }

        // clear results
        this.points.clear();
        double gis = new Gisement(this.circleCenter, this.circleStartPoint, false).getGisement();
        for (int i = 0; i < numberOfSegmentsForCalc; i++) {
            gis += angle;
            double east = MathUtils.pointLanceEast(
                    this.circleCenter.getEast(), gis, this.circleRadius);
            double north = MathUtils.pointLanceNorth(
                    this.circleCenter.getNorth(), gis, this.circleRadius);
            Point p = new Point("", east, north, MathUtils.IGNORE_DOUBLE, false);
            this.points.add(p);
        }

        this.updateLastModification();
        this.notifyUpdate(this);
    }

    @Override
    public String exportToJSON() throws JSONException {
        JSONObject json = new JSONObject();

        if (this.circleCenter != null) {
            json.put(CircularSegmentation.CIRCLE_CENTER, this.circleCenter.getNumber());
        }
        if (this.circleStartPoint != null) {
            json.put(CircularSegmentation.CIRCLE_START_POINT, this.circleStartPoint.getNumber());
        }
        if (this.circleEndPoint != null) {
            json.put(CircularSegmentation.CIRCLE_END_POINT, this.circleEndPoint.getNumber());
        }
        json.put(CircularSegmentation.ARC_LENGTH, this.arcLength);
        json.put(CircularSegmentation.NUMBER_OF_SEGMENTS, this.numberOfSegments);

        return json.toString();
    }

    @Override
    public void importFromJSON(String jsonInputArgs) throws JSONException {
        JSONObject json = new JSONObject(jsonInputArgs);

        this.circleCenter = SharedResources.getSetOfPoints().find(
                json.getString(CircularSegmentation.CIRCLE_CENTER));
        this.circleStartPoint = SharedResources.getSetOfPoints().find(
                json.getString(CircularSegmentation.CIRCLE_START_POINT));
        this.circleEndPoint = SharedResources.getSetOfPoints().find(
                json.getString(CircularSegmentation.CIRCLE_END_POINT));

        this.arcLength = json.getDouble(CircularSegmentation.ARC_LENGTH);
        this.numberOfSegments = json.getInt(CircularSegmentation.NUMBER_OF_SEGMENTS);
    }

    @Override
    public Class<?> getActivityClass() {
        return CircularSegmentationActivity.class;
    }

    @Override
    public String getCalculationName() {
        return App.getContext().getString(R.string.title_activity_circular_segmentation);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public double getCircleRadius() {
        return this.circleRadius;
    }

    public Point getCircleCenter() {
        return this.circleCenter;
    }

    public Point getCircleStartPoint() {
        return this.circleStartPoint;
    }

    public Point getCircleEndPoint() {
        return this.circleEndPoint;
    }

    public int getNumberOfSegments() {
        return this.numberOfSegments;
    }

    public double getArcLength() {
        return this.arcLength;
    }
}