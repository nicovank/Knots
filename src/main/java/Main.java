import com.nvankempen.Utils;
import com.nvankempen.quandles.*;
import com.nvankempen.quandles.utils.Pair;

import java.util.Set;

import static com.nvankempen.Utils.mod;

public class Main {

    public static void main(String[] args) {

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

//        byte a = -1;
//
//        for (byte b = 2; b < n; ++b) {
//            SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));
//
//            if (quandle.isValid()) {
//                for (byte x = 0; x < n; ++x) {
//                    for (byte y = 0; y < n; ++y) {
//                        quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
//                    }
//                }
//
//                System.out.println("[a, b] = [" + a + ", " + b + "]");
//
//                if (!quandle.isValid()) {
//                    System.out.println("Invalid!!!!");
//                    continue;
//                }
//
//                long count = 0;
//                Set<F> set = F.generate(quandle, m);
//                for (F system : set) {
//                     System.out.println(system);
////
////                    boolean flag = true;
////                    for (byte i = 0; i < n; ++i) {
////                        if (system.f(i) != 0) {
////                            flag = false;
////                            break;
////                        }
////                    }
////
////                    if (flag) {
////                        ++count;
////                        continue;
////                    }
////
////                    flag = true;
////
////                    for (byte i = 0; i < n; ++i) {
////                        if (system.prime(i, i) != 0) {
////                            flag = false; break;
////                        }
////                    }
////
////                    if (flag) ++count;
//                }
//
//                System.out.println("Set: " + set.size() + ", Count: " + count);
//            }
//        }

        byte n = 10;
        byte m = 3;

        SingQuandle quandle = new SingQuandle(n);
        int b = 1;

        for (byte x = 0; x < n; x++) {
            for (byte y = 0; y < n; y++) {
                quandle.right(x, y, (byte) mod(3 * x - 2 * y, n));
                quandle.circle(x, y, (byte) mod(b * x + (6 - b) * y, n));
            }
        }

        for (F f : F.generate(quandle, m)) {
            System.out.println(f);
        }
        System.out.println(F.generate(quandle, m).size());
    }
}
