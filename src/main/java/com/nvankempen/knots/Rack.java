package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.*;

public class Rack<Element> {
    public Rack(Group<Element> group) {
        this.group = group;
        this.triangle = new HashMap<>();

        for (Element i : group.getAllElements()) {
            for (Element j : group.getAllElements()) {
                right(i, j, group.getUnknownValue());
            }
        }
    }

    public Rack(Group<Element> group, Map<Doublet<Element, Element>, Element> triangle) {
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
            if (right(x, y) == z) {
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

    public Rack<Element> copy() {
        final Map<Doublet<Element, Element>, Element> copy = new HashMap<>();
        triangle.forEach(copy::put);
        return new Rack<>(group, copy);
    }

    public boolean isComplete() {
        for (Element i : group.getAllElements()) {
            for (Element j : group.getAllElements()) {
                if (right(i, j) == group.getUnknownValue()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValid() {
        for (Element a : group.getAllElements()) {
            for (Element b : group.getAllElements()) {
                boolean unknowns = false;
                boolean found = false;

                for (Element c : group.getAllElements()) {
                    final Element cRb = right(c, b);
                    final Element aRc = right(a, c);
                    final Element aRb = right(a, b);
                    final Element bRc = right(b, c);

                    if (cRb == getGroup().getUnknownValue()) {
                        unknowns = true;
                    } else if (!unknowns && cRb == a) {
                        if (found) {
                            return false;
                        } else {
                            found = true;
                        }
                    }

                    if (right(aRb, c) != group.getUnknownValue() && right(aRc, bRc) != group.getUnknownValue() && right(aRb, c) != right(aRc, bRc)) {
                        return false;
                    }
                }

                if (!unknowns && !found) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rack && ((Rack) other).triangle.equals(this.triangle);
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

        final List<Element> elements = new ArrayList<>(group.getAllElements());
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
