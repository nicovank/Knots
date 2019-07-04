import com.nvankempen.Utils;
import com.nvankempen.quandles.*;

import java.util.Set;

import static com.nvankempen.Utils.mod;

public class Main {
    //
//        System.out.printf("Found %d quandles of order %d. %n", quandles.size(), n);
//        System.out.printf("Executed in %f seconds. %n", ((double) (end - start)) / 1000000000D);
//
    public static void main(String[] args) {

//        long start = System.nanoTime();
//        Set<Quandle> quandles = Quandles.generate(n);
//        long end = System.nanoTime();
//
//        System.out.printf("Found %d quandles of order %d. %n", quandles.size(), n);
//        System.out.printf("Executed in %f seconds. %n", ((double) (end - start)) / 1000000000D);
//
//        for (Quandle quandle : quandles) {
//            System.out.println(quandle);
//        }
        byte n = 6;
        byte m = 6;

//        {
//            byte t = -1;
//            if (mod(t * t, n) != 1) {
//                System.out.println("Rule 1.");
//            } else {
//                for (byte B = 0; B < n; ++B) {
//                    if (mod(B * (1 + t), n) != 0) {
//                        System.out.println("Rule 2.");
//                    } else if (mod(t - (1 - B) * (1 - B), n) != 0) {
//                        System.out.println("Rule 3.");
//                    }
//                }
//            }
//        }

//        {
//            // Single Invariant computation
//            byte a = -1;
//            byte b = 4;
//            byte[][] phi = new byte[][] {
//                    {0, 5, 3, 0, 5, 3}, {2, 0, 3, 2, 0, 3}, {4, 1, 0, 4, 1, 0}, {0, 5, 3, 0, 5, 3}, {2, 0, 3, 2, 0, 3}, {4, 1, 0, 4, 1, 0}
//            };
//
//            byte[][] prime = new byte[][] {
//                    {3, 1, 1, 3, 1, 1}, {1, 3, 1, 1, 3, 1}, {1, 1, 3, 1, 1, 3}, {3, 1, 1, 3, 1, 1}, {1, 3, 1, 1, 3, 1}, {1, 1, 3, 1, 1, 3}
//            };
//
//            SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));
//            for (byte x = 0; x < n; ++x) {
//                for (byte y = 0; y < n; ++y) {
//                    quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
//                    quandle.disc(x, y, (byte) mod(a * (1 - b) * x + (1 - a * (1 - b)) * y, n));
//                }
//            }
//
//            System.out.println(Invariant.compute(n, m, quandle, new Phi(quandle, phi, prime, m)).toString("u"));
//        }

        for (byte a = -1; a < n; ++a) {
            for (byte b = 2; b < n; ++b) {
                SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));

                if (quandle.isValid()) {
                    for (byte x = 0; x < n; ++x) {
                        for (byte y = 0; y < n; ++y) {
                            quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
                            quandle.disc(x, y, (byte) mod(a * (1 - b) * x + (1 - a * (1 - b)) * y, n));

                            if (quandle.left(x, y) != Utils.mod((n - a) * x + (1 - (n - a)) * y, n)) {
                                System.out.println("FAIIIIIIIIIIIIL"); return;
                            }
                        }
                    }

                    System.out.println("[a, b] = [" + a + ", " + b + "]");

                    if (!quandle.isValid()) {
                        System.out.println("Invalid!!!!");
                        continue;
                    }

//                            if (t == -1) {
//                                System.out.println("[t, B] = [" + t + ", " + B + "]");
//                                for (Phi phi : Phi.generate(quandle, m)) {
//                                    if (!phi.isTrivial()) {
//                                        System.out.println(phi);
//                                    }
//                                }
//                            }

                    for (Phi phi : Phi.generate(quandle, m)) {
                        if (!phi.isPhiTrivial()) {
                            System.out.println(phi);
                        }
                    }
                }
            }
        }
    }
}
