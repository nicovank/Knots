package com.nvankempen.quandles;

import java.util.*;

public final class Quandles {

    public static Set<Quandle> generate(byte n) {
        Queue<Quandle> queue = new LinkedList<>();
        Set<Quandle> quandles = new HashSet<>();
        queue.offer(new Quandle(n));

        while (!queue.isEmpty()) {
            Quandle quandle = queue.remove();

            if (quandle.isComplete()) {
                if (quandle.isValid()) {
                    quandles.add(quandle);
                }
            } else {
                for (byte x = 0; x < n; ++x) {
                    Quandle copy = quandle.copy();
                    if (replaceNextUnknown(copy, n, x) && fill(copy, n)) {
                        queue.offer(copy);
                    }
                }
            }
        }

        return quandles;
    }

    private static boolean fill(Quandle q, byte n) {
        return true;
    }

    private static boolean replaceNextUnknown(Quandle quandle, byte n, byte value) {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (quandle.right(i, j) == -1) {
                    quandle.right(i, j, value);
                    return true;
                }
            }
        }

        return false;
    }
}
