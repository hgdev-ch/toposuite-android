package ch.hgdev.toposuite.test.utils;

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
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText(null);
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("-");
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("a");
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("1-");
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("-1-");
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("12.123.321");
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));
        this.eT.setText("321.32-");
        assertEquals(MathUtils.IGNORE_DOUBLE, ViewUtils.readDouble(this.eT));

        // test strings that should be converted to double
        this.eT.setText("0");
        assertEquals(0.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("-0");
        assertEquals(-0.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("1");
        assertEquals(1.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("123");
        assertEquals(123.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("-15");
        assertEquals(-15.0, ViewUtils.readDouble(this.eT));
        this.eT.setText("123.221");
        assertEquals(123.221, ViewUtils.readDouble(this.eT));
        this.eT.setText("-12.653");
        assertEquals(-12.653, ViewUtils.readDouble(this.eT));
        this.eT.setText("12.");
        assertEquals(12.0, ViewUtils.readDouble(this.eT));
    }

    public void testReadInt() {
        // test non valid strings
        this.eT.setText("");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText(null);
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("-");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("a");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("1-");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("-1-");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("12.1");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));
        this.eT.setText("-12.1");
        assertEquals(MathUtils.IGNORE_INT, ViewUtils.readInt(this.eT));

        // test strings that should be converted to integer
        this.eT.setText("0");
        assertEquals(0, ViewUtils.readInt(this.eT));
        this.eT.setText("-0");
        assertEquals(-0, ViewUtils.readInt(this.eT));
        this.eT.setText("1");
        assertEquals(1, ViewUtils.readInt(this.eT));
        this.eT.setText("-1");
        assertEquals(-1, ViewUtils.readInt(this.eT));
        this.eT.setText("12341");
        assertEquals(12341, ViewUtils.readInt(this.eT));
        this.eT.setText("-22231144");
        assertEquals(-22231144, ViewUtils.readInt(this.eT));
    }
}
