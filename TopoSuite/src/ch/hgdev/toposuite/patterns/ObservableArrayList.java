package ch.hgdev.toposuite.patterns;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author HGdev
 * @param <E>
 */
public class ObservableArrayList<E> extends ArrayList<E> implements Observable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -2385665453541274357L;
    
    /**
     * List of observers.
     */
    private List<Observer> observers;
    
    /**
     * Control whether methods that change the list automatically call notifyObservers().
     * If set to false, caller must manually call
     * {@link ObservableArrayList#notifyObservers()} to have the changes reflected in 
     * the observers.
     * 
     * The default value is set to true.
     */
    private boolean notifyOnChange;
    
    /**
     * Construct a new {@link ObservableArrayList}.
     */
    public ObservableArrayList() {
        super();
        this.observers = new ArrayList<Observer>();
        this.notifyOnChange = true;
    }

    @Override
    public boolean add(E object) {
        boolean status = super.add(object);
        if (status && this.notifyOnChange) {
            this.notifyObservers();
        }
        return status;
    }

    @Override
    public void add(int index, E object) {
        super.add(index, object);
        if (this.notifyOnChange) {
            this.notifyObservers();
        }
    }

    @Override
    public E remove(int index) {
        E obj = super.remove(index);
        if (this.notifyOnChange) {
            this.notifyObservers(obj);
        }
        return obj;
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
