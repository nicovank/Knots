package com.nvankempen.quandles;

import java.util.Arrays;
import java.util.Objects;

public final class Quandle {

    private static final class Phi {

        private final byte n;
        private final byte[][] phi;

        Phi(byte n) {
            this.n = n;

            this.phi = new byte[n][n];
            for (byte i = 0; i < n; ++i) {
                for (byte j = 0; j < n; ++j) {
                    set(i, j, (byte) -1);
                }

                set(i, i, (byte) 0);
            }
        }

        Phi(byte n, byte[][] phi) {
            this.n = n;
            this.phi = phi;
        }

        byte get(byte x, byte y) {
            return phi[x][y];
        }

        void set(byte x, byte y, byte z) {
            phi[x][y] = z;
        }

        boolean isComplete() {
            for (byte i = 0; i < n; ++i) {
                for (byte j = 0; j < n; ++j) {
                    if (get(i, j) == -1) {
                        return false;
                    }
                }
            }

            return true;
        }

        boolean isValid(byte[][] triangle) {
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

        Phi copy() {
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

    private final byte n;
    private final byte[][] triangle;
    private final Phi phi;

    public Quandle(byte n) {
        this.n = n;
        this.phi = new Phi(n);
        this.triangle = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                set(i, j, (byte) -1);
            }

            set(i, i, i);
        }
    }

    public Quandle(byte n, byte[][] triangle, byte[][] phi) {
        this.n = n;
        this.triangle = triangle;
        this.phi = new Phi(n, phi);
    }

    public void set(byte i, byte j, byte x) {
        triangle[i][j] = x;
    }

    public byte get(byte i, byte j) {
        return triangle[i][j];
    }

    public byte getPhi(byte i, byte j) {
        return phi.get(i, j);
    }

    public void setPhi(byte i, byte j, byte x) {
        phi.set(i, j, x);
    }

    public Phi phi() {
        return phi;
    }

    public Quandle copy() {
        byte[][] copy = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                copy[i][j] = this.get(i, j);
            }
        }

        return new Quandle(n, copy, this.phi.copy().phi);
    }

    public boolean isValid() {
        return this.isTriangleValid() && this.phi.isValid(triangle);
    }

    public boolean isTriangleValid() {
        for (byte x = 0; x < n; ++x) {

            if (get(x, x) != x) {
                return false;
            }

            for (byte y = 0; y < n; ++y) {

                if (get(get(x, y), y) != x) {
                    return false;
                }

                for (byte z = 0; z < n; ++z) {

                    if (get(get(x, y), z) != get(get(x, z), get(y, z))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isComplete() {
        return this.phi.isComplete() && this.isTriangleComplete();
    }

    public boolean isTriangleComplete() {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (get(i, j) == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Quandle
                && Arrays.deepEquals(((Quandle) other).triangle, this.triangle)
                && ((Quandle) other).phi.equals(this.phi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(triangle), phi.hashCode());
    }

    @Override
    public String toString() {
        return String.format("▷: %s, ϕ: %s", Arrays.deepToString(triangle), phi);
    }
}
