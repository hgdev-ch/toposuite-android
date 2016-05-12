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
     * @param dao a DAO object
     */
    void registerDAO(DAO dao);

    /**
     * Remove a DAO.
     *
     * @param dao a DAO object
     */
    void removeDAO(DAO dao);
}
