package com.nvankempen.quandles;

public final class Tests {
    public static boolean P1(SingQuandle quandle, byte[][] phi) {
        for (byte x = 0; x < quandle.n(); ++x) {
            if (phi[x][x] != 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean P3(SingQuandle quandle, byte[][] phi) {
        for (byte x = 0; x < quandle.n(); ++x) {
            for (byte y = 0; y < quandle.n(); ++y) {
                for (byte z = 0; z < quandle.n(); ++z) {
                    byte xCz = quandle.circle(x, z);
                    byte xDz = quandle.disc(x, z);

                    if (phi[quandle.left(y, xCz)][x] - phi[xCz][y] != - phi[z][quandle.right(y, xDz)] + phi[y][xDz]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
