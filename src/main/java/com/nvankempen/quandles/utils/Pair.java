package com.nvankempen.quandles.utils;

import java.util.Objects;

public class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Pair && ((Pair) other).a.equals(this.a) && ((Pair) other).b.equals(this.b);
    }
}
