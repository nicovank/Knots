package com.nvankempen.knots.utils;

public class Box<E> {
    public static <E> Box<E> create(E element) {
        return new Box<>(element);
    }

    public Box() {

    }

    public Box(E element) {
        this.element = element;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Box && ((Box) other).element.equals(element);
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public String toString() {
        return element.toString();
    }

    private E element;
}
