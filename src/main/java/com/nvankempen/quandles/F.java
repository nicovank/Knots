package com.nvankempen.quandles;

import com.nvankempen.Utils;

import java.util.*;
import java.util.stream.Collectors;

import static com.nvankempen.Utils.mod;

/**
 * Given the quandle operation and its inverse and both R1 and R2, this class has utilities to generate all possible Phi
 * and Phi prime functions that will be valid.
 */
public final class F {

    private static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE;

    private final SingQuandle quandle;
    private final byte[] f;
    private final byte[][] prime;
    private final byte m;

    public F(SingQuandle quandle, byte m) {
        this.m = m;
        this.quandle = quandle;
        this.f = new byte[quandle.n()];
        this.prime = new byte[quandle.n()][quandle.n()];

        for (byte i = 0; i < quandle.n(); ++i) {
            this.f[i] = -1;
            for (byte j = 0; j < quandle.n(); ++j) {
                this.prime[i][j] = -1;
            }
        }
    }

    public F(SingQuandle quandle, byte[] f, byte[][] prime, byte m) {
        this.quandle = quandle;
        this.f = f;
        this.prime = prime;
        this.m = m;
    }

    public byte[] f() {
        return f;
    }

    public byte[][] prime() {
        return prime;
    }

    public byte f(byte x) {
        if (x < 0) {
            return -1;
        }

        return f[x];
    }

    public byte phi(byte x, byte y) {
        return (byte) (f(x) - f(quandle.right(x, y)));
    }

    public byte prime(byte x, byte y) {
        if (x < 0 || y < 0) {
            return -1;
        }

        return prime[x][y];
    }

    public void f(byte x, byte y) {
        f[x] = y;
    }

    public void prime(byte x, byte y, byte z) {
        prime[x][y] = z;
    }

    public F copy() {
        byte[] fcopy = new byte[quandle.n()];
        byte[][] qcopy = new byte[quandle.n()][quandle.n()];

        for (byte i = 0; i < quandle.n(); ++i) {
            fcopy[i] = f(i);
            for (byte j = 0; j < quandle.n(); ++j) {
                qcopy[i][j] = prime(i, j);
            }
        }

        return new F(quandle, fcopy, qcopy, m);
    }

    private byte inverse(byte element) {
//        switch (element) {
//            case 0: return 0;
//            case 1: return 2;
//            case 2: return 1;
//            case 3: return 3;
//            case 4: return 4;
//            case 5: return 5;
//        }
//
//        throw new RuntimeException();

        return (byte) Utils.mod(-element, m);
    }

