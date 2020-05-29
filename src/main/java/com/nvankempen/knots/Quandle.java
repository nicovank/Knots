package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class Quandle<Element> extends Rack<Element> {
    public Quandle(byte n, Group<Element> group) {
        super(n, group);
        group.getAllElements().forEach(e -> right(e, e, e));
    }

    public Quandle(Rack<Element> rack) {
        super(rack.n(), rack.getGroup(), rack.right());
    }

    @Override
    public Quandle<Element> copy() {
        return new Quandle<>(super.copy());
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        for (Element x : getGroup().getAllElements()) {
            if (right(x, x) != getGroup().getUnknownValue() && right(x, x) != x) {
                return false;
            }
        }

        return true;
    }

    public static <Element> void generate(byte n, Group<Element> group, Consumer<Quandle<Element>> onResult) {
        new RecursiveQuandleSearcher<>(onResult, new Quandle<>(n, group)).invoke();
    }

    public boolean isInvolutory() {
        for (Element a : getGroup().getAllElements()) {
            for (Element b : getGroup().getAllElements()) {
                if (left(a, left(a, b)) != b || right(right(b, a), a) != b) {
                    return false;
                }
            }
        }

        return true;
    }

    private static final class RecursiveQuandleSearcher<Element> extends RecursiveAction {
        private static final int DIRECT_SOLVE_THRESHOLD = 5;

        RecursiveQuandleSearcher(Consumer<Quandle<Element>> onResult, Quandle<Element> quandle) {
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
                final Doublet<Element, Element> unknown = Objects.requireNonNull(findNextUnknown(quandle));

                final Queue<RecursiveQuandleSearcher<Element>> tasks = new ArrayDeque<>();
                for (Element z : quandle.getGroup().getAllElements()) {
                    final Quandle<Element> copy = quandle.copy();
                    copy.right(unknown.getA(), unknown.getB(), z);
                    tasks.add(new RecursiveQuandleSearcher<>(onResult, copy));
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

        private final Consumer<Quandle<Element>> onResult;
        private final Quandle<Element> quandle;
    }

    private static <Element> Doublet<Element, Element> findNextUnknown(Quandle<Element> quandle) {
        for (Element a : quandle.getGroup().getAllElements()) {
            for (Element b : quandle.getGroup().getAllElements()) {
                if (quandle.right(a, b) == quandle.getGroup().getUnknownValue()) {
                    return Doublet.create(a, b);
                }
            }
        }

        return null;
    }

    private static <Element> int countUnknowns(Quandle<Element> quandle) {
        int count = 0;
        for (Element a : quandle.getGroup().getAllElements()) {
            for (Element b : quandle.getGroup().getAllElements()) {
                if (quandle.right(a, b) == quandle.getGroup().getUnknownValue()) {
                    ++count;
                }
            }
        }

        return count;
    }
}
