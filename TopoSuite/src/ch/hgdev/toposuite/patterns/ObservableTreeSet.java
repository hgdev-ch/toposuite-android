package ch.hgdev.toposuite.patterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


/**
 * 
 * @author HGdev
 *
 * @param <E>
 */
public class ObservableTreeSet<E> extends TreeSet<E> implements Observable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 310907601029773320L;
    
    /**
     * List of observers.
     */
    private List<Observer> observers;
    
    /**
     * Control whether methods that change the list automatically call notifyObservers().
     * If set to false, caller must manually call
     * {@link ObservableTreeSet#notifyObservers()} to have the changes reflected in 
     * the observers.
     * 
     * The default value is set to true.
     */
    private boolean notifyOnChange;
   
    public ObservableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
        this.observers = new ArrayList<Observer>();
        this.notifyOnChange = true;
    }

    @Override
    public boolean add(E object) {
        boolean status = super.add(object);
        if (status && notifyOnChange) {
            this.notifyObservers();
        }
        return status;
    }

    @Override
    public boolean remove(Object object) {
        boolean status = super.remove(object);
        if (status && notifyOnChange) {
            this.notifyObservers(object);
        }
        return status;
    }

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }
    
    @Override
    public void notifyObservers() {
        this.notifyObservers(null);
    }

    @Override
    public void notifyObservers(Object obj) {
        for (Observer o : this.observers) {
            o.update(this, obj);
        }
    }
    
    /**
     * Getter for notifyOnChange flag.
     * @return the notifyOnChange
     */
    public boolean isNotifyOnChange() {
        return this.notifyOnChange;
    }

    /**
     * Setter for notifyOnChange flag.
     * @param notifyOnChange
     *            the notifyOnChange to set
     */
    public void setNotifyOnChange(boolean _notifyOnChange) {
        this.notifyOnChange = _notifyOnChange;
    }
}
