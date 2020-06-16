package com.nvankempen.knots;

import java.util.Arrays;
import java.util.List;

public abstract class Ring<Element> {
    /**
     * This method should return which value should be used as the "unknown" placeholder while generating.
     * This value should NOT be returned when calling {@link #getAllElements()}.
     * For example, when using Z(n), the unknown value of -1 can be used.
     *
     * @return The value which should be used as the "unknown" placeholder.
     */
    public abstract Element getUnknownValue();

    /**
     * This method should return the identity of the ring with respect to the additive operation.
     * That is, for any element a from the ring, <code>add(a, getAdditiveIdentity())</code> and
     * <code>add(getAdditiveIdentity(), a)</code> should both equal a.
     *
     * @return the identity of the ring with respect to the additive operation.
     */
    public abstract Element getAdditiveIdentity();

    /**
     * This method should return the identity of the ring with respect to the multiplicative operation.
     * That is, for any element a from the ring, <code>add(a, getMultiplicativeIdentity())</code> and
     * <code>add(getMultiplicativeIdentity(), a)</code> should both equal a.
     *
     * @return the identity of the ring with respect to the multiplicative operation.
     */
    public abstract Element getMultiplicativeIdentity();

    /**
     * This method should return all possible elements in the ring.
     * For example, it should return [0, 1, 2, 3] when considering Z(4).
     * It uses a List to simplify further operations, when it would be more natural to make it a set.
     *
     * @return the elements contained in the ring.
     */
    public abstract List<Element> getAllElements();

    public abstract Element add(Element a, Element b);

    public abstract Element multiply(Element a, Element b);

    public Element add(Element a, Element b, Element... rest) {
        return Arrays.stream(rest).reduce(add(a, b), this::add);
    }

    public Element multiply(Element a, Element b, Element... rest) {
        return Arrays.stream(rest).reduce(multiply(a, b), this::multiply);
    }

    /**
     * This inverse method uses brute-force by default, therefore it is a good idea to override it whenever possible. By
     * default, it returns null when no inverse is found. This should NEVER happen, since an element should ALWAYS have
     * an additive inverse within the ring.
     *
     * @param a The element to find the additive inverse of.
     * @return The additive inverse of a with respect to the ring's additive operation.
     */
    public Element getAdditiveInverse(Element a) {
        for (Element b : getAllElements()) {
            if (add(a, b).equals(getAdditiveIdentity()) && add(b, a).equals(getAdditiveIdentity())) {
                return b;
            }
        }

        return null;
    }

    /**
     * This inverse method uses brute-force by default, therefore it is a good idea to override it whenever possible. By
     * default, it returns null when no inverse is found. This should NEVER happen, since an element should ALWAYS have
     * a multiplicative inverse within the ring.
     *
     * @param a The element to find the multiplicative inverse of.
     * @return The additive inverse of a with respect to the ring's multiplicative operation.
     */
    public Element getMultiplicativeInverse(Element a) {
        for (Element b : getAllElements()) {
            if (multiply(a, b).equals(getMultiplicativeIdentity())
                    && multiply(b, a).equals(getMultiplicativeIdentity())) {

                return b;
            }
        }

        return null;
    }
}
