package com.nvankempen.quandles;

import java.util.*;

import static com.nvankempen.Utils.mod;

/**
 * Given the quandle operation and its inverse and both R1 and R2, this class has utilities to generate all possible Phi
 * and Phi prime functions that will be valid.
 */
public final class Phi {
    private final SingQuandle quandle;
    private final byte[][] phi;
    private final byte[][] prime;

    public Phi(SingQuandle quandle) {
        this.quandle = quandle;
        this.phi = new byte[quandle.n()][quandle.n()];
        this.prime = new byte[quandle.n()][quandle.n()];

        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                this.phi[i][j] = -1;
                this.prime[i][j] = -1;
            }

            this.phi[i][i] = 0;
        }
    }

    public Phi(SingQuandle quandle, byte[][] phi, byte[][] prime) {
        this.quandle = quandle;
        this.phi = phi;
        this.prime = prime;
    }

    public byte[][] phi() {
        return phi;
    }

    public byte[][] prime() {
        return prime;
    }

    public byte phi(byte x, byte y) {
        if (x < 0 || y < 0) {
            return -1;
        }

        return phi[x][y];
    }

    public byte prime(byte x, byte y) {
        if (x < 0 || y < 0) {
            return -1;
        }

        return prime[x][y];
    }

    public void phi(byte x, byte y, byte z) {
        phi[x][y] = z;
    }

    public void prime(byte x, byte y, byte z) {
        prime[x][y] = z;
    }

    public Phi copy() {
            byte[][] pcopy = new byte[quandle.n()][quandle.n()];
            byte[][] qcopy = new byte[quandle.n()][quandle.n()];

            for (byte i = 0; i < quandle.n(); ++i) {
                for (byte j = 0; j < quandle.n(); ++j) {
                    pcopy[i][j] = phi(i, j);
                    qcopy[i][j] = prime(i, j);
                }
            }

            return new Phi(quandle, pcopy, qcopy);
    }

    public boolean isValid() {
        for (byte x = 0; x < quandle.n(); ++x) {

            // ϕ(x, x) = 0
            if (phi[x][x] != -1 && phi[x][x] != 0) {
                return false;
            }

            for (byte y = 0; y < quandle.n(); ++y) {
                byte xCy = quandle.circle(x, y);
                byte xDy = quandle.disc(x, y);
                byte xRy = quandle.right(x, y);
                byte xPy = phi(x, y);
                byte xQy = prime(x, y);
                byte yPx = prime(y, x);
                byte xLy = quandle.left(x, y);

                // ϕ'(x, y) + ϕ(R1(x, y), R2(x, y)) = ϕ(x, y) + ϕ'(y, x ▷ y)
                if (xQy != -1
                        && phi(xCy, xDy) != -1
                        && xPy != -1
                        && prime(y, xRy) != -1
                        && mod(xQy + phi(xCy, xDy) - xPy - prime(y, xRy), quandle.n()) != 0) {

                    return false;
                }

                for (byte z = 0; z < quandle.n(); ++z) {
                    byte xPz = phi(x, z);
                    byte xRz = quandle.right(x, z);
                    byte yRz = quandle.right(y, z);
                    byte zPy = phi(z, y);
                    byte zRy = quandle.right(z, y);
                    byte xCz = quandle.circle(x, z);
                    byte xDz = quandle.disc(x, z);

                    // ϕ(x, y) + ϕ(x ▷ y, z) = ϕ(x, z) + ϕ(x ▷ z, y ▷ z)
                    if (xPy != -1
                            && phi(xRy, z) != -1
                            && xPz != -1
                            && phi(xRz, yRz) != -1
                            && mod(xPy + phi(xRy, z) - xPz - phi(xRz, yRz), quandle.n()) != 0) {

                        return false;
                    }

                    // ϕ(y, R2(x, z ▷ y)) + ϕ(R1(x ◁ y, z), y) + ϕ'(x ◁ y, z) = ϕ(y, x) + ϕ(z, y) + ϕ'(x, z ▷ y)
                    if (yPx != -1
                            && prime(xLy, z) != -1
                            && phi(quandle.circle(xLy, z), y) != -1
                            && zPy != -1
                            && prime(x, zRy) != -1
                            && phi(y, quandle.disc(x, zRy)) != -1
                            && mod(phi(y, quandle.disc(x, zRy)) + phi(quandle.circle(xLy, z), y) + prime(xLy, z) - yPx - zPy - prime(x, zRy), quandle.n()) != 0) {

                        return false;
                    }

                    // ϕ(y ◁ R1(x, z), x) + ϕ(y, R2(x, z)) = ϕ(z, y ▷ R2(x, z)) + ϕ(R1(x, z), y)
                    if (phi(quandle.left(y, xCz), x) != -1
                            && phi(xCz, y) != -1
                            && phi(z, quandle.right(y, xDz)) != -1
                            && phi(y, xDz) != -1
                            && mod(phi(quandle.left(y, xCz), x) + phi(y, xDz) - phi(z, quandle.right(y, xDz)) - phi(xCz, y), quandle.n()) != 0) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isComplete() {
        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                if (phi[i][j] == -1 || prime[i][j] == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Set<Phi> generate(SingQuandle quandle) {
        Queue<Phi> queue = new LinkedList<>();
        Set<Phi> functions = new HashSet<>();
        queue.offer(new Phi(quandle));

        while (!queue.isEmpty()) {
            Phi phi = queue.remove();

            if (phi.isComplete()) {
                if (phi.isValid()) {
                    functions.add(phi);
                }
            } else {
                for (byte x = 0; x < quandle.n(); ++x) {
                    Phi copy = phi.copy();
                    if (replaceNextUnknown(copy, x) && fill(copy)) {
                        if (copy.isValid()) queue.offer(copy);
                    }
                }
            }
        }

        return functions;
    }

    public static boolean replaceNextUnknown(Phi phi, byte value) {
        for (byte i = 0; i < phi.quandle.n(); ++i) {
            for (byte j = 0; j < phi.quandle.n(); ++j) {
                if (phi.phi(i, j) == -1) {
                    phi.phi(i, j, value);
                    return true;
                }
            }
        }

        for (byte i = 0; i < phi.quandle.n(); ++i) {
            for (byte j = 0; j < phi.quandle.n(); ++j) {
                if (phi.prime(i, j) == -1) {
                    phi.prime(i, j, value);
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean fill(Phi phi) {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quandle, Arrays.deepHashCode(phi), Arrays.deepHashCode(prime));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Phi
                && ((Phi) other).quandle.equals(this.quandle)
                && Arrays.deepEquals(((Phi) other).phi, this.phi)
                && Arrays.deepEquals(((Phi) other).prime, this.prime);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(phi) + " " + Arrays.deepToString(prime);
    }
}
