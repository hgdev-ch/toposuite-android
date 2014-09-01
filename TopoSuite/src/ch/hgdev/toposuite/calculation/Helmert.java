package ch.hgdev.toposuite.calculation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.common.math.DoubleMath;

import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.R;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.FreeStation.IntermediateResults;
import ch.hgdev.toposuite.points.Point;
import ch.hgdev.toposuite.utils.MathUtils;

public class Helmert extends Calculation {
	private ArrayList<Measure>      measures;
	
	private final ArrayList<FreeStation.Result> results;
	
	private String                  stationNumber;
	private Point                   stationResult;
	
	private double                  no;
	private double                  eo;
	
	/**
	 * Rotation.
	 */
	private double                  rotation;
	
	/**
     * Mean east error.
     */
    private double                  sE;

    /**
     * Mean north error.
     */
    private double                  sN;

    /**
     * Mean altitude error.
     */
    private double                  sA;

    /**
     * Mean FS.
     */
    private double                  meanFS;
	
	public Helmert(CalculationType _type, String _description, boolean hasDAO) {
		super(_type, _description, hasDAO);
		this.results = new ArrayList<FreeStation.Result>();
	}
	
	public Helmert(boolean hasDAO) {
		this(CalculationType.HELMERT, "TODO", hasDAO);
	}

