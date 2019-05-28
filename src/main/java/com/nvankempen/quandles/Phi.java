package com.nvankempen.quandles;

import java.util.Arrays;

public final class Phi {

    private final byte n;
    private final byte[][] phi;

    public Phi(byte n) {
        this.n = n;

        this.phi = new byte[n][n];
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                set(i, j, (byte) -1);
            }

            set(i, i, (byte) 0);
        }
    }

    // TODO PRIVATE
    public Phi(byte n, byte[][] phi) {
        this.n = n;
        this.phi = phi;
    }

    public byte get(byte x, byte y) {
        return phi[x][y];
    }

    public void set(byte x, byte y, byte z) {
        phi[x][y] = z;
    }

    public boolean isComplete() {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (get(i, j) == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid(byte[][] triangle) {
        for (byte x = 0; x < n; ++x) {
            if (get(x, x) != 0) {
                return false;
            }

            for (byte y = 0; y < n; ++y) {
                for (byte z = 0; z < n; ++z) {
                    if (get(x, y) + get(triangle[x][y], z) != get(x, z) + get(triangle[x][z], triangle[y][z])) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public Phi copy() {
        Phi copy = new Phi(n);
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                copy.set(i, j, this.get(i, j));
            }
        }

        return copy;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(phi);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Phi && Arrays.deepEquals(((Phi) other).phi, this.phi);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(phi);
    }
}
