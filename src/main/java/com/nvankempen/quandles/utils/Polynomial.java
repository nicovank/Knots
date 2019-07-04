package com.nvankempen.quandles.utils;

import java.util.HashMap;
import java.util.Map;

public class Polynomial {
    private final Map<Integer, Integer> coefficients;

    public Polynomial() {
        this.coefficients = new HashMap<>();
    }

    // adds on of the given exponent.
    public void add(int exponent) {
        if (coefficients.containsKey(exponent)) {
            coefficients.put(exponent, coefficients.get(exponent) + 1);
        } else {
            coefficients.put(exponent, 1);
        }
    }

    public String toString(String variable) {
        return coefficients.entrySet().stream()
                .map(e -> String.format("%d%s^%d", e.getValue(), variable, e.getKey()))
                .reduce((a, b) -> a + " + " + b).orElse("");
    }

    @Override
    public String toString() {
        return this.toString("x");
    }
}
