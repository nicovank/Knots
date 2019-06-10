package com.nvankempen.quandles;

import java.util.*;

public final class Quandles {

    public static Set<Quandle> generate(byte n) {
        Queue<Quandle> queue = new LinkedList<>();
        Set<Quandle> quandles = new HashSet<>();
        queue.offer(new Quandle(n));

        while (!queue.isEmpty()) {
            Quandle quandle = queue.remove();

            if (quandle.isComplete()) {
                if (quandle.isValid()) {
                    quandles.add(quandle);
                }
            } else {
                for (byte x = 0; x < n; ++x) {
                    Quandle copy = quandle.copy();
                    if (replaceNextUnknown(copy, n, x) && fill(copy, n)) {
                        queue.offer(copy);
                    }
                }
            }
        }

        return quandles;
    }

    private static boolean fill(Quandle q, byte n) {
        boolean changes = true;

        while (changes) {

            changes = false;

            for (byte a = 0; a < n; ++a) {

                // a ▷ a = a
                if (q.right(a, a) != a) {
                    if (q.right(a, a) == -1) {
                        q.right(a, a, a);
                        changes = true;
                    }

                    return false;
                }

                // a ◁ a = a
                if (q.left(a, a) != a) {
                    if (q.left(a, a) == -1) {
                        q.left(a, a, a);
                        changes = true;
                    }

                    return false;
                }

                for (byte b = 0; b < n; ++b) {

                    byte aLb = q.left(a, b);
                    byte bRa = q.right(b, a);
                    byte aRb = q.right(a, b);

                    if (aRb != -1 && q.left(b, aRb) != a) {
                        if (q.left(b, aRb) == -1) {
                            q.left(b, aRb, a);
                        } else {
                            return false;
                        }
                    }

                    // (a ◁ b) ▷ a = b
                    if (aLb != -1) {
                        if (q.right(aLb, a) == -1) {
                            q.right(aLb, a, b);
                            changes = true;
                        } else if (q.right(aLb, a) != b) {
                            return false;
                        }
                    }

                    // a ◁ (b ▷ a) = b
                    if (bRa != -1) {
                        if (q.left(a, bRa) == -1) {
                            q.left(a, bRa, b);
                            changes = true;
                        } else if (q.left(a, bRa) != b) {
                            return false;
                        }
                    }

                    for (byte c = 0; c < n; ++c) {

                        byte bLc = q.left(b, c);
                        byte aLc = q.left(a, c);
                        byte cRb = q.right(c, b);
                        byte cRa = q.right(c, a);
                        byte bRc = q.right(b, c);
                        byte cLb = q.left(c, b);

                        // a ◁ (b ◁ c) = (a ◁ b) ◁ (a ◁ c)
                        if (bLc != -1 && aLb != -1 && aLc != -1) {
                            if (q.left(a, bLc) != q.left(aLb, aLc)) {
                                if (q.left(aLb, aLc) == -1) {
                                    q.left(aLb, aLc, q.left(a, bLc));
                                    changes = true;
                                } else if (q.left(a, bLc) == -1) {
                                    q.left(a, bLc, q.left(aLb, aLc));
                                    changes = true;
                                } else {
                                    return false;
                                }
                            }
                        }

                        // (c ▷ b) ▷ a = (c ▷ a) ▷ (b ▷ a)
                        if (cRb != -1 && cRa != -1 && bRa != -1) {
                            if (q.right(cRb, a) != q.right(cRa, bRa)) {
                                if (q.right(cRb, a) == -1) {
                                    q.right(cRb, a, q.right(cRa, bRa));
                                    changes = true;
                                } else if (q.right(cRa, bRa) == -1) {
                                    q.right(cRa, bRa, q.right(cRb, a));
                                    changes = true;
                                } else {
                                    return false;
                                }
                            }
                        }

                        // a ◁ (b ▷ c) = (a ◁ b) ▷ (a ◁ c)
                        if (bRc != -1 && aLb != -1 && aLc != -1) {
                            if (q.left(a, bRc) != q.right(aLb, aLc)) {
                                if (q.left(a, bRc) == -1) {
                                    q.left(a, bRc, q.right(aLb, aLc));
                                    changes = true;
                                } else if (q.right(aLb, aLc) == -1) {
                                    q.right(aLb, aLc, q.left(a, bRc));
                                    changes = true;
                                } else {
                                    return false;
                                }
                            }
                        }

                        // (c ◁ b) ▷ a = (c ▷ a) ◁ (b ▷ a)
                        if (cLb != -1 && cRa != -1 && bRa != -1) {
                            if (q.right(cLb, a) != q.left(cRa, bRa)) {
                                if (q.right(cLb, a) == -1) {
                                    q.right(cLb, a, q.left(cRa, bRa));
                                    changes = true;
                                } else if (q.left(cRa, bRa) == -1) {
                                    q.left(cRa, bRa, q.right(cLb, a));
                                    changes = true;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private static boolean replaceNextUnknown(Quandle quandle, byte n, byte value) {
        for (byte i = 0; i < n; ++i) {
            for (byte j = 0; j < n; ++j) {
                if (quandle.right(i, j) == -1) {

                    quandle.right(i, j, value);

                    if (quandle.left(j, value) != i) {
                        if (quandle.left(j, value) == -1) {
                            quandle.left(j, value, i);
                        } else {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }
}
