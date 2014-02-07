package ch.hgdev.toposuite.dao.collections;

import java.util.ArrayList;
import java.util.List;

import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.dao.interfaces.DAOMapper;

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
    private static final long serialVersionUID = -2385665453541274357L;

    /**
     * List of observers.
     */
    private List<DAO>         daoList;

    /**
     * Control whether methods that change the list automatically call the
     * notify* methods. If set to false, caller must manually call
     * {@link DAOMapperArrayList#notifyCreation(Object)()} and
     * {@link DAOMapperArrayList#notifyDeletion(Object)()} to have the changes
     * reflected in the DAO.
     * 
     * The default value is set to true.
     */
    private boolean           notifyOnChange;

    /**
     * Construct a new {@link DAOMapperArrayList}.
     */
    public DAOMapperArrayList() {
        super();
        this.daoList = new ArrayList<DAO>();
        this.notifyOnChange = true;
    }

    @Override
    public boolean add(E obj) {
        boolean status = super.add(obj);
        if (status && this.notifyOnChange) {
            this.notifyCreation(obj);
        }
        return status;
    }

    @Override
    public void add(int index, E obj) {
        super.add(index, obj);
        if (this.notifyOnChange) {
            this.notifyCreation(obj);
        }
    }

    @Override
    public E remove(int index) {
        E obj = super.remove(index);
        if (this.notifyOnChange) {
            this.notifyDeletion(obj);
        }
        return obj;
    }

    @Override
    public boolean remove(Object obj) {
        boolean status = super.remove(obj);
        if (status && this.notifyOnChange) {
            this.notifyDeletion(obj);
        }
        return status;
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
}