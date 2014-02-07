package ch.hgdev.toposuite.dao.interfaces;

/**
 * A Data Access Object.
 * 
 * @author HGdev
 */
public interface DAO {
    /**
     * Insert a new object.
     * 
     * @param obj
     *            An Object
     */
    void create(Object obj);

    /**
     * Update an existing object.
     * 
     * @param obj
     *            An Object
     */
    void update(Object obj);

    /**
     * Delete an existing object.
     * 
     * @param obj
     *            An Object
     */
    void delete(Object obj);

    /**
     * Delete all element from table.
     */
    void deleteAll();
}
