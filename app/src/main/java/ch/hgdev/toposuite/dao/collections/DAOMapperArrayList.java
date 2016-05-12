package ch.hgdev.toposuite.dao.collections;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.dao.DAOException;
import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.dao.interfaces.DAOMapper;
import ch.hgdev.toposuite.utils.Logger;

/**
 * DAOMapperArrayList is an ArrayList that is synchronized with the database
 * through a DAO object.
 * 
 * @author HGdev
 * @param <E>
 *            The type of the List.
 */
public class DAOMapperArrayList<E> extends ArrayList<E> implements DAOMapper {
    /**
     * Serial UID.
     */
    private static final long         serialVersionUID = -2385665453541274357L;

    /**
     * Searcher interface for finding object in the collection.
     */
    private final Searcher<? super E> searcher;

    /**
     * List of observers.
     */
    private final List<DAO>           daoList;

    /**
     * Control whether methods that change the list automatically call the
     * notify* methods. If set to false, caller must manually call
     * {@link DAOMapperArrayList#notifyCreation(Object)()} and
     * {@link DAOMapperArrayList#notifyDeletion(Object)()} to have the changes
     * reflected in the DAO.
     * 
     * The default value is set to true.
     */
    private boolean                   notifyOnChange;

    /**
     * Construct a new {@link DAOMapperArrayList}.
     */
    public DAOMapperArrayList(Searcher<? super E> _searcher) {
        super();
        this.daoList = new ArrayList<>();
        this.notifyOnChange = true;
        this.searcher = _searcher;
    }

    @Override
    public boolean add(E obj) {
        boolean status = super.add(obj);
        if (status && this.notifyOnChange) {
            try {
                this.notifyCreation(obj);
            } catch (DAOException e) {
                Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
            }
        }
        return status;
    }

    @Override
    public void add(int index, E obj) {
        super.add(index, obj);
        if (this.notifyOnChange) {
            try {
                this.notifyCreation(obj);
            } catch (DAOException e) {
                Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public E remove(int index) {
        E obj = super.remove(index);
        if (this.notifyOnChange) {
            try {
                this.notifyDeletion(obj);
            } catch (DAOException e) {
                Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
            }
        }
        return obj;
    }

    @Override
    public boolean remove(Object obj) {
        boolean status = super.remove(obj);
        if (status && this.notifyOnChange) {
            try {
                this.notifyDeletion(obj);
            } catch (DAOException e) {
                Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
            }
        }
        return status;
    }

    @Override
    public void clear() {
        super.clear();
        if (this.notifyOnChange) {
            try {
                this.notifyClear();
            } catch (DAOException e) {
                Logger.log(Logger.ErrLabel.DAO_ERROR, e.getMessage());
            }
        }
    }

    /**
     * Find a object E in the ArrayList.
     * 
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
     * 
     * @return the notifyOnChange
     */
    public boolean isNotifyOnChange() {
        return this.notifyOnChange;
    }

    /**
     * Setter for notifyOnChange flag.
     * 
     * @param _notifyOnChange
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
    public void notifyCreation(Object obj) throws DAOException {
        for (DAO dao : this.daoList) {
            dao.create(obj);
        }
    }

    @Override
    public void notifyDeletion(Object obj) throws DAOException {
        for (DAO dao : this.daoList) {
            dao.delete(obj);
        }
    }

    @Override
    public void notifyClear() throws DAOException {
        for (DAO dao : this.daoList) {
            dao.deleteAll();
        }
    }
}