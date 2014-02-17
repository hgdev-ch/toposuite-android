package ch.hgdev.toposuite.utils;

/**
 * Small class that implements a tuple, as seen in some other programming
 * languages.
 * 
 * @author HGdev
 * 
 * @param <X>
 *            First tuple element.
 * @param <Y>
 *            Second tuple element.
 */
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}