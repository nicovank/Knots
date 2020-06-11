package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.*;

public class Shelf<Element> {
    public Shelf(Group<Element> group) {
        this.group = group;
        this.triangle = new HashMap<>();
    }

    public Shelf(Group<Element> group, Map<Doublet<Element, Element>, Element> triangle) {
        this.group = group;
        this.triangle = new HashMap<>(triangle);
    }

    public Group<Element> getGroup() {
        return group;
    }

    public int n() {
        return group.getAllElements().size();
    }

    public Map<Doublet<Element, Element>, Element> right() {
        return triangle;
    }

    public Element left(Element z, Element y) {
        for (Element x : group.getAllElements()) {
            if (right(x, y).equals(z)) {
                return x;
            }
        }

        return group.getUnknownValue();
    }

    public Element right(Element i, Element j) {
        return triangle.getOrDefault(Doublet.create(i, j), group.getUnknownValue());
    }

    public void right(Element i, Element j, Element x) {
        triangle.put(Doublet.create(i, j), x);
    }

    public Shelf<Element> copy() {
        final Map<Doublet<Element, Element>, Element> copy = new HashMap<>();
        triangle.forEach(copy::put);
        return new Shelf<>(group, copy);
    }

    public boolean isComplete() {
        for (Element i : group.getAllElements()) {
            for (Element j : group.getAllElements()) {
                if (right(i, j).equals(group.getUnknownValue())) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid() {
        for (Element a : group.getAllElements()) {
            for (Element b : group.getAllElements()) {
                for (Element c : group.getAllElements()) {
                    final Element aRc = right(a, c);
                    final Element aRb = right(a, b);
                    final Element bRc = right(b, c);

                    if (!right(aRb, c).equals(group.getUnknownValue()) && !right(aRc, bRc).equals(group.getUnknownValue()) && !right(aRb, c).equals(right(aRc, bRc))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Shelf && ((Shelf) other).triangle.equals(this.triangle);
    }

    @Override
    public int hashCode() {
        return triangle.hashCode();
    }

    @Override
    public String toString() {
        if (n() == 0) {
            return "[]";
        }

        final List<Element> elements = group.getAllElements();
        final StringBuilder builder = new StringBuilder();

        builder.append("[");
        builder.append(right(elements.get(0), elements.get(0)));
        for (int i = 1; i < n(); ++i) {
            builder.append(", ").append(right(elements.get(0), elements.get(i)));
        }
        builder.append("]");

        for (int i = 1; i < n(); ++i) {
            builder.append(", [");
            builder.append(right(elements.get(i), elements.get(0)));
            for (int j = 1; j < n(); ++j) {
                builder.append(", ").append(right(elements.get(i), elements.get(j)));
            }
            builder.append("]");
        }
        builder.append("]");

        return new String(builder);
    }

    private final Group<Element> group;
    private final Map<Doublet<Element, Element>, Element> triangle;
}
