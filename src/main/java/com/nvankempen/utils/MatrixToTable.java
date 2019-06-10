package com.nvankempen.utils;

public class MatrixToTable {
    public static String transform(byte n, byte[][] array, String operation) {
        StringBuilder builder = new StringBuilder();

        if (operation.length() == 0) {
            operation = "   ";
        } else if (operation.length() == 1) {
            operation = "  " + operation;
        } else if (operation.length() == 2) {
            operation = " " + operation;
        }

        builder.append(String.format(" %s | ", operation));

        for (byte i = 0; i < n; ++i) {
            builder.append(String.format("%2d ", i));
        }

        int width = builder.length();
        builder.append('\n');
        builder.append("-".repeat(width)).append('\n');

        for (byte i = 0; i < n; ++i) {
            builder.append(" ".repeat(operation.length() - 3));
            builder.append(" ").append(String.format(" %2d ", i));
            builder.append("| ");

            for (byte j = 0; j < n; ++j) {
                builder.append(String.format("%2d ", array[i][j]));
            }

            builder.append('\n');
        }

        return builder.toString();
    }
}