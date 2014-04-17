package ch.hgdev.toposuite.test.utils;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import android.widget.EditText;
import ch.hgdev.toposuite.utils.MathUtils;
import ch.hgdev.toposuite.utils.ViewUtils;

public class TestViewUtils extends AndroidTestCase {

    private EditText eT;

    @Override
    protected void setUp() {
        this.eT = new EditText(this.getContext());
    }

    public void testReadDouble() {
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
    }

    public void testReadInt() {
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
    }
}
