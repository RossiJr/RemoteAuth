package com.rossijr.remoteauth.db;

import jakarta.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDAO<T> implements GenericDAOI<T> {
    /**
     * <p>Save an object in the database</p>
     * @param entity object to be saved
     * @return saved object
     */
    @Override
    public T save(T entity) {
        try (Session session = Configuration.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException("Error during persist operation - Hibernate", e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }

    /**
     * <p>Update an object in the database</p>
     * @param entity object to be updated
     * @return updated object
     */
    @Override
    public T update(T entity) {
        try (Session session = Configuration.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            T newObj = session.merge(entity);
            transaction.commit();
            return newObj;
        } catch (HibernateException e) {
            throw new RuntimeException("Error during update operation - Hibernate", e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>Delete an object from the database</p>
     * @param entity object to be deleted
     * @return true if the object was successfully deleted, false otherwise
     */
    @Override
    public boolean delete(T entity) {
        try (Session session = Configuration.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>Gets an object by its id</p>
     * @param clazz class of the object
     * @param id id of the object
     * @return constructed object
     */
    @Override
    public T getById(Class<T> clazz, Object id) {
        try (Session session = Configuration.getSessionFactory().openSession()) {
            T obj = session.get(clazz, id);
            return obj;
        } catch (HibernateException e) {
            throw new RuntimeException("Error during select by id operation - Hibernate", e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all objects with the specified column value
     * <p><b>It's important to note that the column value is a column name, so this method is vulnerable to SQL injection in case of user input</b></p>
     * @param clazz Class of the object
     * @param column Column name
     * @param value Column value
     * @return List of objects
     */
    @Override
    public List getByColumn(Class<T> clazz, String column, String value) {
        try (Session session = Configuration.getSessionFactory().openSession()) {
            Query query = session.createQuery("from " + clazz.getName() + " where " + column + " = :value", clazz);
            query.setParameter("value", value);
            return query.getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException("Error during select by column operation - Hibernate", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
