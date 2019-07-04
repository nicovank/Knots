package com.nvankempen.quandles;

import java.util.Arrays;
import java.util.Objects;

public class SingQuandle extends Quandle {

    private final byte[][] C;
    private final byte[][] D;

    public SingQuandle(byte n) {
        super(n);

        this.C = new byte[n][n];
        this.D = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                circle(i, j, (byte) -1);
                disc(i, j, (byte) -1);
            }
        }
    }

    public SingQuandle(byte n, byte[][] lefttriangle, byte[][] righttriangle) {
        super(n, lefttriangle, righttriangle);
        this.C = new byte[n][n];
        this.D = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                circle(i, j, (byte) -1);
                disc(i, j, (byte) -1);
            }
        }
    }

    public SingQuandle(byte n, byte[][] lefttriangle, byte[][] righttriangle, byte[][] C, byte[][] D) {
        super(n, lefttriangle, righttriangle);
        this.C = C;
        this.D = D;
    }

    public SingQuandle(Quandle quandle) {
        this(quandle.n(), quandle.left(), quandle.right());
    }

    public SingQuandle(Quandle quandle, byte[][] C, byte[][] D) {
        this(quandle.n(), quandle.left(), quandle.right(), C, D);
    }

    public byte[][] circle() {
        return C;
    }

    public byte[][] disc() {
        return D;
    }

    public byte circle(byte i, byte j) {
        if (i < 0 || j < 0) {
            return -1;
        }

        return C[i][j];
    }

    public byte disc(byte i, byte j) {
        if (i < 0 || j < 0) {
            return -1;
        }

        return D[i][j];
    }

    public void circle(byte i, byte j, byte x) {
        C[i][j] = x;
    }

    public void disc(byte i, byte j, byte x) {
        D[i][j] = x;
    }

    public SingQuandle copy() {
        byte[][] C = new byte[super.n()][super.n()];
        byte[][] D = new byte[super.n()][super.n()];

        for (byte i = 0; i < super.n(); ++i) {
            for (byte j = 0; j < super.n(); ++j) {
                C[i][j] = circle(i, j);
                D[i][j] = disc(i, j);
            }
        }

        return new SingQuandle(super.copy(), C, D);
    }

    public boolean isComplete() {
        if (!super.isComplete()) {
            return false;
        }

        for (byte i = 0; i < super.n(); ++i) {
            for (byte j = 0; j < super.n(); ++j) {
                if (circle(i, j) == -1 || disc(i, j) == -1) {
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

                // R2(x, y) = R1(y, x ▷ y)
                if (xDy != -1
                        && circle(y, xRy) != -1
                        && xDy != circle(y, xRy)) {

                    return false;
                }

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
                && Arrays.deepEquals(((SingQuandle) other).left(), this.left())
                && Arrays.deepEquals(((SingQuandle) other).right(), this.right())
                && Arrays.deepEquals(((SingQuandle) other).circle(), this.circle())
                && Arrays.deepEquals(((SingQuandle) other).disc(), this.disc());

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Arrays.deepHashCode(C), Arrays.deepHashCode(D));
    }

    @Override
    public String toString() {
        return String.format("R1: %s R2: %s", Arrays.deepToString(circle()), Arrays.deepToString(disc()));
    }
}
