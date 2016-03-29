/**
 * This package contains all the classes necessary for persistent storage.
 * 
 *  <p>When you want to create a new persistent element, you have to follow
 *  the following procedure:</p>
 *  
 *  <ol>
 *      <li>
 *          Create a new class called <em>NewClassNameTable.java</em> where
 *          <em>NewClassName</em> is the name of the element you want to
 *          make persistent.
 *          See {@link ch.hgdev.toposuite.dao.CalculationsTable}.
 *      </li>
 *      <li>
 *          Update the methods
 *          {@link ch.hgdev.toposuite.dao.DBHelper#onCreate(android.database.sqlite.SQLiteDatabase)}
 *          and
 *          {@link ch.hgdev.toposuite.dao.DBHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)} 
 *      </li>
 *      <li>
 *          Increment the database version in App.java: {@link ch.hgdev.toposuite.App#DATABASE_VERSION}.
 *      </li>
 *      <li>
 *          Create a new class called <em>NewClassNameDataSource.java</em>,
 *          where <em>NewClassName</em> is the same as the one you chose
 *          previously. This new class is your DAO, so you have to implement
 *          the {@link ch.hgdev.toposuite.dao.interfaces.DAO} interface.
 *          
 *          Follow the structure of
 *          {@link ch.hgdev.toposuite.dao.CalculationsDataSource}.
 *      </li>
 *      <li>
 *          Implement the DAOMapper interface in the collection that holds
 *          the element you want to persist.
 *      </li>
 *      <li>
 *          Implement the DAOUpdater interface in the class of your element.
 *      </li>
 *  </ol>
 * 
 * @author HGdev
 */
package ch.hgdev.toposuite.dao;