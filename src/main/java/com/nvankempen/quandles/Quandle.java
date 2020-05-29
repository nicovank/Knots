package com.nvankempen.quandles;

public class Quandle extends Rack {
    public Quandle(byte n) {
        super(n, initial(n));
    }

    public Quandle(Rack rack) {
        super(rack.n(), rack.right());
    }

    public Quandle(byte n, byte[][] triangle) {
        super(n, triangle);
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

        for (byte x = 0; x < n(); ++x) {
            if (right(x, x) != -1 && right(x, x) != x) {
                return false;
            }
        }

        return true;
    }

    public boolean isInvolutory() {
        for (byte a = 0; a < n(); ++a) {
            for (byte b = 0; b < n(); ++b) {
                if (left(a, left(a, b)) != b || right(right(b, a), a) != b) {
                    return false;
                }
            }
        }

        return true;
    }

    private static byte[][] initial(byte n) {
        final byte[][] initial = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                initial[i][j] = (byte) -1;
            }

            initial[i][i] = i;
        }

        return initial;
    }
}
