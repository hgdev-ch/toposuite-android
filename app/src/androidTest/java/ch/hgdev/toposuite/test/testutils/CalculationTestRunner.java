package ch.hgdev.toposuite.test.testutils;

import android.support.test.runner.AndroidJUnitRunner;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import ch.hgdev.toposuite.App;

/**
 * Base class for any calculation test.
 *
 * @author HGdev
 *
 */
public class CalculationTestRunner extends AndroidJUnitRunner {
    protected  DecimalFormat df0;
    protected DecimalFormat df1;
    protected DecimalFormat df2;
    protected DecimalFormat df3;
    protected DecimalFormat df4;
    protected DecimalFormat df5;
    protected  DecimalFormat df8;

    protected void setUp() {
        this.df0 = new DecimalFormat("0");
        this.df0.setRoundingMode(RoundingMode.HALF_UP);
        this.df1 = new DecimalFormat("0.0");
        this.df1.setRoundingMode(RoundingMode.HALF_UP);
        this.df2 = new DecimalFormat("0.00");
        this.df2.setRoundingMode(RoundingMode.HALF_UP);
        this.df3 = new DecimalFormat("0.000");
        this.df3.setRoundingMode(RoundingMode.HALF_UP);
        this.df4 = new DecimalFormat("0.0000");
        this.df4.setRoundingMode(RoundingMode.HALF_UP);
        this.df5 = new DecimalFormat("0.00000");
        this.df5.setRoundingMode(RoundingMode.HALF_UP);
        this.df8 = new DecimalFormat("0.00000000");
        this.df8.setRoundingMode(RoundingMode.HALF_UP);

        // we want to keep a good precision for the tests
        App.setCoordinateDecimalRounding(20);

        // make sure we use a consistent locale for these tests
        LocaleTestUtil.setLocale("en", "US");
    }
}