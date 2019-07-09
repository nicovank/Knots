package com.nvankempen.quandles.groups;

import com.nvankempen.Utils;

public class Z extends Group<Integer> {

    private final int n;

    public Z(int n) {
        this.n = n;
    }

    @Override
    public Integer operation(Integer x, Integer y) throws OperandNotInGroupException {
        if (x < 0 || x >= n) {
            throw new OperandNotInGroupException("%s is not an element of Z(%d).", x, n);
        }
        if (y < 0 || y >= n) {
            throw new OperandNotInGroupException("%s is not an element of Z(%d).", y, n);
        }

        return Utils.mod(x + y, n);
    }

    @Override
    public Integer[] elements() {
        Integer[] elements = new Integer[n];

        for (int i = 0; i < n; ++i) {
            elements[i] = i;
        }

        return elements;
    }
}
