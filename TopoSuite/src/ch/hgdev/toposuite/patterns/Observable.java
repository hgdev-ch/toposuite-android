package ch.hgdev.toposuite.patterns;

public interface Observable {
    public void addObserver(Observer o);
    public void notifyObservers();
    public void notifyObservers(Object obj);
}
