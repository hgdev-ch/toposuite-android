package ch.hgdev.toposuite.dao.interfaces;

public interface DAOMapper {
    public void registerDAO(DAO dao);
    public void removeDAO(DAO dao);
    public void notifyCreation(Object obj);
    public void notifyUpdate(Object obj);
    public void notifyDeletion(Object obj);
}