	@Override
	public void compute() throws CalculationException {
		if (!this.hasDeactivatedMeasure()) {
            this.results.clear();
        }
		
		this.sE = 0.0;
        this.sN = 0.0;
        this.sA = 0.0;
        this.meanFS = 0.0;

        // fictive coordinates centroid
        double centroidYFict = 0.0;
        double centroidXFict = 0.0;
        Point centroidFict;

        // cadastral coordinates centroid
        double centroidYCadast = 0.0;
        double centroidXCadast = 0.0;
        Point centroidCadast;

        // altitude related stuff
        double totalWeights = 0.0;
        double meanAltitude = 0.0;
        int nbAltitudes = 0;

        int numberOfDeactivatedOrientations = 0;
        int index = -1;
        
        for (Measure m : this.measures) {
            index++;
            
            if (m.isDeactivated()) {
                numberOfDeactivatedOrientations++;
                continue;
            }
            
            // centroid calculation
            Point tmp = SharedResources.getSetOfPoints().find(m.getPoint().getNumber());
            
            centroidYCadast += tmp.getEast();
            centroidXCadast += tmp.getNorth();

            centroidYFict += m.getPoint().getEast();
            centroidXFict += m.getPoint().getNorth();
            
            this.results.add(new FreeStation.Result(tmp));
        }
        
        int n = this.results.size() - numberOfDeactivatedOrientations;
        centroidFict = new Point("", centroidYFict / n, centroidXFict / n,
                0.0, false, false);
        centroidCadast = new Point("", centroidYCadast / n, centroidXCadast / n,
                0.0, false, false);
        
        this.eo = centroidFict.getEast()-centroidCadast.getEast();
        this.no = centroidFict.getNorth()-centroidCadast.getNorth();

        List<FreeStation.IntermediateResults> intermRes = new ArrayList<FreeStation.IntermediateResults>();
        double meanRotations = 0.0;
        double meanConstants = 0.0;

        for (int i = 0; i < this.results.size(); i++) {
            if (this.measures.get(i).isDeactivated()) {
                // dummy intermediate results in order to avoid indexes problems
                intermRes.add(new FreeStation.IntermediateResults(
                        MathUtils.IGNORE_DOUBLE,
                        MathUtils.IGNORE_DOUBLE,
                        MathUtils.IGNORE_DOUBLE,
                        MathUtils.IGNORE_DOUBLE));
                continue;
            }
            
            Gisement g1 = new Gisement(
                    centroidFict, this.results.get(i).getPoint(), false);
            Gisement g2 = new Gisement(
                    centroidCadast, this.measures.get(i).getPoint(), false);
            intermRes.add(
                    new FreeStation.IntermediateResults(
                            g1.getGisement(), g1.getHorizDist(),
                            g2.getGisement(), g2.getHorizDist()));

            // calculation of the rotation between fictive and cadastral
            // coordinates according to the following formula:
            // mod400(gis_cadastral - gis_fictive)
            double rotation = (g2.getGisement() - g1.getGisement()) + 400.0;
            intermRes.get(i).rotation = rotation;

            // calculation of the multiplication constants between fictive and
            // cadastral coordinates according to the following formula:
            // dist_cadastral / dist_fictive
            double constant = g2.getHorizDist() / g1.getHorizDist();
            intermRes.get(i).constant = constant;
            meanConstants += constant;
        }
        
        double minRotation = this.getMinRotation(intermRes);
        for (IntermediateResults res : intermRes) {
            double rotation = res.rotation;
            // skip values to ignore (deactivated measures)
            if (MathUtils.isIgnorable(rotation)) {
                continue;
            }
            if (DoubleMath.fuzzyCompare(rotation - minRotation, -15.0, App.getAngleTolerance()) < 0) {
                rotation += 400.0;
            } else if (DoubleMath.fuzzyCompare(rotation - minRotation, 15.0,
                    App.getAngleTolerance()) > 0) {
                rotation -= 400.0;
            }
            meanRotations += rotation;
        }

        meanRotations = MathUtils.modulo400(meanRotations / n);
        meanConstants /= n;

        // calculation of the gisement/distance between the fictive centroid and
        // the station (which is actually at the coordinates 0;0).
        this.stationResult = new Point(this.stationNumber, 0.0, 0.0, 0.0, false, false);
        Gisement g = new Gisement(centroidFict, this.stationResult, false);
        double gisFictiveGToSt = g.getGisement(); // gisement g-St
        double distFictiveGToSt = g.getHorizDist(); // distance g-St
        
        // calculation of the station coordinates
        double tmp1 = MathUtils.gradToRad(gisFictiveGToSt + meanRotations);
        double tmp2 = distFictiveGToSt * meanConstants;
        this.stationResult.setEast((Math.sin(tmp1) * tmp2) + centroidCadast.getEast());
        this.stationResult.setNorth((Math.cos(tmp1) * tmp2) + centroidCadast.getNorth());
        double altitude = (!MathUtils.isZero(meanAltitude)) ? meanAltitude / totalWeights : 0.0;
        this.stationResult.setAltitude(altitude);

        double diffAlt = 0.0;

        double u = 0.0;
        double v = 0.0;

        for (int i = 0; i < this.results.size(); i++) {
            if (this.measures.get(i).isDeactivated()) {
                continue;
            }

            // vE [cm]
            double newE = this.measures.get(i).getPoint().getEast() + eo;
            double vE = (newE - this.measures.get(i).getPoint().getEast()) * 100;
            this.results.get(i).setvE(vE);

            // vN [cm]
            double newN = this.measures.get(i).getPoint().getNorth() + no;
            double vN = (newN - this.measures.get(i).getPoint().getNorth()) * 100;
            this.results.get(i).setvN(vN);

            // vA [cm]
            double vA = (altitude - this.results.get(i).getPoint().getAltitude()) * 100;
            this.results.get(i).setvA(vA);

            // FS [cm]
            double fS = Math.sqrt(Math.pow(vE, 2) + Math.pow(vN, 2));
            this.results.get(i).setfS(fS);

            this.sE += vE * vE;
            this.sN += vN * vN;
            diffAlt += this.results.get(i).getWeight() * Math.pow(vA, 2);
            this.meanFS += fS;

            u += Math.pow(this.results.get(i).getPoint().getEast() - centroidYFict, 2);
            v += Math.pow(this.results.get(i).getPoint().getNorth() - centroidXFict, 2);
        }

        this.sE = Math.sqrt((this.sE + this.sN) / ((2 * (
                this.results.size() - numberOfDeactivatedOrientations)) - 4));
        this.sE = this.sE * Math.sqrt(
                (1 / (this.results.size() - numberOfDeactivatedOrientations)) + ((
                        Math.pow(centroidYFict, 2) + Math.pow(centroidXFict, 2)) / (u + v)));
        this.sN = this.sE;

        this.sA = Math.sqrt(diffAlt / ((nbAltitudes - 1) * totalWeights));
        this.meanFS /= (this.results.size() - numberOfDeactivatedOrientations);

        this.rotation = meanRotations;
        
        // FIXME Improve history label
        this.updateLastModification();
        this.setDescription(this.getCalculationName()
                + " - " + App.getContext().getString(R.string.station_label));
        this.notifyUpdate(this);
	}

