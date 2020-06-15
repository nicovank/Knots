package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.HashMap;
import java.util.Map;

/**
 * This class needs two groups to operate on. The regular group (as used in SingQuandle and its superclasses), and the
 * group phi and phi prime output to.
 *
 * @param <X> The group the quandle operates on (used for right, left, R1, and R2).
 * @param <A> The Abelian group used for phi and phi prime.
 */
public class OrientedSingQuandle<X, A> extends SingQuandle<X> {
    
    /**
     * The OrientedSingquandle constructors require that the given singquandle is already well-defined and valid.
     * This could change in the future, but for now it speeds up the search for phi and phi prime functions.
     *
     * @param singquandle A complete and valid singquandle
     */
    public OrientedSingQuandle(SingQuandle<X> singquandle, Group<A> group) {
        super(singquandle, singquandle.R1());

        this.group = group;
        this.phi = new HashMap<>();
        this.prime = new HashMap<>();
    }

    public OrientedSingQuandle(SingQuandle<X> singquandle, Group<A> group, Map<Doublet<X, X>, A> phi,
                               Map<Doublet<X, X>, A> prime) {

        super(singquandle, singquandle.R1());

        this.group = group;
        this.phi = phi;
        this.prime = prime;
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
                        && !group.operation(xQy, phi(xCy, xDy)).equals(group.operation(xPy, prime(y, xRy)))) {

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
                            && !group.operation(group.inverse(phi(xLy, y)), prime(xLy, z), phi(R1(xLy, z), y))
                            .equals(group.operation(zPy, prime(x, zRy), group.inverse(phi(left(R2(x, zRy), y), y))))) {

                        return false;
                    }

                    // 5.2
                    if (!phi(left(y, xCz), x).equals(unknown)
                            && !phi(left(y, xCz), xCz).equals(unknown)
                            && !phi(left(right(y, xDz), z), z).equals(unknown)
                            && !phi(y, xDz).equals(unknown)
                            && !group.operation(phi(left(y, xCz), x), group.inverse(phi(left(y, xCz), xCz)))
                            .equals(group.operation(group.inverse(phi(left(right(y, xDz), z), z)), phi(y, xDz)))) {

                        return false;
                    }

                    // 6.1
                    if (!xPy.equals(unknown)
                            && !phi(xRy, z).equals(unknown)
                            && !xPz.equals(unknown)
                            && !phi(xRz, yRz).equals(unknown)
                            && !group.operation(xPy, phi(xRy, z)).equals(group.operation(xPz, phi(xRz, yRz)))) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    private final Group<A> group;
    private final Map<Doublet<X, X>, A> phi;
    private final Map<Doublet<X, X>, A> prime;
}
