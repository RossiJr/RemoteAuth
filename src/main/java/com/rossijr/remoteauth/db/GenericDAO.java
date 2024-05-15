package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.db.exceptions.DatabaseNotSupportedException;

import java.util.List;

public class GenericDAO<T> implements GenericDAOI<T> {
    private final String databaseType;
    private final GenericDAOI<T> dao;

    /**
     * Constructor to initialize the database type and the DAO.
     * It is empty because the database type is already defined in the configuration file.
     */
    public GenericDAO(){
        this.databaseType = Configuration.getDatabaseType();
        if(databaseType.equals("sql")){
            dao = new SQLDao<>();
        } else if(databaseType.equals("nosql")){
            dao = new NoSQLDao<>();
        } else {
            throw new DatabaseNotSupportedException("Database type not supported: " + databaseType);
        }
    }


    /**
     * <p>Save an object in the database</p>
     * @param entity object to be saved
     * @return saved object
     */
    @Override
    public T save(T entity) {
        return dao.save(entity);
    }

    /**
     * <p>Update an object in the database</p>
     * @param entity object to be updated
     * @return updated object
     */
    @Override
    public T update(T entity) {
        return dao.update(entity);
    }

    /**
     * <p>Delete an object from the database</p>
     * @param entity object to be deleted
     * @return true if the object was successfully deleted, false otherwise
     */
    @Override
    public boolean delete(T entity) {
        return dao.delete(entity);
    }

    /**
     * <p>Gets an object by its id</p>
     * @param clazz class of the object
     * @param id id of the object
     * @return constructed object
     */
    @Override
    public T getById(Class<T> clazz, Object id) {
        return dao.getById(clazz, id);
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
        return dao.getByColumn(clazz, column, value);
    }
}
