package ch.hgdev.toposuite.dao.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.dao.interfaces.DAOMapper;


/**
 * 
 * @author HGdev
 *
 * @param <E>
 */
public class DAOMapperTreeSet<E> extends TreeSet<E> implements DAOMapper {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 310907601029773320L;
    
    /**
     * Searcher interface for finding object in the collection.
     */
    private Searcher<? super E> searcher;
    
    /**
     * List of observers.
     */
    private List<DAO> daoList;
    
    /**
     * FIXME update comment
     * Control whether methods that change the list automatically call notifyObservers().
     * If set to false, caller must manually call
     * {@link DAOMapperTreeSet#notifyObservers()} to have the changes reflected in 
     * the observers.
     * 
     * The default value is set to true.
     */
    private boolean notifyOnChange;
   
    public DAOMapperTreeSet(Comparator<? super E> comparator) {
        super(comparator);
        this.daoList = new ArrayList<DAO>();
        this.notifyOnChange = true;
    }
    
    public DAOMapperTreeSet(Comparator<? super E> comparator, Searcher<? super E>  _searcher) {
        this(comparator);
        this.searcher = _searcher;
    }

    @Override
    public boolean add(E obj) {
        boolean status = super.add(obj);
        if (status && notifyOnChange) {
            this.notifyCreation(obj);
        }
        return status;
    }

    @Override
    public boolean remove(Object obj) {
        boolean status = super.remove(obj);
        if (status && notifyOnChange) {
            this.notifyDeletion(obj);
        }
        return status;
    }
    
    /**
     * Find a object E in the TreeSet.
     * @param needle
     *            the needle to find in the haystack
     * @return the object that match the search criteria
     */
    public E find(Object needle) {
        for (E element : this) {
            if (this.searcher.isFound(element, needle)) {
                return element;
            }
        }
        return null;
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
    
    @Override
    public void registerDAO(DAO dao) {
        this.daoList.add(dao);
    }

    @Override
    public void removeDAO(DAO dao) {
        this.daoList.remove(dao);
    }

    @Override
    public void notifyCreation(Object obj) {
        for (DAO dao : this.daoList) {
            dao.create(obj);
        }
    } 

    @Override
    public void notifyDeletion(Object obj) {
        for (DAO dao : this.daoList) {
            dao.delete(obj);
        }
    }
    
    @Override
    public void notifyUpdate(Object obj) {
     // actually not used
    }

    /**
     * Setter for the searcher interface.
     * @param searcher
     *            searcher interface implementation
     */
    public void setSearcheable(Searcher<E> _searcher) {
        this.searcher = _searcher;
    }
}
