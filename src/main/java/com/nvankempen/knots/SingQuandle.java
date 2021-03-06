package com.nvankempen.knots;

import com.nvankempen.knots.utils.Doublet;

import java.util.HashMap;
import java.util.Map;

public class SingQuandle<Element> extends Quandle<Element> {
    public SingQuandle(Ring<Element> group) {
        super(group);
        this.R1 = new HashMap<>();
    }

    public SingQuandle(Quandle<Element> quandle, Map<Doublet<Element, Element>, Element> R1) {
        super(quandle);
        this.R1 = R1;
    }

    public Map<Doublet<Element, Element>, Element> R2() {
        final Map<Doublet<Element, Element>, Element> R2 = new HashMap<>();

        for (Element a : getGroup().getAllElements()) {
            for (Element b : getGroup().getAllElements()) {
                R2.put(Doublet.create(a, b), R2(a, b));
            }
        }

        return R2;
    }

    public Element R1(Element x, Element y) {
        return R1.getOrDefault(Doublet.create(x, y), getGroup().getUnknownValue());
    }

    public Element R2(Element x, Element y) {
        return R1(y, right(x, y));
    }

    public void R1(Element x, Element y, Element z) {
        R1.put(Doublet.create(x, y), z);
    }

    public Map<Doublet<Element, Element>, Element> R1() {
        return R1;
    }

    @Override
    public SingQuandle<Element> copy() {
        final Map<Doublet<Element, Element>, Element> copy = new HashMap<>();
        R1.forEach(copy::put);
        return new SingQuandle<>(super.copy(), copy);
    }

    @Override
    public boolean isComplete() {
        for (Element i : getGroup().getAllElements()) {
            for (Element j : getGroup().getAllElements()) {
                if (R1(i, j).equals(getGroup().getUnknownValue())) {
                    return false;
                }
            }
        }

        return super.isComplete();
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        final Element unknown = getGroup().getUnknownValue();

        for (Element x : getGroup().getAllElements()) {
            for (Element y : getGroup().getAllElements()) {
                final Element xRy = right(x, y);
                final Element xLy = left(x, y);
                final Element xCy = R1(x, y);
                final Element xDy = R2(x, y);

                if (!right(xCy, xDy).equals(unknown)
                        && !R2(y, xRy).equals(unknown)
                        && !right(xCy, xDy).equals(R2(y, xRy))) {

                    return false;
                }

                for (Element z : getGroup().getAllElements()) {
                    final Element zRy = right(z, y);
                    final Element xCz = R1(x, z);
                    final Element xDz = R1(x, z);

                    if (!right(R1(xLy, z), y).equals(unknown)
                            && !R1(x, zRy).equals(unknown)
                            && !right(R1(xLy, z), y).equals(R1(x, zRy))) {

                        return false;
                    }

                    if (!R2(xLy, z).equals(unknown)
                            && !left(R2(x, zRy), y).equals(unknown)
                            && !R2(xLy, z).equals(left(R2(x, zRy), y))) {

                        return false;
                    }

                    if (!right(left(y, xCz), x).equals(unknown)
                            && !left(right(y, xDz), z).equals(unknown)
                            && !right(left(y, xCz), x).equals(left(right(y, xDz), z))) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public <Other> boolean isIsomorphicTo(Rack<Other> other) {
        throw new UnsupportedOperationException();
    }

    private final Map<Doublet<Element, Element>, Element> R1;
}
