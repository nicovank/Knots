import com.nvankempen.Utils;
import com.nvankempen.quandles.*;

import java.util.Set;

import static com.nvankempen.Utils.mod;

public class Main {

    public static void main(String[] args) {

        byte n = 5;
        byte m = 5;

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

        {
            // Single Invariant computation
            byte a = 2;
            byte b = 3;
            byte[][] phi = new byte[][] {
                    {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}
            };

            byte[][] prime = new byte[][] {
                    {4, 3, 3, 3, 3}, {3, 4, 3, 3, 3}, {3, 3, 4, 3, 3}, {3, 3, 3, 4, 3}, {3, 3, 3, 3, 4}
            };

            SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));
            for (byte x = 0; x < n; ++x) {
                for (byte y = 0; y < n; ++y) {
                    quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
                }
            }

            System.out.println(Invariant.compute(n, m, quandle, new Phi(quandle, phi, prime, m)).toString("u"));
        }

//        for (byte a = 2; a < n; ++a) {
//            for (byte b = 2; b < n; ++b) {
//                SingQuandle quandle = new SingQuandle(Quandle.alexander(n, a));
//
//                if (quandle.isValid()) {
//                    for (byte x = 0; x < n; ++x) {
//                        for (byte y = 0; y < n; ++y) {
//                            quandle.circle(x, y, (byte) mod(b * x + (1 - b) * y, n));
//                        }
//                    }
//
//                    System.out.println("[a, b] = [" + a + ", " + b + "]");
//
//                    if (!quandle.isValid()) {
//                        System.out.println("Invalid!!!!");
//                        continue;
//                    }
//
////                            if (t == -1) {
////                                System.out.println("[t, B] = [" + t + ", " + B + "]");
////                                for (Phi phi : Phi.generate(quandle, m)) {
////                                    if (!phi.isTrivial()) {
////                                        System.out.println(phi);
////                                    }
////                                }
////                            }
//
//                    for (Phi phi : Phi.generate(quandle, m)) {
//                        if (!phi.isTrivial()) {
//                            System.out.println(phi + (phi.isValid() ? "" : "*"));
//                        }
//                    }
//                }
//            }
//        }
    }
}
