package com.nvankempen.knots;

import com.nvankempen.knots.utils.Permutations;

import java.util.List;

public class Rack<Element> extends Shelf<Element> {
    public Rack(Ring<Element> group) {
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

                    if (cRb.equals(getGroup().getUnknownValue())) {
                        return true;
                    } else if (cRb.equals(a)) {
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
                final Other px = phi.get(elements.indexOf(x));

                for (Element y : getGroup().getAllElements()) {
                    final Other py = phi.get(elements.indexOf(y));

                    if (!phi.get(elements.indexOf(right(x, y))).equals(other.right(px, py))) {
                        continue searching;
                    }
                }
            }

            return true;
        }

        return false;
    }
}
