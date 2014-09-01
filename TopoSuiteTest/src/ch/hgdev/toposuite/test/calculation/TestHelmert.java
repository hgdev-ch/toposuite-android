package ch.hgdev.toposuite.test.calculation;

import junit.framework.Assert;
import ch.hgdev.toposuite.SharedResources;
import ch.hgdev.toposuite.calculation.CalculationException;
import ch.hgdev.toposuite.calculation.Helmert;
import ch.hgdev.toposuite.points.Point;

public class TestHelmert extends CalculationTest {
	public void testHelmet1() throws CalculationException {
		// Fill cadastral coordinates
		SharedResources.getSetOfPoints().add(new Point("401", 4637.204, 2209.208, 0.0, true, true));
		
		// Fictive coordinate
		Point p1 = new Point("401", 4637.203, 2209.208, 0.0, true, false);
		
		Helmert.Measure m1 = new Helmert.Measure(p1, true, false);
		
		Helmert helmert = new Helmert(false);
		helmert.getMeasures().add(m1);
		
		helmert.compute();
		
		Assert.assertEquals("178.756", this.df3.format(helmert.getEo()));
		Assert.assertEquals("-210.429", this.df3.format(helmert.getNo()));
		Assert.assertEquals("-11.5500", this.df4.format(helmert.getRotation()));
	}
}
