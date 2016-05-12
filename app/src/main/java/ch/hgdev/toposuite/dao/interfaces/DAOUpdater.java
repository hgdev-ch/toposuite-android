package ch.hgdev.toposuite.dao.interfaces;

import ch.hgdev.toposuite.dao.DAOException;

/**
 * Interface for updating through a DAO.
 *
 * @author HGdev
 */
public interface DAOUpdater extends DAOLinker {
    /**
     * Notify all the DAO that a new Object has been updated.
     *
     * @param obj an object
     */
    void notifyUpdate(Object obj) throws DAOException;
}
