package ch.hgdev.toposuite.dao.interfaces;

import ch.hgdev.toposuite.dao.DAOException;

/**
 * A Data Access Object.
 *
 * @author HGdev
 */
public interface DAO {
    /**
     * Insert a new object.
     *
     * @param obj An Object
     */
    void create(Object obj) throws DAOException;

    /**
     * Update an existing object.
     *
     * @param obj An Object
     */
    void update(Object obj) throws DAOException;

    /**
     * Delete an existing object.
     *
     * @param obj An Object
     */
    void delete(Object obj) throws DAOException;

    /**
     * Delete all element from table.
     */
    void deleteAll() throws DAOException;
}
