package ch.hgdev.toposuite.utils;

/**
 * Very simple Pair class. Android Pair class has each of the element final
 * which make them impossible to reassign. This small class probably makes cry
 * real Java developers notably because it has its attributes public. Sorry
 * guys, but Java syntax is just too painful for what I want to achieve. :-)
 * 
 * @author HGdev
 * 
 * @param <A>
 * @param <B>
 */
public class Pair<A, B> {
    public A first;
    public B second;

    public Pair(A a, B b) {
        this.first = a;
        this.second = b;
    }
}
