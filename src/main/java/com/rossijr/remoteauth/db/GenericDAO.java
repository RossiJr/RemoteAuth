package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.db.annotations.model_annotation.Column;
import com.rossijr.remoteauth.db.annotations.model_annotation.Table;
import com.rossijr.remoteauth.db.config.DbConfig;
import com.rossijr.remoteauth.db.queries.DAO;
import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.QueryFactory;
import com.rossijr.remoteauth.db.queries.QueryGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class GenericDAO<T> implements DAO<T> {
    private final Class<T> clazz;
    private Connection connection;
    private QueryExecutor<T> queryExecutor;
    private QueryGenerator queryGenerator;

    public GenericDAO(QueryFactory queryFactory, Class<T> clazz) {
        this.clazz = clazz;
        this.queryExecutor = queryFactory.createQueryExecutor();
        this.queryGenerator = queryFactory.createQueryGenerator();
    }


    @Override
    public T getById(Object id) throws SQLException {
        String query = this.queryGenerator.generateSelectByID(this.clazz, id);
        Object x = this.queryExecutor.executeSelectQueryByID(query, id, this.clazz, this.getConnection());
        return this.clazz.cast(x);
    }

    @Override
    public List<T> getByColumn(String column, Object value) {
        String query = String.format("SELECT * FROM %s WHERE %s %s ?", DbConfig.getValue(clazz.getAnnotation(Table.class).key()), column,
                Arrays.stream(clazz.getDeclaredFields()).filter(f ->
                        (f.getAnnotation(Column.class) != null && DbConfig.getValue(f.getAnnotation(Column.class).key()).equals(column))
                                || f.getName().equals(column)).findFirst().orElseThrow().getType().equals(String.class) ? "LIKE" : "=");
        try {
            return (List<T>) this.queryExecutor.executeSelectQuery(query, this.clazz, this.getConnection(), value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object insert(T object) throws SQLException {
        return this.queryExecutor.executeInsertQuery(this.queryGenerator.generateInsertObjectQuery(this.clazz, object), object, this.getConnection());
    }

    @Override
    public T update(T object) throws SQLException {
        return this.clazz.cast(this.queryExecutor.executeUpdateQuery(this.queryGenerator.generateUpdateQuery(this.clazz, object), object, this.getConnection()));
    }

    /**
     * <p>Method to perform a query in the database</p>
     * <p>Use this function just in case of <b>SELECT</b> operations.</p>
     * @param query SQL query to be performed
     * @param params parameters to be replaced in the query
     * @return Queried object of the class T
     * @throws SQLException if an error occurs during the query
     */
    @Override
    public List<T> query(String query, Object... params) throws SQLException {
        return (List<T>) this.queryExecutor.executeSelectQuery(query, this.clazz, this.getConnection(), params);
    }

    public Connection getConnection() throws SQLException {
        return (connection == null || connection.isClosed()) ? new DbConnection().getConnection() : connection;
    }
}
