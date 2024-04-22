package com.rossijr.remoteauth.db;

import com.rossijr.remoteauth.db.annotations.model_annotation.Table;
import com.rossijr.remoteauth.db.queries.DAO;
import com.rossijr.remoteauth.db.queries.QueryExecutor;
import com.rossijr.remoteauth.db.queries.QueryFactory;
import com.rossijr.remoteauth.db.queries.QueryGenerator;

import java.sql.Connection;
import java.sql.SQLException;

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
    public T getByColumn(String column, Object value) {
        return null;
    }

    @Override
    public T insert(T object) {
        return null;
    }

    @Override
    public T update(T object) {
        return null;
    }

    @Override
    public T query(String query, Object... params) throws SQLException {
        return this.clazz.cast(this.queryExecutor.executeSelectQuery(query, this.clazz, this.getConnection(), params));
    }

    public Connection getConnection() throws SQLException {
        return (connection == null || connection.isClosed()) ? new DbConnection().getConnection() : connection;
    }
}
