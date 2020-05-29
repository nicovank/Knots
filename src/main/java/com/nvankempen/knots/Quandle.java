package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class Quandle extends Rack {
    public Quandle(byte n) {
        super(n, initial(n));
    }

    public Quandle(Rack rack) {
        super(rack.n(), rack.right());
    }

    public Quandle(byte n, byte[][] triangle) {
        super(n, triangle);
    }

    @Override
    public Quandle copy() {
        return new Quandle(super.copy());
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        for (byte x = 0; x < n(); ++x) {
            if (right(x, x) != -1 && right(x, x) != x) {
                return false;
            }
        }

        return true;
    }

    public static void generate(byte n, Consumer<Quandle> onResult) {
        new RecursiveQuandleSearcher(onResult, new Quandle(n)).invoke();
    }

    public boolean isInvolutory() {
        for (byte a = 0; a < n(); ++a) {
            for (byte b = 0; b < n(); ++b) {
                if (left(a, left(a, b)) != b || right(right(b, a), a) != b) {
                    return false;
                }
            }
        }

        return true;
    }

    private static byte[][] initial(byte n) {
        final byte[][] initial = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                initial[i][j] = (byte) -1;
            }

            initial[i][i] = i;
        }

        return initial;
    }

    private static final class RecursiveQuandleSearcher extends RecursiveAction {
        private static final int DIRECT_SOLVE_THRESHOLD = 5;

        RecursiveQuandleSearcher(Consumer<Quandle> onResult, Quandle quandle) {
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

                final Queue<RecursiveQuandleSearcher> tasks = new ArrayDeque<>();
                for (byte z = 0; z < quandle.n(); ++z) {
                    final Quandle copy = quandle.copy();
                    copy.right(unknown.getA(), unknown.getB(), z);
                    tasks.add(new RecursiveQuandleSearcher(onResult, copy));
                }

                if (!tasks.isEmpty()) {
                    if (countUnknowns(quandle) < DIRECT_SOLVE_THRESHOLD) {
                        tasks.forEach(RecursiveQuandleSearcher::compute);
                    } else {
                        invokeAll(tasks);
                    }
                }
            }
        }

        private final Consumer<Quandle> onResult;
        private final Quandle quandle;
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
