package ch.hgdev.toposuite.dao.interfaces;

/**
 * Interface for mapping a collection to database table through a DAO.
 * 
 * @author HGdev
 */
public interface DAOMapper extends DAOLinker {
    /**
     * Notify all the DAO that a new Object has been added.
     * 
     * @param obj
     *            an object
     */
    public void notifyCreation(Object obj);

    /**
     * Notify all the DAO that a new Object has been deleted.
     * 
     * @param obj
     *            an object
     */
    public void notifyDeletion(Object obj);
}