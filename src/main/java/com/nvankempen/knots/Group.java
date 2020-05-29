package com.nvankempen.knots;

import java.util.SortedSet;

public interface Group<E> {
    E getUnknownValue();
    SortedSet<E> getAllElements();
}