	@Override
	public Class<?> getActivityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCalculationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportToJSON() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importFromJSON(String jsonInputArgs) throws JSONException {
		// TODO Auto-generated method stub
	}
	
	/**
     * Find the minimum rotation from the list of intermediate results.
     * 
     * @param results
     * @return
     */
    private double getMinRotation(List<IntermediateResults> results) {
        double minRotation = Double.MAX_VALUE;
        for (IntermediateResults r : results) {
            if (MathUtils.isIgnorable(r.rotation)) {
                continue;
            }
            if (DoubleMath.fuzzyCompare(r.rotation, minRotation, App.getAngleTolerance()) < 0) {
                minRotation = r.rotation;
            }
        }
        return minRotation;
    }
    
    public final Point getStationResult() {
        return this.stationResult;
    }
	
	private boolean hasDeactivatedMeasure() {
        for (Measure m : this.measures) {
            if (m.isDeactivated()) {
                return true;
            }
        }
        return false;
    }
	
	public final ArrayList<Measure> getMeasures() {
		return measures;
	}

	public final void setMeasures(ArrayList<Measure> measures) {
		this.measures = measures;
	}

	public final double getRotation() {
		return rotation;
	}

	public final void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public final double getsE() {
		return sE;
	}

	public final void setsE(double sE) {
		this.sE = sE;
	}

	public final double getsN() {
		return sN;
	}

	public final void setsN(double sN) {
		this.sN = sN;
	}

	public final double getsA() {
		return sA;
	}

	public final void setsA(double sA) {
		this.sA = sA;
	}

	public final double getMeanFS() {
		return meanFS;
	}

	public final void setMeanFS(double meanFS) {
		this.meanFS = meanFS;
	}

	public final ArrayList<FreeStation.Result> getResults() {
		return results;
	}

	public final double getNo() {
		return no;
	}

	public final void setNo(double no) {
		this.no = no;
	}

	public final double getEo() {
		return eo;
	}

	public final void setEo(double eo) {
		this.eo = eo;
	}

	public static class Measure {
		 private Point   point;
		
		/**
         * Planimetry adjustment point.
         */
        private boolean planimetryAdjustmentPoint;
        
        /**
         * Altimetry adjustment point.
         */
        private boolean altimetryAdjustmentPoint;
        
        private boolean deactivated;
        
        public Measure(Point _point, boolean _planimetryAdjustmentPoint, boolean _altimetryAdjustmentPoint) {
        	this.point = _point;
        	this.planimetryAdjustmentPoint = _planimetryAdjustmentPoint;
        	this.altimetryAdjustmentPoint = _altimetryAdjustmentPoint;
        }
        
        public Measure(Point _point, boolean _planimetryAdjustmentPoint, boolean _altimetryAdjustmentPoint, boolean _deactivated) {
        	this(_point, _planimetryAdjustmentPoint, _altimetryAdjustmentPoint);
        	this.deactivated = _deactivated;
        }

		public final Point getPoint() {
			return point;
		}

		public final void setPoint(Point point) {
			this.point = point;
		}

		public final boolean isPlanimetryAdjustmentPoint() {
			return planimetryAdjustmentPoint;
		}

		public final void setPlanimetryAdjustmentPoint(boolean planimetryAdjustmentPoint) {
			this.planimetryAdjustmentPoint = planimetryAdjustmentPoint;
		}

		public final boolean isAltimetryAdjustmentPoint() {
			return altimetryAdjustmentPoint;
		}

		public final void setAltimetryAdjustmentPoint(boolean altimetryAdjustmentPoint) {
			this.altimetryAdjustmentPoint = altimetryAdjustmentPoint;
		}
		
		public final boolean isDeactivated() {
            return this.deactivated;
        }
	}
}
