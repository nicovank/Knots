package com.nvankempen.quandles;

import static com.nvankempen.Utils.mod;

public class Quandle extends Rack {

    private static byte[][] initial(byte n) {
        byte[][] initial = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                initial[i][j] = (byte) -1;
            }

            initial[i][i] = i;
        }

        return initial;
    }

    public Quandle(byte n) {
        super(n, initial(n), initial(n));
    }

    public Quandle(Rack rack) {
        super(rack.n(), rack.left(), rack.right());
    }

    public Quandle(byte n, byte[][] lefttriangle, byte[][] righttriangle) {
        super(n, lefttriangle,  righttriangle);
    }

    @Override
    public Quandle copy() {
        return new Quandle(super.copy());
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        for (byte x = 0; x < super.n(); ++x) {
            if ((right(x, x) != -1 && right(x, x) != x) || (left(x, x) != -1 && left(x, x) != x)) {
                return false;
            }
        }

        return true;
    }

    public boolean isInvolutory() {
        for (byte a = 0; a < super.n(); ++a) {
            for (byte b = 0; b < super.n(); ++b) {
                if (left(a, left(a, b)) != b || right(right(b, a), a) != b) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Quandle alexander(byte n, byte a) {
        Quandle quandle = new Quandle(n);

        for (byte x = 0; x < n; ++x) {
            for (byte y = 0; y < n; ++y) {
                quandle.right(x, y, (byte) mod(a * x + mod(1 - a, n) * y, n));
                quandle.left(y, (byte) mod(a * x + mod(1 - a, n) * y, n), x);
            }
        }

        return quandle;
    }

    public boolean isAlexander() {
        return coefficient() != -1;
    }

    public int coefficient() {
        searching_for_a: for (byte a = 1; a < super.n(); ++a) {
            for (byte x = 0; x < super.n(); ++x) {
                for (byte y = 0; y < super.n(); ++y) {
                    if (right(x, y) != mod(a * x + mod(1 - a, super.n()) * y, super.n())) {
                        continue searching_for_a;
                    }
                }
            }

            return a;
        }

        return -1;
    }

    @Override
    public String toString() {
        return "Q " + super.toString();
    }
}
