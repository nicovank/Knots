package com.nvankempen.knots;

import java.util.Collection;
import java.util.List;

public abstract class Group<Element> {
    /**
     * This method should return which value should be used as the "unknown" placeholder while generating.
     * This value should NOT be returned when calling {@link #getAllElements()}.
     * For example, when using Z(4), the unknown value of -1 can be used.
     *
     * @return The value which should be used as the "unknown" placeholder.
     */
    public abstract Element getUnknownValue();

    /**
     * This method should return the identity of the group with respect to the operation.
     * That is, for any element a from the group, <code>operation(a, getIdentity())</code> and
     * <code>operation(getIdentity(), a)</code> should both equal a.
     *
     * @return the identity of the group with respect to the operation.
     */
    public abstract Element getIdentity();

    /**
     * This method should return all possible elements in the group.
     * For example, it should return [0, 1, 2, 3] when considering Z(4).
     * It uses a List to simplify further operations, but it should in fact be a set.
     *
     * @return the elements contained in the group.
     */
    public abstract List<Element> getAllElements();

    public abstract Element operation(Element a, Element b);

    /**
     * This method returns the inverse of a given element with respect to the group
     * {@link #operation(Element, Element)}. It uses brute-force by default, therefore it is a good idea to override it
     * whenever possible. By default, it returns null when no inverse is found. This should NEVER happen, since an
     * element should ALWAYS have an inverse within the group.
     *
     * @param a The element to find the inverse of.
     * @return The inverse of a with respect to the group operation.
     */
    public Element inverse(Element a) {
        for (Element b : getAllElements()) {
            if (operation(a, b) == a && operation(b, a) == a) {
                return b;
            }
        }

        return null;
    }

    public Element pow(Element a, int n) {
        if (n < 0) {
            return pow(inverse(a), -n);
        }

        return (n == 0) ? getIdentity() : operation(a, pow(a, n - 1));
    }

    public boolean isClosed() {
        final Collection<Element> elements = getAllElements();

        for (Element a : elements) {
            for (Element b : elements) {
                if (!elements.contains(operation(a, b))) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isAssociative() {
        final Collection<Element> elements = getAllElements();

        for (Element a : elements) {
            for (Element b : elements) {
                for (Element c : elements) {
                    if (operation(operation(a, b), c) != operation(a, operation(b, c))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isAbelian() {
        final Collection<Element> elements = getAllElements();

        for (Element a : elements) {
            for (Element b : elements) {
                if (operation(a, b) != operation(b, a)) {
                    return false;
                }
            }
        }

        return true;
    }
}
