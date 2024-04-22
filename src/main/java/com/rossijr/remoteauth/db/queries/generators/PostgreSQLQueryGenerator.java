package com.rossijr.remoteauth.db.queries.generators;

import com.rossijr.remoteauth.db.queries.QueryGenerator;

public class PostgreSQLQueryGenerator implements QueryGenerator {
    @Override
    public String generateInsertObjectQuery(String tableName, Object object) {
        return "";
    }

    @Override
    public String generateSelectByID(Class<?> model, Object id) {
        return "";
    }

    @Override
    public String generateQuery(Class<?> model, String query, Object... params) {
        return query;
    }
}