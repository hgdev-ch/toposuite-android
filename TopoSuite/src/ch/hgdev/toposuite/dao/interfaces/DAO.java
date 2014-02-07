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
    public void create(Object obj);

    /**
     * Update an existing object.
     * 
     * @param obj
     *            An Object
     */
    public void update(Object obj);

    /**
     * Delete an existing object.
     * 
     * @param obj
     *            An Object
     */
    public void delete(Object obj);
}
