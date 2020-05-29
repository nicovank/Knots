package com.nvankempen.knots;

import java.util.Arrays;
import java.util.Objects;

public class SingQuandle extends Quandle {

    private final byte[][] C;

    public SingQuandle(byte n) {
        super(n);

        this.C = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                circle(i, j, (byte) -1);
            }
        }
    }

    public SingQuandle(byte n, byte[][] triangle) {
        super(n, triangle);
        this.C = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                circle(i, j, (byte) -1);
            }
        }
    }

    public SingQuandle(byte n, byte[][] triangle, byte[][] C) {
        super(n, triangle);
        this.C = C;
    }

    public SingQuandle(Quandle quandle) {
        this(quandle.n(), quandle.right());
    }

    public SingQuandle(Quandle quandle, byte[][] C) {
        this(quandle.n(), quandle.right(), C);
    }

    public byte[][] circle() {
        return C;
    }

    public byte circle(byte i, byte j) {
        if (i < 0 || j < 0) {
            return -1;
        }

        return C[i][j];
    }

    public byte disc(byte x, byte y) {
        if (x < 0 || y < 0) {
            return -1;
        }

        return circle(y, right(x, y));
    }

    public void circle(byte i, byte j, byte x) {
        C[i][j] = x;
    }

    public SingQuandle copy() {
        byte[][] C = new byte[super.n()][super.n()];

        for (byte i = 0; i < super.n(); ++i) {
            for (byte j = 0; j < super.n(); ++j) {
                C[i][j] = circle(i, j);
            }
        }

        return new SingQuandle(super.copy(), C);
    }

    public boolean isComplete() {
        if (!super.isComplete()) {
            return false;
        }

        for (byte i = 0; i < super.n(); ++i) {
            for (byte j = 0; j < super.n(); ++j) {
                if (circle(i, j) == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        for (byte x = 0; x < super.n(); ++x) {
            for (byte y = 0; y < super.n(); ++y) {

                byte xLy = left(x, y);
                byte xDy = disc(x, y);
                byte xRy = right(x, y);
                byte xCy = circle(x, y);

                // R1(x, y) ▷ R2(x, y) = R2(y, x ▷ y)
                if (right(xCy, xDy) != -1
                        && disc(y, xRy) != -1
                        && right(xCy, xDy) != disc(y, xRy)) {

                    return false;
                }

                for (byte z = 0; z < super.n(); ++z) {

                    byte zRy = right(z ,y);
                    byte xCz = circle(x, z);
                    byte xDz = disc(x, z);

                    // R1(x ◁ y, z) ▷ y = R1(x, z ▷ y)
                    if (right(circle(xLy, z), y) != -1
                            && circle(x, zRy) != -1
                            && right(circle(xLy, z), y) != circle(x, zRy)) {

                        return false;
                    }

                    // R2(x ◁ y, z) = R2(x, z ▷ y) ◁ y
                    if (disc(xLy, z) != -1
                            && left(disc(x, zRy), y) != -1
                            && disc(xLy, z) != left(disc(x, zRy), y)) {

                        return false;
                    }

                    // (y ◁ R1(x, z)) ▷ x = (y ▷ R2(x, z)) ◁ z
                    if (right(left(y, xCz), x) != -1
                            && left(right(y, xDz), z) != -1
                            && right(left(y, xCz), x) != left(right(y, xDz), z)) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SingQuandle
                && Arrays.deepEquals(((SingQuandle) other).right(), this.right())
                && Arrays.deepEquals(((SingQuandle) other).circle(), this.circle());

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Arrays.deepHashCode(C));
    }

    @Override
    public String toString() {
        return Arrays.deepToString(circle());
    }
}
