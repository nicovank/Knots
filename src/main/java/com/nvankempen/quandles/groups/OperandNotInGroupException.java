package com.nvankempen.quandles.groups;

public class OperandNotInGroupException extends Exception {
    public OperandNotInGroupException() {
        super();
    }

    public OperandNotInGroupException(String format, Object... args) {
        super(String.format(format, args));
    }
}
