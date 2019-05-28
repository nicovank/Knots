package com.nvankempen.quandles;

import java.util.*;

public final class Quandles {

    public static Set<Quandle> generate(byte n) {
        Queue<Quandle> queue = new LinkedList<>();
        Set<Quandle> quandles = new HashSet<>();
        queue.offer(new Quandle(n));

        while (!queue.isEmpty()) {
            Quandle quandle = queue.remove();

            if (quandle.isTriangleComplete()) {
                if (quandle.isTriangleValid()) {
                    quandles.add(quandle);
                }
            } else {
                if (quandle.isTriangleComplete()) {
                    for (byte x = 0; x < n * n; ++x) {
                        Quandle copy = quandle.copy();
                        replaceNextPhiUnknown(copy, n, x);
                        if (fill(copy, n)) {
                            queue.offer(copy);
                        }
                    }
                } else {
                    for (byte x = 0; x < n; ++x) {
                        Quandle copy = quandle.copy();
                        replaceNextTriangleUnknown(copy, n, x);
                        if (fill(copy, n)) {
                            queue.offer(copy);
                        }
                    }
                }
            }
        }

        return quandles;
    }

    private static boolean fill(Quandle quandle, byte n) {

        boolean changes = true;

        while (changes) {
            changes = false;

            for (byte x = 0; x < n; ++x) {
                if (quandle.get(x, x) == -1) {
                    quandle.set(x, x, x);
                    changes = true;
                } else if (quandle.get(x, x) != x) {
                    return false;
                }

                for (byte y = 0; y < n; ++y) {
                    if (quandle.get(x, y) != -1) {
                        if (quandle.get(quandle.get(x, y), y) == -1) {
                            quandle.set(quandle.get(x, y), y, x);
                            changes = true;
                        } else if (quandle.get(quandle.get(x, y), y) != x) {
                            return false;
                        }
                    }

                    for (byte z = 0; z < n; ++z) {
                        if (quandle.get(y, z) != -1 && quandle.get(x, y) != -1 && quandle.get(x, z) != -1) {
                            if (quandle.get(x, quandle.get(y, z)) == -1 && quandle.get(quandle.get(x, y), quandle.get(x, z)) != -1) {
                                quandle.set(x, quandle.get(y, z), quandle.get(quandle.get(x, y), quandle.get(x, z)));
                                changes = true;
                            } else if (quandle.get(x, quandle.get(y, z)) != -1 && quandle.get(quandle.get(x, y), quandle.get(x, z)) == -1) {
                                quandle.set(quandle.get(x, y), quandle.get(x, z), quandle.get(x, quandle.get(y, z)));
                                changes = true;
                            } else if (quandle.get(x, quandle.get(y, z)) != quandle.get(quandle.get(x, y), quandle.get(x, z))) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private static void replaceNextTriangleUnknown(Quandle quandle, byte n, byte value) {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (quandle.get(i, j) == -1) {
                    quandle.set(i, j, value);
                    return;
                }
            }
        }
    }

    private static void replaceNextPhiUnknown(Quandle quandle, byte n, byte value) {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (quandle.getPhi(i, j) == -1) {
                    quandle.setPhi(i, j, value);
                    return;
                }
            }
        }
    }
}
