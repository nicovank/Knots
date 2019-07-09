package com.nvankempen.quandles.groups;

import java.util.List;

public abstract class Group<E> {
    /**
     * Applies the group operation to the given operands.
     * For example, in U(5), operation(3, 4) -> 2.
     *
     * @param x The first operand.
     * @param y The second operand.
     * @return The result of the operation.
     */
    public abstract E operation(E x, E y) throws OperandNotInGroupException;

    /**
     * Returns a list of all possible elements in this group.
     * @return a list of all possible elements in this group.
     */
    public abstract E[] elements();
}
