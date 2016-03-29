package ch.hgdev.toposuite.dao.collections;

public interface Searcher<E>  {
    public boolean isFound(E currentElement, Object expectedElement);
}