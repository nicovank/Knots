import com.nvankempen.quandles.*;

import java.util.Set;

import static com.nvankempen.Utils.mod;

public class Main {
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

        Quandle.alexander((byte) 4, (byte) 2);

        byte n = 6;

        for (byte t = 1; t < n; ++t) {
            for (byte B = 0; B < n; ++B) {
                SingQuandle quandle = new SingQuandle(Quandle.alexander(n, t));

                if (quandle.isValid()) {
                    for (byte x = 0; x < n; ++x) {
                        for (byte y = 0; y < n; ++y) {
                            quandle.circle(x, y, (byte) mod((1 - t - B) * x + (t + B) * y, n));
                            quandle.disc(x, y, (byte) mod((1 - B) * x + B * y, n));
                        }
                    }

                    System.out.println("[t, B] = [" + t + ", " + B + "]");
                    System.out.println(quandle.isComplete());
                    for(QuandlePhi phi : QuandlePhi.generate(quandle)) {
                        if (Tests.P3(quandle, phi.phi())) {
                            System.out.println(phi);
                        }
                    }
                }
            }
        }

//        System.out.println(quandle);
//         QuandlePhi.generate(quandle).forEach(System.out::println);
//        System.out.println(QuandlePhi.generate(quandle).size());

//        for (byte B = 0; B < n; ++B) {
//            SingQuandle quandle = new SingQuandle(Quandle.alexander(n, t));
//            for (byte x = 0; x < n; ++x) {
//                for (byte y = 0; y < n; ++y) {
//                    quandle.circle(x, y, (byte) mod((1 - t - B) * x + (t + B) * y, n));
//                    quandle.disc(x, y, (byte) mod((1 - B) * x + B * y, n));
//                }
//            }
//
//            // This will also check circle and disc
//            if (quandle.isValid()) {
//                System.out.printf("[t, B] = [%d, %d] %n", t, B);
//
//                Set<Phi> functions = Phi.generate(quandle);
//                for (Phi phi : functions) {
//                    System.out.println("\t" + phi);
//                }
//            }
//        }


//        for (byte n = 0; n >= 0; ++n) {
//            System.out.println("n = " + n);
//            for (byte t = 1; t < n; ++t) {
//                SingQuandle quandle = new SingQuandle(Quandle.alexander(n, t));
//
//                if (quandle.isValid()) { // This will only check the triangle operations since circle and disc are not defined yet.
//                    for (byte B = 0; B < n; ++B) {
//                        for (byte x = 0; x < n; ++x) {
//                            for (byte y = 0; y < n; ++y) {
//                                quandle.circle(x, y, (byte) mod((1 - t - B) * x + (t + B) * y, n));
//                                quandle.disc(x, y, (byte) mod((1 - B) * x + B * y, n));
//                            }
//                        }
//
//                        // This will also check circle and disc
//                        if (quandle.isValid()) {
//                            System.out.printf("[a, b] = [%d, %d] %n", t, B);
//                        }
//                    }
//                }
//            }
//        }
    }
}
