package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

/**
 * This class needs two rings to operate on. The regular group (as used in SingQuandle and its superclasses), and the
 * ring phi and phi prime output to.
 *
 * @param <X> The ring the quandle operates on (used for right, left, R1, and R2).
 * @param <A> The Abelian ring used for phi and phi prime.
 */
public class OrientedSingQuandle<X, A> extends SingQuandle<X> {

    /**
     * The OrientedSingquandle constructors require that the given singquandle is already well-defined and valid.
     * This could change in the future, but for now it speeds up the search for phi and phi prime functions.
     *
     * @param singquandle A complete and valid singquandle
     */
    public OrientedSingQuandle(SingQuandle<X> singquandle, Ring<A> group) {
        super(singquandle, singquandle.R1());

        this.group = group;
        this.phi = new HashMap<>();
        this.prime = new HashMap<>();
    }

    public OrientedSingQuandle(SingQuandle<X> singquandle, Ring<A> group, Map<Doublet<X, X>, A> phi,
                               Map<Doublet<X, X>, A> prime) {

        super(singquandle, singquandle.R1());

        this.group = group;
        this.phi = phi;
        this.prime = prime;
    }

    public Ring<A> getPhiGroup() {
        return group;
    }

    public A phi(X x, X y) {
        return phi.getOrDefault(Doublet.create(x, y), group.getUnknownValue());
    }

    public A prime(X x, X y) {
        return prime.getOrDefault(Doublet.create(x, y), group.getUnknownValue());
    }

    public void phi(X x, X y, A z) {
        phi.put(Doublet.create(x, y), z);
    }

    public void prime(X x, X y, A z) {
        prime.put(Doublet.create(x, y), z);
    }

    @Override
    public OrientedSingQuandle<X, A> copy() {
        final Map<Doublet<X, X>, A> phi = new HashMap<>();
        final Map<Doublet<X, X>, A> prime = new HashMap<>();
        this.phi.forEach(phi::put);
        this.prime.forEach(prime::put);
        return new OrientedSingQuandle<>(super.copy(), group, phi, prime);
    }

    @Override
    public boolean isComplete() {
        for (X i : getGroup().getAllElements()) {
            for (X j : getGroup().getAllElements()) {
                if (phi(i, j).equals(getGroup().getUnknownValue()) || prime(i, j).equals(getGroup().getUnknownValue())) {
                    return false;
                }
            }
        }

        return super.isComplete();
    }

