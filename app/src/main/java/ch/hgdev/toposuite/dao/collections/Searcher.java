package ch.hgdev.toposuite.dao.collections;

public interface Searcher<E>  {
    boolean isFound(E currentElement, Object expectedElement);
}