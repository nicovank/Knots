package com.nvankempen.knots;

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
}
