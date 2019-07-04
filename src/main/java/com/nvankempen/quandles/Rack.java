package com.nvankempen.quandles;

import java.util.Arrays;
import java.util.Objects;

public class Rack {
    private final byte n;
    private final byte[][] lefttriangle;
    private final byte[][] righttriangle;

    public Rack(byte n) {
        this.n = n;
        this.lefttriangle = new byte[n][n];
        this.righttriangle = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                left(i, j, (byte) -1);
                right(i, j, (byte) -1);
            }
        }
    }

    public Rack(byte n, byte[][] lefttriangle, byte[][] righttriangle) {
        this.n = n;
        this.lefttriangle = lefttriangle;
        this.righttriangle = righttriangle;
    }

    public byte n() {
        return n;
    }

    public byte[][] left() {
        return lefttriangle;
    }

    public byte[][] right() {
        return righttriangle;
    }

    public byte left(byte i, byte j) {
        if (i < 0 || j < 0) {
            return -1;
        }

        return lefttriangle[i][j];
    }

    public byte right(byte i, byte j) {
        if (i < 0 || j < 0) {
            return -1;
        }

        return righttriangle[i][j];
    }

    public void left(byte i, byte j, byte x) {
        lefttriangle[i][j] = x;
    }

    public void right(byte i, byte j, byte x) {
        righttriangle[i][j] = x;
    }

    public Rack copy() {
        byte[][] lcopy = new byte[n][n];
        byte[][] rcopy = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                lcopy[i][j] = left(i, j);
                rcopy[i][j] = right(i, j);
            }
        }

        return new Rack(n, lcopy, rcopy);
    }

    public boolean isComplete() {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (left(i, j) == -1 || right(i, j) == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid() {
        for (byte a = 0; a < n; ++a) {
            for (byte b = 0; b < n; ++b) {

                if (right(right(a, b), b) != -1 && right(right(a, b), b) != a) {
                    return false;
                }

                for (byte c = 0; c < n; ++c) {
                    if (right(right(a, b), c) != -1
                            && right(right(a, c), right(b, c)) != -1
                            && right(right(a, b), c) != right(right(a, c), right(b, c))) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rack
                && Arrays.deepEquals(((Rack) other).lefttriangle, this.lefttriangle)
                && Arrays.deepEquals(((Rack) other).righttriangle, this.righttriangle);

    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(lefttriangle), Arrays.deepHashCode(lefttriangle));
    }

    @Override
    public String toString() {
        return Arrays.deepToString(righttriangle);
    }
}
