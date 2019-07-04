package com.nvankempen.quandles;

import java.util.*;
import java.util.stream.Collectors;

import static com.nvankempen.Utils.mod;

/**
 * Given the quandle operation and its inverse and both R1 and R2, this class has utilities to generate all possible Phi
 * and Phi prime functions that will be valid.
 */
public final class Phi {

    private static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE;

    private final SingQuandle quandle;
    private final byte[][] phi;
    private final byte[][] prime;
    private final byte m;

    public Phi(SingQuandle quandle, byte m) {
        this.m = m;
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

    public Phi(SingQuandle quandle, byte[][] phi, byte[][] prime, byte m) {
        this.quandle = quandle;
        this.phi = phi;
        this.prime = prime;
        this.m = m;
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

            return new Phi(quandle, pcopy, qcopy, m);
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
                byte xLy = quandle.left(x, y);

                // ϕ'(x, y) + ϕ(R1(x, y), R2(x, y)) = ϕ(x, y) + ϕ'(y, x ▷ y)
                if (xQy != -1
                        && phi(xCy, xDy) != -1
                        && xPy != -1
                        && prime(y, xRy) != -1
                        && mod(xQy + phi(xCy, xDy) - xPy - prime(y, xRy), m) != 0) {

                    return false;
                }

                for (byte z = 0; z < quandle.n(); ++z) {
                    byte xPz = phi(x, z);
                    byte xRz = quandle.right(x, z);
                    byte yRz = quandle.right(y, z);
                    byte zRy = quandle.right(z, y);
                    byte xCz = quandle.circle(x, z);
                    byte xDz = quandle.disc(x, z);

                    // ϕ(x, y) + ϕ(x ▷ y, z) = ϕ(x, z) + ϕ(x ▷ z, y ▷ z)
                    if (xPy != -1
                            && phi(xRy, z) != -1
                            && xPz != -1
                            && phi(xRz, yRz) != -1
                            && mod(xPy + phi(xRy, z) - xPz - phi(xRz, yRz), m) != 0) {

                        return false;
                    }

                    // - ϕ(x ◁ y, y) + ϕ'(x ◁ y, z) + ϕ(R1(x ◁ y, z), y) = ϕ(z, y) + ϕ'(x, z ▷ y) - ϕ(R2(x, z ▷ y) ◁ y, y)
                    if (phi(xLy, y) != -1
                            && prime(xLy, z) != -1
                            && phi(quandle.circle(xLy, z), y) != -1
                            && phi(z, y) != -1
                            && prime(x, zRy) != -1
                            && phi(quandle.left(quandle.disc(x, zRy), y), y) != -1
                            && - phi(xLy, y) + prime(xLy, z) + phi(quandle.circle(xLy, z), y) != phi(z, y) + prime(x, zRy) - phi(quandle.left(quandle.disc(x, zRy), y), y)) {

                        return false;
                    }

                    // ϕ(y ◁ R1(x, z), x) - ϕ(y ◁ R1(x, z), R1(x, z)) = - ϕ((y ▷ R2(x, z)) ◁ z, z) + ϕ(y, R2(x, z))
                    if (phi(quandle.left(y, xCz), x) != -1
                            && phi(quandle.left(y,xCz), xCz) != -1
                            && phi(quandle.left(quandle.right(y, xDz), z), z) != -1
                            && phi(y, xDz) != -1
                            && phi(quandle.left(y, xCz), x) - phi(quandle.left(y,xCz), xCz) != - phi(quandle.left(quandle.right(y, xDz), z), z) + phi(y, xDz)) {

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

    public static Set<Phi> generate(SingQuandle quandle, byte m) {
        Queue<Phi> queue = new LinkedList<>();
        Set<Phi> functions = new HashSet<>();
        queue.offer(new Phi(quandle, m));

        while (!queue.isEmpty()) {
            Phi phi = queue.remove();
            // System.out.println(phi);

            if (phi.isComplete()) {
                if (phi.isValid()) {
                    functions.add(phi);
                }
            } else {
                for (byte x = 0; x < m; ++x) {
                    Phi copy = phi.copy();
                    if (replaceNextUnknown(copy, x) && fill(copy)) {
                        if (copy.isValid() && queue.size() < MAX_QUEUE_SIZE) queue.offer(copy);
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

    public boolean isTrivial() {
        return isPhiTrivial() && isPrimeTrivial();
    }

    public boolean isPhiTrivial() {
        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                if (phi(i, j) != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isPrimeTrivial() {
        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                if (prime(i, j) != 0) {
                    return false;
                }
            }
        }

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
        return (Arrays.deepToString(phi) + " " + Arrays.deepToString(prime))
                .replaceAll("\\[", "{").replaceAll("]", "}");
    }

    public String latex() {
        return "\\begin{bmatrix}"
                + Arrays.stream(prime).map(row -> {
                    StringBuilder builder = new StringBuilder();
                    for (byte b : row) {
                        builder.append(b).append(" & ");
                    }

                    return builder.substring(0, builder.length() - 3);
                }).collect(Collectors.joining("\\\\"))
                + "\\end{bmatrix}";
    }
}
