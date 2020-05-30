package com.nvankempen.knots;

import java.util.List;

public interface Group<E> {
    /**
     * This method should return which value should be used as the "unknown" placeholder while generating.
     * This value should NOT be returned when calling {@link #getAllElements()}.
     * For example, when using Z(4), the unknown value of -1 can be used.
     *
     * @return The value which should be used as the "unknown" placeholder.
     */
    E getUnknownValue();

    /**
     * This method should return all possible elements in the group.
     * For example, it should return [0, 1, 2, 3] when considering Z(4).
     * It uses a List to simplify further operations, but it should in fact be a set.
     *
     * @return the elements contained in the group.
     */
    List<E> getAllElements();
}
