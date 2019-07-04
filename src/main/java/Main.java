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
        byte n = 10;
        byte m = 2;
        byte t = -1;
        byte B = 8;

        SingQuandle quandle = new SingQuandle(Quandle.alexander(n, t));

        for (byte x = 0; x < n; ++x) {
            for (byte y = 0; y < n; ++y) {
                quandle.circle(x, y, (byte) mod((1 - t - B) * x + (t + B) * y, n));
                quandle.disc(x, y, (byte) mod((1 - B) * x + B * y, n));
            }
        }

        if (!quandle.isValid()) {
            System.out.println("Nope.");
        }

        Phi.generate(quandle, m).forEach(phi -> {
            System.out.println(phi);
            System.out.println(Invariant.compute(n, m, B, phi).toString("u"));
        });

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

//        for (byte t = (byte) (1 - n); t < n; ++t) {
//            if (mod(t * t, n) == 1) {
//                for (byte B = 0; B < n; ++B) {
//                    if (mod(B * (1 + t), n) == 0 && mod(t - (1 - B) * (1 - B), n) == 0) {
//                        SingQuandle quandle = new SingQuandle(Quandle.alexander(n, t));
//
//                        if (quandle.isValid()) {
//                            for (byte x = 0; x < n; ++x) {
//                                for (byte y = 0; y < n; ++y) {
//                                    quandle.circle(x, y, (byte) mod((1 - t - B) * x + (t + B) * y, n));
//                                    quandle.disc(x, y, (byte) mod((1 - B) * x + B * y, n));
//                                }
//                            }
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
//                            System.out.println("[t, B] = [" + t + ", " + B + "]");
//
//                            for (Phi phi : Phi.generate(quandle, m)) {
//                                if (!phi.isTrivial()) {
//                                    System.out.println(phi);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}
