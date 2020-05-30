package com.nvankempen.knots;

import com.nvankempen.knots.utils.Permutations;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Function;

public class Rack<Element> extends Shelf<Element> {
    public Rack(Group<Element> group) {
        super(group);
    }

    public Rack(Shelf<Element> shelf) {
        super(shelf.getGroup(), shelf.right());
    }

    @Override
    public Rack<Element> copy() {
        return new Rack<>(super.copy());
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        for (Element a : getGroup().getAllElements()) {
            for (Element b : getGroup().getAllElements()) {
                boolean found = false;

                for (Element c : getGroup().getAllElements()) {
                    final Element cRb = right(c, b);

                    if (cRb == getGroup().getUnknownValue()) {
                        return true;
                    } else if (cRb == a) {
                        if (found) {
                            return false;
                        } else {
                            found = true;
                        }
                    }
                }

                if (!found) {
                    return false;
                }
            }
        }

        return true;
    }

    public <Other> boolean isIsomorphicTo(Rack<Other> other) {
        if (n() != other.n()) {
            return false;
        }

        final List<Element> elements = getGroup().getAllElements();

        searching:
        for (List<Other> phi: Permutations.all(other.getGroup().getAllElements())) {
            for (Element x : getGroup().getAllElements()) {
                for (Element y : getGroup().getAllElements()) {
                    if (phi.get(elements.indexOf(right(x, y))) != other.right(phi.get(elements.indexOf(x)), phi.get(elements.indexOf(y)))) {
                        continue searching;
                    }
                }
            }

            return true;
        }

        return false;
    }
}
