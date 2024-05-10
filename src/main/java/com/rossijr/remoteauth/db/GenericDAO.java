package com.rossijr.remoteauth.db;

import jakarta.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDAO<T> implements GenericDAOI<T> {
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

    @Override
    public List getByColumn(Class<T> clazz, String column, String value) {
        try (Session session = Configuration.getSessionFactory().openSession()) {
            Query query = session.createQuery("FROM " + clazz.getName(), clazz);
            return query.getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException("Error during select by column operation - Hibernate", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
