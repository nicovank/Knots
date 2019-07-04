package com.nvankempen.quandles;

import com.nvankempen.Utils;
import com.nvankempen.quandles.utils.Polynomial;

public final class Invariant {

    private Invariant() {

    }

    public static Polynomial compute(byte n, byte m, SingQuandle quandle, Phi phi) {

        Polynomial polynomial = new Polynomial();

        for (byte x = 0; x < n; ++x) {
            for (byte y = 0; y < n; ++y) {
//                if (Utils.mod((B - 1) * x + (3 - B) * y, n) == 0 && Utils.mod((B + 1) * x - (B + 1) * y, n) == 0) {
//                    System.out.printf("prime(%d, %d) %n", Utils.mod(2 * y - x, n), Utils.mod(3 * y - 2 * x, n));
//                    sum += phi.phi(x, y);
//                    sum += phi.phi(y, (byte) Utils.mod(2 * y - x, n));
//                    sum += phi.prime((byte) Utils.mod(2 * y - x, n), (byte) Utils.mod(3 * y - 2 * x, n));
//                }

//                polynomial.add(phi.prime(x, y) + phi.prime((byte) Utils.mod(3 * x + 3 * y, n), (byte) Utils.mod(2 * x + 4 * y, n)));
//                polynomial.add(phi.prime(x, y) + phi.phi((byte) Utils.mod(8 * x + 3 * y, n), (byte) Utils.mod(7 * x + 4 * y, n)));
//                polynomial.add(phi.prime(x, y) + phi.prime((byte) Utils.mod(3 * x + 8 * y, n), (byte) Utils.mod(2 * x + 9 * y, n)));


//                polynomial.add(phi.phi(x, y) + phi.prime(y, (byte) Utils.mod(2 * y - x, n)));
//                polynomial.add(phi.prime(x, y) + phi.prime(quandle.circle(x, y), quandle.disc(x, y)));
//                polynomial.add(phi.prime(x, y) + phi.phi(quandle.circle(x, y), quandle.disc(x, y)));
                polynomial.add(phi.phi(quandle.left(y, x), x) + phi.prime(quandle.left(y, x), x));
            }
        }

        return polynomial;
    }
}