    @Override
    public boolean isValid() {
        // We do not check super.isValid() since we assume the underlying singquandle is already well-defined and valid.
        final A unknown = group.getUnknownValue();

        for (X x : getGroup().getAllElements()) {
            for (X y : getGroup().getAllElements()) {
                final X xRy = right(x, y);
                final X xLy = left(x, y);
                final X xCy = R1(x, y);
                final X xDy = R2(x, y);
                final A xPy = phi(x, y);
                final A xQy = prime(x, y);

                // 5.3
                if (!xQy.equals(unknown)
                        && !phi(xCy, xDy).equals(unknown)
                        && !xPy.equals(unknown)
                        && !prime(y, xRy).equals(unknown)
                        && !group.add(xQy, phi(xCy, xDy)).equals(group.add(xPy, prime(y, xRy)))) {

                    return false;
                }

                for (X z : getGroup().getAllElements()) {
                    final X xRz = right(x, z);
                    final X yRz = right(y, z);
                    final X xCz = R1(x, z);
                    final X xDz = R2(x, z);
                    final X zRy = right(z, y);
                    final A xPz = phi(x, z);
                    final A zPy = phi(z, y);

                    // 5.1
                    if (!phi(xLy, y).equals(unknown)
                            && !prime(xLy, z).equals(unknown)
                            && !phi(R1(xLy, z), y).equals(unknown)
                            && !zPy.equals(unknown)
                            && !prime(x, zRy).equals(unknown)
                            && !phi(left(R2(x, zRy), y), y).equals(unknown)
                            && !group.add(group.getAdditiveInverse(phi(xLy, y)), prime(xLy, z), phi(R1(xLy, z), y))
                            .equals(group.add(
                                    zPy,
                                    prime(x, zRy),
                                    group.getAdditiveInverse(phi(left(R2(x, zRy), y), y))
                            ))) {

                        return false;
                    }

                    // 5.2
                    if (!phi(left(y, xCz), z).equals(unknown)
                            && !phi(left(y, xCz), xCz).equals(unknown)
                            && !phi(left(right(y, xDz), z), z).equals(unknown)
                            && !phi(y, xDz).equals(unknown)
                            && !group.add(phi(left(y, xCz), z), group.getAdditiveInverse(phi(left(y, xCz), xCz)))
                            .equals(group.add(group.getAdditiveInverse(phi(left(right(y, xDz), z), z)), phi(y, xDz)))) {

                        return false;
                    }

                    // 6.1
                    if (!xPy.equals(unknown)
                            && !phi(xRy, z).equals(unknown)
                            && !xPz.equals(unknown)
                            && !phi(xRz, yRz).equals(unknown)
                            && !group.add(xPy, phi(xRy, z)).equals(group.add(xPz, phi(xRz, yRz)))) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * This code assumes that phi is already well-defined and valid, and that we are solving uniquely for phi prime.
     * To change this and include a search for phi, uncomment code relating to equation 6.1 in {@link #isValid()}.
     * Further, findNextUnknown will need to be modified to return a triplet, with an extra variable indicating whether
     * the unknown was found in phi or phi prime. Finally the countUnknowns and the searcher's compute will have to be
     * modified accordingly.
     */
    public static <X, A> void generate(OrientedSingQuandle<X, A> initial,
                                       Consumer<OrientedSingQuandle<X, A>> onResult) {

        new RecursiveOrientedSingQuandleSearcher<>(onResult, initial).invoke();
    }

    private static final class RecursiveOrientedSingQuandleSearcher<X, A> extends RecursiveAction {
        private static final int DIRECT_SOLVE_THRESHOLD = 5;

        RecursiveOrientedSingQuandleSearcher(Consumer<OrientedSingQuandle<X, A>> onResult,
                                             OrientedSingQuandle<X, A> quandle) {

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
                final Doublet<X, X> unknown = Objects.requireNonNull(findNextUnknown(quandle));

                final Queue<RecursiveOrientedSingQuandleSearcher<X, A>> tasks = new ArrayDeque<>();
                for (A z : quandle.getPhiGroup().getAllElements()) {
                    final OrientedSingQuandle<X, A> copy = quandle.copy();
                    copy.prime(unknown.getA(), unknown.getB(), z);
                    if (copy.isValid()) {
                        tasks.add(new RecursiveOrientedSingQuandleSearcher<>(onResult, copy));
                    }
                }

                if (!tasks.isEmpty()) {
                    if (countUnknowns(quandle) < DIRECT_SOLVE_THRESHOLD) {
                        tasks.forEach(RecursiveOrientedSingQuandleSearcher::compute);
                    } else {
                        invokeAll(tasks);
                    }
                }
            }
        }

        private final Consumer<OrientedSingQuandle<X, A>> onResult;
        private final OrientedSingQuandle<X, A> quandle;
    }

    private static <X, A> Doublet<X, X> findNextUnknown(OrientedSingQuandle<X, A> quandle) {
        for (X a : quandle.getGroup().getAllElements()) {
            for (X b : quandle.getGroup().getAllElements()) {
                // We only check for prime, since we assume phi is already well-defined and valid.
                if (quandle.prime(a, b).equals(quandle.getPhiGroup().getUnknownValue())) {
                    return Doublet.create(a, b);
                }
            }
        }

        return null;
    }

    private static <X, A> int countUnknowns(OrientedSingQuandle<X, A> quandle) {
        int count = 0;
        for (X a : quandle.getGroup().getAllElements()) {
            for (X b : quandle.getGroup().getAllElements()) {
                // We only check for prime, since we assume phi is already well-defined and valid.
                if (quandle.prime(a, b).equals(quandle.getPhiGroup().getUnknownValue())) {
                    ++count;
                }
            }
        }

        return count;
    }

    private final Ring<A> group;
    private final Map<Doublet<X, X>, A> phi;
    private final Map<Doublet<X, X>, A> prime;
}
