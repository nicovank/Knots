import com.nvankempen.knots.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int PROGRESS_REPORT_THRESHOLD = 300;

    public static void main(String[] args) {
        final long start = System.nanoTime();
        search();
        final long elapsed = System.nanoTime() - start;

        System.out.println("Generated quandles in " + (elapsed / Math.pow(10, 9)) + " seconds.");
    }

    private static void search() {
        final Ring<Byte> X = group();
        final Ring<Byte> A = group();
        final OrientedSingQuandle<Byte, Byte> initial = new OrientedSingQuandle<>(new SingQuandle<>(X), A);

        /* The Cayley table for the quandle operation is the following
                 ||     0 |     1 |     t | t + 1 ||
                 ||-------------------------------||
               0 ||     0 | t + 1 |     1 |     t ||
               1 ||     t |     1 | t + 1 |     0 ||
               t || t + 1 |     0 |     t |     1 ||
           t + 1 ||     1 |     t |     0 | t + 1 ||
         */
        for (Byte x : X.getAllElements()) {
            for (Byte y : X.getAllElements()) {
                initial.right(x, y, (new byte[][]{
                        {0, 3, 1, 2},
                        {2, 1, 3, 0},
                        {3, 0, 2, 1},
                        {1, 2, 0, 3},
                })[x][y]);

                initial.phi(x, y, X.multiply(
                        X.multiply(X.add(x, X.getAdditiveInverse(y)), X.add(x, X.getAdditiveInverse(y))),
                        y
                ));
            }
        }

        for (byte p = 0; p < 4; ++p) {
            System.out.println("Searching with P(t) = " + ((p < 2) ? p : ((p == 2) ? "t" : "t + 1")) + ".");

            final OrientedSingQuandle<Byte, Byte> quandle = initial.copy();
            for (Byte x : X.getAllElements()) {
                for (Byte y : X.getAllElements()) {
                    quandle.R1(x, y, X.add(X.multiply(p, x), X.multiply(X.add((byte) 1, X.getAdditiveInverse(p)), y)));
                }
            }

//            for (Byte x : X.getAllElements()) {
//                for (Byte y : X.getAllElements()) {
//                    byte r = quandle.R2(x, y);
//                    System.out.printf("R2(%d, %d) = %s%n", x, y, ((r < 2) ? r : ((r == 2) ? "t" : "t + 1")));
//                }
//            }

            if (quandle.isValid()) {
                final Set<OrientedSingQuandle<Byte, Byte>> quandles = ConcurrentHashMap.newKeySet();
                OrientedSingQuandle.generate(quandle, quandles::add);
                quandles.forEach(System.out::println);
            } else {
                System.out.println("The initial quandle is not valid.");
            }
        }
    }

    private static Ring<Byte> group() {
        return new Ring<>() {

            // This Group represents Z_2[t] / (t^2 +t +1)
            // It contains 0, 1, t, t + 1, where t is represented by 2 and t + 1 is represented by 3.

            @Override
            public Byte getUnknownValue() {
                return -1;
            }

            @Override
            public Byte getAdditiveIdentity() {
                return 0;
            }

            @Override
            public Byte getMultiplicativeIdentity() {
                return 1;
            }

            @Override
            public List<Byte> getAllElements() {
                return List.of((byte) 0, (byte) 1, (byte) 2, (byte) 3);
            }

            @Override
            public Byte add(Byte a, Byte b) {
                return (new byte[][]{
                        {0, 1, 2, 3},
                        {1, 0, 3, 2},
                        {2, 3, 0, 1},
                        {3, 2, 1, 0},
                })[a][b];
            }

            @Override
            public Byte multiply(Byte a, Byte b) {
                return (new byte[][]{
                        {0, 0, 0, 0},
                        {0, 1, 2, 3},
                        {0, 2, 3, 1},
                        {0, 3, 1, 2}
                })[a][b];
            }
        };
    }

    private static Ring<Byte> Z(int n) {
        return new Ring<>() {
            @Override
            public Byte getUnknownValue() {
                return -1;
            }

            @Override
            public Byte getAdditiveIdentity() {
                return 0;
            }

            @Override
            public Byte getMultiplicativeIdentity() {
                return 1;
            }

            @Override
            public List<Byte> getAllElements() {
                final List<Byte> elements = new ArrayList<>();
                for (byte i = 0; i < n; ++i) {
                    elements.add(i);
                }
                return elements;
            }

            @Override
            public Byte add(Byte a, Byte b) {
                return mod(a + b, n);
            }

            @Override
            public Byte multiply(Byte a, Byte b) {
                return mod(a * b, n);
            }
        };
    }

    private static byte mod(int a, int n) {
        return (byte) ((a < 0) ? ((a % n) + n) : (a % n));
    }
}
