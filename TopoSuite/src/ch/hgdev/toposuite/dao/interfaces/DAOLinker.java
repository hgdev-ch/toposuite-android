package ch.hgdev.toposuite.dao.interfaces;

/**
 * Link a collection or simple object to DAO.
 * 
 * @author HGdev
 */
public interface DAOLinker {
    /**
     * Register a new DAO.
     * 
     * @param dao
     *            a DAO object
     */
    public void registerDAO(DAO dao);

    /**
     * Remove a DAO.
     * 
     * @param dao
     *            a DAO object
     */
    public void removeDAO(DAO dao);
}
