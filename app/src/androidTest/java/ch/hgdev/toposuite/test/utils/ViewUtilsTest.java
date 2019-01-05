package ch.hgdev.toposuite.test.utils;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.hgdev.toposuite.test.testutils.UtilsTestRunner;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ViewUtilsTest extends UtilsTestRunner {

    private EditText eT;

    @Before
    public void setUp() {
        super.setUp();
        this.eT = new EditText(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void readDouble() {
        // test non valid strings
        this.eT.setText("");
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText(null);
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("-");
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("a");
        Assert.assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));

        // test strings that should be converted to double
        this.eT.setText("0");
        Assert.assertEquals(0.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("-0");
        Assert.assertEquals(-0.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("1-");
        Assert.assertEquals(1.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("1");
        Assert.assertEquals(1.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("-1-");
        Assert.assertEquals(-1.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("123");
        Assert.assertEquals(123.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("-15");
        Assert.assertEquals(-15.0, ViewUtils.readDouble(this.eT));

        this.eT.setText("12.123.321");
        Assert.assertEquals(12.123, ViewUtils.readDouble(this.eT));
        this.eT.setText("123.221");
        Assert.assertEquals(123.221, ViewUtils.readDouble(this.eT));
        this.eT.setText("-12.653");
        Assert.assertEquals(-12.653, ViewUtils.readDouble(this.eT));
        this.eT.setText("12.");
        Assert.assertEquals(12.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("321.32-");
        Assert.assertEquals(321.32, ViewUtils.readDouble(this.eT));

        // with some locale, like French, "," is used as the decimal separator
        this.eT.setText("3,14");
        Assert.assertEquals(3.14, ViewUtils.readDouble(this.eT));
        this.eT.setText("3,14,156");
        Assert.assertEquals(3.14, ViewUtils.readDouble(this.eT));

        // test case for scientific notation (yup, someEditText.getText().toString() has the "good"
        // idea of converting "0.0009" to "9.0E-4"... How kind of it...
        this.eT.setText("9.0E-4");
        Assert.assertEquals(0.0009, ViewUtils.readDouble(this.eT));
        this.eT.setText("9.0e-4");
        Assert.assertEquals(0.0009, ViewUtils.readDouble(this.eT));
        this.eT.setText("3.3E3");
        Assert.assertEquals(3300.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("3.3e3");
        Assert.assertEquals(3300.0, ViewUtils.readDouble(this.eT));

        // test case for numbers with a leading "+" sign
        this.eT.setText("+4.0");
        Assert.assertEquals(4.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("+4");
        Assert.assertEquals(4.0, ViewUtils.readDouble(this.eT));

    }

    @Test
    public void readInt() {
        // test non valid strings
        this.eT.setText("");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText(null);
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("-");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("a");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("1-");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("-1-");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("12.1");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("-12.1");
        Assert.assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));

        // test strings that should be converted to integer
        this.eT.setText("0");
        Assert.assertEquals(0, ViewUtils.readInt(this.eT));
        this.eT.setText("-0");
        Assert.assertEquals(-0, ViewUtils.readInt(this.eT));
        this.eT.setText("1");
        Assert.assertEquals(1, ViewUtils.readInt(this.eT));
        this.eT.setText("-1");
        Assert.assertEquals(-1, ViewUtils.readInt(this.eT));
        this.eT.setText("12341");
        Assert.assertEquals(12341, ViewUtils.readInt(this.eT));
        this.eT.setText("-22231144");
        Assert.assertEquals(-22231144, ViewUtils.readInt(this.eT));

        // test case for number with a leading "+" sign
        this.eT.setText("+4");
        Assert.assertEquals(4, ViewUtils.readInt(this.eT));
    }
}
