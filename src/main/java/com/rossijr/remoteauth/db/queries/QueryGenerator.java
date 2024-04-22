package com.rossijr.remoteauth.db.queries;

public interface QueryGenerator {
    String generateInsertObjectQuery(String tableName, Object object);
    String generateSelectByID(Class<?> model, Object id);
    String generateQuery(Class<?> model, String query, Object... params);
}
