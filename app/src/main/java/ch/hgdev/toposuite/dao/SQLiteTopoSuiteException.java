package ch.hgdev.toposuite.dao;

import java.io.Serial;

public class SQLiteTopoSuiteException extends DAOException {

    /**
     * Serial UID.
     */
    @Serial
    private static final long serialVersionUID = 5756139318913008377L;

    /**
     * Constructs a new SQLiteTopoSuiteException.
     * @param msg
     *            The message to display in the exception
     */
    public SQLiteTopoSuiteException(String msg) {
        super(msg);
    }
}
