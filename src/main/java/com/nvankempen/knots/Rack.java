package com.nvankempen.knots;

import java.util.Arrays;

public class Rack {
    public Rack(byte n) {
        this.n = n;
        this.triangle = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                right(i, j, (byte) -1);
            }
        }
    }

    public Rack(byte n, byte[][] triangle) {
        this.n = n;
        this.triangle = triangle;
    }

    public byte n() {
        return n;
    }

    public byte[][] right() {
        return triangle;
    }

    public byte left(byte z, byte y) {
        if (z < 0 || y < 0) {
            return -1;
        }

        for (byte x = 0; x < n; ++x) {
            if (right(x, y) == z) {
                return x;
            }
        }

        return -1;
    }

    public byte right(byte i, byte j) {
        if (i < 0 || j < 0) {
            return -1;
        }

        return triangle[i][j];
    }

    public void right(byte i, byte j, byte x) {
        triangle[i][j] = x;
    }

    public Rack copy() {
        byte[][] copy = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                copy[i][j] = right(i, j);
            }
        }

        return new Rack(n, copy);
    }

    public boolean isComplete() {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (right(i, j) == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid() {
        for (byte a = 0; a < n; ++a) {
            for (byte b = 0; b < n; ++b) {
                boolean found = false;

                for (byte c = 0; c < n; ++c) {
                    final byte cRb = right(c, b);
                    final byte aRc = right(a, c);
                    final byte aRb = right(a, b);
                    final byte bRc = right(b, c);

                    if (cRb == a) {
                        if (found) {
                            return false;
                        } else {
                            found = true;
                        }
                    }

                    if (right(aRb, c) != -1 && right(aRc, bRc) != -1 && right(aRb, c) != right(aRc, bRc)) {
                        return false;
                    }
                }

                if (!found) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rack && Arrays.deepEquals(((Rack) other).triangle, this.triangle);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(triangle);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(triangle);
    }

    private final byte n;
    private final byte[][] triangle;
}
