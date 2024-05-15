package com.rossijr.remoteauth.db;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.List;

public class NoSQLDao<T> implements GenericDAOI<T>{

    /**
     * <p>Save an object in the database using Data Nucleus with JDO</p>
     * @param entity object to be saved
     * @return saved object
     */
    @Override
    public T save(T entity) {
        PersistenceManager pm = Configuration.getPmf().getPersistenceManager();
        Transaction tx = null;
        try (pm) {
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(entity);
            tx.commit();
            return entity;
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * <p>Update an object in the database using Data Nucleus with JDO</p>
     * @param entity object to be updated
     * @return updated object
     */
    @Override
    public T update(T entity) {
        PersistenceManager pm = Configuration.getPmf().getPersistenceManager();
        Transaction tx = null;
        try (pm) {
            tx = pm.currentTransaction();
            tx.begin();
            T updatedEntity = pm.makePersistent(entity);
            tx.commit();
            return updatedEntity;
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * <p>Delete an object from the database using Data Nucleus with JDO</p>
     * @param entity object to be deleted
     * @return true if the object was successfully deleted, false otherwise
     */
    @Override
    public boolean delete(T entity) {
        PersistenceManager pm = Configuration.getPmf().getPersistenceManager();
        Transaction tx = null;
        try (pm) {
            tx = pm.currentTransaction();
            tx.begin();
            pm.deletePersistent(entity);
            tx.commit();
            return true;
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * <p>Gets an object by its id using Data Nucleus with JDO</p>
     * @param clazz class of the object
     * @param id id of the object
     * @return constructed object
     */
    @Override
    public T getById(Class<T> clazz, Object id) {
        try (PersistenceManager pm = Configuration.getPmf().getPersistenceManager()) {
            return pm.getObjectById(clazz, id);
        }
    }

    /**
     * <p>Gets a list of objects by a column value using Data Nucleus with JDO</p>
     * @param clazz class of the object
     * @param column column to be queried
     * @param value value to be queried
     * @return list of objects
     */
    @Override
    public List<T> getByColumn(Class<T> clazz, String column, String value) {
        try (PersistenceManager pm = Configuration.getPmf().getPersistenceManager()) {
            Query<T> query = pm.newQuery(clazz);
            query.setFilter(column + " == valueParam");
            query.declareParameters("String valueParam");
            return (List<T>) query.execute(value);
        }
    }

}
