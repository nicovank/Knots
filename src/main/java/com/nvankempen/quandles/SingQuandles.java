package com.nvankempen.quandles;

import java.util.*;

public class SingQuandles {

//    public static Set<SingQuandle> findAPhi(byte n) {
//
//    }

    public static Set<SingQuandle> generate(Quandle q) {

        if (!(q.isComplete() && q.isValid())) {
            throw new RuntimeException("Too lazy to make this an actual exception.");
        }

        Queue<SingQuandle> queue = new LinkedList<>();
        Set<SingQuandle> quandles = new HashSet<>();
        queue.offer(new SingQuandle(q));

        while (!queue.isEmpty()) {
            SingQuandle quandle = queue.remove();

            if (quandle.isComplete()) {
                if (quandle.isValid()) {
                    quandles.add(quandle);
                }
            } else {
                for (byte x = 0; x < q.n(); ++x) {
                    SingQuandle copy = quandle.copy();
                    if (replaceNextUnknown(copy, q.n(), x) && fill(copy, q.n())) {
                        queue.offer(copy);
                    }
                }
            }
        }

        return quandles;
    }

    private static boolean fill(SingQuandle q, byte n) {
        boolean changes = true;

        while (changes) {

            changes = false;

            // TODO
        }

        return true;
    }

    private static boolean replaceNextUnknown(SingQuandle quandle, byte n, byte value) {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (quandle.circle(i, j) == -1) {
                    quandle.circle(i, j, value);
                    return true;
                }
            }
        }

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (quandle.disc(i, j) == -1) {
                    quandle.disc(i, j, value);
                    return true;
                }
            }
        }

        return false;
    }
}
