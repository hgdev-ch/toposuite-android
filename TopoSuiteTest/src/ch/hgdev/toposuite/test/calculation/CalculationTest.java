package ch.hgdev.toposuite.test.calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.test.AndroidTestCase;
import ch.hgdev.toposuite.App;

/**
 * Base class for any calculation test.
 * 
 * @author HGdev
 * 
 */
public class CalculationTest extends AndroidTestCase {
    protected DecimalFormat df1;
    protected DecimalFormat df2;
    protected DecimalFormat df3;
    protected DecimalFormat df4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.df1 = new DecimalFormat("#.#");
        this.df1.setRoundingMode(RoundingMode.HALF_UP);
        this.df2 = new DecimalFormat("#.##");
        this.df2.setRoundingMode(RoundingMode.HALF_UP);
        this.df3 = new DecimalFormat("#.###");
        this.df3.setRoundingMode(RoundingMode.HALF_UP);
        this.df4 = new DecimalFormat("#.####");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);

        // we want to keep a good precision for the tests
        App.setCoordinateDecimalRounding(20);
    }

}