    public boolean isValid() {
        for (byte x = 0; x < quandle.n(); ++x) {
            for (byte y = 0; y < quandle.n(); ++y) {

                byte xRy = quandle.right(x, y);
                byte xLy = quandle.left(x, y);
                byte xCy = quandle.circle(x, y);
                byte xDy = quandle.disc(x, y);
                byte xPy = prime(x, y);

                if (xPy != -1
                        && f(xCy) != -1
                        && f(quandle.right(xCy, xDy)) != -1
                        && f(x) != -1
                        && f(xRy) != -1
                        && prime(y, xRy) != -1
                        && mod(xPy + f(xCy) - f(quandle.right(xCy, xDy)), quandle.n()) != mod(f(x) - f(xRy) + prime(y, xRy), quandle.n())) {

                    return false;
                }

                for (byte z = 0; z < quandle.n(); ++z) {
                    byte zRy = quandle.right(z, y);
                    byte xCz = quandle.circle(x, z);
                    byte xDz = quandle.disc(x, z);

                    if (f(xLy) != -1
                            && f(x) != -1
                            && prime(xLy, z) != -1
                            && f(quandle.circle(xLy, z)) != -1
                            && f(quandle.right(quandle.circle(xLy, z), y)) != -1
                            && f(z) != -1
                            && f(zRy) != -1
                            && prime(x, zRy) != -1
                            && f(quandle.left(quandle.disc(x, zRy), y)) != -1
                            && f(quandle.disc(x, zRy)) != -1
                            && mod(- f(xLy) + f(x) + prime(xLy, z) + f(quandle.circle(xLy, z)) - f(quandle.right(quandle.circle(xLy, z), y)), quandle.n())
                                != mod(f(z) - f(zRy) + prime(x, zRy) - f(quandle.left(quandle.disc(x, zRy), y)) + f(quandle.disc(x, zRy)), quandle.n())
                    ) {

                        return false;
                    }

                    if (f(quandle.right(quandle.left(y, xCz), x)) != -1
                            && f(quandle.left(quandle.right(y, xDz), z)) != -1
                            && f(quandle.right(quandle.left(y, xCz), x)) != f(quandle.left(quandle.right(y, xDz), z))) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

//    public boolean isValid() {
//        for (byte x = 0; x < quandle.n(); ++x) {
//
//            // ϕ(x, x) = 0
//            if (phi(x, x) != -1 && phi(x, x) != 0) {
//                return false;
//            }
//
//            for (byte y = 0; y < quandle.n(); ++y) {
//                byte xCy = quandle.circle(x, y);
//                byte xDy = quandle.disc(x, y);
//                byte xRy = quandle.right(x, y);
//                byte xPy = phi(x, y);
//                byte xQy = prime(x, y);
//                byte xLy = quandle.left(x, y);
//
//                // ϕ'(x, y) + ϕ(R1(x, y), R2(x, y)) = ϕ(x, y) + ϕ'(y, x ▷ y)
//                if (xQy != -1
//                        && phi(xCy, xDy) != -1
//                        && xPy != -1
//                        && prime(y, xRy) != -1
//                        && mod(xQy + phi(xCy, xDy) + inverse(xPy) + inverse(prime(y, xRy)), quandle.n()) != 0) {
//
//                    return false;
//                }
//
//                for (byte z = 0; z < quandle.n(); ++z) {
//                    byte xPz = phi(x, z);
//                    byte xRz = quandle.right(x, z);
//                    byte yRz = quandle.right(y, z);
//                    byte zRy = quandle.right(z, y);
//                    byte xCz = quandle.circle(x, z);
//                    byte xDz = quandle.disc(x, z);
//
//                    // ϕ(x, y) + ϕ(x ▷ y, z) = ϕ(x, z) + ϕ(x ▷ z, y ▷ z)
//                    if (xPy != -1
//                            && phi(xRy, z) != -1
//                            && xPz != -1
//                            && phi(xRz, yRz) != -1
//                            && mod(xPy + phi(xRy, z) + inverse(xPz) + inverse(phi(xRz, yRz)), quandle.n()) != 0) {
//
//                        return false;
//                    }
//
//                    // - ϕ(x ◁ y, y) + ϕ'(x ◁ y, z) + ϕ(R1(x ◁ y, z), y) = ϕ(z, y) + ϕ'(x, z ▷ y) - ϕ(R2(x, z ▷ y) ◁ y, y)
//                    if (phi(xLy, y) != -1
//                            && prime(xLy, z) != -1
//                            && phi(quandle.circle(xLy, z), y) != -1
//                            && phi(z, y) != -1
//                            && prime(x, zRy) != -1
//                            && phi(quandle.left(quandle.disc(x, zRy), y), y) != -1
//                            && mod(inverse(phi(xLy, y)) + prime(xLy, z) + phi(quandle.circle(xLy, z), y) + inverse(phi(z, y)) + inverse(prime(x, zRy)) + phi(quandle.left(quandle.disc(x, zRy), y), y), quandle.n()) != 0) {
//
//                        return false;
//                    }
//
//                    // ϕ(y ◁ R1(x, z), x) - ϕ(y ◁ R1(x, z), R1(x, z)) = - ϕ((y ▷ R2(x, z)) ◁ z, z) + ϕ(y, R2(x, z))
//                    if (phi(quandle.left(y, xCz), x) != -1
//                            && phi(quandle.left(y,xCz), xCz) != -1
//                            && phi(quandle.left(quandle.right(y, xDz), z), z) != -1
//                            && phi(y, xDz) != -1
//                            && mod(phi(quandle.left(y, xCz), x) + inverse(phi(quandle.left(y,xCz), xCz)) + phi(quandle.left(quandle.right(y, xDz), z), z) + phi(y, xDz), quandle.n()) != 0) {
//
//                        return false;
//                    }
//                }
//            }
//        }
//
//        return true;
//    }

    public boolean isComplete() {
        for (byte i = 0; i < quandle.n(); ++i) {

            if (f[i] == -1) {
                return false;
            }

            for (byte j = 0; j < quandle.n(); ++j) {
                if (prime[i][j] == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Set<F> generate(SingQuandle quandle, byte m) {
        Queue<F> queue = new LinkedList<>();
        Set<F> functions = new HashSet<>();
        F initial = new F(quandle, m);

        // TODO REMOVE THIS EVENTUALLY
        for (byte i = 0; i < quandle.n(); i++) {
            for (byte j = 0; j < quandle.n(); j++) {
                initial.prime(i, j, (byte) 1);
            }
        }

        queue.offer(initial);

        while (!queue.isEmpty()) {
            F system = queue.remove();

            if (system.isComplete()) {
                if (system.isValid()) {
                    functions.add(system);
                }
            } else {
                for (byte x = 0; x < m; ++x) {
                    F copy = system.copy();
                    if (replaceNextUnknown(copy, x) && fill(copy)) {
                        if (copy.isValid() && queue.size() < MAX_QUEUE_SIZE) queue.offer(copy);
                    }
                }
            }
        }

        return functions;
    }

    public static boolean replaceNextUnknown(F system, byte value) {
        for (byte i = 0; i < system.quandle.n(); ++i) {
            if (system.f(i) == -1) {
                system.f(i, value);
                return true;
            }
        }

        for (byte i = 0; i < system.quandle.n(); ++i) {
            for (byte j = 0; j < system.quandle.n(); ++j) {
                if (system.prime(i, j) == -1) {
                    system.prime(i, j, value);
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean fill(F system) {
        return true;
    }

    public boolean isTrivial() {
        return isFTrivial() && isPrimeTrivial();
    }

    public boolean isFTrivial() {
        for (byte i = 0; i < quandle.n(); ++i) {
            if (f(i) != 0) {
                return false;
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
        return Objects.hash(quandle, Arrays.hashCode(f), Arrays.deepHashCode(prime));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof F
                && ((F) other).quandle.equals(this.quandle)
                && Arrays.equals(((F) other).f, this.f)
                && Arrays.deepEquals(((F) other).prime, this.prime);
    }

    @Override
    public String toString() {
//        return Arrays.toString(f) + " " + Arrays.deepToString(prime);
        StringBuilder builder = new StringBuilder();

        for (byte i = 0; i < quandle.n(); i++) {
            if (f(i) == 0) builder.append(i).append(", ");
        }

        builder.append("from X to 0 in Z_").append(m).append("\n");

        for (byte i = 0; i < quandle.n(); i++) {
            if (f(i) == 1) builder.append(i).append(", ");
        }

        builder.append("from X to 1 in Z_").append(m).append("\n");

        for (byte i = 0; i < quandle.n(); i++) {
            if (f(i) == 2) builder.append(i).append(", ");
        }

        builder.append("from X to 1 in Z_").append(m).append("\n");

        return new String(builder);
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
