package com.nvankempen.quandles;

import com.nvankempen.utils.MatrixToTable;

import java.util.*;

import static com.nvankempen.Utils.mod;

public final class QuandlePhi {
    private final Quandle quandle;
    private final byte[][] phi;

    public QuandlePhi(Quandle quandle) {
        this.quandle = quandle;
        this.phi = new byte[quandle.n()][quandle.n()];

        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                this.phi[i][j] = -1;
            }

            this.phi[i][i] = 0;
        }
    }

    public QuandlePhi(Quandle quandle, byte[][] phi) {
        this.quandle = quandle;
        this.phi = phi;
    }

    public byte[][] phi() {
        return phi;
    }

    public byte phi(byte x, byte y) {
        if (x < 0 || y < 0) {
            return -1;
        }

        return phi[x][y];
    }

    public void phi(byte x, byte y, byte z) {
        phi[x][y] = z;
    }

    public QuandlePhi copy() {
        byte[][] pcopy = new byte[quandle.n()][quandle.n()];
        byte[][] qcopy = new byte[quandle.n()][quandle.n()];

        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                pcopy[i][j] = phi(i, j);
            }
        }

        return new QuandlePhi(quandle, pcopy);
    }

    public boolean isValid() {
        for (byte x = 0; x < quandle.n(); ++x) {

            // ϕ(x, x) = 0
            if (phi[x][x] != -1 && phi[x][x] != 0) {
                return false;
            }

            for (byte y = 0; y < quandle.n(); ++y) {
                byte xRy = quandle.right(x, y);
                byte xPy = phi(x, y);

                for (byte z = 0; z < quandle.n(); ++z) {
                    byte xPz = phi(x, z);
                    byte xRz = quandle.right(x, z);
                    byte yRz = quandle.right(y, z);

                    // ϕ(x, y) + ϕ(x ▷ y, z) = ϕ(x, z) + ϕ(x ▷ z, y ▷ z)
                    if (xPy != -1
                            && phi(xRy, z) != -1
                            && xPz != -1
                            && phi(xRz, yRz) != -1
                            && mod(xPy + phi(xRy, z) - xPz - phi(xRz, yRz), quandle.n()) != 0) {

                        return false;
                    }

                    if (quandle instanceof SingQuandle) {

                        SingQuandle q = (SingQuandle) quandle;
                        byte xCz = q.circle(x, z);
                        byte xDz = q.disc(x, z);

                        if (phi(quandle.left(y, xCz), x) != -1
                                && phi(xCz, y) != -1
                                && phi(z, quandle.right(y, xDz)) != -1
                                && phi(y, xDz) != -1
                                && phi(quandle.left(y, xCz), x) - phi(xCz, y) != - phi(z, quandle.right(y, xDz)) + phi(y, xDz)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean isComplete() {
        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                if (phi[i][j] == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Set<QuandlePhi> generate(Quandle quandle) {
        Queue<QuandlePhi> queue = new LinkedList<>();
        Set<QuandlePhi> functions = new HashSet<>();
        queue.offer(new QuandlePhi(quandle));

        while (!queue.isEmpty()) {
            QuandlePhi phi = queue.remove();

            if (phi.isComplete()) {
                if (phi.isValid()) {
                    functions.add(phi);
                }
            } else {
                for (byte x = 0; x < quandle.n(); ++x) {
                    QuandlePhi copy = phi.copy();
                    if (replaceNextUnknown(copy, x) && fill(copy)) {
                        if (copy.isValid()) queue.offer(copy);
                    }
                }
            }
        }

        return functions;
    }

    public static boolean replaceNextUnknown(QuandlePhi phi, byte value) {
        for (byte i = 0; i < phi.quandle.n(); ++i) {
            for (byte j = 0; j < phi.quandle.n(); ++j) {
                if (phi.phi(i, j) == -1) {
                    phi.phi(i, j, value);
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean fill(QuandlePhi phi) {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quandle, Arrays.deepHashCode(phi));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof QuandlePhi
                && ((QuandlePhi) other).quandle.equals(this.quandle)
                && Arrays.deepEquals(((QuandlePhi) other).phi, this.phi);
    }

    @Override
    public String toString() {
        return MatrixToTable.transform(quandle.n(), this.phi, "ϕ");
    }
}
