package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

/**
 * This class provides a utility capable of generating all quandles of a given order.
 */
public final class Quandles {

    private static final int DIRECT_SOLVE_THRESHOLD = 5;

    private static final class QuandleSeeker extends RecursiveAction {
        QuandleSeeker(Consumer<Quandle> onResult, Quandle quandle) {
            this.onResult = onResult;
            this.quandle = quandle;
        }

        @Override
        public void compute() {
            if (quandle.isComplete()) {
                if (quandle.isValid()) {
                    onResult.accept(quandle);
                }
            } else {
                final Doublet<Byte, Byte> unknown = Objects.requireNonNull(findNextUnknown(quandle));

                final Queue<QuandleSeeker> tasks = new ArrayDeque<>();
                for (byte z = 0; z < quandle.n(); ++z) {
                    final Quandle copy = quandle.copy();
                    copy.right(unknown.getA(), unknown.getB(), z);
                    tasks.add(new QuandleSeeker(onResult, copy));
                }

                if (!tasks.isEmpty()) {
                    if (countUnknowns(quandle) < DIRECT_SOLVE_THRESHOLD) {
                        tasks.forEach(QuandleSeeker::compute);
                    } else {
                        invokeAll(tasks);
                    }
                }
            }
        }

        private final Consumer<Quandle> onResult;
        private final Quandle quandle;
    }

    public static void generate(byte n, Consumer<Quandle> onResult) {
        new QuandleSeeker(onResult, new Quandle(n)).invoke();
    }

    private static Doublet<Byte, Byte> findNextUnknown(Quandle quandle) {
        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                if (quandle.right(i, j) == -1) {
                    return Doublet.create(i, j);
                }
            }
        }

        return null;
    }

    private static int countUnknowns(Quandle quandle) {
        int count = 0;
        for (byte i = 0; i < quandle.n(); ++i) {
            for (byte j = 0; j < quandle.n(); ++j) {
                if (quandle.right(i, j) == -1) {
                    ++count;
                }
            }
        }

        return count;
    }
}
