package ch.hgdev.toposuite.dao.collections;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import ch.hgdev.toposuite.App;
import ch.hgdev.toposuite.dao.interfaces.DAO;
import ch.hgdev.toposuite.dao.interfaces.DAOMapper;
import ch.hgdev.toposuite.export.DataExporter;
import ch.hgdev.toposuite.export.SaveStrategy;

import com.google.common.collect.Iterables;

/**
 * DAOMapperTreeSet is a TreeSet that is synchronized with the database through
 * a DAO object.
 * 
 * @author HGdev
 * @param <E>
 *            the type of the Set
 */
public class DAOMapperTreeSet<E extends DataExporter> extends TreeSet<E> implements DAOMapper,
        SaveStrategy {
    /**
     * Serial UID.
     */
    private static final long   serialVersionUID = 310907601029773320L;

    /**
     * Searcher interface for finding object in the collection.
     */
    private Searcher<? super E> searcher;

    /**
     * List of observers.
     */
    private List<DAO>           daoList;

    /**
     * Control whether methods that change the list automatically call the
     * notify* methods. If set to false, caller must manually call
     * {@link DAOMapperTreeSet#notifyCreation(Object)()} and
     * {@link DAOMapperTreeSet#notifyDeletion(Object)()} to have the changes
     * reflected in the DAO.
     * 
     * The default value is set to true.
     */
    private boolean             notifyOnChange;

    public DAOMapperTreeSet(Comparator<? super E> comparator) {
        super(comparator);
        this.daoList = new ArrayList<DAO>();
        this.notifyOnChange = true;
    }

    public DAOMapperTreeSet(Comparator<? super E> comparator, Searcher<? super E> _searcher) {
        this(comparator);
        this.searcher = _searcher;
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
    public boolean remove(Object obj) {
        boolean status = super.remove(obj);
        if (status && this.notifyOnChange) {
            this.notifyDeletion(obj);
        }
        return status;
    }

    @Override
    public void clear() {
        super.clear();
        if (this.notifyOnChange) {
            this.notifyClear();
        }
    }

    /**
     * Get an element of the Set at a given position.
     * 
     * @param position
     *            position of the item in the Set.
     * @return a element
     */
    public E get(int position) {
        return Iterables.get(this, position);
    }

    /**
     * Find a object E in the TreeSet.
     * 
     * @param needle
     *            the needle to find in the haystack
     * @return the object that match the search criteria
     */
    public E find(Object needle) {
        for (E element : this) {
            if (this.searcher.isFound(element, needle)) {
                return element;
            }
        }
        return null;
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
        App.arePointsExported = false;
        for (DAO dao : this.daoList) {
            dao.create(obj);
        }
    }

    @Override
    public void notifyDeletion(Object obj) {
        App.arePointsExported = false;
        for (DAO dao : this.daoList) {
            dao.delete(obj);
        }
    }

    @Override
    public void notifyClear() {
        App.arePointsExported = false;
        for (DAO dao : this.daoList) {
            dao.deleteAll();
        }
    }

    /**
     * Setter for the searcher interface.
     * 
     * @param searcher
     *            searcher interface implementation
     */
    public void setSearcheable(Searcher<E> _searcher) {
        this.searcher = _searcher;
    }

    @Override
    public int saveAsCSV(Context context, String path, String filename) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File(path, filename));
        return this.saveAsCSV(context, outputStream);
    }

    @Override
    public int saveAsCSV(Context context, String filename) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(
                filename, Context.MODE_PRIVATE);
        return this.saveAsCSV(context, outputStream);
    }

    @Override
    public int saveAsCSV(Context context, FileOutputStream outputStream) throws IOException {
        int lines = 0;

        for (E element : this) {
            outputStream.write(element.toCSV().getBytes());
            outputStream.write("\r\n".getBytes());
            lines++;
        }
        outputStream.close();

        return lines;
    }
}
