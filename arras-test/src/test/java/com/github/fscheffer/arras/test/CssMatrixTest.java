package com.github.fscheffer.arras.test;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CssMatrixTest {

    CssMatrix a = new CssMatrix(0f, 1f, 1f, 0f);

    CssMatrix b = new CssMatrix(0.000f, 1.00000f, 1f, 0.0f);

    CssMatrix c = new CssMatrix(1f, 0.5f, 1f, 0.0f);

    CssMatrix d = new CssMatrix(0.123456f, 9876.54321f, 12345678.9f, -1.0f);

    @Test
    public void testEquality() {

        Assert.assertEquals(this.a, this.b);
        Assert.assertNotEquals(this.a, this.c);
        Assert.assertNotEquals(this.b, this.c);
    }

    @Test
    public void testToString() {

        Assert.assertEquals(this.a.toString(), "matrix(0.0, 1.0, 1.0, 0.0)");
        Assert.assertEquals(this.b.toString(), "matrix(0.0, 1.0, 1.0, 0.0)");
        Assert.assertEquals(this.c.toString(), "matrix(1.0, 0.5, 1.0, 0.0)");
        Assert.assertEquals(this.d.toString(), "matrix(0.123456, 9876.543, 1.2345679E7, -1.0)");
    }

    @Test
    public void testFromString() {

        Assert.assertEquals(CssMatrix.fromString("matrix()"), new CssMatrix());
        Assert.assertEquals(CssMatrix.fromString("matrix(0.05)"), new CssMatrix(0.05f));
        Assert.assertEquals(CssMatrix.fromString("matrix(0, 1, -2, 3)"), new CssMatrix(0f, 1f, -2f, 3f));
    }
}
