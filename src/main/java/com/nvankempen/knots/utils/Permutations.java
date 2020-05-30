package com.nvankempen.knots.utils;

import java.util.*;
import java.util.function.Consumer;

public class Permutations {
    public static <Element> void forEach(Collection<Element> elements, Consumer<List<Element>> onResult) {
        forEach(new ArrayList<>(elements), onResult, elements.size());
    }

    public static <Element> Set<List<Element>> all(Collection<Element> elements) {
        final Set<List<Element>> permutations = new HashSet<>();
        forEach(new ArrayList<>(elements), permutations::add, elements.size());
        return permutations;
    }

    private static <Element> void forEach(List<Element> elements, Consumer<List<Element>> onResult, int n) {
        if (n == 1) {
            onResult.accept(List.copyOf(elements));
        }

        for (int i = 0; i < n; ++i) {
            forEach(elements, onResult, n - 1);

            final int j = (n % 2 == 1) ? 0 : i;
            final Element swap = elements.get(n - 1);
            elements.set(n - 1, elements.get(j));
            elements.set(j, swap);
        }
    }
}
