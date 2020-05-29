package com.nvankempen.knots.utils;

import java.util.Objects;

public class Doublet<A, B> {
    public static <A, B> Doublet<A, B> create(A a, B b) {
        return new Doublet<>(a, b);
    }

    public Doublet(A a, B b) {
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
        return other instanceof Doublet && ((Doublet) other).a.equals(this.a) && ((Doublet) other).b.equals(this.b);
    }

    @Override
    public String toString() {
        return "[" + a + ", " + b + "]";
    }

    private final A a;
    private final B b;
}
