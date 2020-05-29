package com.nvankempen.knots;

public class SingQuandle extends Quandle {
    public SingQuandle(byte n) {
        super(n);
        this.R1 = new byte[n][n];

        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                R1(i, j, (byte) -1);
            }
        }
    }

    public SingQuandle(Quandle quandle, byte[][] R1) {
        super(quandle);
        this.R1 = R1;
    }

    public byte R1(byte x, byte y) {
        if (x < 0 || y < 0) {
            return -1;
        }

        return R1[x][y];
    }

    public byte R2(byte x, byte y) {
        return R1(y, right(x, y));
    }

    public void R1(byte x, byte y, byte z) {
        R1[x][y] = z;
    }

    @Override
    public SingQuandle copy() {
        final byte[][] copy = new byte[n()][n()];
        for (byte i = 0; i < n(); i++) {
            for (byte j = 0; j < n(); j++) {
                copy[i][j] = R1(i, j);
            }
        }

        return new SingQuandle(super.copy(), copy);
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        for (byte x = 0; x < n(); ++x) {
            for (byte y = 0; y < n(); y++) {
                final byte xRy = right(x, y);
                final byte xLy = left(x, y);
                final byte xCy = R1(x, y);
                final byte xDy = R2(x, y);

                if (right(xCy, xDy) != -1 && R2(y, xRy) != -1 && right(xCy, xDy) != R2(y, xRy)) {
                    return false;
                }

                for (byte z = 0; z < n(); ++z) {
                    final byte zRy = right(z, y);
                    final byte xCz = R1(x, z);
                    final byte xDz = R1(x, z);

                    if (right(R1(xLy, z), y) != -1 && R1(x, zRy) != -1 && right(R1(xLy, z), y) != R1(x, zRy)) {
                        return false;
                    }

                    if (R2(xLy, z) != -1 && left(R2(x, zRy), y) != -1 && R2(xLy, z) != left(R2(x, zRy), y)) {
                        return false;
                    }

                    if (right(left(y, xCz), x) != -1 && left(right(y, xDz), z) != -1 && right(left(y, xCz), x) != left(right(y, xDz), z)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private final byte[][] R1;
}
