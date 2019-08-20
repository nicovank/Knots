import com.nvankempen.Utils;
import com.nvankempen.quandles.*;

import java.util.Set;

import static com.nvankempen.Utils.mod;

public class Main {

    public static void main(String[] args) {

        byte n = 5;
        byte m = 5;

//        {
////            Quandle quandle = new Quandle(n, new byte[][] {
////                    {0, 2, 1, 4, 3, 0},
////                    {2, 1, 0, 5, 1, 3},
////                    {1, 0, 2, 2, 5, 4},
////                    {4, 5, 3, 3, 0, 1},
////                    {3, 4, 5, 0, 4, 2},
////                    {5, 3, 4, 1, 2, 5}
////            });
//
//            Quandle quandle = new Quandle(n, new byte[][] {
//                    {0, 2, 1}, {2, 1, 0}, {1, 0, 2}
//            });
//
//            Set<QuandlePhi> list = QuandlePhi.generate(quandle);
//            list.forEach(System.out::println);
//            System.out.println(list.size());
//        }

//        {
//            // Single Invariant computation
//            byte a = 2;
//            byte b = 3;
//            byte[][] phi = new byte[][] {
//                    {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}
//            };
//
//            byte[][] prime = new byte[][] {
//                    {4, 3, 3, 3, 3}, {3, 4, 3, 3, 3}, {3, 3, 4, 3, 3}, {3, 3, 3, 4, 3}, {3, 3, 3, 3, 4}
//            };
//
//            SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));
//            for (byte x = 0; x < n; ++x) {
//                for (byte y = 0; y < n; ++y) {
//                    quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
//                }
//            }
//
//            System.out.println(Invariant.compute(n, m, quandle, new Phi(quandle, phi, prime, m)).toString("u"));
//        }

        for (byte a = 2; a < n; ++a) {
            for (byte b = 2; b < n; ++b) {
                SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));

                if (quandle.isValid()) {
                    for (byte x = 0; x < n; ++x) {
                        for (byte y = 0; y < n; ++y) {
                            quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
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
                        if (!phi.isTrivial()) {
                            if (phi.toString().equals("{{0, 0, 0, 3, 3, 3}, {3, 0, 3, 0, 3, 0}, {3, 0, 0, 0, 3, 3}, {3, 0, 3, 0, 3, 0}, {3, 3, 0, 0, 0, 3}, {3, 0, 3, 0, 3, 0}} {{3, 5, 4, 0, 4, 5}, {2, 0, 2, 0, 0, 0}, {1, 5, 3, 5, 1, 0}, {0, 0, 2, 0, 2, 0}, {4, 0, 4, 5, 3, 5}, {5, 0, 0, 0, 5, 0}}")) {
                                System.out.println("here");
                            }
                            System.out.println(phi);
                        }
                    }
                }
            }
        }
    }
}
