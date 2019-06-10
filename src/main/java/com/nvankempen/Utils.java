package com.nvankempen;

public final class Utils {

    public static int mod(int a, int m) {
        return (a % m < 0) ? ((a % m) + m) : (a % m);
    }

    private Utils() {

    }
}
