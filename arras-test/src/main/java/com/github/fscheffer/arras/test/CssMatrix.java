package com.github.fscheffer.arras.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;

public class CssMatrix {

    private final List<Float> matrix;

    private final float       epsilon;

    public CssMatrix(List<Float> matrix) {
        this(matrix, 0.00001f);
    }

    public CssMatrix(Float... matrix) {
        this(Arrays.<Float> asList(matrix), 0.00001f);
    }

    public CssMatrix(List<Float> matrix, float epsilon) {
        this.matrix = matrix;
        this.epsilon = epsilon;
    }

    @Override
    public String toString() {
        return "matrix(" + InternalUtils.join(this.matrix, ", ") + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CssMatrix)) {
            return false;
        }
        CssMatrix other = (CssMatrix) obj;
        if (this.matrix == null) {
            if (other.matrix != null) {
                return false;
            }
        }
        else if (!this.equals(other.matrix)) {
            return false;
        }
        return true;
    }

    private boolean equals(List<Float> that) {

        int size = this.matrix.size();

        if (size != that.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {

            float a = this.matrix.get(i);
            float b = that.get(i);

            if (Math.abs(a - b) > this.epsilon) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.matrix == null ? 0 : this.matrix.hashCode());
        return result;
    }

    public static CssMatrix fromString(String value) {

        if (!value.startsWith("matrix(") || !value.endsWith(")")) {
            throw new IllegalArgumentException(value);
        }

        String substring = value.substring(7, value.length() - 1);

        if (InternalUtils.isBlank(substring)) {
            return new CssMatrix(Collections.<Float> emptyList());
        }

        List<Float> matrix = CollectionFactory.newList();

        for (String s : substring.split(",")) {

            Float f = Float.valueOf(s);
            matrix.add(f);
        }

        return new CssMatrix(matrix);
    }
}
